package org.superhelt.card.om;

import java.util.ArrayList;
import java.util.List;

public class Score {

    private final Player player;
    private final List<Integer> scores;

    public Score(Player player, List<Integer> scores) {
        this.player = player;
        this.scores = new ArrayList<>(scores);
    }

    public Player getPlayer() {
        return player;
    }

    public List<Integer> getScores() {
        return scores;
    }
}
