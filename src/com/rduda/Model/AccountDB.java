package com.rduda.Model;

import com.rduda.API.Account;
import com.rduda.API.AccountStore;
import com.rduda.API.Image;
import com.rduda.Model.Exception.AccountAlreadyExistsException;
import com.rduda.Model.Exception.AuthenticationException;
import com.rduda.Model.Exception.NoSuchAccountException;
import com.rduda.Model.Exception.NoSuchImageException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Robin on 2015-11-11.
 * <p>
 * Persistent storage for Account entities using Hibernate.
 */
class AccountDB implements AccountStore {
    private static final int MAX_FIND_ANY = 12;

    @Override
    public Account add(Account toAdd) throws AccountAlreadyExistsException {
        AccountMapping account = (AccountMapping) toAdd;
        EntityManager manager = HibernateHelper.getManager();
        try {
            manager.getTransaction().begin();
            account.setToken(SessionHelper.getSessionId());
            manager.persist(account);

            manager.getTransaction().commit();
        } catch (Exception e) {
            if (manager.getTransaction().isActive())
                manager.getTransaction().rollback();
            throw new AccountAlreadyExistsException();
        } finally {
            manager.close();
        }
        return account;
    }

    @Override
    public Account findByUsername(String username) throws NoSuchAccountException {
        EntityManager manager = HibernateHelper.getManager();
        try {
            manager.getTransaction().begin();
            Account account = (Account) manager.createQuery(
                    "from AccountMapping where username = ?1 order by username")
                    .setParameter(1, username)
                    .getSingleResult();
            manager.getTransaction().commit();
            return account;
        } catch (Exception e) {
            if (manager.getTransaction().isActive())
                manager.getTransaction().rollback();

            if (e instanceof NoResultException)
                throw new NoSuchAccountException();

            throw new RuntimeException();
        } finally {
            manager.close();
        }
    }

    @Override
    public void remove(Account target) throws NoSuchAccountException {
        EntityManager manager = HibernateHelper.getManager();
        try {
            manager.getTransaction().begin();

            AccountMapping account = manager.find(AccountMapping.class, target.getId());
            account.setFriends(null);

            // FK's not set to proper cascade mode, must remove manually..
            manager.createNativeQuery("DELETE FROM post WHERE sender = :id OR receiver = :id")
                    .setParameter("id", target.getId()).executeUpdate();

            manager.createNativeQuery("DELETE FROM message WHERE sender = :id OR receiver = :id")
                    .setParameter("id", target.getId()).executeUpdate();

            manager.createNativeQuery("DELETE FROM friend WHERE friends_id = :id OR AccountMapping_id = :id")
                    .setParameter("id", target.getId()).executeUpdate();

            manager.createNativeQuery("DELETE FROM request WHERE requests_id = :id OR AccountMapping_id = :id")
                    .setParameter("id", target.getId()).executeUpdate();
            manager.getTransaction().commit();

            manager.getTransaction().begin();
            manager.createQuery("delete AccountMapping as account where account.id = ?1")
                    .setParameter(1, target.getId()).executeUpdate();

            manager.getTransaction().commit();
        } catch (Exception e) {
            if (manager.getTransaction().isActive())
                manager.getTransaction().rollback();

            throw new NoSuchAccountException();
        } finally {
            manager.close();
        }
    }

    @Override
    public List<Account> findByAny(String query) {
        List<Account> result = new ArrayList<>();

        EntityManager manager = HibernateHelper.getManager();
        try {
            manager.getTransaction().begin();

            Iterator accounts = manager.createQuery(
                    "from AccountMapping where username like :query or firstName like :query or lastName like :query order by username")
                    .setParameter("query", "%" + query + "%")
                    .setMaxResults(MAX_FIND_ANY)
                    .getResultList().iterator();

            while (accounts.hasNext()) {
                result.add((Account) accounts.next());
            }

            manager.getTransaction().commit();
        } catch (Exception e) {
            if (manager.getTransaction().isActive())
                manager.getTransaction().rollback();
        } finally {
            manager.close();
        }
        return result;
    }

    @Override
    public Account authenticate(String username, String password) throws AuthenticationException {
        Account authenticated = null;

        EntityManager manager = HibernateHelper.getManager();
        try {
            manager.getTransaction().begin();
            AccountMapping account = (AccountMapping) manager.createQuery(
                    "from AccountMapping where username = ?1")
                    .setParameter(1, username)
                    .getSingleResult();

            if (HashHelper.hash(password, account.getSalt()).equals(account.getHashedPassword())) {
                account.setToken(SessionHelper.getSessionId());
                authenticated = account;
            }

            manager.getTransaction().commit();
        } catch (Exception e) {
            if (manager.getTransaction().isActive())
                manager.getTransaction().rollback();

            if (e instanceof NoResultException)
                throw new NoSuchAccountException();

            throw new RuntimeException();
        } finally {
            manager.close();
        }

        if (authenticated == null)
            throw new AuthenticationException();
        else
            return authenticated;
    }

    @Override
    public void setProfile(Account actor, String firstName, String lastName, String country, Integer age) {
        EntityManager manager = HibernateHelper.getManager();
        try {
            manager.getTransaction().begin();

            AccountMapping account = manager.find(AccountMapping.class, actor.getId());

            account.setFirstName(firstName)
                    .setLastName(lastName)
                    .setAge(age)
                    .setCountry(country);

            manager.getTransaction().commit();
        } catch (Exception e) {
            if (manager.getTransaction().isActive())
                manager.getTransaction().rollback();
        } finally {
            manager.close();
        }
    }

    @Override
    public void setProfileImage(Account actor, String base64encoded) {
        EntityManager manager = HibernateHelper.getManager();
        try {
            manager.getTransaction().begin();

            AccountMapping account = manager.find(AccountMapping.class, actor.getId());

            if (account.getProfileImage() != null) {
                ImageMapping previous = manager.find(ImageMapping.class, account.getProfileImage());
                manager.remove(previous);
            }

            ImageMapping image = new ImageMapping();
            image.setBase64(base64encoded);
            manager.persist(image);
            manager.refresh(image);

            account.setProfileImage(image.getId());

            manager.getTransaction().commit();
        } catch (Exception e) {
            if (manager.getTransaction().isActive())
                manager.getTransaction().rollback();
        } finally {
            manager.close();
        }
    }

    @Override
    public Image getProfileImage(long id) throws NoSuchImageException {
        Image result = null;

        EntityManager manager = HibernateHelper.getManager();
        try {
            manager.getTransaction().begin();

            AccountMapping account = manager.find(AccountMapping.class, id);
            result = manager.find(ImageMapping.class, account.getProfileImage());

            manager.getTransaction().commit();
        } catch (Exception e) {
            if (manager.getTransaction().isActive())
                manager.getTransaction().rollback();
        } finally {
            manager.close();
        }
        if (result == null) {
            throw new NoSuchImageException();
        } else
            return result;
    }

    @Override
    public Account findById(long id) throws NoSuchAccountException {
        Account result = null;

        EntityManager manager = HibernateHelper.getManager();
        try {
            manager.getTransaction().begin();
            result = manager.find(AccountMapping.class, id);
            manager.getTransaction().commit();
        } catch (Exception e) {
            if (manager.getTransaction().isActive())
                manager.getTransaction().rollback();
        } finally {
            manager.close();
        }
        if (result == null) {
            throw new NoSuchAccountException();
        } else
            return result;
    }
}
