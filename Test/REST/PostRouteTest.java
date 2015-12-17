package REST;

import Core.PostManagerClient;
import com.rduda.API.Request.PostList;
import com.rduda.API.Request.PostMapping;
import com.rduda.API.Request.RequestMapping;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Entity;

/**
 * Created by Robin on 2015-11-28.
 * <p>
 * Tests the /post routes in the REST api.
 */
public class PostRouteTest implements PostManagerClient {
    private RequestMapping account;
    private RequestMapping friend;

    @Before
    public void setUp() {
        account = AccountRouteTest.register(USER);
        friend = AccountRouteTest.register(FRIEND);
    }

    @After
    public void tearDown() throws Exception {
        AccountRouteTest.unregister(account);
        AccountRouteTest.unregister(friend);
    }

    @Test
    public void postUnknown() throws Exception {
        PostMapping post = new PostMapping(account)
                .setTarget(FRIEND)
                .setContent("message");

        try {
            post(post, REST.Post.POST_FRIEND);
            throw new Exception("Did not throw 404 when posting to non-friend.");
        } catch (NotFoundException ignored) {
        }
    }

    private void post(PostMapping post, String path) {
        REST.getClient()
                .path(path)
                .request(REST.TYPE)
                .post(Entity.entity(post, REST.TYPE), RequestMapping.class);
    }

    @Test
    public void postHome() throws Exception {
        PostMapping post = new PostMapping(account)
                .setContent("home");

        post(post, REST.Post.POST_HOME);

        assert getHomePosts().getList().size() != 0;
    }

    private PostList getHomePosts() {
        return REST.getClient()
                .path(REST.Post.POST_HOME)
                .queryParam("actor", account.getActor())
                .queryParam("token", account.getToken())
                .request(REST.TYPE)
                .get(PostList.class);
    }

    @Test
    public void postFriend() throws Exception {
        FriendRouteTest.makeFriends(account, friend);

        PostMapping post = new PostMapping(account)
                .setContent("content")
                .setTarget(FRIEND);

        post(post, REST.Post.POST_FRIEND);
    }

    @Test
    public void removePost() throws Exception {
        postHome();

        PostMapping post = new PostMapping(account)
                .setId(getHomePosts().getList().get(0).getId());

        removePost(post);
    }

    private void removePost(PostMapping post) {
        REST.getClient()
                .path(REST.Post.REMOVE)
                .request(REST.TYPE)
                .post(Entity.entity(post, REST.TYPE), RequestMapping.class);
    }

    @Test
    public void friendPosts() throws Exception {
        FriendRouteTest.makeFriends(account, friend);
        postHome();

        assert getAccountPostsByFriend().getList().size() != 0;
    }

    private PostList getAccountPostsByFriend() {
        return REST.getClient()
                .path(REST.Post.POST_FRIEND)
                .queryParam("actor", friend.getActor())
                .queryParam("token", friend.getToken())
                .queryParam("target", USER)
                .request(REST.TYPE)
                .get(PostList.class);
    }

    private PostList getFriendPosts() {
        return REST.getClient()
                .path(REST.Post.POST_FRIEND)
                .queryParam("actor", account.getActor())
                .queryParam("token", account.getToken())
                .queryParam("target", FRIEND)
                .request(REST.TYPE)
                .get(PostList.class);
    }

    @Test
    public void removeRemotePost() throws Exception {
        postFriend();

        PostList posts = getFriendPosts();
        assert posts.getList().size() != 0;

        PostMapping post = new PostMapping(account)
                .setId(posts.getList().get(0).getId());

        removePost(post);

        assert getFriendPosts().getList().size() == 0;
    }

    @Test
    public void streamPosts() throws Exception {
        FriendRouteTest.makeFriends(account, friend);
        postHome();

        PostList result = REST.getClient()
                .path(REST.Post.STREAM_POSTS)
                .queryParam("actor", friend.getActor())
                .queryParam("token", friend.getToken())
                .request(REST.TYPE)
                .get(PostList.class);

        assert result.getList().size() != 0;
    }

    @Test
    public void getFriendPostsRejected() throws Exception {
        try {
            PostList result = REST.getClient()
                    .path(REST.Post.POST_FRIEND)
                    .queryParam("actor", friend.getActor())
                    .queryParam("token", friend.getToken())
                    .queryParam("target", account.getActor())
                    .request(REST.TYPE)
                    .get(PostList.class);

            throw new Exception("Does not throw 404 when friend does not exist.");
        } catch (NotFoundException ignored) {
        }
    }
}
