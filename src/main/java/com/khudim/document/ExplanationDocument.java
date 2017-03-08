package com.khudim.document;

import com.khudim.helpers.Tag;
import org.w3c.dom.Document;

import java.io.Serializable;

/**
 * Created by Beaver.
 */
public class ExplanationDocument extends AbstractDocument implements Serializable {

    private String priceTag = Tag.PRICE.tag();
    private String startDateTag = "ns2:creationDate";
    private String urlTag = Tag.URL.tag();
    private String guidTag = Tag.GUID.tag();

    private String type = "explanation";

    public ExplanationDocument(Document document){
        this.price = getContent(priceTag,document);
        this.startDate = getContent(startDateTag,document);
        this.url = getContent(urlTag,document);
        this.guid = getContent(guidTag,document);
        this.city = getCityFromPlacer(document);
    }

    @Override
    public String getType() {
        return type;
    }
}
