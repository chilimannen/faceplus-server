package Core;

import org.junit.Test;

/**
 * Created by Robin on 2015-11-28.
 * <p>
 * Tests a PostManager.
 */
public interface PostManagerClient {
    public static final String USER = "pst";
    public static final String FRIEND = "fst";

    /**
     * Verifies that it is not possible to send a post to an Account
     * that is not in the friendlist.
     */
    @Test
    void postUnknown() throws Exception;

    /**
     * Tests that it is possible to post to home-wall.
     */
    @Test
    void postHome() throws Exception;

    /**
     * Tests that it is possible to post to a friends wall.
     */
    @Test
    void postFriend() throws Exception;

    /**
     * Tests that it is possible to remove a post.
     */
    @Test
    void removePost() throws Exception;

    /**
     * Tests that it is possible to see the posts of a friend.
     */
    @Test
    void friendPosts() throws Exception;

    /**
     * Tests that it is possible to remove a post on a friends wall
     * made by the current account.
     */
    @Test
    void removeRemotePost() throws Exception;

    /**
     * Tests that the latest posts friend have uploaded on the home-wall
     * is included in the stream.
     */
    @Test
    void streamPosts() throws Exception;

    /**
     * Verifies that it is not possible to retrieve the posts of anotther account
     * where the other account is not a friend.
     */
    @Test
    void getFriendPostsRejected() throws Exception;
}
