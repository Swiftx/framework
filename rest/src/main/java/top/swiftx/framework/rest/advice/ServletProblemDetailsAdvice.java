package top.swiftx.framework.rest.advice;

import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

/**
 * MVC（Servlet）全局异常处理器。
 *
 * <p>继承 {@link ResponseEntityExceptionHandler}，自动将所有
 * {@link ErrorResponse}（含 {@code UnAuthorizedException} 等自定义异常）按 RFC 9457
 * 渲染为 {@code application/problem+json}。
 *
 * <p>问题类型 {@link ProblemDetail} 的 {@code type} 字段改写由全局注册的
 * {@link ErrorResponse.Interceptor} 完成；由于 Spring 7 已将拦截器从处理器实例上移除，
 * 这里在 {@link #handleExceptionInternal} 中显式应用容器内所有
 * {@code ErrorResponse.Interceptor}（例如 framework/problem 提供的文档地址改写器），
 * 以保证 MVC 服务与 WebFlux 网关行为一致。重复应用对幂等的拦截器无副作用。
 *
 * @author 胡永强
 */
@ControllerAdvice
public class ServletProblemDetailsAdvice extends ResponseEntityExceptionHandler {
    private final List<ErrorResponse.Interceptor> interceptors;

    /**
     * 创建 MVC 问题详情异常处理器。
     *
     * @param interceptors 容器内所有 {@link ErrorResponse.Interceptor}，可为空
     */
    public ServletProblemDetailsAdvice(List<ErrorResponse.Interceptor> interceptors) {
        this.interceptors = interceptors;
    }

    @Override
    @Nullable
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex,
            @Nullable Object body,
            HttpHeaders headers,
            HttpStatusCode statusCode,
            WebRequest request
    ) {
        var entity = super.handleExceptionInternal(
                ex,
                body,
                headers,
                statusCode,
                request
        );
        if (entity != null) {
            applyInterceptors(ex, entity.getBody());
        }
        return entity;
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
