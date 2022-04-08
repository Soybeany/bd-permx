package com.soybeany.permx.model;

/**
 * @author Soybeany
 * @date 2021/5/26
 */
@SuppressWarnings("unused")
public class PermissionDefine {

    private final String name;
    private final String value;
    private final String description;

    public PermissionDefine(String name, String value, String description) {
        this.name = name;
        this.value = value;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public PermissionParts getParts() {
        return PermissionParts.parse(value);
    }

}
