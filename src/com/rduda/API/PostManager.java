package com.rduda.API;

import com.rduda.Model.Exception.FriendException;
import com.rduda.Model.Exception.NoSuchFriendException;
import com.rduda.Model.Exception.PostInvalidArgumentsException;

import java.util.List;

/**
 * Created by Robin on 2015-11-18.
 * <p>
 * Handles the retrieval and submission of posts form the
 * post-store.
 */
public interface PostManager {
    /**
     * Post a log message to the Account in-context.
     * @param content Textual content of the message.
     * @throws PostInvalidArgumentsException when the post headers are incorrect.
     */
    void postHome(String content) throws PostInvalidArgumentsException;

    /**
     * Posts a log message to a friend.
     * @param target Friend to post the message to referenced by username.
     * @param content Textual content of the post.
     * @throws FriendException when the Sender and receiver are not friends.
     */
    void postFriend(String target, String content) throws FriendException;

    /**
     * Removes a post where the Account in-context is either the
     * sender or receiver of the Post.
     * @param postId unique post identifier.
     */
    void removePost(long postId);

    /**
     * Get all posts on the wall of the Account in-context.
     * @return All posts on the wall of the Account both received
     *      by friends and posted by self.
     */
    List<Post> getHomePosts();

    /**
     * Get all posts on a friends wall where the sender is the friend
     *      or a friend of the Account in-context.
     * @param friend Friend to post the message to referenced by username.
     * @return A list of all posts where the sender is known from a Friends wall.
     * @throws NoSuchFriendException when the target is not a friend of self.
     */
    List<Post> getFriendPosts(String friend) throws NoSuchFriendException;

    /**
     * Get all the posts from the stream. The stream includes all Posts that
     * friends have posted on their own wall. Friend->Friend posts will not be
     * shown in the stream.
     * @return A list of posts from the Stream.
     */
    List<Post> getStreamPosts();
}
