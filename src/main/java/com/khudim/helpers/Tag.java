package com.khudim.helpers;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Beaver.
 */
public enum Tag {
    PRICE("price"),
    CUSTOMER("customer"),
    URL("urlOOS"),
    GUID("guid"),
    REGION("region"),
    PUBLICATION_DATE("publicationDate");

    private final String tag;

    Tag(String tag) {
        this.tag = tag;
    }

    public String tag() {
        return tag;
    }

    public static Tag findByTag(String tag) {
        for (Tag tagValue : Tag.values()) {
            if (tagValue.tag().equals(tag)) {
                return tagValue;
            }
        }
        return null;
    }
}
