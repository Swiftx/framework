package top.swiftx.framework.rest.exception;

import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.ErrorResponseException;

import java.net.URI;

/**
 * framework/rest 标准问题详情异常的公共基类。
 *
 * @author gpt-5.6-sol
 */
public abstract class RestProblemException extends ErrorResponseException {
    /**
     * 创建标准问题详情异常。
     *
     * @param status HTTP 状态
     * @param type   问题类型
     * @param title  问题标题
     * @param cause  原始异常；不存在时为空
     */
    protected RestProblemException(
            HttpStatusCode status,
            String type,
            String title,
            @Nullable Throwable cause
    ) {
        super(status, cause);
        setType(URI.create(type));
        setTitle(title);
    }

    /**
     * 在值存在时添加问题详情扩展属性。
     *
     * @param name  属性名称
     * @param value 属性值；不存在时为空
     * @return 当前异常
     */
    public RestProblemException withProperty(
            String name,
            @Nullable Object value
    ) {
        if (value != null) {
            getBody().setProperty(name, value);
        }
        return this;
    }
}
