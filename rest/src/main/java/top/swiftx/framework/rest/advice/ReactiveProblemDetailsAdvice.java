package top.swiftx.framework.rest.advice;

import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * WebFlux（Reactive）全局异常处理器，用于网关等响应式应用。
 *
 * <p>继承响应式 {@link ResponseEntityExceptionHandler}，自动将所有 {@link ErrorResponse}
 * （含 {@code UnAuthorizedException} 等自定义异常）按 RFC 9457 渲染为
 * {@code application/problem+json}。网关过滤器（如 {@code JwtGatewayFilter}）通过
 * {@code Mono.error} 抛出的 {@code ErrorResponseException} 会经由 {@code DispatcherHandler}
 * 被本处理器捕获，因此无需额外自定义 {@code ErrorWebExceptionHandler}。
 *
 * <p>由于 WebFlux 不会自动应用 {@code WebFluxConfigurer.addErrorResponseInterceptors}
 * 注册的 {@link ErrorResponse.Interceptor}（不同于 MVC 的 {@code ExceptionHandlerExceptionResolver}），
 * 这里在 {@link #handleExceptionInternal} 中显式应用容器内所有
 * {@code ErrorResponse.Interceptor}（例如 framework/problem 提供的文档地址改写器），
 * 使网关的 {@code type} 字段与 MVC 服务一致地改写为问题文档地址。
 *
 * @author 胡永强
 */
@ControllerAdvice
public class ReactiveProblemDetailsAdvice extends ResponseEntityExceptionHandler {
    private final List<ErrorResponse.Interceptor> interceptors;

    /**
     * 创建 WebFlux 问题详情异常处理器。
     *
     * @param interceptors 容器内所有 {@link ErrorResponse.Interceptor}，可为空
     */
    public ReactiveProblemDetailsAdvice(
            List<ErrorResponse.Interceptor> interceptors
    ) {
        this.interceptors = interceptors;
    }

    @Override
    protected Mono<ResponseEntity<Object>> handleExceptionInternal(
            Exception ex,
            @Nullable Object body,
            @Nullable HttpHeaders headers,
            HttpStatusCode statusCode,
            ServerWebExchange exchange
    ) {
        return super.handleExceptionInternal(
                        ex,
                        body,
                        headers,
                        statusCode,
                        exchange
                )
                .doOnNext(entity -> applyInterceptors(
                        ex,
                        entity.getBody()
                ));
    }

    /**
     * 对 {@link ProblemDetail} 应用所有已注册的 {@link ErrorResponse.Interceptor}。
     *
     * @param ex   原始异常
     * @param body 处理器产出的响应体
     */
    private void applyInterceptors(Exception ex, @Nullable Object body) {
        if (interceptors.isEmpty()) return;
        if (body instanceof ProblemDetail problemDetail && ex instanceof ErrorResponse errorResponse) {
            for (var interceptor : interceptors) {
                interceptor.handleError(problemDetail, errorResponse);
            }
        }
    }
}
