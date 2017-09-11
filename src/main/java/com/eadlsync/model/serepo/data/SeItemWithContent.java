package com.eadlsync.model.serepo.data;

import ch.hsr.isf.serepo.data.restinterface.seitem.CreateSeItem;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Arrays;

/**
 *
 */
public class SeItemWithContent extends CreateSeItem {

    private byte[] content;
    private String mimeType;

    public SeItemWithContent() {
    }

    public SeItemWithContent(byte[] content, String mimeType) {
        super();
        this.content = content;
        this.mimeType = mimeType;
    }

    @JsonIgnore
    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @JsonIgnore
    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @Override
    public String toString() {
        return "SeItemWithContent{" +
                "content=" + content.toString() +
                '}';
    }
}
