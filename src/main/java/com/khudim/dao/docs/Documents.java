package com.khudim.dao.docs;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Beaver.
 */
@Entity
@Table(name = "documents")
public class Documents implements Serializable{

    @Id
    @Column
    private String guid;
    @Column(name = "document_type")
    private String documentType;
    @Column(name="creation_date")
    private Long creationDate;
    @Column
    private byte[] content;

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

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }
}
