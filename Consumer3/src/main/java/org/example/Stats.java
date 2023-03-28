package org.example;

public class Stats {
    private String userId;

    private int numLikes;

    private int numDislikes;

    public Stats (String userId, int numLikes, int numDislikes) {
        this.userId = userId;
        this.numLikes = numLikes;
        this.numDislikes = numDislikes;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getNumLikes() {
        return numLikes;
    }

    public void setNumLikes(int numLikes) {
        this.numLikes = numLikes;
    }

    public int getNumDislikes() {
        return numDislikes;
    }

    public void setNumDislikes(int numDislikes) {
        this.numDislikes = numDislikes;
    }
}
