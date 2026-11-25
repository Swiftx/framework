package top.swiftx.framework.problem.markdown;

/**
 * 问题类型文档解析结果。
 *
 * @param title       文档标题
 * @param status      HTTP 状态码
 * @param description 文档描述
 * @param bodyHtml    正文 HTML
 */
public record ParsedDocument(
        String title,
        int status,
        String description,
        String bodyHtml
) {
}
