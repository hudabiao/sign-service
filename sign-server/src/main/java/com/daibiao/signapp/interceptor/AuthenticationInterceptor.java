package com.daibiao.signapp.interceptor;

import com.daibiao.signapp.annotion.PassToken;
import com.daibiao.signapp.model.User;
import com.daibiao.signapp.service.UserService;
import com.daibiao.signapp.util.TokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

import io.jsonwebtoken.Claims;
import sun.net.www.protocol.http.HttpURLConnection;

/**
 * @author hudaibiao-1
 * @version 1.0.0
 * @ClassName com.daibiao.signapp.interceptor.AuthenticationInterceptor
 * @description 登录校验拦截器
 * @date 2020-03-18 16:44:00
 */
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {

    private static final String CURRENT_USER = "current_user";

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("token");
        if(StringUtils.isBlank(token)){
            token = request.getParameter("token");
        }

        //如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //检查是否有passToken注释，有则跳过验证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }

        //执行认证
        if (token == null) {
            response.setStatus(HttpURLConnection.HTTP_UNAUTHORIZED);
            log.info("无token，请重新登录");
            return false;
        } else {
            //获取token中的用户信息
            Claims claims;
            try {
                claims = TokenUtil.parseJWT(token);
            } catch (ExpiredJwtException e) {
                response.setStatus(HttpURLConnection.HTTP_UNAUTHORIZED);
                log.info("解析token失效,{}",e.getMessage());
                return false;
            }
            String id = claims.getId();
            User user = userService.selectUserById(id);
            if (user == null) {
                response.setStatus(HttpURLConnection.HTTP_UNAUTHORIZED);
                log.info("用户不存在，请重新登录");
                return false;
            }
            request.setAttribute(CURRENT_USER, user);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
