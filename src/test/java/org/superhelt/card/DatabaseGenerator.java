package org.superhelt.card;

import org.superhelt.card.db.CsvParser;
import org.superhelt.card.db.SQLiteDao;
import org.superhelt.card.om.Player;
import org.superhelt.card.om.Round;
import org.superhelt.card.om.Score;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class DatabaseGenerator {

    public static void main(String[] args) throws Exception {
        Path basePath = Paths.get("src\\test\\resources");
        CsvParser csvParser = new CsvParser(basePath);
        List<Player> players = csvParser.getPlayers();

        Map<Integer, List<Score>> scores = csvParser.getScores(players);

        List<Round> rounds = csvParser.getRounds(scores);

        SQLiteDao writer = new SQLiteDao("card.db");

        players.forEach(writer::createPlayer);
        rounds.forEach(r-> {
            writer.createRound(r);
            r.getScores().forEach(s->writer.createScore(r, s));
        });
    }
}
