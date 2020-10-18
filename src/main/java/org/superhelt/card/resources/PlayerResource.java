package org.superhelt.card.resources;

import org.superhelt.card.db.SQLiteDao;
import org.superhelt.card.om.Player;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/players")
@Produces(MediaType.APPLICATION_JSON)
public class PlayerResource {

    private final SQLiteDao sqLiteDao;

    @Inject
    public PlayerResource(SQLiteDao sqLiteDao) {
        this.sqLiteDao = sqLiteDao;
    }

    @GET
    public Response getPlayers() {
        List<Player> players = sqLiteDao.getPlayers();

        return Response.ok(players).build();
    }
}
