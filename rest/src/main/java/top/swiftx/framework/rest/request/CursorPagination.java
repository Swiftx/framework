package top.swiftx.framework.rest.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.jspecify.annotations.Nullable;

import java.io.Serializable;

/**
 * 游标分页查询参数。
 *
 * @author gpt-5.6-sol
 */
@Data
@Schema(description = "游标分页")
public class CursorPagination implements Serializable {
    @Schema(description = "上一页响应返回的游标；首次查询时省略")
    private @Nullable String cursor;

    @Positive
    @Schema(description = "本次最多返回的记录数", minimum = "1")
    private @Nullable Integer limit;
}
