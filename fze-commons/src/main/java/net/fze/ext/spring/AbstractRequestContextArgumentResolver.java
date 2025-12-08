package net.fze.ext.spring;

import net.fze.annotation.Context;
import net.fze.common.RequestContext;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求上下文参数解析器
 * 用于解析方法参数中的 {@link Context} 注解，将请求上下文注入到方法参数中。
 *
 * {code
 * public void test(@Context RequestContext ctx) {
 *     // 从请求上下文获取用户ID
 *     String userId = ctx.getAttribute("userId");
 * }
 * }
 */
 public abstract class AbstractRequestContextArgumentResolver implements HandlerMethodArgumentResolver {

        public abstract RequestContext<HttpServletRequest> createRequestContext(HttpServletRequest req);
        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            return parameter.hasParameterAnnotation(Context.class);
        }

        @Override
        public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest request, WebDataBinderFactory binderFactory) throws Exception {
            HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            Class<?> parameterType = parameter.getParameterType();
            if (parameter.getParameterType().isAssignableFrom(RequestContext.class)) {
                return createRequestContext(req);
            }
            throw new IllegalArgumentException("parameter " + parameterType.getName() + " type is invalid");
        }
}
