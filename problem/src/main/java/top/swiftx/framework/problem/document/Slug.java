package top.swiftx.framework.problem.document;

import org.jspecify.annotations.Nullable;

import java.util.Locale;

/**
 * 问题类型标识工具。
 *
 * @author gpt-5.6-sol
 */
public final class Slug {
    /**
     * 阻止创建工具类实例。
     */
    private Slug() {
    }

    /**
     * 从文件名生成问题类型标识。
     *
     * @param filename 文件名
     * @return 问题类型标识
     */
    public static String fromFilename(@Nullable String filename) {
        if (filename == null) {
            return "";
        }
        return normalize(filename.replaceFirst(
                "(?i)\\.md$",
                ""
        ));
    }

    /**
     * 规范化问题类型标识。
     *
     * @param value 原始值
     * @return 规范化后的问题类型标识
     */
    public static String normalize(@Nullable String value) {
        if (value == null) {
            return "";
        }
        return value.trim()
                .replaceAll("([a-z0-9])([A-Z])", "$1-$2")
                .replace('_', '-')
                .replace(' ', '-')
                .replaceAll("[^A-Za-z0-9-]+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "")
                .toLowerCase(Locale.ROOT);
    }
}
