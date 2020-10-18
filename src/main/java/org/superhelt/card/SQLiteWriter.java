package org.superhelt.card;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.superhelt.card.om.Player;

import java.sql.*;

public class SQLiteWriter implements AutoCloseable {

    private static final Logger log = LoggerFactory.getLogger(SQLiteWriter.class);

    private final Connection conn;

    public SQLiteWriter(String databaseName) {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:card.db");
            createTables();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to connect to database " + databaseName, e);
        }
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

    private void createTables() {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("create table if not exists player (id integer not null primary key, name text)");
            st.executeUpdate("create table if not exists round (id integer not null primary key, date text)");
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
