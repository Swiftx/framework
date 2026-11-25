package top.swiftx.framework.storage;

import lombok.Data;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * REST 框架配置。
 *
 * @author gpt-5.6-sol
 */
@Data
@ConfigurationProperties(prefix = "framework.rest")
public class StorageProperties {
    /**
     * 是否启用
     */
    private boolean enabled = true;

    /**
     * CORS配置
     * 举例
     */
    private @Nullable Map<String, CORS> cors;

    /**
     * 跨域配置。
     *
     * @author gpt-5.6-sol
     */
    @Data
    public static class CORS {
        /**
         * 是否启用
         */
        private boolean enabled = true;

        /**
         * 允许的方法
         */
        private String allowedMethods = "*";

        /**
         * 允许的来源
         */
        private String allowedOrigins = "*";

        /**
         * 允许的请求头
         */
        private String allowedHeaders = "*";
    }

}
