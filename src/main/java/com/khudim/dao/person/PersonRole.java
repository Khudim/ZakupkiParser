package com.khudim.dao.person;

/**
 * @author hudyshkin
 */
public enum PersonRole {
    USER("USER"),
    ADMIN("ADMIN");

    private final String role;

    PersonRole(String role) {
        this.role = role;
    }

    public String role() {
        return role;
    }

    public static PersonRole findByRole(String role) {
        for (PersonRole personRole : values()) {
            if (personRole.role().equals(role)) {
                return personRole;
            }
        }
        return null;
    }
}
