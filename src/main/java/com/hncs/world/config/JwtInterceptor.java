package com.hncs.world.config;

import com.hncs.world.common.ErrorCode;
import com.hncs.world.common.utils.JwtUtil;
import com.hncs.world.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 放行OPTIONS请求
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            return true;
        }

        // 获取token
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 验证token - 修正语法错误
        if (token == null || !jwtUtil.validateToken(token) || jwtUtil.isTokenExpired(token)) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS.getCode(), "Token已过期或无效，请重新登录");
        }

        // 将用户ID存入请求域 - 修正语法错误
        Long userId = jwtUtil.getUserIdFromToken(token);
        request.setAttribute("userId", userId);

        return true;
    }
}