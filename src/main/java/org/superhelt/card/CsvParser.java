package org.superhelt.card;

import org.superhelt.card.om.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CsvParser {

    private final Path basePath;

    public CsvParser(Path basePath) {
        this.basePath = basePath;
    }

    public List<Player> getPlayers() {
        List<Player> result = new ArrayList<>();

        try(BufferedReader reader = Files.newBufferedReader(basePath.resolve("Players.csv"))) {
            String line;
            while((line = reader.readLine()) != null) {

            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to fetch players", e);
        }

        return result;
    }
}
