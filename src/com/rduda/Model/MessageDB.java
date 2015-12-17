package com.rduda.Model;

import com.rduda.API.Account;
import com.rduda.API.Message;
import com.rduda.API.MessageStore;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robin on 2015-11-20.
 * <p>
 * Message store implementation using Hibernate.
 */
class MessageDB implements MessageStore {
    private static final int LIMIT = 8;

    @Override
    public List<Message> getMessagesFrom(Account actor, Account target, long offset) {
        List<Message> messages = new ArrayList<>();
        EntityManager manager = HibernateHelper.getManager();
        try {
            manager.getTransaction().begin();

            AccountMapping sender = manager.find(AccountMapping.class, actor.getId());
            AccountMapping friend = manager.find(AccountMapping.class, target.getId());

            List result = manager.createQuery("from MessageMapping as message " +
                    "where (message.receiver = ?1 or message.receiver = ?2) " +
                    "and (message.sender = ?1 or message.sender = ?2)" +
                    "and (message.id < :offset) " +
                    "order by message.id desc")
                    .setParameter(1, sender)
                    .setParameter(2, friend)
                    .setParameter("offset", offset)
                    .setMaxResults(LIMIT)
                    .getResultList();

            for (Object message : result) {
                messages.add((Message) message);
            }
            manager.getTransaction().commit();
        } catch (Exception e) {
            if (manager.getTransaction().isActive())
                manager.getTransaction().rollback();
        } finally {
            manager.close();
        }
        return messages;
    }

    @Override
    public void sendMessage(Message message) {
        EntityManager manager = HibernateHelper.getManager();
        try {
            manager.getTransaction().begin();

            AccountMapping actor = manager.find(AccountMapping.class, message.getSender().getId());
            AccountMapping target = manager.find(AccountMapping.class, message.getReceiver().getId());

            MessageMapping envelope = new MessageMapping();
            envelope.setContent(message.getContent());
            envelope.setDate(message.getDate());
            envelope.setSender(actor);
            envelope.setReceiver(target);
            manager.persist(envelope);

            manager.getTransaction().commit();
        } catch (Exception e) {
            if (manager.getTransaction().isActive())
                manager.getTransaction().rollback();
        } finally {
            manager.close();
        }
    }

    @Override
    public List<Message> getLatestMessages(Account actor) {
        List<Message> messages = new ArrayList<>();
        EntityManager manager = HibernateHelper.getManager();
        try {
            manager.getTransaction().begin();

            AccountMapping account = manager.find(AccountMapping.class, actor.getId());

            List result = manager.createQuery("FROM MessageMapping as message " +
                    "WHERE message.receiver = :receiver " +
                    "AND message.sender IN (:friends) " +
                    "AND message.id = " +
                    "    (select MAX(id) from MessageMapping WHERE sender = message.sender)" +
                    "ORDER BY id DESC")
                    .setParameter("receiver", account)
                    .setParameter("friends", account.getFriends())
                    .setMaxResults(LIMIT)
                    .getResultList();

            for (Object message : result) {
                messages.add((Message) message);
            }
            manager.getTransaction().commit();
        } catch (Exception e) {
            if (manager.getTransaction().isActive())
                manager.getTransaction().rollback();
        } finally {
            manager.close();
        }
        return messages;
    }

}
