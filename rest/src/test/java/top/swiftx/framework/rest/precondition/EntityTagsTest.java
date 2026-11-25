package top.swiftx.framework.rest.precondition;

import org.junit.jupiter.api.Test;
import top.swiftx.framework.rest.exception.BadRequestException;
import top.swiftx.framework.rest.exception.ConflictException;
import top.swiftx.framework.rest.exception.PreconditionRequiredException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * HTTP 实体标签前置条件契约测试。
 *
 * @author gpt-5.6-sol
 */
class EntityTagsTest {

    /**
     * 验证修订号会被格式化为标准强实体标签。
     */
    @Test
    void formatsStrongRevisionTag() {
        assertThat(EntityTags.format(42)).isEqualTo("\"42\"");
    }

    /**
     * 验证当前强实体标签可以通过写入前置条件校验。
     */
    @Test
    void acceptsCurrentStrongTag() {
        EntityTags.requireCurrent("\"42\"", 42);
    }

    /**
     * 验证缺失、格式错误和过期的前置条件具有不同错误语义。
     */
    @Test
    void distinguishesMissingMalformedAndStaleTags() {
        // 缺失 If-Match 表示调用方没有提供必需的写入前置条件。
        assertThatThrownBy(() -> EntityTags.requireCurrent(null, 42))
                .isInstanceOf(PreconditionRequiredException.class);

        // 弱标签和多值标签不符合当前资源写入契约。
        assertThatThrownBy(() -> EntityTags.requireCurrent("W/\"42\"", 42))
                .isInstanceOf(BadRequestException.class);
        assertThatThrownBy(() -> EntityTags.requireCurrent("\"41\", \"42\"", 42))
                .isInstanceOf(BadRequestException.class);

        // 合法但过期的强标签表示并发写入冲突。
        assertThatThrownBy(() -> EntityTags.requireCurrent("\"41\"", 42))
                .isInstanceOf(ConflictException.class);
    }
}
