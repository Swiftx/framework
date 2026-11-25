package top.swiftx.framework.rest.exception;

import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;

/**
 * 上游服务返回了无效响应。
 *
 * @author gpt-5.6-sol
 */
public class BadGatewayException extends RestProblemException {
    /**
     * 创建上游服务响应异常。
     *
     * @param cause 原始异常；不存在时为空
     */
    public BadGatewayException(@Nullable Throwable cause) {
        super(HttpStatus.BAD_GATEWAY, "common/bad-gateway", "上游服务响应异常", cause);
    }

    /**
     * 创建带错误详情的上游服务响应异常。
     *
     * @param detail 错误详情
     * @param cause  原始异常；不存在时为空
     * @return 上游服务响应异常
     */
    public static BadGatewayException of(
            String detail,
            @Nullable Throwable cause
    ) {
        var exception = new BadGatewayException(cause);
        exception.setDetail(detail);
        return exception;
    }
}
