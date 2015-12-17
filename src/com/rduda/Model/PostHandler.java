package com.rduda.Model;

import com.rduda.API.*;
import com.rduda.Model.Exception.*;

import java.util.Date;
import java.util.List;

/**
 * Created by Robin on 2015-11-18.
 * <p>
 * PostHandler is a business layer component,
 * it may only be accessed through an existing
 * Account-context from the AccountManager.
 */
class PostHandler implements PostManager {
    private StoreProvider provider;
    private Account account;
    private PostStore posts;
    private AccountStore accounts;

    protected PostHandler(StoreProvider provider, Account account) throws AuthenticationException {
        this.provider = provider;
        this.account = account;
        this.posts = provider.getPostStore();
        this.accounts = provider.getAccountStore();
    }

    @Override
    public void postHome(String content) throws PostInvalidArgumentsException {
        Post post = provider.getPostModel(account, account, content, new Date().toString());
        posts.addPost(post);
    }

    @Override
    public void postFriend(String target, String content) throws FriendException {
        try {
            Account friend = accounts.findByUsername(target);
            Post post = provider.getPostModel(account, friend, content, new Date().toString());

            if (provider.getFriendStore().getFriendList(account).contains(friend))
                posts.addPost(post);
            else
                throw new NoSuchFriendException();
        } catch (NoSuchAccountException e) {
            throw new FriendException();
        }
    }

    @Override
    public void removePost(long postId) {
        posts.removePost(account, postId);
    }

    @Override
    public List<Post> getHomePosts() {
        return PostBean.FromList(posts.getHomePosts(account));
    }

    @Override
    public List<Post> getFriendPosts(String friend) throws NoSuchFriendException {
        try {
            return PostBean.FromList(posts.getFriendPosts(account, accounts.findByUsername(friend)));
        } catch (NoSuchAccountException e) {
            throw new NoSuchFriendException();
        }
    }

    @Override
    public List<Post> getStreamPosts() {
        return PostBean.FromList(posts.getStreamPosts(account));
    }
}
