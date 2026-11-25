package top.swiftx.framework.rest.exception;

import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;

/**
 * 目标资源曾经存在但现在已经失效。
 *
 * @author gpt-5.6-sol
 */
public class GoneException extends RestProblemException {
    /**
     * 创建目标已失效异常。
     *
     * @param e 原始异常；不存在时为空
     */
    public GoneException(@Nullable Throwable e) {
        super(HttpStatus.GONE, "common/gone", "目标已失效", e);
    }

    /**
     * 创建带错误详情的目标已失效异常。
     *
     * @param detail 错误详情
     * @param cause  原始异常；不存在时为空
     * @return 目标已失效异常
     */
    public static GoneException of(
            String detail,
            @Nullable Throwable cause
    ) {
        var exception = new GoneException(cause);
        exception.setDetail(detail);
        return exception;
    }
}
