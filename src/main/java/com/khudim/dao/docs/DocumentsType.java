package com.khudim.dao.docs;

/**
 * Created by Beaver.
 */
public enum DocumentsType {

    CONTRACT("contract"),
    PURCHASE("purchase"),
    EXPLANATION("explanation");

    DocumentsType(String type) {
        this.type = type;
    }

    private final String type;

    public String type() {
        return type;
    }
}
