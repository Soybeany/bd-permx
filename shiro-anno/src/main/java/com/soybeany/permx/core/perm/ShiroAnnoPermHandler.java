package com.soybeany.permx.core.perm;

import com.soybeany.permx.api.ICodePermHandler;
import com.soybeany.permx.exception.BdPermxRtException;
import com.soybeany.permx.model.CheckRule;
import com.soybeany.permx.model.CheckRule.WithAnonymity;
import com.soybeany.permx.model.CheckRule.WithPermission;
import com.soybeany.permx.model.CheckRuleStorage;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class ShiroAnnoPermHandler implements ICodePermHandler {

    @Autowired
    private RequestMappingHandlerMapping mapping;

    private static final Map<Class<?>, CheckRuleProvider> RESTRICT_PROVIDER_MAPPING = new HashMap<Class<?>, CheckRuleProvider>() {{
        put(RequiresGuest.class, (url, annotation, permDefines) -> {
            WithAnonymity restrict = new WithAnonymity();
            restrict.setPattern(url);
            return restrict;
        });
        put(RequiresPermissions.class, (url, annotation, permDefines) -> {
            WithPermission restrict = new WithPermission();
            restrict.setPattern(url);
            RequiresPermissions permissions = (RequiresPermissions) annotation;
            Set<String> requiredPermissions = restrict.getRequiredPermissions();
            for (String permission : permissions.value()) {
                if (!permDefines.contains(permission)) {
                    throw new BdPermxRtException("使用了未定义的权限:" + permission);
                }
                requiredPermissions.add(permission);
            }
            return restrict;
        });
        put(RequiresAuthentication.class, (url, annotation, permDefines) -> {
            WithPermission restrict = new WithPermission();
            restrict.setPattern(url);
            return restrict;
        });
    }};

    @Override
    public void onHandleCodePerms(Set<String> permDefines) {
        mapping.getHandlerMethods().forEach((info, method) -> onHandleMethod(info, method, permDefines));
    }

    // ********************内部方法********************

    private void onHandleMethod(RequestMappingInfo info, HandlerMethod method, Set<String> permDefines) {
        for (Annotation annotation : method.getMethod().getAnnotations()) {
            CheckRuleProvider provider = RESTRICT_PROVIDER_MAPPING.get(annotation.annotationType());
            if (null == provider) {
                continue;
            }
            for (String url : info.getPatternValues()) {
                CheckRule rule = provider.get(url, annotation, permDefines);
                CheckRuleStorage.addRule(rule);
            }
            return;
        }
    }

    // ****************************************内部类****************************************

    private interface CheckRuleProvider {
        CheckRule get(String url, Annotation annotation, Set<String> permDefines);
    }

}
