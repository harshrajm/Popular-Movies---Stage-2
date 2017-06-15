package com.example.android.popularmoviesone;

import java.io.Serializable;

/**
 * Created by Harshraj on 09-03-2017.
 */

public class Movies implements Serializable {

    private int id;
    private String title;
    private String posterPath;
    private String synopsis;
    private double userRating;
    private String releaseDate;
    private String youTubekey;
    private String reviews;
    private int dbId;

    public int getDbId() {
        return dbId;
    }

    public void setDbId(int dbId) {
        this.dbId = dbId;
    }

    public Movies(int id, String title, String posterPath, String synopsis, String releaseDate, double userRating) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.synopsis = synopsis;
        this.releaseDate = releaseDate;
        this.userRating = userRating;
    }

    public Movies(int id, String title, String posterPath, String synopsis, double userRating, String releaseDate, String youTubekey, String reviews,int dbId) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.synopsis = synopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
        this.youTubekey = youTubekey;
        this.reviews = reviews;
        this.dbId = dbId;
    }

    public String getYouTubekey() {
        return youTubekey;
    }

    public void setYouTubekey(String youTubekey) {
        this.youTubekey = youTubekey;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    /*@Override
    public String toString() {
        return "Movies{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", synopsis='" + synopsis + '\'' +
                ", userRating=" + userRating +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }*/

    @Override
    public String toString() {
        return "Movies{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", synopsis='" + synopsis + '\'' +
                ", userRating=" + userRating +
                ", releaseDate='" + releaseDate + '\'' +
                ", youTubekey='" + youTubekey + '\'' +
                ", reviews='" + reviews + '\'' +
                ", dbId=" + dbId +
                '}';
    }
}
