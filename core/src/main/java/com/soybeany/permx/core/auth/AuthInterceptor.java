package com.soybeany.permx.core.auth;

import com.soybeany.permx.api.IAuthListener;
import com.soybeany.permx.api.ISessionManager;
import com.soybeany.permx.exception.BdPermxNoSessionException;
import com.soybeany.permx.model.CheckRule;
import com.soybeany.permx.model.CheckRuleStorage;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class AuthInterceptor<Input, Session> implements HandlerInterceptor {

    private static final Supplier<InnerException> UNAUTHORIZED_SUPPLIER = () -> new InnerException(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");

    @Autowired
    private IAuthListener<Session> listener;
    @Autowired
    private ISessionManager<Input, Session> sessionManager;

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler) throws Exception {
        try {
            listener.onStartRequest(request);
            Optional<Session> sessionOptional = getSession(request);
            sessionOptional.ifPresent(session -> listener.onFoundSession(session));
            String path = request.getServletPath();
            CheckRule rule = CheckRuleStorage.getMatchedRule(path);
            // 若没有匹配到pattern，则特殊处理
            if (null == rule) {
                // 404的响应不处理
                if (HttpServletResponse.SC_NOT_FOUND == response.getStatus()) {
                    return true;
                }
                // 只要是已登录用户(存在会话)，即可访问
                sessionOptional.orElseThrow(UNAUTHORIZED_SUPPLIER);
                return true;
            }
            // 若允许匿名访问，则放行
            if (rule instanceof CheckRule.WithAnonymity) {
                return true;
            }
            // 先检查是否已登录
            Session session = sessionOptional.orElseThrow(UNAUTHORIZED_SUPPLIER);
            // 再检查是否有权限
            if (!sessionManager.canAccess((CheckRule.WithPermission) rule, session)) {
                throw new InnerException(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
            }
        } catch (Exception e) {
            response.setContentType("text/plain; charset=utf-8");
            if (e instanceof InnerException) {
                InnerException innerException = (InnerException) e;
                setupResponse(response, innerException.code, innerException.msg);
            } else {
                setupResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            }
            listener.onFinishRequest(request);
            return false;
        }
        return true;
    }

    @Override
    public void afterCompletion(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler, Exception ex) {
        // 只有preHandle返回true时，才会执行此回调
        listener.onFinishRequest(request);
    }

    // ********************内部方法********************

    private Optional<Session> getSession(HttpServletRequest request) {
        try {
            return Optional.of(sessionManager.loadSession(request));
        } catch (BdPermxNoSessionException e) {
            return Optional.empty();
        }
    }

    private void setupResponse(HttpServletResponse response, int status, String msg) throws Exception {
        response.setStatus(status);
        response.getWriter().print(msg);
    }

    // ********************内部类********************

    @AllArgsConstructor
    private static class InnerException extends Exception {
        int code;
        String msg;
    }

}
