package top.swiftx.framework.rest.exception;

import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;

/**
 * 资源状态与当前请求发生冲突。
 *
 * @author gpt-5.6-sol
 */
public class ConflictException extends RestProblemException {
    /**
     * 创建资源冲突异常。
     *
     * @param e 原始异常；不存在时为空
     */
    public ConflictException(@Nullable Throwable e) {
        super(HttpStatus.CONFLICT, "common/conflict", "冲突", e);
    }

    /**
     * 创建业务规则冲突异常。
     *
     * @return 业务规则冲突异常
     */
    public static ConflictException ofBusiness() {
        var e = new ConflictException(null);
        e.setDetail("业务规则冲突");
        return e;
    }

    /**
     * 创建带错误详情且没有原始异常的资源冲突异常。
     *
     * @param detail 错误详情
     * @return 资源冲突异常
     */
    public static ConflictException of(String detail) {
        return of(detail, null);
    }

    /**
     * 创建带错误详情的资源冲突异常。
     *
     * @param detail 错误详情
     * @param cause  原始异常；不存在时为空
     * @return 资源冲突异常
     */
    public static ConflictException of(
            String detail,
            @Nullable Throwable cause
    ) {
        var exception = new ConflictException(cause);
        exception.setDetail(detail);
        return exception;
    }
}
