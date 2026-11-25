package top.swiftx.framework.rest.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jspecify.annotations.Nullable;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * 增量数据集响应。
 *
 * @param <T> 修改项目类型
 * @param <K> 移除项目标识类型
 * @author gpt-5.6-sol
 */
@Schema(description = "增量响应")
public class DeltaSetResponse<T, K> implements Serializable {
    @Schema(description = "元数据")
    private @Nullable Meta meta;

    @Schema(description = "修改项目列表")
    private @Nullable Set<T> changes;

    @Schema(description = "移除项目ID")
    private @Nullable Set<K> removes;

    /**
     * 增量响应元数据。
     *
     * @author gpt-5.6-sol
     */
    public static class Meta {
        @Schema(description = "数据接口版本")
        private @Nullable String apiVersion;

        @Schema(description = "数据截止时间")
        private @Nullable Date lastRequested;

        @Schema(description = "是否全量数据")
        private boolean full;
    }
}
