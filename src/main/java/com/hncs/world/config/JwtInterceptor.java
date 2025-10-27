package com.hncs.world.config;

import com.hncs.world.common.ErrorCode;
import com.hncs.world.utils.JwtUtil;
import com.hncs.world.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT拦截器
 */
@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestUri = request.getRequestURI();
        log.info("===== JwtInterceptor拦截到请求：{} =====", requestUri); // 关键日志
        // 放行OPTIONS请求
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            return true;
        }

        // 获取并处理Token时，打印日志
        String token = request.getHeader("Authorization");
        log.info("前端传递的原始Authorization：{}", token); // 打印原始请求头
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            log.info("截取后的Token：{}", token); // 打印截取后的Token
        } else {
            log.info("Token格式错误，未包含Bearer前缀");
        }

        // 验证token - 修正语法错误
        if (token == null || !jwtUtil.validateToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 补充401状态码
            throw new BusinessException(ErrorCode.INVALID_PARAMS.getCode(), "Token已过期或无效，请重新登录");
        }

        // 将用户ID存入请求域 - 修正语法错误
        Long userId = jwtUtil.getUserIdFromToken(token);
        request.setAttribute("userId", userId);

        return true;
    }
}