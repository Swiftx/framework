package top.swiftx.framework.rest.precondition;

import org.jspecify.annotations.Nullable;
import org.springframework.http.ETag;
import top.swiftx.framework.rest.exception.BadRequestException;
import top.swiftx.framework.rest.exception.ConflictException;
import top.swiftx.framework.rest.exception.PreconditionRequiredException;

import java.util.regex.Pattern;

/**
 * 基于资源修订号处理强 ETag 与 If-Match 写入前置条件。
 *
 * @author gpt-5.6-sol
 * @see ETag
 * @see <a href="https://www.rfc-editor.org/rfc/rfc9110.html#section-13.1.1">RFC 9110 If-Match</a>
 */
public final class EntityTags {

    /**
     * 当前资源写入契约接受的单个强数字实体标签格式。
     */
    private static final Pattern STRONG_REVISION_TAG = Pattern.compile("\"[0-9]+\"");

    /**
     * 工具类不允许实例化。
     */
    private EntityTags() {
    }

    /**
     * 将非负资源修订号格式化为强 ETag。
     *
     * @param revision 资源修订号
     * @return 带双引号的强 ETag
     * @throws IllegalArgumentException 修订号为负数
     */
    public static String format(long revision) {
        return fromRevision(revision).formattedTag();
    }

    /**
     * 校验请求提供了当前资源的单个强 ETag。
     *
     * @param ifMatch If-Match 请求头
     * @param revision 当前资源修订号
     * @throws PreconditionRequiredException 请求未提供 If-Match
     * @throws BadRequestException If-Match 不符合单个强数字标签格式
     * @throws ConflictException If-Match 与当前修订号不一致
     * @throws IllegalArgumentException 当前资源修订号为负数
     */
    public static void requireCurrent(
            @Nullable String ifMatch,
            long revision
    ) {
        // 缺少前置条件时使用专用 428 问题响应。
        if (ifMatch == null || ifMatch.isBlank()) {
            throw PreconditionRequiredException.of("修改资源必须提供 If-Match 请求头");
        }

        // 当前契约只接受一个由资源服务产生的强数字 ETag。
        if (!STRONG_REVISION_TAG.matcher(ifMatch).matches()) {
            throw BadRequestException.of("If-Match 必须是资源返回的单个强 ETag");
        }
        var actual = ETag.create(ifMatch);
        if (actual.compare(fromRevision(revision), true)) return;

        throw ConflictException.of("资源已被其他请求修改，请重新加载最新版本");
    }

    /**
     * 将资源修订号转换为 Spring 标准实体标签。
     *
     * @param revision 资源修订号
     * @return 强实体标签
     * @throws IllegalArgumentException 修订号为负数
     */
    private static ETag fromRevision(long revision) {
        if (revision < 0) throw new IllegalArgumentException("资源修订号不能为负数");
        return new ETag(Long.toString(revision), false);
    }
}
