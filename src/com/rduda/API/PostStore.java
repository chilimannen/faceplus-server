package com.rduda.API;

import com.rduda.Model.Exception.NoSuchFriendException;

import java.util.List;

/**
 * Created by Robin on 2015-11-18.
 *
 * Provides persistent storage for Posts.
 */
public interface PostStore {
    /**
     * Adds a new post to the store.
     * @param post To be added.
     */
    void addPost(Post post);

    /**
     * Removes a post from the store. The postId must belong to a post
     *      that was created by Account.
     * @param account The Account that created the post.
     * @param postId The identifier of the post to be removed.
     */
    void removePost(Account account, long postId);

    /**
     * Get all posts on the wall of the Account.
     * @param account as the receiver of the post.
     * @return All posts on the wall of the Account both received
     *      by friends and posted by self.
     */
    List<Post> getHomePosts(Account account);

    /**
     * Get all the posts from the stream. The stream includes all Posts that
     * friends have posted on their own wall. Friend->Friend posts will not be
     * shown in the stream.
     * @param account as the stream perspective.
     * @return A list of posts from the Stream.
     */
    List<Post> getStreamPosts(Account account);

    /**
     * Get all posts on a friends wall where the sender is the friend
     *      or a friend of the Account in-context.
     * @param friend as the target for message retrieval.
     * @return A list of all posts where the sender is known from a Friends wall.
     * @throws NoSuchFriendException when the target is not a friend of self.
     */
    List<Post> getFriendPosts(Account account, Account friend) throws NoSuchFriendException;
}
