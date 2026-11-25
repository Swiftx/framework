package top.swiftx.framework.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;

/**
 * 对象存储自动配置。
 *
 * @author gpt-5.6-sol
 */
@Slf4j
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties(StorageProperties.class)
@ConditionalOnProperty(
        name = "framework.storage.enabled",
        matchIfMissing = true
)
public class StorageConfiguration {
    private final ApplicationContext context;
    private final StorageProperties properties;
}
