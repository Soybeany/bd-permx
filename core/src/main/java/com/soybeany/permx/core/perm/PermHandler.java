package com.soybeany.permx.core.perm;

import com.soybeany.permx.api.ICodePermHandler;
import com.soybeany.permx.api.PermDefineConsumer;
import com.soybeany.permx.core.config.PermxConfig;
import com.soybeany.permx.model.CheckRule;
import com.soybeany.permx.model.CheckRuleStorage;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.Set;

/**
 * @author Soybeany
 * @date 2022/4/8
 */
public class PermHandler implements InitializingBean {

    @Autowired
    private PermxConfig permxConfig;
    @Autowired
    private PermDefineConsumer permDefineConsumer;
    @Autowired(required = false)
    private ICodePermHandler codePermHandler;

    @Override
    public void afterPropertiesSet() {
        Set<String> permDefines = permDefineConsumer.getPermValueSet();
        // 先添加代码中指定的配置
        if (null != codePermHandler) {
            codePermHandler.onHandleCodePerms(permDefines);
        }
        // 再添加yml中指定的配置
        Optional.ofNullable(permxConfig.getPerm())
                .ifPresent(perm -> CheckRuleStorage.addRules(CheckRule.WithPermission.fromEntityMap(permDefines, perm)));
        Optional.ofNullable(permxConfig.getAnon())
                .ifPresent(anon -> CheckRuleStorage.addRules(CheckRule.WithAnonymity.fromPatternList(anon)));
        // 更新全局配置
        CheckRuleStorage.updateAllRules();
        // 执行回调
        if (null != codePermHandler) {
            codePermHandler.onInitialized();
        }
    }
}
