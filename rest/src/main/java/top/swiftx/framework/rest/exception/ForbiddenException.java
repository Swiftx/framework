package top.swiftx.framework.rest.exception;

import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;

/**
 * 当前凭证没有执行请求所需的权限。
 *
 * @author gpt-5.6-sol
 */
public class ForbiddenException extends RestProblemException {
    /**
     * 创建权限不足异常。
     *
     * @param e 原始异常；不存在时为空
     */
    public ForbiddenException(@Nullable Throwable e) {
        super(HttpStatus.FORBIDDEN, "common/forbidden", "权限不足", e);
    }

    /**
     * 创建带错误详情的权限不足异常。
     *
     * @param detail 错误详情
     * @param cause  原始异常；不存在时为空
     * @return 权限不足异常
     */
    public static ForbiddenException of(
            String detail,
            @Nullable Throwable cause
    ) {
        var exception = new ForbiddenException(cause);
        exception.setDetail(detail);
        return exception;
    }
}
