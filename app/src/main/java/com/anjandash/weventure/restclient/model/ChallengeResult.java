package com.anjandash.weventure.restclient.model;

/**
 * Created by hlib on 11/17/18.
 */

public class ChallengeResult {
    private int challengeId;
    private int points;

    public ChallengeResult() {
    }

    public int getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
