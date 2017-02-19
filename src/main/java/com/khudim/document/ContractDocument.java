package com.khudim.document;

import com.khudim.helpers.Tag;
import org.w3c.dom.Document;

import java.io.Serializable;

/**
 * Created by Beaver.
 */
public class ContractDocument extends AbstractDocument implements Serializable {

    private String priceTag = Tag.PRICE.tag();
    private String startDateTag = "ns2:createDateTime";
    private String urlTag = Tag.URL.tag();
    private String guidTag = Tag.GUID.tag();
    private String cityTag = "ns2:city";

    public ContractDocument(Document document){
        this.price = getContent(priceTag,document);
        this.startDate = getContent(startDateTag,document);
        this.url = getContent(urlTag,document);
        this.guid = getContent(guidTag,document);
        this.city = getContent(cityTag,document);
    }

}
