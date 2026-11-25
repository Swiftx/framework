package top.swiftx.framework.rest.exception;

import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;

/**
 * 等待上游服务响应超时。
 *
 * @author gpt-5.6-sol
 */
public class GatewayTimeoutException extends RestProblemException {
    /**
     * 创建上游服务响应超时异常。
     *
     * @param cause 原始异常；不存在时为空
     */
    public GatewayTimeoutException(@Nullable Throwable cause) {
        super(HttpStatus.GATEWAY_TIMEOUT, "common/gateway-timeout", "上游服务响应超时", cause);
    }

    /**
     * 创建带错误详情的上游服务响应超时异常。
     *
     * @param detail 错误详情
     * @param cause  原始异常；不存在时为空
     * @return 上游服务响应超时异常
     */
    public static GatewayTimeoutException of(
            String detail,
            @Nullable Throwable cause
    ) {
        var exception = new GatewayTimeoutException(cause);
        exception.setDetail(detail);
        return exception;
    }
}
