package com.soybeany.permx.model;


public class PermissionParts {
    private static final String SEPARATOR = ":";
    private static final String WILDCARD = "*";

    private final String module;
    private final String function;

    public PermissionParts(String module, String function) {
        this.module = module;
        this.function = function;
    }

    public static PermissionParts parse(String permission) {
        String[] parts = permission.split(SEPARATOR);
        String function = (parts.length > 1 ? parts[1] : WILDCARD);
        return new PermissionParts(parts[0], function);
    }

    public static boolean hasPermissions(Iterable<PermissionParts> provided, Iterable<String> permissions) {
        out:
        for (String permission : permissions) {
            for (PermissionParts parts : provided) {
                if (parts.isMatch(permission)) {
                    break out;
                }
            }
            return false;
        }
        return true;
    }

    public String getModule() {
        return module;
    }

    public String getFunction() {
        return function;
    }

    public boolean isMatch(String permission) {
        PermissionParts another = parse(permission);
        // module不一致，则不相等
        if (!isPartMatch(module, another.module)) {
            return false;
        }
        // function不一致，则不相等
        return isPartMatch(function, another.function);
    }

    public String toPermissionString() {
        return module + SEPARATOR + function;
    }

    // ****************************************

    private boolean isPartMatch(String ref, String target) {
        if (WILDCARD.equals(ref)) {
            return true;
        }
        return ref.equals(target);
    }

}