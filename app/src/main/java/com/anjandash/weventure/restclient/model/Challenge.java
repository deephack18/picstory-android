package com.anjandash.weventure.restclient.model;

/**
 * Created by hlib on 11/16/18.
 */

public class Challenge {
    private int found;
    private String image;
    private int challengeId;

    public Challenge() {
    }

    public int getFound() {
        return found;
    }

    public void setFound(int found) {
        this.found = found;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }
}
