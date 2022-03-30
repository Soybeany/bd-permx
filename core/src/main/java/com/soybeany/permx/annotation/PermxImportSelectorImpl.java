package com.soybeany.permx.annotation;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * @author Soybeany
 * @date 2022/3/9
 */
class PermxImportSelectorImpl implements ImportSelector {
    @Nonnull
    @Override
    public String[] selectImports(@Nonnull AnnotationMetadata importingClassMetadata) {
        Map<String, Object> map = importingClassMetadata.getAnnotationAttributes(EnablePermx.class.getName());
        if (null == map) {
            throw new RuntimeException("没有配置EnablePermx");
        }
        return new String[]{
                ((Class<?>) map.get("authVerifier")).getName(),
                ((Class<?>) map.get("sessionProcessor")).getName(),
                ((Class<?>) map.get("permDefineProvider")).getName(),
                ((Class<?>) map.get("sessionStorage")).getName(),
                ((Class<?>) map.get("sessionIdProcessor")).getName(),
                ((Class<?>) map.get("authListener")).getName(),
        };
    }
}
