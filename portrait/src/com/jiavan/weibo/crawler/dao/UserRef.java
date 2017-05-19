package com.jiavan.weibo.crawler.dao;

/** User Reference DTO
 * Created by Jiavan on 2017/4/8.
 */
public class UserRef {
    private long uid;
    private String username;
    private int depth;

    public UserRef(long uid, String username, int depth) {
        this.uid = uid;
        this.username = username;
        this.depth = depth;
    }

    public UserRef() { }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    @Override
    public String toString() {
        return "UserRef{" +
                "uid=" + uid +
                ", username='" + username + '\'' +
                ", depth=" + depth +
                '}';
    }
}
