package net.fze.ext.spring;

import net.fze.annotation.RequireContext;
import net.fze.common.Context;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 请求上下文参数解析器, 需实现 WebMvcConfigurer.addArgumentResolvers 方法注册解析器才能生效
 * 用于解析方法参数中的 {@link Context} 注解，将请求上下文注入到方法参数中。
 *
 * {code
 * public void test(@Context RequestContext ctx) {
 *     // 从请求上下文获取用户ID
 *     String userId = ctx.getAttribute("userId");
 * }
 * }
 * @author jarrysix
 */
 public abstract class AbstractRequestContextArgumentResolver implements HandlerMethodArgumentResolver, WebMvcConfigurer {

    public abstract Context createRequestContext(HttpServletRequest req);

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequireContext.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest request, WebDataBinderFactory binderFactory) throws Exception {
        Class<?> parameterType = parameter.getParameterType();
        if (Context.class.isAssignableFrom(parameter.getParameterType())) {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if(requestAttributes == null){
                return createRequestContext(null);
            }
            // parameter type is Context or its subclass
            return createRequestContext(requestAttributes.getRequest());
        }
        throw new IllegalArgumentException("parameter " + parameterType.getName() + " type is invalid");
    }

    /**
     * 添加请求上下文参数解析器, 需要实现方法WebMvcConfigurer.addArgumentResolvers，以注册参数解析器
     *
     * @param argumentResolvers 处理器方法参数解析器列表
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(this);
    }
}
