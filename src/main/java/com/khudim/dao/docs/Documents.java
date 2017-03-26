package com.khudim.dao.docs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by Beaver.
 */
@Entity
@Table(name = "documents")
public class Documents implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "guid")
    private String guid;
    @Column(name = "content")
    private String content;
    @Column(name = "creation_date")
    private Long creationDate;
    @Column(name = "region")
    private String region;
    @Column(name = "url")
    private String url;
    @Column(name = "price")
    private Double price;

    public Documents() {
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Long creationDate) {
        this.creationDate = creationDate;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Documents{" +
                "guid='" + guid + '\'' +
                ", creationDate=" + creationDate +
                ", region='" + region + '\'' +
                ", url='" + url + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Documents documents = (Documents) o;
        if (guid != null ? !guid.equals(documents.guid) : documents.guid != null) return false;
        if (creationDate != null ? !creationDate.equals(documents.creationDate) : documents.creationDate != null)
            return false;
        if (region != null ? !region.equals(documents.region) : documents.region != null) return false;
        if (url != null ? !url.equals(documents.url) : documents.url != null) return false;
        return price != null ? price.equals(documents.price) : documents.price == null;
    }

    @Override
    public int hashCode() {
        int result = guid != null ? guid.hashCode() : 0;
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (region != null ? region.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        return result;
    }
}
