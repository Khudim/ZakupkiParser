package com.khudim.document;

import com.khudim.helpers.Tag;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.Serializable;

/**
 * Created by Beaver.
 */
public class PurchaseDocument extends AbstractDocument implements Serializable {

    private String priceTag = Tag.PRICE.tag();
    private String startDateTag = Tag.PUBLICATION_DATE.tag();
    private String urlTag = Tag.URL.tag();
    private String guidTag = Tag.GUID.tag();

    public PurchaseDocument(Document document){
        this.price = getContent(priceTag,document);
        this.startDate = getContent(startDateTag,document);
        this.url = getContent(urlTag,document);
        this.guid = getContent(guidTag,document);
    }

}
