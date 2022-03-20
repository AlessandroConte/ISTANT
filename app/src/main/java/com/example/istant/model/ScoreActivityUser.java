package com.is.istant.model;

import java.util.List;

/**
 * entity 'scoreActivityUser' of the database
 */
public class ScoreActivityUser {
    private final String id;
    private int score;
    private String comment;
    private final String idActivity;
    private final String uid;

    public ScoreActivityUser(String id, int score, String comment,
                             String idActivity, String uid) {
        this.id = id;
        this.score = score;
        this.comment = comment;
        this.idActivity = idActivity;
        this.uid = uid;
    }

    public String getIdActivity() {
        return idActivity;
    }

    public int getScore() {
        return score;
    }

    /**
     * getScoreActivities is the method that gives the score of a given activity
     * @param listActivities is the list of the activities
     * @param mainActivity is the activity under consideration
     * @return the score
     */
    public static float getScoreActivities(List<ScoreActivityUser> listActivities, Activity mainActivity) {
        int sumScore = 0;
        int numScore = 0;
        for (ScoreActivityUser sau: listActivities) {
            if (sau.getIdActivity().equals(mainActivity.getId())) {
                numScore += 1;
                sumScore += sau.getScore();
            }
        }

        if (numScore == 0) {
            return 0;
        } else {
            return (float) sumScore / numScore;
        }

    }
}
