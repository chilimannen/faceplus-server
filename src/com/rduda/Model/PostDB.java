package com.rduda.Model;

import com.rduda.API.Account;
import com.rduda.API.Post;
import com.rduda.API.PostStore;
import com.rduda.Model.Exception.NoSuchFriendException;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Robin on 2015-11-18.
 * <p>
 * Persistent storage for Posts using Hibernate.
 */
class PostDB implements PostStore {
    private static final int POST_READ_LIMIT = 1000;

    @Override
    public void addPost(Post envelope) {
        PostMapping post = (PostMapping) envelope;
        EntityManager manager = HibernateHelper.getManager();
        try {
            manager.getTransaction().begin();
            manager.merge(post);
            manager.getTransaction().commit();
        } catch (Exception e) {
            if (manager.getTransaction().isActive())
                manager.getTransaction().rollback();
        } finally {
            manager.close();
        }
    }

    @Override
    public void removePost(Account account, long postId) {
        EntityManager manager = HibernateHelper.getManager();
        try {
            manager.getTransaction().begin();
            PostMapping post = manager.find(PostMapping.class, postId);

            if (post.getSender().getId().equals(account.getId()) || post.getReceiver().getId().equals(account.getId()))
                manager.remove(post);

            manager.getTransaction().commit();
        } catch (Exception e) {
            if (manager.getTransaction().isActive())
                manager.getTransaction().rollback();
        } finally {
            manager.close();
        }
    }

    @Override
    public List<Post> getHomePosts(Account account) {
        List<Post> posts = new ArrayList<>();

        EntityManager manager = HibernateHelper.getManager();
        try {
            manager.getTransaction().begin();

            Iterator iterator = manager.createQuery(
                    "from PostMapping as post where post.receiver = ?1 order by post.id desc")
                    .setParameter(1, account)
                    .setMaxResults(POST_READ_LIMIT)
                    .getResultList().iterator();

            while (iterator.hasNext()) {
                posts.add((PostMapping) iterator.next());
            }

            manager.getTransaction().commit();
        } catch (Exception e) {
            if (manager.getTransaction().isActive())
                manager.getTransaction().rollback();
        } finally {
            manager.close();
        }
        return posts;
    }

    @Override
    public List<Post> getStreamPosts(Account actor) {
        List<Post> posts = new ArrayList<>();

        EntityManager manager = HibernateHelper.getManager();
        try {
            manager.getTransaction().begin();
            AccountMapping account = manager.find(AccountMapping.class, actor.getId());
            Iterator iterator = manager.createQuery("from PostMapping as post where post.receiver = post.sender and post.receiver in ?1 order by post.id desc")
                    .setParameter(1, account.getFriends())
                    .setMaxResults(POST_READ_LIMIT)
                    .getResultList().iterator();

            while (iterator.hasNext())
                posts.add((Post) iterator.next());

            manager.getTransaction().commit();
        } catch (Exception e) {
            if (manager.getTransaction().isActive())
                manager.getTransaction().rollback();
        } finally {
            manager.close();
        }
        return posts;
    }

    @Override
    public List<Post> getFriendPosts(Account actor, Account source) throws NoSuchFriendException {
        List<Post> posts = new ArrayList<>();
        EntityManager manager = HibernateHelper.getManager();
        try {
            manager.getTransaction().begin();
            AccountMapping account = manager.find(AccountMapping.class, actor.getId());
            AccountMapping friend = manager.find(AccountMapping.class, source.getId());

            if (account.getFriends().contains(friend)) {
                List result = manager.createQuery(
                        "from PostMapping as post where post.receiver = :target and " +
                                "(post.sender in (:friends) or post.sender = :account) order by post.id desc")
                        .setParameter("friends", account.getFriends())
                        .setParameter("account", account)
                        .setParameter("target", friend)
                        .setMaxResults(POST_READ_LIMIT)
                        .getResultList();

                for (Object post : result) {
                    posts.add((Post) post);
                }
            } else
                throw new NoSuchFriendException();

            manager.getTransaction().commit();
        } catch (Exception e) {
            if (manager.getTransaction().isActive())
                manager.getTransaction().rollback();

            if (e instanceof NoSuchFriendException)
                throw new NoSuchFriendException();
        } finally {
            manager.close();
        }
        return posts;
    }
}
