package top.swiftx.framework.rest.exception;

import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

/**
 * 请求频率超过服务端允许范围。
 *
 * @author gpt-5.6-sol
 */
public class TooManyRequestsException extends RestProblemException {
    /**
     * 响应头。
     */
    private final HttpHeaders headers = new HttpHeaders();

    /**
     * 创建请求过于频繁异常。
     *
     * @param cause 原始异常；不存在时为空
     */
    public TooManyRequestsException(@Nullable Throwable cause) {
        super(
                HttpStatus.TOO_MANY_REQUESTS,
                "common/too-many-requests",
                "请求过于频繁",
                cause
        );
    }

    /**
     * 创建带重试时间的请求过于频繁异常。
     *
     * @param detail            错误详情
     * @param retryAfterSeconds 建议重试等待秒数；不存在时为空
     * @return 请求过于频繁异常
     */
    public static TooManyRequestsException of(
            String detail,
            @Nullable Long retryAfterSeconds
    ) {
        var exception = new TooManyRequestsException(null);
        exception.setDetail(detail);
        exception.withProperty("retryAfterSeconds", retryAfterSeconds);
        if (retryAfterSeconds != null) {
            exception.headers.set(HttpHeaders.RETRY_AFTER, retryAfterSeconds.toString());
        }
        return exception;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HttpHeaders getHeaders() {
        return headers;
    }
}
