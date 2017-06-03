package com.jiavan.weibo.dao;

/**
 * Created by Jiavan on 2017/4/21.
 * <p>
 * Tweet data to object
 */
public class Tweet {
    private long id;
    private long uid;
    private String url;
    private long createdAt;
    private String source;
    private String text;
    private String retweetText;

    public Tweet(long id, long uid, String url, long createdAt, String source, String text, String retweetText) {
        this.id = id;
        this.uid = uid;
        this.url = url;
        this.createdAt = createdAt;
        this.source = source;
        this.text = text;
        this.retweetText = retweetText;
    }

    public Tweet() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRetweetText() {
        return retweetText;
    }

    public void setRetweetText(String retweetText) {
        this.retweetText = retweetText;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "id=" + id +
                ", uid=" + uid +
                ", url='" + url + '\'' +
                ", createdAt=" + createdAt +
                ", source='" + source + '\'' +
                ", text='" + text + '\'' +
                ", retweetText='" + retweetText + '\'' +
                '}';
    }
}
