package org.superhelt.card;

import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.superhelt.card.db.SQLiteDao;

public class CardBinder extends AbstractBinder implements Factory<SQLiteDao>  {

    @Override
    protected void configure() {
        bindFactory(this).to(SQLiteDao.class);
    }

    @Override
    public SQLiteDao provide() {
        return new SQLiteDao("C:\\projects\\CardRest\\card.db");
    }

    @Override
    public void dispose(SQLiteDao instance) {
        try {
            instance.close();
        } catch (Exception e) {
            throw new RuntimeException("Unable to close sqlite dao", e);
        }
    }
}
