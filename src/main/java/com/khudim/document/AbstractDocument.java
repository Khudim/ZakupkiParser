package com.khudim.document;


import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.Serializable;

/**
 * Created by Beaver.
 */
public abstract class AbstractDocument implements IParsedDocument, Serializable {

    protected String price = "1";
    protected String startDate = "2";
    protected String url = "3";
    protected String guid = "4";
    protected String city = "test";

    protected String getContent(String tag, Document document) {
        NodeList nodeList = document.getElementsByTagName(tag);
        Node node = nodeList.item(0);
        if (node == null) {
            return "";
        }
        return node.getTextContent();
    }

    public String getCityFromPlacer(Document document) {
        NodeList nodeList = document.getElementsByTagName("ns2:placer");
        Node node = nodeList.item(0);
        String content = node.getTextContent();
        String[] splitedContent = content.split("\\n");
        for (String string : splitedContent) {
            if (string.contains(" г ") || string.contains(" ул ") || string.contains(" дом ")) {
                String[] split = string.split(",");
                for (String s : split) {
                    if (s.contains(" г ")) {
                       return s;
                    }
                }
            }
        }
        return "-";
    }

        @Override
        public String getPrice () {
            return price;
        }

        @Override
        public String getStartDate () {
            return startDate;
        }

        @Override
        public String getUrl () {
            return url;
        }

        @Override
        public String getGuid () {
            return guid;
        }

        @Override
        public String getCity () {
            return city;
        }

}
