package com.zpj.minote.model;

import org.json.JSONException;
import org.json.JSONObject;

public class NoteItem {

    private String id;
    private String folderId;
    private long createDate;
    private long modifyDate;

    private String snippet;
    private String status;
    private String subject;
    private String tag;
    private String type;
    private long colorId;
    private long alertTag;
    private long alertDate;

    public static NoteItem from(JSONObject obj) throws JSONException {
        NoteItem item = new NoteItem();
        item.id = obj.getString("id");
        item.folderId = obj.getString("folderId");
        item.createDate = obj.getLong("createDate");
        item.modifyDate = obj.getLong("modifyDate");
        item.snippet = obj.getString("snippet");
        item.status = obj.getString("status");
        item.subject = obj.getString("subject");
        item.tag = obj.getString("tag");
        item.colorId = obj.getLong("colorId");
        item.alertTag = obj.getLong("alertTag");
        item.alertDate = obj.getLong("alertDate");
        return item;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(long modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getColorId() {
        return colorId;
    }

    public void setColorId(long colorId) {
        this.colorId = colorId;
    }

    public long getAlertTag() {
        return alertTag;
    }

    public void setAlertTag(long alertTag) {
        this.alertTag = alertTag;
    }

    public long getAlertDate() {
        return alertDate;
    }

    public void setAlertDate(long alertDate) {
        this.alertDate = alertDate;
    }
}
