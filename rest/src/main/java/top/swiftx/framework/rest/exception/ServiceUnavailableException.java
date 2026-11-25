package top.swiftx.framework.rest.exception;

import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;

/**
 * 当前依赖的服务暂时不可用。
 *
 * @author gpt-5.6-sol
 */
public class ServiceUnavailableException extends RestProblemException {
    /**
     * 创建服务暂时不可用异常。
     *
     * @param cause 原始异常；不存在时为空
     */
    public ServiceUnavailableException(@Nullable Throwable cause) {
        super(
                HttpStatus.SERVICE_UNAVAILABLE,
                "common/service-unavailable",
                "服务暂时不可用",
                cause
        );
    }

    /**
     * 创建带错误详情的服务暂时不可用异常。
     *
     * @param detail 错误详情
     * @param cause  原始异常；不存在时为空
     * @return 服务暂时不可用异常
     */
    public static ServiceUnavailableException of(
            String detail,
            @Nullable Throwable cause
    ) {
        var exception = new ServiceUnavailableException(cause);
        exception.setDetail(detail);
        return exception;
    }
}
