package com.khudim.helpers;

/**
 * Created by Beaver.
 */
public enum Tag {
    PRICE("ns2:price"),
    CUSTOMER("ns2:customer"),
    URL("ns2:urlOOS"),
    GUID("ns2:guid"),
    PUBLICATION_DATE("ns2:publicationDate");

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
