package top.swiftx.framework.rest.exception;

import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;

/**
 * 请求语法、格式或参数不符合接口契约。
 *
 * @author gpt-5.6-sol
 */
public class BadRequestException extends RestProblemException {
    /**
     * 创建无效请求异常。
     *
     * @param cause 原始异常；不存在时为空
     */
    public BadRequestException(@Nullable Throwable cause) {
        super(HttpStatus.BAD_REQUEST, "common/bad-request", "请求无效", cause);
    }

    /**
     * 创建带错误详情的无效请求异常。
     *
     * @param detail 错误详情
     * @return 无效请求异常
     */
    public static BadRequestException of(String detail) {
        var exception = new BadRequestException(null);
        exception.setDetail(detail);
        return exception;
    }

    /**
     * 创建带错误详情和原始异常的无效请求异常。
     *
     * @param detail 错误详情
     * @param cause  原始异常；不存在时为空
     * @return 无效请求异常
     */
    public static BadRequestException of(
            String detail,
            @Nullable Throwable cause
    ) {
        var exception = new BadRequestException(cause);
        exception.setDetail(detail);
        return exception;
    }
}
