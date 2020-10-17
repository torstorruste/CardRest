package org.superhelt.card;

import org.superhelt.card.om.Player;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class DatabaseGenerator {

    public static void main(String[] args) throws Exception {
        Path basePath = Paths.get("C:\\projects\\CardRest\\src\\main\\resources");
        CsvParser csvParser = new CsvParser(basePath);
        List<Player> players = csvParser.getPlayers();

        players.stream().map(Player::getName).forEach(System.out::println);
    }
}
