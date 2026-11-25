package top.swiftx.framework.problem.document;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import top.swiftx.framework.problem.ProblemProperties;
import top.swiftx.framework.problem.markdown.MarkdownParser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 问题类型文档注册表测试。
 */
class DocumentRegistryTest {
    /**
     * 测试从类路径加载问题类型文档。
     */
    @Test
    void loadsDocumentsFromClasspath() {
        ProblemProperties properties = new ProblemProperties();
        DocumentRegistry registry = new DocumentRegistry(
                new PathMatchingResourcePatternResolver(),
                properties,
                new MarkdownParser()
        );

        assertTrue(registry.contains("sample-error"));
        assertEquals(1, registry.findAll().size());
        Document document = registry.find("SAMPLE_ERROR").orElseThrow();
        assertEquals("sample-error", document.slug());
        assertEquals("示例错误", document.title());
        assertEquals(422, document.status());
    }
}
