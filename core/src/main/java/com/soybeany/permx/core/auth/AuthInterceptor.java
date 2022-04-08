package com.soybeany.permx.core.auth;

import com.soybeany.permx.api.IAuthExceptionProcessor;
import com.soybeany.permx.api.IAuthListener;
import com.soybeany.permx.api.ISessionManager;
import com.soybeany.permx.exception.BdPermxNoSessionException;
import com.soybeany.permx.model.CheckRule;
import com.soybeany.permx.model.CheckRuleStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class AuthInterceptor<Input, Session> implements HandlerInterceptor {

    private static final Supplier<AuthenticationException> UNAUTHORIZED_SUPPLIER = AuthenticationException::new;

    @Autowired
    private IAuthListener<Session> listener;
    @Autowired
    private IAuthExceptionProcessor authExceptionProcessor;
    @Autowired
    private ISessionManager<Input, Session> sessionManager;

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler) throws IOException {
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
                throw new AuthorizationException();
            }
        } catch (Exception e) {
            try {
                listener.onFinishRequest(request);
            } catch (Exception ignore) {
            }
            authExceptionProcessor.onPreHandle(request, response);
            if (e instanceof AuthenticationException) {
                authExceptionProcessor.onAuthenticationException(request, response);
            } else if (e instanceof AuthorizationException) {
                authExceptionProcessor.onAuthorizationException(request, response);
            } else {
                authExceptionProcessor.onOtherAuthException(request, response, e);
            }
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

    // ********************内部类********************

    private static class AuthenticationException extends Exception {
    }

    private static class AuthorizationException extends Exception {
    }

}
