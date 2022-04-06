package com.soybeany.demo.model;

import com.soybeany.permx.model.PermissionParts;
import lombok.Data;

import java.util.Collection;

/**
 * @author Soybeany
 * @date 2022/3/29
 */
@Data
public class Session {

    private final Collection<PermissionParts> permissions;

}
