package org.superhelt.card.resources;

import org.superhelt.card.db.SQLiteDao;
import org.superhelt.card.om.Player;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/players")
@Produces(MediaType.APPLICATION_JSON)
public class PlayerResource {

    private final SQLiteDao dao;

    @Inject
    public PlayerResource(SQLiteDao dao) {
        this.dao = dao;
    }

    @GET
    public Response getPlayers() {
        List<Player> players = dao.getPlayers();

        return Response.ok(players).build();
    }

    @Path("/{id}")
    @GET
    public Response getPlayer(@PathParam("id") int id) {
        var players = dao.getPlayers();
        var player = players.stream().filter(p->p.getId()==id).findFirst();

        if(player.isPresent()) {
            return Response.ok(player.get()).build();
        } else {
            return Response.status(404).build();
        }
    }
}
