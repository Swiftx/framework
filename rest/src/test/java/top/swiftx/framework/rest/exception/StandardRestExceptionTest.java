package top.swiftx.framework.rest.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 标准 REST 异常工厂契约测试。
 *
 * @author gpt-5.6-sol
 */
class StandardRestExceptionTest {

    /**
     * 验证冲突异常可以直接携带调用方提供的安全详情。
     */
    @Test
    void conflictFactoryUsesProvidedDetail() {
        // 构造异常并同时验证状态码、详情与根因契约。
        var exception = ConflictException.of("资源名称已存在");

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(exception.getBody().getDetail()).isEqualTo("资源名称已存在");
        assertThat(exception.getCause()).isNull();
    }

    /**
     * 验证资源不存在异常可以统一组合资源名称和标识。
     */
    @Test
    void notFoundFactoryUsesResourceNameAndId() {
        // 构造异常并验证统一的资源身份详情。
        var exception = NotFoundException.ofResource("用户", 42L);

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exception.getBody().getDetail()).isEqualTo("用户不存在: 42");
        assertThat(exception.getCause()).isNull();
    }
}
