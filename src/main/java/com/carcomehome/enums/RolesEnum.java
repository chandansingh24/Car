package com.carcomehome.enums;

/**
 * Defines the possible roles. 
 */
public enum RolesEnum {

    BASICUSER(1, "ROLE_USER"),
    OWNER(2, "ROLE_OWNER"),
    ADMIN(3, "ROLE_ADMIN");


    private final int id;

    private final String roleName;

    RolesEnum(int id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    public int getId() {
        return id;
    }

    public String getRoleName() {
        return roleName;
    }
}
