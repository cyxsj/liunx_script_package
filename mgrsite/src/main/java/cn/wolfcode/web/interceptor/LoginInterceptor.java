package cn.wolfcode.web.interceptor;

import cn.wolfcode.p2p.base.anno.NeedLogin;
import cn.wolfcode.p2p.base.anno.NotNeedLogin;
import cn.wolfcode.p2p.util.UserContext;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录检查
 */
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter{

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //拿到方法
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        //拿到方法上的needlogin标签
        NotNeedLogin notNeedLogin = handlerMethod.getMethodAnnotation(NotNeedLogin.class);
        //如果方法上有该标签,那么就是需要登录才可以访问,而logininfo为空,那么久是没有登录,跳转到登录页面
        if (notNeedLogin == null && UserContext.getLoginInfo() == null){
            response.sendRedirect("/login.html");
            return false;
        }
        return true;
    }
}
