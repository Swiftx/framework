package top.swiftx.framework.problem.render;

import org.jspecify.annotations.Nullable;
import top.swiftx.framework.problem.document.Document;
import top.swiftx.framework.problem.document.Slug;
import top.swiftx.framework.problem.uri.TypeUriResolver;

import java.net.URI;
import java.util.List;

/**
 * 问题类型页面渲染器。
 *
 * @author gpt-5.6-sol
 */
public class PageRenderer {
    private static final String BOOTSTRAP_CSS = "/webjars/bootstrap/5.3.8/css/bootstrap.min.css";

    private final TypeUriResolver uriResolver;

    /**
     * 创建问题类型页面渲染器。
     *
     * @param uriResolver URI 解析器
     */
    public PageRenderer(TypeUriResolver uriResolver) {
        this.uriResolver = uriResolver;
    }

    /**
     * 渲染文档索引页面。
     *
     * @param documents 文档列表
     * @return HTML 页面
     */
    public String renderIndex(List<Document> documents) {
        StringBuilder content = new StringBuilder();
        content.append("""
                <section class="problem-hero rounded-3 p-4 p-md-5 mb-4">
                  <div class="d-flex flex-column flex-md-row gap-3 justify-content-between align-items-md-end">
                    <div>
                      <span class="badge text-bg-light mb-3">RFC 9457</span>
                      <h1 class="display-6 fw-semibold mb-2">问题类型文档</h1>
                      <p class="lead mb-0">用于说明 API 问题详情类型的可读文档。</p>
                    </div>
                    <div class="text-md-end">
                      <div class="small text-white-50">已注册类型</div>
                      <div class="fs-2 fw-semibold">""").append(documents.size()).append("""
                </div>
                    </div>
                  </div>
                </section>
                """);

        if (documents.isEmpty()) {
            content.append("""
                    <div class="alert alert-secondary" role="alert">
                      当前服务未注册问题类型文档。
                    </div>
                    """);
        } else {
            content.append("<div class=\"list-group shadow-sm\">\n");
            for (Document document : documents) {
                content.append("<a class=\"list-group-item list-group-item-action p-4\" href=\"")
                        .append(escape(uriResolver.resolvePublicPath(document.slug())))
                        .append("\">")
                        .append("<div class=\"d-flex flex-column flex-md-row gap-2 justify-content-between\">")
                        .append("<div><h2 class=\"h5 mb-1\">")
                        .append(escape(document.title()))
                        .append("</h2><p class=\"text-body-secondary mb-0\">")
                        .append(escape(document.description()))
                        .append("</p></div>")
                        .append("<div class=\"d-flex align-items-start gap-2\">")
                        .append("<span class=\"badge ")
                        .append(statusClass(document.status()))
                        .append("\">HTTP ")
                        .append(document.status())
                        .append("</span><code>")
                        .append(escape(document.slug()))
                        .append("</code></div></div></a>\n");
            }
            content.append("</div>\n");
        }
        return layout("问题类型文档", content.toString());
    }

    /**
     * 渲染单个文档页面。
     *
     * @param document 问题类型文档
     * @return HTML 页面
     */
    public String renderDocument(Document document) {
        String type = uriResolver.resolvePublicType(document.slug())
                .map(URI::toString)
                .orElse(uriResolver.resolvePublicPath(document.slug()));

        String content = """
                <nav class="mb-4">
                  <a class="link-secondary text-decoration-none" href="%s">&larr; 问题类型文档</a>
                </nav>
                <article class="problem-card bg-body rounded-3 shadow-sm">
                  <header class="border-bottom p-4 p-md-5">
                    <div class="d-flex flex-wrap gap-2 mb-3">
                      <span class="badge %s">HTTP %s</span>
                      <span class="badge text-bg-secondary">%s</span>
                    </div>
                    <h1 class="fw-semibold mb-3">%s</h1>
                    <p class="lead text-body-secondary mb-4">%s</p>
                    <div class="type-uri p-3 rounded-2">
                      <div class="small text-uppercase text-body-secondary fw-semibold mb-1">类型 URI</div>
                      <code>%s</code>
                    </div>
                  </header>
                  <div class="problem-markdown p-4 p-md-5">
                    %s
                  </div>
                </article>
                """.formatted(
                escape(uriResolver.resolvePublicPath("")),
                escape(statusClass(document.status())),
                document.status(),
                escape(document.slug()),
                escape(document.title()),
                escape(document.description()),
                escape(type),
                document.bodyHtml()
        );
        return layout(document.title(), content);
    }

    /**
     * 渲染未找到文档页面。
     *
     * @param slug      文档标识
     * @param documents 可用文档列表
     * @return HTML 页面
     */
    public String renderNotFound(String slug, List<Document> documents) {
        StringBuilder content = new StringBuilder();
        content.append("""
                <section class="problem-card bg-body rounded-3 shadow-sm p-4 p-md-5">
                  <span class="badge text-bg-warning mb-3">404</span>
                  <h1 class="fw-semibold mb-3">问题类型文档不存在</h1>
                  <p class="text-body-secondary">未注册对应文档：<code>""")
                .append(escape(Slug.normalize(slug)))
                .append("""
                </code>.</p>
                  <a class="btn btn-primary" href=\"""")
                .append(escape(uriResolver.resolvePublicPath("")))
                .append("""
                ">查看已注册类型</a>
                </section>
                """);
        if (!documents.isEmpty()) {
            content.append("<div class=\"mt-4\"><h2 class=\"h5 mb-3\">可用类型</h2><div class=\"d-flex flex-wrap gap-2\">");
            for (Document document : documents) {
                content.append("<a class=\"badge rounded-pill text-bg-light text-decoration-none border\" href=\"")
                        .append(escape(uriResolver.resolvePublicPath(document.slug())))
                        .append("\">")
                        .append(escape(document.slug()))
                        .append("</a>");
            }
            content.append("</div></div>");
        }
        return layout("问题类型文档不存在", content.toString());
    }

    /**
     * 渲染完整页面布局。
     *
     * @param title   页面标题
     * @param content 页面内容
     * @return HTML 页面
     */
    private String layout(String title, String content) {
        return """
                <!doctype html>
                <html lang="zh-CN" data-bs-theme="light">
                <head>
                  <meta charset="utf-8">
                  <meta name="viewport" content="width=device-width, initial-scale=1">
                  <title>%s</title>
                  <link rel="stylesheet" href="%s">
                  <style>
                    body { background: #f5f7fb; }
                    .page-shell { max-width: 1040px; }
                    .problem-hero { background: linear-gradient(135deg, #1d4ed8, #0f766e); color: #fff; }
                    .problem-card { border: 1px solid rgba(15, 23, 42, .08); }
                    .type-uri { background: #f8fafc; border: 1px solid #e2e8f0; overflow-wrap: anywhere; }
                    .problem-markdown { font-size: 1.02rem; line-height: 1.75; }
                    .problem-markdown h1, .problem-markdown h2, .problem-markdown h3 { margin-top: 1.75rem; margin-bottom: .75rem; }
                    .problem-markdown h1:first-child, .problem-markdown h2:first-child, .problem-markdown h3:first-child { margin-top: 0; }
                    .problem-markdown pre { padding: 1rem; border-radius: .5rem; background: #0f172a; color: #e2e8f0; overflow-x: auto; }
                    .problem-markdown code { color: #be123c; }
                    .problem-markdown pre code { color: inherit; }
                    .problem-markdown table { width: 100%%; margin: 1rem 0; border-collapse: collapse; }
                    .problem-markdown th, .problem-markdown td { padding: .75rem; border: 1px solid #dee2e6; }
                    .problem-markdown th { background: #f8f9fa; }
                  </style>
                </head>
                <body>
                  <main class="page-shell container py-4 py-md-5">
                    %s
                  </main>
                </body>
                </html>
                """.formatted(escape(title), BOOTSTRAP_CSS, content);
    }

    /**
     * 获取状态码样式。
     *
     * @param status HTTP 状态码
     * @return 样式类名
     */
    private String statusClass(int status) {
        if (status >= 500) {
            return "text-bg-danger";
        }
        if (status >= 400) {
            return "text-bg-warning";
        }
        if (status >= 300) {
            return "text-bg-info";
        }
        return "text-bg-success";
    }

    /**
     * 转义 HTML 文本。
     *
     * @param value 原始文本
     * @return 转义后的文本
     */
    private String escape(@Nullable String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
