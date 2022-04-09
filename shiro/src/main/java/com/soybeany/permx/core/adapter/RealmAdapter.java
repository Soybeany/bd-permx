package com.soybeany.permx.core.adapter;

import com.soybeany.permx.core.exception.ShiroAuthenticationWrapException;
import com.soybeany.permx.core.exception.ShiroAuthenticationWrapRtException;
import com.soybeany.permx.exception.BdPermxAuthException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

/**
 * @author Soybeany
 * @date 2022/4/2
 */
public class RealmAdapter<Input> extends AuthorizingRealm {

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
        Session session = SecurityUtils.getSubject().getSession();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//        info.addStringPermission("document:read");
        return info;
    }

}
