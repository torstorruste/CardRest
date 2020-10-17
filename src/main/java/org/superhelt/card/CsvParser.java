package org.superhelt.card;

import org.superhelt.card.om.Player;
import org.superhelt.card.om.Round;
import org.superhelt.card.om.Score;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class CsvParser {

    private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final Path basePath;

    public CsvParser(Path basePath) {
        this.basePath = basePath;
    }

    public List<Player> getPlayers() {
        List<Player> result = new ArrayList<>();
        final AtomicInteger id = new AtomicInteger(0);

        try(BufferedReader reader = Files.newBufferedReader(basePath.resolve("Players.csv"))) {
            handleRows(reader, line->result.add(new Player(id.getAndIncrement(), line.replaceAll("\"", ""))));
        } catch (IOException e) {
            throw new RuntimeException("Unable to fetch players", e);
        }

        return result;
    }

    public Map<Integer, List<Score>> getScores(List<Player> players) {
        Map<Integer, List<Score>> result = new HashMap<>();

        try(BufferedReader reader = Files.newBufferedReader(basePath.resolve("Score.csv"))) {
            handleRows(reader, line->{
                String[] fields = line.replaceAll("\"", "").split(",");
                Player player = getPlayerByName(players, fields[0]);
                int id = Integer.parseInt(fields[1]);
                List<Integer> scores = new ArrayList<>();
                for(int i=2;i<9;i++) {
                    scores.add(Integer.parseInt(fields[i]));
                }

                Score score = new Score(player, scores);

                result.computeIfAbsent(id, (k)->new ArrayList<>()).add(score);
            });
        } catch (IOException e) {
            throw new RuntimeException("Unable to fetch scores", e);
        }

        return result;
    }

    public List<Round> getRounds(Map<Integer, List<Score>> scores) {
        List<Round> result = new ArrayList<>();

        try(BufferedReader reader = Files.newBufferedReader(basePath.resolve("Rounds.csv"))) {
            handleRows(reader, line->{
                String[] fields = line.replaceAll("\"", "").split(",");
                int id = Integer.parseInt(fields[0]);
                LocalDate date = LocalDate.parse(fields[1], df);

                result.add(new Round(id, date, scores.get(id)));
            });
        } catch (IOException e) {
            throw new RuntimeException("Unable to fetch rounds", e);
        }

        return result;
    }

    private void handleRows(BufferedReader reader, Consumer<String> lineHandler) throws IOException {
        String line;
        while((line = reader.readLine()) != null) {
            lineHandler.accept(line);
        }
    }

    private Player getPlayerByName(List<Player> players, String name) {
        return players.stream().filter(p->p.getName().equals(name)).findFirst().orElseThrow(()->new RuntimeException("Unknown player: "+name));
    }
}
