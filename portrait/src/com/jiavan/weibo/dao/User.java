package com.jiavan.weibo.dao;

/**
 * UserInfo user to object
 * Created by Jiavan on 2017/4/4.
 */
public class User {
    private long id;
    private long uid;
    private String avatar;
    private String username;
    private String homePage;
    private int gender;
    private String address;
    private String introduction;
    private String company;
    private String school;
    private String blog;
    private int level;
    private String credit;
    private long regTime; // registration time
    private long followCount;
    private long fansCount;
    private long birthday;
    private int tweetCount;
    private String job;
    private String tags;
    private String loveState;
    private long lastModify;  // record last modify time

    public User(long id, long uid, String avatar, String username, String homePage, int gender, String address, String introduction, String company, String school, String blog, int level, String credit, long regTime, long followCount, long fansCount, long birthday, int tweetCount, String job, String tags, String loveState, long lastModify) {
        this.id = id;
        this.uid = uid;
        this.avatar = avatar;
        this.username = username;
        this.homePage = homePage;
        this.gender = gender;
        this.address = address;
        this.introduction = introduction;
        this.company = company;
        this.school = school;
        this.blog = blog;
        this.level = level;
        this.credit = credit;
        this.regTime = regTime;
        this.followCount = followCount;
        this.fansCount = fansCount;
        this.birthday = birthday;
        this.tweetCount = tweetCount;
        this.job = job;
        this.tags = tags;
        this.loveState = loveState;
        this.lastModify = lastModify;
    }

    public User() {}

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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getBlog() {
        return blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public long getRegTime() {
        return regTime;
    }

    public void setRegTime(long regTime) {
        this.regTime = regTime;
    }

    public long getFollowCount() {
        return followCount;
    }

    public void setFollowCount(long followCount) {
        this.followCount = followCount;
    }

    public long getFansCount() {
        return fansCount;
    }

    public void setFansCount(long fansCount) {
        this.fansCount = fansCount;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getLoveState() {
        return loveState;
    }

    public void setLoveState(String loveState) {
        this.loveState = loveState;
    }

    public long getLastModify() {
        return lastModify;
    }

    public void setLastModify(long lastModify) {
        this.lastModify = lastModify;
    }

    public int getTweetCount() {
        return tweetCount;
    }

    public void setTweetCount(int tweetCount) {
        this.tweetCount = tweetCount;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", uid=" + uid +
                ", avatar='" + avatar + '\'' +
                ", username='" + username + '\'' +
                ", homePage='" + homePage + '\'' +
                ", gender=" + gender +
                ", address='" + address + '\'' +
                ", introduction='" + introduction + '\'' +
                ", company='" + company + '\'' +
                ", school='" + school + '\'' +
                ", blog='" + blog + '\'' +
                ", level=" + level +
                ", credit='" + credit + '\'' +
                ", regTime=" + regTime +
                ", followCount=" + followCount +
                ", fansCount=" + fansCount +
                ", birthday=" + birthday +
                ", tweetCount=" + tweetCount +
                ", job='" + job + '\'' +
                ", tags='" + tags + '\'' +
                ", loveState='" + loveState + '\'' +
                ", lastModify=" + lastModify +
                '}';
    }
}
