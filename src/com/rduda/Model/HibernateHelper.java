package com.rduda.Model;

import com.rduda.Model.Exception.StoreException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by Robin on 2015-11-12.
 * <p>
 * Factory for hibernate Managers.
 */
abstract class HibernateHelper {
    private static EntityManagerFactory managers;

    private synchronized static boolean initialized() {
        if (managers == null) {
            managers = Persistence.createEntityManagerFactory("persistence");
        }
        return true;
    }

    /**
     * @return An entity manager attached to the Hibernate configuration.
     */
    public static EntityManager getManager() {
        if (initialized())
            return managers.createEntityManager();
        else
            throw new StoreException();
    }

}
