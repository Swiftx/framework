package top.swiftx.framework.problem.document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import top.swiftx.framework.problem.ProblemProperties;
import top.swiftx.framework.problem.markdown.MarkdownParser;
import top.swiftx.framework.problem.markdown.ParsedDocument;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 问题类型文档注册表。
 *
 * @author gpt-5.6-sol
 */
public class DocumentRegistry {
    private static final Logger log = LoggerFactory.getLogger(DocumentRegistry.class);

    private final Map<String, Document> documents;
    private final List<Document> sortedDocuments;

    /**
     * 创建问题类型文档注册表。
     *
     * @param resolver   资源解析器
     * @param properties 问题类型文档配置
     * @param parser     Markdown 解析器
     */
    public DocumentRegistry(
            ResourcePatternResolver resolver,
            ProblemProperties properties,
            MarkdownParser parser
    ) {
        Map<String, Document> loaded = load(resolver, properties, parser);
        documents = Collections.unmodifiableMap(loaded);
        sortedDocuments = loaded.values().stream()
                .sorted(Comparator.comparing(Document::slug))
                .toList();
        log.info("已加载 {} 个问题类型文档", documents.size());
    }

    /**
     * 按文档标识查找文档。
     *
     * @param slug 文档标识
     * @return 文档结果
     */
    public Optional<Document> find(String slug) {
        return Optional.ofNullable(documents.get(Slug.normalize(slug)));
    }

    /**
     * 获取全部文档。
     *
     * @return 文档列表
     */
    public List<Document> findAll() {
        return sortedDocuments;
    }

    /**
     * 判断文档是否存在。
     *
     * @param slug 文档标识
     * @return 是否存在
     */
    public boolean contains(String slug) {
        return documents.containsKey(Slug.normalize(slug));
    }

    /**
     * 加载配置指定的全部文档。
     *
     * @param resolver   资源解析器
     * @param properties 问题类型文档配置
     * @param parser     Markdown 解析器
     * @return 文档映射
     */
    private Map<String, Document> load(
            ResourcePatternResolver resolver,
            ProblemProperties properties,
            MarkdownParser parser
    ) {
        Map<String, Document> loaded = new LinkedHashMap<>();
        for (String location : properties.getLocations()) {
            try {
                Resource[] resources = resolver.getResources(location);
                for (Resource resource : resources) {
                    loadResource(
                            loaded,
                            resource,
                            parser,
                            properties.isFailOnInvalid()
                    );
                }
            } catch (IOException ex) {
                handleInvalid(
                        "扫描问题类型文档失败：" + location, ex,
                        properties.isFailOnInvalid()
                );
            }
        }
        return loaded;
    }

    /**
     * 加载单个文档资源。
     *
     * @param loaded        已加载文档
     * @param resource      文档资源
     * @param parser        Markdown 解析器
     * @param failOnInvalid 文档无效时是否失败
     */
    private void loadResource(
            Map<String, Document> loaded,
            Resource resource,
            MarkdownParser parser,
            boolean failOnInvalid
    ) {
        String source = resource.getDescription();
        try {
            if (!resource.isReadable()) {
                return;
            }
            String slug = Slug.fromFilename(resource.getFilename());
            if (slug.isBlank()) {
                throw new IllegalArgumentException(
                        source + " 的问题类型标识为空"
                );
            }
            if (loaded.containsKey(slug)) {
                throw new IllegalArgumentException(
                        source + " 的问题类型标识重复：" + slug
                );
            }

            String markdown = new String(
                    resource.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8
            );
            ParsedDocument content = parser.parse(markdown, source);
            loaded.put(slug, new Document(
                    slug,
                    content.title(),
                    content.status(),
                    content.description(),
                    content.bodyHtml(),
                    source
            ));
        } catch (Exception ex) {
            handleInvalid(
                    "无效的问题类型文档：" + source,
                    ex,
                    failOnInvalid
            );
        }
    }

    /**
     * 处理无效文档。
     *
     * @param message       错误消息
     * @param ex            异常
     * @param failOnInvalid 文档无效时是否失败
     */
    private void handleInvalid(
            String message,
            Exception ex,
            boolean failOnInvalid
    ) {
        if (failOnInvalid) {
            throw new IllegalStateException(message, ex);
        } else {
            log.warn(message, ex);
        }
    }
}
