package com.soybeany.demo.model;

import com.soybeany.permx.impl.SessionImpl;
import com.soybeany.permx.model.PermissionParts;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;

/**
 * @author Soybeany
 * @date 2022/3/29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Session extends SessionImpl {

    private final Collection<PermissionParts> permissions;

}
