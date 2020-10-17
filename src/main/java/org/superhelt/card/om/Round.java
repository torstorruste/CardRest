package org.superhelt.card.om;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Round {

    private final int id;
    private final LocalDate date;
    private final List<Score> scores;

    public Round(int id, LocalDate date, List<Score> scores) {
        this.id = id;
        this.date = date;
        this.scores = new ArrayList<>(scores);
    }

    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<Score> getScores() {
        return scores;
    }
}
