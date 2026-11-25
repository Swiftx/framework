package top.swiftx.framework.problem.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.swiftx.framework.problem.document.DocumentRegistry;
import top.swiftx.framework.problem.render.PageRenderer;

/**
 * 问题类型文档控制器。
 */
@Controller
@RequestMapping("${framework.problem.path-prefix:/problems}")
public class DocumentController {
    private final DocumentRegistry registry;
    private final PageRenderer renderer;

    /**
     * 创建问题类型文档控制器。
     *
     * @param registry 文档注册表
     * @param renderer 页面渲染器
     */
    public DocumentController(
            DocumentRegistry registry,
            PageRenderer renderer
    ) {
        this.registry = registry;
        this.renderer = renderer;
    }

    /**
     * 返回问题类型文档索引页面。
     *
     * @return HTML 响应
     */
    @ResponseBody
    @GetMapping(value = {"", "/"}, produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> index() {
        return html(HttpStatus.OK, renderer.renderIndex(registry.findAll()));
    }

    /**
     * 返回指定问题类型文档页面。
     *
     * @param slug 文档标识
     * @return HTML 响应
     */
    @ResponseBody
    @GetMapping(value = "/{slug}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> document(@PathVariable String slug) {
        return registry.find(slug)
                .map(document -> html(
                        HttpStatus.OK,
                        renderer.renderDocument(document)
                )).orElseGet(() -> html(
                        HttpStatus.NOT_FOUND,
                        renderer.renderNotFound(slug, registry.findAll())
                ));
    }

    /**
     * 构造 HTML 响应。
     *
     * @param status HTTP 状态
     * @param body   响应正文
     * @return HTML 响应
     */
    private ResponseEntity<String> html(HttpStatus status, String body) {
        return ResponseEntity.status(status)
                .contentType(MediaType.TEXT_HTML)
                .body(body);
    }
}
