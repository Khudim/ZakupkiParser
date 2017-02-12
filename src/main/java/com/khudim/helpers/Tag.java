package com.khudim.helpers;

/**
 * Created by Beaver.
 */
public enum Tag {
    PRICE("ns2:price"),
    URL("ns2:urlOOS");

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
