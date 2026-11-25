package top.swiftx.framework.rest.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.jspecify.annotations.Nullable;

import java.io.Serializable;
import java.util.List;

/**
 * 页码分页响应。
 *
 * @param <T> 数据项类型
 * @author gpt-5.6-sol
 */
@Schema(description = "分页响应")
@Getter
public class PageableResponse<T> implements Serializable {
    @Schema(description = "元数据")
    private @Nullable Meta meta;

    @Schema(description = "数据项")
    private @Nullable List<T> data;

    /**
     * 创建待反序列化的空分页响应。
     */
    public PageableResponse() {
    }

    /**
     * 页码分页元数据。
     *
     * @author gpt-5.6-sol
     */
    public static class Meta {
        @Getter
        @Schema(description = "总记录数")
        private @Nullable Long total;

        @Getter
        @Schema(description = "是否还有数据")
        private boolean next;

        /**
         * 创建待反序列化的空分页元数据。
         */
        public Meta() {
        }

    }
}
