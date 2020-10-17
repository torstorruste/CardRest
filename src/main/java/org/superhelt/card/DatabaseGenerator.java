package org.superhelt.card;

import org.superhelt.card.om.Player;
import org.superhelt.card.om.Round;
import org.superhelt.card.om.Score;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class DatabaseGenerator {

    public static void main(String[] args) throws Exception {
        Path basePath = Paths.get("C:\\projects\\CardRest\\src\\main\\resources");
        CsvParser csvParser = new CsvParser(basePath);
        List<Player> players = csvParser.getPlayers();

        players.stream().map(Player::getName).forEach(System.out::println);

        Map<Integer, List<Score>> scores = csvParser.getScores(players);

        List<Round> rounds = csvParser.getRounds(scores);

        rounds.stream().sorted(Comparator.comparing(Round::getDate)).map(Round::getDate).forEach(System.out::println);
    }
}
