package top.swiftx.framework.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author 胡永强
 */
@Data
@AllArgsConstructor
public class InterceptorRegister {
    /**
     * 加载顺序
     */
    private int order;

    /**
     * 路径规则
     */
    private String[] path;

    /**
     * 拦截器实例
     */
    private HandlerInterceptor handler;
}
