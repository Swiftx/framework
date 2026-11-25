package top.swiftx.framework.rest.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.jspecify.annotations.Nullable;

import java.io.Serializable;

/**
 * 页码分页查询参数。
 *
 * @author gpt-5.6-sol
 */
@Data
@Schema(description = "页码分页")
public class PageNumberPagination implements Serializable {
    @Schema(description = "页码")
    @Min(1)
    protected @Nullable Integer page;

    @Schema(description = "每页显示记录数")
    @Min(1)
    @Max(200)
    private @Nullable Integer pageSize;
}
