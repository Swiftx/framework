package top.swiftx.framework.problem.markdown;

import org.commonmark.Extension;
import org.commonmark.ext.front.matter.YamlFrontMatterExtension;
import org.commonmark.ext.front.matter.YamlFrontMatterVisitor;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * 问题类型 Markdown 解析器。
 *
 * @author gpt-5.6-sol
 */
public class MarkdownParser {
    private final Parser parser;
    private final HtmlRenderer renderer;

    /**
     * 创建问题类型 Markdown 解析器。
     */
    public MarkdownParser() {
        List<Extension> extensions = List.of(
                TablesExtension.create(),
                YamlFrontMatterExtension.create()
        );
        parser = Parser.builder()
                .extensions(extensions)
                .build();
        renderer = HtmlRenderer.builder()
                .extensions(extensions)
                .escapeHtml(true)
                .sanitizeUrls(true)
                .build();
    }

    /**
     * 解析 Markdown 文档。
     *
     * @param markdown Markdown 内容
     * @param source   文档来源
     * @return 文档解析结果
     */
    public ParsedDocument parse(String markdown, String source) {
        Node document = parser.parse(markdown);
        YamlFrontMatterVisitor visitor = new YamlFrontMatterVisitor();
        document.accept(visitor);

        Map<String, List<String>> metadata = visitor.getData();
        String title = value(metadata, "title");
        String status = value(metadata, "status");
        String description = value(metadata, "description");

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException(source + " 缺少头部字段：title");
        }
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException(source + " 缺少头部字段：status");
        }

        int statusCode;
        try {
            statusCode = Integer.parseInt(status);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(source + " 的状态码无效：" + status, ex);
        }
        if (statusCode < 100 || statusCode > 599) {
            throw new IllegalArgumentException(source + " 的状态码必须在 100 到 599 之间");
        }

        String bodyHtml = renderer.render(document);
        return new ParsedDocument(
                title.trim(),
                statusCode,
                description == null ? "" : description.trim(),
                bodyHtml
        );
    }

    /**
     * 获取头部字段值。
     *
     * @param metadata 头部字段
     * @param key      字段名
     * @return 字段值
     */
    private @Nullable String value(
            Map<String, List<String>> metadata,
            String key
    ) {
        List<String> values = metadata.get(key);
        if (values == null || values.isEmpty()) {
            return null;
        }
        return values.getFirst();
    }
}
