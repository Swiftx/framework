package top.swiftx.framework.problem.document;

/**
 * 问题类型文档。
 *
 * @param slug        文档标识
 * @param title       文档标题
 * @param status      HTTP 状态码
 * @param description 文档描述
 * @param bodyHtml    正文 HTML
 * @param source      文档来源
 */
public record Document(
        String slug,
        String title,
        int status,
        String description,
        String bodyHtml,
        String source
) {
}
