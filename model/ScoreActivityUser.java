package com.is.istant.model;

import java.util.List;

/**
 * entity 'scoreActivityUser' of the database
 */
public class ScoreActivityUser {
    private int score;
    private String comment;
    private final Activity idActivity;
    private final User uid;

    public ScoreActivityUser(int score, String comment, Activity idActivity, User uid) {
        this.score = score;
        this.comment = comment;
        this.idActivity = idActivity;
        this.uid = uid;
    }

    public Activity getIdActivity() {
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
            if (sau.getIdActivity().equals(mainActivity)) {
                numScore += 1;
                sumScore += sau.getScore();
            }
        }

        if (numScore == 0) {
            return 0;
        } else {
            return sumScore/numScore;
        }

    }
}
