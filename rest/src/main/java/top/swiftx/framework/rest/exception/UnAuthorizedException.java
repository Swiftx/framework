package top.swiftx.framework.rest.exception;

import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;

/**
 * 请求缺少有效认证凭证。
 *
 * @author gpt-5.6-sol
 */
public class UnAuthorizedException extends RestProblemException {
    /**
     * 创建认证异常。
     *
     * @param e 原始异常；不存在时为空
     */
    public UnAuthorizedException(@Nullable Throwable e) {
        super(HttpStatus.UNAUTHORIZED, "common/unauthorized", "缺少有效的凭证", e);
    }

    /**
     * 创建认证信息无效异常。
     *
     * @return 认证信息无效异常
     */
    public static UnAuthorizedException ofInvalid() {
        var e = new UnAuthorizedException(null);
        e.setDetail("认证信息过期或无效");
        return e;
    }

    /**
     * 创建带原始异常的认证信息无效异常。
     *
     * @param source 原始异常
     * @return 认证信息无效异常
     */
    public static UnAuthorizedException ofInvalid(Throwable source) {
        var e = new UnAuthorizedException(source);
        e.setDetail("认证信息过期或无效");
        return e;
    }

    /**
     * 创建认证信息缺失异常。
     *
     * @return 认证信息缺失异常
     */
    public static UnAuthorizedException ofMissing() {
        var e = new UnAuthorizedException(null);
        e.setDetail("认证信息缺失");
        return e;
    }

    /**
     * 创建认证失败异常。
     *
     * @return 认证失败异常
     */
    public static UnAuthorizedException ofFailed() {
        var e = new UnAuthorizedException(null);
        e.setDetail("提供了凭证但认证失败");
        return e;
    }
}
