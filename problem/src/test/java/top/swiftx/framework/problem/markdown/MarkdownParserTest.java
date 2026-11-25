package top.swiftx.framework.problem.markdown;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 问题类型 Markdown 解析器测试。
 */
class MarkdownParserTest {
    /**
     * 测试解析头部字段和 Markdown 正文。
     */
    @Test
    void parsesFrontMatterAndMarkdown() {
        String markdown = """
                ---
                title: 校验失败
                status: 422
                description: 请求内容不符合要求。
                ---

                # 校验失败

                | 字段 | 消息 |
                | --- | --- |
                | name | 必填 |

                <script>alert("bad")</script>
                """;

        ParsedDocument content = new MarkdownParser()
                .parse(markdown, "inline");

        assertEquals("校验失败", content.title());
        assertEquals(422, content.status());
        assertEquals("请求内容不符合要求。", content.description());
        assertTrue(content.bodyHtml().contains("<table>"));
        assertTrue(content.bodyHtml().contains("&lt;script&gt;"));
        assertFalse(content.bodyHtml().contains("<script>"));
    }
}
