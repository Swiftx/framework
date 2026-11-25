package top.swiftx.framework.rest.exception;

import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;

/**
 * 执行请求所需的前置条件缺失。
 *
 * @author gpt-5.6-sol
 */
public class PreconditionRequiredException extends RestProblemException {
    /**
     * 创建缺少请求前置条件异常。
     *
     * @param cause 原始异常；不存在时为空
     */
    public PreconditionRequiredException(@Nullable Throwable cause) {
        super(
                HttpStatus.PRECONDITION_REQUIRED,
                "common/precondition-required",
                "缺少请求前置条件",
                cause
        );
    }

    /**
     * 创建带错误详情的缺少请求前置条件异常。
     *
     * @param detail 错误详情
     * @return 缺少请求前置条件异常
     */
    public static PreconditionRequiredException of(String detail) {
        var exception = new PreconditionRequiredException(null);
        exception.setDetail(detail);
        return exception;
    }
}
