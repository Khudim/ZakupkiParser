package com.khudim.document;

import org.w3c.dom.Document;

/**
 * Created by Beaver.
 */
public interface IParsedDocument {

    Double getPrice();
    String getUrl();
    String getStartDate();
    String getGuid();
    String getCity();
    boolean isIncorrectDocument();
    void fillDocument(Document document);
}