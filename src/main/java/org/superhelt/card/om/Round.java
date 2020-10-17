package org.superhelt.card.om;

import java.util.List;

public class Round {

    private final int id;
    private final List<Score> scores;

    public Round(int id, List<Score> scores) {
        this.id = id;
        this.scores = scores;
    }

    public int getId() {
        return id;
    }

    public List<Score> getScores() {
        return scores;
    }
}
