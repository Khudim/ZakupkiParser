package com.khudim.document;

import com.khudim.helpers.Tag;
import org.apache.commons.lang3.StringUtils;
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

    private String type = "contract";

    public ContractDocument(Document document){
        this.price = getContent(priceTag,document);
        this.startDate = getContent(startDateTag,document);
        this.url = getContent(urlTag,document);
        this.guid = getContent(guidTag,document);
        String cityFromContent = getContent(cityTag,document);
        if(StringUtils.isBlank(cityFromContent)){
            cityFromContent = getCityFromPlacer(document);
        }
        this.city = cityFromContent;
    }

    @Override
    public String getType() {
        return type;
    }
}
