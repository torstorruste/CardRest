package org.superhelt.card.resources;

import org.superhelt.card.db.SQLiteDao;
import org.superhelt.card.om.Round;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("rounds")
@Produces(MediaType.APPLICATION_JSON)
public class RoundResource {

    private final SQLiteDao dao;

    @Inject
    public RoundResource(SQLiteDao dao) {
        this.dao = dao;
    }

    @GET
    public Response getRounds() {
        var rounds = dao.getRounds();

        return Response.ok(rounds).build();
    }

    @Path("/{id}")
    @GET
    public Response getRound(@PathParam("id") int id) {
        var rounds = dao.getRounds();
        Optional<Round> round = rounds.stream().filter(r -> r.getId() == id).findFirst();

        if(round.isPresent()) {
            return Response.ok(round.get()).build();
        } else {
            return Response.status(404).build();
        }
    }
}
