package top.swiftx.framework.rest.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import org.jspecify.annotations.Nullable;

import java.io.Serializable;
import java.util.List;

/**
 * 基于游标的通用分页响应。
 *
 * @param meta 分页元数据
 * @param data 数据项
 * @param <T>  数据项类型
 * @param <C>  游标类型
 * @author gpt-5.6-sol
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(
        title = "游标分页响应",
        description = "基于游标返回分页元数据与数据项。"
)
public record CursorPageResponse<T, C>(
        @Schema(
                title = "分页元数据",
                description = "当前游标分页的元数据信息。"
        )
        Meta<C> meta,

        @Schema(
                title = "数据项",
                description = "当前页的数据项。"
        )
        List<T> data
) implements Serializable {

    /**
     * 创建不可变的游标分页响应。
     */
    public CursorPageResponse {
        data = List.copyOf(data);
    }

    /**
     * 根据数据和分页信息创建游标分页响应。
     *
     * @param data       当前页数据
     * @param limit      本次请求的最大记录数
     * @param nextCursor 下一页游标
     * @param remaining  当前页之后的剩余记录数
     * @param snapshot   数据快照标识
     * @param total      数据总数
     * @param <T>        数据项类型
     * @param <C>        游标类型
     * @return 游标分页响应
     */
    public static <T, C> CursorPageResponse<T, C> of(
            List<T> data,
            Integer limit,
            @Nullable C nextCursor,
            @Nullable Long remaining,
            @Nullable String snapshot,
            @Nullable Long total
    ) {
        return new CursorPageResponse<>(
                new Meta<>(
                        limit,
                        nextCursor != null,
                        nextCursor,
                        remaining,
                        snapshot,
                        total
                ),
                data
        );
    }

    /**
     * 游标分页元数据。
     *
     * @param limit      本次请求的最大记录数
     * @param hasNext    是否还有下一页
     * @param nextCursor 下一页游标
     * @param remaining  当前页之后的剩余记录数
     * @param snapshot   数据快照标识
     * @param total      数据总数
     * @param <C>        游标类型
     * @author gpt-5.6-sol
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(
            title = "游标分页元数据",
            description = "描述当前游标分页的位置与数据规模。"
    )
    public record Meta<C>(
            @Schema(
                    title = "分页限制",
                    description = "本次请求的最大记录数。"
            )
            Integer limit,

            @Schema(
                    title = "存在下一页",
                    description = "是否还有下一页。"
            )
            boolean hasNext,

            @Schema(
                    title = "下一页游标",
                    description = "下一页游标；没有下一页时省略。"
            )
            @Nullable C nextCursor,

            @Schema(
                    title = "剩余记录数",
                    description = "服务端可确定时，当前页之后的剩余记录数。"
            )
            @Nullable Long remaining,

            @Schema(
                    title = "数据快照",
                    description = "本次游标分页所基于的数据快照标识。"
            )
            @Nullable String snapshot,

            @Schema(
                    title = "总记录数",
                    description = "数据源可确定时的总记录数。"
            )
            @Nullable Long total
    ) implements Serializable {
    }
}
