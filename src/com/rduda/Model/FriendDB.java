package com.rduda.Model;

import com.rduda.API.Account;
import com.rduda.API.FriendStore;
import com.rduda.Model.Exception.*;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robin on 2015-11-18.
 * <p>
 * Entity store managing Friends using Hibernate.
 */
class FriendDB implements FriendStore {

    private AccountMapping findByUsername(String name) throws FriendException {
        EntityManager manager = HibernateHelper.getManager();
        try {
            manager.getTransaction().begin();
            AccountMapping account = (AccountMapping) manager.createQuery(
                    "from AccountMapping where username = ?1")
                    .setParameter(1, name)
                    .getSingleResult();
            manager.getTransaction().commit();
            return account;
        } catch (Exception e) {
            if (manager.getTransaction().isActive())
                manager.getTransaction().rollback();

            if (e instanceof NoResultException)
                throw new NoSuchFriendException();

            throw new RuntimeException("Hibernate persistence error: transaction failed.");
        } finally {
            manager.close();
        }
    }

    @Override
    public void friendRequest(Account actor, Account target) throws FriendException {
        EntityManager manager = HibernateHelper.getManager();
        try {
            manager.getTransaction().begin();
            AccountMapping account = manager.find(AccountMapping.class, actor.getId());
            AccountMapping friend = manager.find(AccountMapping.class, target.getId());

            if (account.getRequests().contains(friend)) {
                account.getRequests().remove(friend);

                account.getFriends().add(friend);
                friend.getFriends().add(account);
            } else if (!friend.getRequests().contains(account))
                friend.getRequests().add(account);

            manager.getTransaction().commit();
        } catch (Exception e) {
            if (manager.getTransaction().isActive())
                manager.getTransaction().rollback();
        } finally {
            manager.close();
        }
    }

    @Override
    public void acceptRequest(Account actor, Account target) throws FriendException {
        EntityManager manager = HibernateHelper.getManager();
        try {
            manager.getTransaction().begin();

            AccountMapping account = manager.find(AccountMapping.class, actor.getId());
            AccountMapping friend = manager.find(AccountMapping.class, target.getId());

            if (account.getRequests().contains(friend)) {
                account.getRequests().remove(friend);
                friend.getRequests().remove(account);

                account.getFriends().add(friend);
                friend.getFriends().add(account);
            } else
                throw new FriendRequestMissingException();

            manager.getTransaction().commit();
        } catch (Exception e) {
            if (manager.getTransaction().isActive())
                manager.getTransaction().rollback();

            if (e instanceof FriendRequestMissingException)
                throw e;
        } finally {
            manager.close();
        }
    }

    @Override
    public void declineRequest(Account actor, Account target) {
        EntityManager manager = HibernateHelper.getManager();
        try {
            manager.getTransaction().begin();

            AccountMapping account = manager.find(AccountMapping.class, actor.getId());
            AccountMapping friend = manager.find(AccountMapping.class, target.getId());

            friend.getRequests().remove(account);
            account.getRequests().remove(friend);

            manager.getTransaction().commit();
        } catch (Exception e) {
            if (manager.getTransaction().isActive())
                manager.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            manager.close();
        }
    }

    @Override
    public void terminateFriendship(Account actor, Account target) throws FriendException {
        EntityManager manager = HibernateHelper.getManager();
        try {
            manager.getTransaction().begin();

            AccountMapping account = manager.find(AccountMapping.class, actor.getId());
            AccountMapping friend = manager.find(AccountMapping.class, target.getId());

            if (!account.getFriends().remove(friend))
                throw new NoSuchFriendException();

            if (!friend.getFriends().remove(account))
                throw new NoSuchFriendException();

            manager.getTransaction().commit();
        } catch (Exception e) {
            if (manager.getTransaction().isActive())
                manager.getTransaction().rollback();

            if (e instanceof FriendException)
                throw e;
        } finally {
            manager.close();
        }
    }

    @Override
    public List<Account> getFriendList(Account account) throws FriendException {
        return new ArrayList<>(findByUsername(account.getActor()).getFriends());
    }

    @Override
    public List<Account> getFriendRequests(Account account) throws FriendException {
        return new ArrayList<>(findByUsername(account.getActor()).getRequests());
    }

    @Override
    public boolean isFriendsWith(Account account, Account target) throws FriendException {
        return (getFriendList(target).contains(account) && getFriendList(account).contains(target));
    }

    @Override
    public boolean isFriendRequested(Account account, Account target) throws FriendException {
        return (getFriendRequests(target).contains(account));
    }
}
