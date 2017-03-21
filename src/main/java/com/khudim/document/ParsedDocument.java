package com.khudim.document;


import com.khudim.helpers.Tag;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.khudim.helpers.Tag.*;
import static com.khudim.helpers.Tag.GUID;
import static com.khudim.helpers.Tag.PUBLICATION_DATE;

/**
 * Created by Beaver.
 */
public class ParsedDocument implements IParsedDocument, Serializable {

    private static Map<Enum, String[]> tagsMap = new HashMap<>();

    static {
        tagsMap.put(PRICE, new String[]{"ns2:price"});
        tagsMap.put(CUSTOMER, new String[]{"ns2:customer"});
        tagsMap.put(URL, new String[]{"ns2:urlOOS"});
        tagsMap.put(REGION, new String[]{"ns2:city"});
        tagsMap.put(GUID, new String[]{"ns2:guid"});
        tagsMap.put(PUBLICATION_DATE, new String[]{"ns2:publicationDate", "ns2:creationDate"});
    }

    private Double price;
    private String startDate;
    private String url;
    private String guid;
    private String city;

    private String getContent(Tag enumTag, Document document) {
        for (String tag : tagsMap.get(enumTag)) {
            NodeList nodeList = document.getElementsByTagName(tag);
            Node node = nodeList.item(0);
            if (node == null || StringUtils.isBlank(node.getTextContent())) {
                continue;
            }
            return node.getTextContent();
        }
        return null;
    }

    @Override
    public Double getPrice() {
        return price;
    }

    @Override
    public String getStartDate() {
        return startDate;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getGuid() {
        return guid;
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public void fillDocument(Document document) {
        String price = getContent(PRICE, document);
        this.price = price == null ? null : Double.parseDouble(price);
        this.startDate = getContent(PUBLICATION_DATE, document);
        this.url = getContent(URL, document);
        this.guid = getContent(GUID, document);
        String city = getContent(REGION, document);
        this.city = city == null ? null : city.trim().toLowerCase().startsWith("г.") ? city.substring(2).trim() : city;
    }

    public boolean isIncorrectDocument() {
        return getGuid() == null || getPrice() == null || getCity() == null || getStartDate() == null || getUrl() == null;
    }
}
