package top.swiftx.framework.rest.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.jspecify.annotations.Nullable;

import java.io.Serializable;
import java.util.Date;

/**
 * 增量数据集查询参数。
 *
 * @author gpt-5.6-sol
 */
@Data
@Schema(description = "标准增量数据集请求")
public class DeltaSetRequest implements Serializable {
    @Schema(description = "数据接口版本")
    private @Nullable String apiVersion;

    @Schema(description = "数据截止时间")
    private @Nullable Date lastRequested;
}
