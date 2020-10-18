package org.superhelt.card.db;

import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.superhelt.card.om.Player;
import org.superhelt.card.om.Round;
import org.superhelt.card.om.Score;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class SQLiteDao implements AutoCloseable {

    private static final Logger log = LoggerFactory.getLogger(SQLiteDao.class);
    private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final Connection conn;

    public SQLiteDao(String databaseName) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:"+databaseName);
            createTables();
        } catch (Exception e) {
            throw new RuntimeException("Unable to connect to database " + databaseName, e);
        }
    }

    public List<Player> getPlayers() {
        List<Player> result = new ArrayList<>();

        try(PreparedStatement st = conn.prepareStatement("select * from player");
            ResultSet rs = st.executeQuery()) {
            while(rs.next()) {
                result.add(mapPlayer(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to fetch players", e);
        }

        return result;
    }

    public List<Round> getRounds() {
        List<Round> result = new ArrayList<>();
        List<Player> players = getPlayers();

        try(PreparedStatement st = conn.prepareStatement("select * from round");
            ResultSet rs = st.executeQuery()) {
            while(rs.next()) {
                result.add(mapRound(players, rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to fetch rounds", e);
        }

        return result;
    }

    private Round mapRound(List<Player> players, ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        LocalDate date = LocalDate.parse(rs.getString("date"), df);
        List<Score> scores = getScores(players, id);

        return new Round(id, date, scores);
    }

    private List<Score> getScores(List<Player> players, int roundId) throws SQLException {
        List<Score> result = new ArrayList<>();

        try (PreparedStatement st = conn.prepareStatement("select * from score where roundid=?")) {
            st.setInt(1, roundId);

            try(ResultSet rs = st.executeQuery()) {
                while(rs.next()) {
                    result.add(mapScore(players, rs));
                }
            }
        }

        return result;
    }

    private Score mapScore(List<Player> players, ResultSet rs) throws SQLException {
        int playerId = rs.getInt("playerid");
        Player player = players.stream().filter(p->p.getId()==playerId).findFirst().orElseThrow(()->new IllegalArgumentException("Unknown player with id "+playerId));
        List<Integer> scores = new ArrayList<>();
        for(int i=1;i<=7; i++) {
            scores.add(rs.getInt("r"+i));
        }

        return new Score(player, scores);
    }

    private Player mapPlayer(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");

        return new Player(id, name);
    }

    public void createPlayer(Player player) {
        log.info("Preparing to create player {}", player.getName());
        try (PreparedStatement st = conn.prepareStatement("insert into player values (?,?)")) {
            st.setInt(1, player.getId());
            st.setString(2, player.getName());

            int rowCount = st.executeUpdate();

            if(rowCount!=1) {
                throw new RuntimeException("Number of rows updated was "+rowCount);
            }
            log.info("Successfully created player {}", player.getName());
        } catch (SQLException e) {
            throw new RuntimeException("Unable to create player "+player.getName(), e);
        }
    }

    public void createRound(Round round) {
        log.info("Preparing to create round {}", round.getDate());
        try(PreparedStatement st = conn.prepareStatement("insert into round values (?,?)")) {
            st.setInt(1, round.getId());
            st.setString(2, df.format(round.getDate()));

            int rowCount = st.executeUpdate();

            if(rowCount!=1) {
                throw new RuntimeException("Number of rows updated was "+rowCount);
            }
            log.info("Successfully created round {}", round.getDate());
        } catch (SQLException e) {
            throw new RuntimeException("Unable to create round "+round.getDate(), e);
        }
    }

    public void createScore(Round round, Score score) {
        log.info("Preparing to create score for round {} and player {}", round.getDate(), score.getPlayer().getName());
        try(PreparedStatement st = conn.prepareStatement("insert into score values (?,?,?,?,?,?,?,?,?)")) {
            st.setInt(1, round.getId());
            st.setInt(2, score.getPlayer().getId());
            for(int i=0;i<score.getScores().size(); i++) {
                st.setInt(i+3, score.getScores().get(i));
            }

            int rowCount = st.executeUpdate();

            if(rowCount!=1) {
                throw new RuntimeException("Number of rows updated was "+rowCount);
            }
            log.info("Successfully created score for round {} and player {}", round.getDate(), score.getPlayer().getName());
        } catch (SQLException e) {
            throw new RuntimeException("Unable to create score for round "+round.getId()+" and player "+score.getPlayer().getName());
        }
    }

    private void createTables() {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("create table if not exists player (id integer not null primary key autoincrement, name text)");
            st.executeUpdate("create table if not exists round (id integer not null primary key autoincrement, date text)");
            st.executeUpdate("create table if not exists score (roundid integer not null, playerid integer not null, " +
                    "r1 integer, r2 integer, r3 integer, r4 integer, r5 integer, r6 integer, r7 integer," +
                    "foreign key (roundid) references round (id), foreign key (playerid) references player (id))");
        } catch (SQLException throwables) {
            throw new RuntimeException("Unable to create tables");
        }
    }

    @Override
    public void close() throws Exception {
        if(conn!=null) {
            conn.close();
        }
    }
}
