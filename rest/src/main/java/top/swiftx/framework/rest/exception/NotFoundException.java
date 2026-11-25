package top.swiftx.framework.rest.exception;

import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;

/**
 * 请求的资源不存在。
 *
 * @author gpt-5.6-sol
 */
public class NotFoundException extends RestProblemException {
    /**
     * 创建资源不存在异常。
     *
     * @param cause 原始异常；不存在时为空
     */
    public NotFoundException(@Nullable Throwable cause) {
        super(HttpStatus.NOT_FOUND, "common/not-found", "访问目标不存在", cause);
    }

    /**
     * 创建使用默认详情的资源不存在异常。
     *
     * @return 资源不存在异常
     */
    public static NotFoundException ofResource() {
        return ofResource("资源不存在");
    }

    /**
     * 创建带错误详情的资源不存在异常。
     *
     * @param detail 错误详情
     * @return 资源不存在异常
     */
    public static NotFoundException ofResource(String detail) {
        var exception = new NotFoundException(null);
        exception.setDetail(detail);
        return exception;
    }

    /**
     * 创建包含资源名称和标识的资源不存在异常。
     *
     * @param resourceName 资源名称
     * @param resourceId   资源标识
     * @return 资源不存在异常
     */
    public static NotFoundException ofResource(
            String resourceName,
            Object resourceId
    ) {
        return ofResource(resourceName + "不存在: " + resourceId);
    }
}
