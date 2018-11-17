package com.anjandash.weventure.restclient.model;

/**
 * Created by hlib on 11/17/18.
 */

public class NewChallenge {
    private String img;
    private int challengeId;

    public NewChallenge() {
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }
}
