package com.adibsurani.hackernews.local.realm;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

public class StoryRealm extends RealmObject {

    @PrimaryKey
    private String id;
    private String author;
    private String descendants;
    private RealmList<KidsRealm> kids;
    private int score;
    private long time;
    private String title;
    private String type;
    private String url;

    public StoryRealm(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescendants() {
        return descendants;
    }

    public void setDescendants(String descendants) {
        this.descendants = descendants;
    }

    public RealmList<KidsRealm> getKids() {
        return kids;
    }

    public void setKids(RealmList<KidsRealm> kids) {
        this.kids = kids;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
