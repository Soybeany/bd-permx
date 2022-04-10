package com.soybeany.permx.core.adapter;

import com.soybeany.exception.BdRtException;
import com.soybeany.permx.api.ISessionProcessor;
import com.soybeany.permx.core.exception.ShiroAuthenticationWrapException;
import com.soybeany.permx.core.exception.ShiroAuthenticationWrapRtException;
import com.soybeany.permx.exception.BdPermxAuthException;
import com.soybeany.permx.exception.BdPermxNoSessionException;
import com.soybeany.permx.model.PermissionParts;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @author Soybeany
 * @date 2022/4/2
 */
@Component
public class RealmAdapter<Input, S> extends AuthorizingRealm {

    @Lazy
    @Autowired
    private ISessionProcessor<Input, S> sessionProcessor;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof AuthenticationTokenAdapter;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        AuthenticationTokenAdapter<Input> tokenAdapter = (AuthenticationTokenAdapter<Input>) token;
        try {
            tokenAdapter.getAuthVerifier().onVerify(tokenAdapter.getInput());
        } catch (BdPermxAuthException e) {
            throw new ShiroAuthenticationWrapException(e);
        } catch (RuntimeException e) {
            throw new ShiroAuthenticationWrapRtException(e);
        }
        return new SimpleAuthenticationInfo(new SimplePrincipalCollection(token.getPrincipal(), "main"), token.getCredentials());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        S session;
        try {
            session = SessionDaoAdapter.loadSession();
        } catch (BdPermxNoSessionException e) {
            throw new BdRtException("无法从shiro会话中获取自定义会话");
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        for (PermissionParts parts : sessionProcessor.getPermissionsFromSession(session)) {
            info.addStringPermission(parts.toPermissionString());
        }
        return info;
    }

}
