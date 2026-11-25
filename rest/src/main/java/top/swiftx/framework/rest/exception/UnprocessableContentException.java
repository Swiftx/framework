package top.swiftx.framework.rest.exception;

import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;

/**
 * 请求语法正确，但其内容无法按资源语义处理。
 *
 * @author gpt-5.6-sol
 */
public class UnprocessableContentException extends RestProblemException {
    /**
     * 创建无法处理请求内容异常。
     *
     * @param cause 原始异常；不存在时为空
     */
    public UnprocessableContentException(@Nullable Throwable cause) {
        super(
                HttpStatus.UNPROCESSABLE_CONTENT,
                "common/unprocessable-content",
                "无法处理请求内容",
                cause
        );
    }

    /**
     * 创建带错误详情和原始异常的无法处理请求内容异常。
     *
     * @param detail 错误详情
     * @param cause  原始异常；不存在时为空
     * @return 无法处理请求内容异常
     */
    public static UnprocessableContentException of(
            String detail,
            @Nullable Throwable cause
    ) {
        var exception = new UnprocessableContentException(cause);
        exception.setDetail(detail);
        return exception;
    }
}
