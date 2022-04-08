package com.soybeany.permx.annotation;

import com.soybeany.permx.api.*;
import com.soybeany.permx.core.auth.AuthInterceptor;
import com.soybeany.permx.core.auth.AuthInterceptorConfigurer;
import com.soybeany.permx.core.config.PermxConfig;
import com.soybeany.permx.core.perm.PermDefineConsumerImpl;
import com.soybeany.permx.impl.*;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Soybeany
 * @date 2022/3/9
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
        PermxConfig.class,
        AuthInterceptor.class,
        AuthInterceptorConfigurer.class,
        PermDefineConsumerImpl.class,
        PermxImportSelectorImpl.class,
})
public @interface EnablePermx {

    /**
     * 配置会话处理者
     */
    Class<? extends ISessionProcessor<?, ?>> sessionProcessor();

    /**
     * 一般接口认证/鉴权过程异常时的处理者
     */
    Class<? extends IAuthExceptionProcessor> authExceptionProcessor() default AuthExceptionProcessorImpl.class;

    /**
     * 配置代码中的权限定义提供者
     */
    Class<? extends PermDefineProvider> permDefineProvider() default EmptyPermDefineProvider.class;

    /**
     * 配置会话存储者
     */
    Class<? extends ISessionStorage> sessionStorage() default SessionStorageStdImpl.class;

    /**
     * 配置会话id处理者
     */
    Class<? extends ISessionIdProcessor> sessionIdProcessor() default HeaderSessionIdProcessorStdImpl.class;

    /**
     * 鉴权的监听器
     */
    Class<? extends IAuthListener> authListener() default AuthListenerStdImpl.class;

}
