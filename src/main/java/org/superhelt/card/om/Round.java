package org.superhelt.card.om;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.superhelt.card.serializers.LocalDateDeserializer;
import org.superhelt.card.serializers.LocalDateSerializer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Round {

    private final int id;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
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
