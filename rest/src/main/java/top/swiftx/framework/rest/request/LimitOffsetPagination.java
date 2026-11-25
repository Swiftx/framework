package top.swiftx.framework.rest.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.jspecify.annotations.Nullable;

import java.io.Serializable;

/**
 * 偏移量分页查询参数。
 *
 * @author gpt-5.6-sol
 */
@Data
@Schema(description = "偏移值分页")
public class LimitOffsetPagination implements Serializable {
    @Schema(description = "显示记录数")
    private @Nullable Integer limit;

    @Schema(description = "偏移量")
    private @Nullable Integer offset;
}
