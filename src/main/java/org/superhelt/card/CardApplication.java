package org.superhelt.card;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/")
public class CardApplication extends ResourceConfig {
    public CardApplication() {
        register(new CardBinder());
        packages("org.superhelt.card");
    }
}
