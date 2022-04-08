package com.soybeany.permx.core.adapter;

import com.soybeany.permx.core.exception.ShiroAuthenticationWrapException;
import com.soybeany.permx.exception.BdPermxAuthException;
import com.soybeany.util.file.BdFileUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.stereotype.Component;

/**
 * @author Soybeany
 * @date 2022/4/2
 */
@Component
public class RealmAdapter<Input> implements Realm {

    @Override
    public String getName() {
        return "realm-" + BdFileUtils.getUuid();
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof AuthenticationTokenAdapter;
    }

    @SuppressWarnings("unchecked")
    @Override
    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        AuthenticationTokenAdapter<Input> tokenAdapter = (AuthenticationTokenAdapter<Input>) token;
        try {
            tokenAdapter.getAuthVerifier().onVerify(tokenAdapter.getInput());
        } catch (BdPermxAuthException e) {
            throw new ShiroAuthenticationWrapException(e);
        }
        return new SimpleAuthenticationInfo(new SimplePrincipalCollection(token.getPrincipal(), "main"), token.getCredentials());
    }
}
