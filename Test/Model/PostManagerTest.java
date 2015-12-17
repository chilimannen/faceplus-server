package Model;

import Core.PostManagerClient;
import Mock.ManagerProvider;
import com.rduda.API.AccountManager;
import com.rduda.Model.Exception.AuthenticationException;
import com.rduda.Model.Exception.FriendException;
import com.rduda.Model.Exception.NoSuchAccountException;
import com.rduda.API.FriendManager;
import com.rduda.API.Post;
import com.rduda.API.PostManager;
import com.rduda.Model.Exception.NoSuchFriendException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Robin on 2015-11-18.
 *
 * Tests a PostManager.
 */
public class PostManagerTest implements PostManagerClient {
    private static final String ACCOUNT = "plc";
    private static final String PASSWORD = ".1";
    private static final String FRIEND = "plx";
    private static final String content = "hey this is the post... ";
    private static final String content_friend = "hey nice wall";
    private AccountManager account;
    private AccountManager friend;

    @Before
    public void setUp() throws AuthenticationException {
        account = ManagerProvider.getAccountManager();
        account.register(ACCOUNT, PASSWORD, "", "");

        friend = ManagerProvider.getAccountManager();
        friend.register(FRIEND, PASSWORD, "", "");
    }

    @After
    public void tearDown() throws NoSuchAccountException {
        account.unregister();
        friend.unregister();
    }

    @Override
    @Test
    public void postUnknown() throws Exception {
        try {
            account.getPostManager().postFriend(FRIEND, content);
            throw new Exception("Posting to unknown user should not succeed.");
        } catch (FriendException ignored) {
        }
    }

    @Override
    @Test
    public void postHome() throws Exception {
        makeFriends();
        account.getPostManager().postHome(content);
        assert account.getPostManager().getHomePosts().size() != 0;

        friend.getPostManager().postHome(content);
        assert friend.getPostManager().getHomePosts().size() != 0;
    }

    private void makeFriends() throws Exception {
        account.getFriendManager().request(FRIEND);
        friend.getFriendManager().accept(ACCOUNT);

        if (!account.getFriendManager().getFriendList().contains(friend.getAccount()))
            throw new Exception("Friend setup failed: Run FriendHandlerTest.");
    }

    @Override
    @Test
    public void postFriend() throws Exception {
        makeFriends();
        FriendManager friendManager = account.getFriendManager();

        account.getPostManager().postFriend(
                friendManager.getFriendList().get(0).getActor(),
                content_friend);
        assert account.getPostManager().getFriendPosts(
                friendManager.getFriendList().get(0).getActor())
                .size() != 0;
    }

    @Override
    @Test
    public void removePost() throws Exception {
        makeFriends();
        PostManager postManager = account.getPostManager();
        postManager.postHome(content);

        int postCount = postManager.getHomePosts().size();
        postManager.removePost(postManager.getHomePosts().get(0).getId());
        assert postManager.getHomePosts().size() != postCount;
    }

    @Override
    @Test
    public void friendPosts() throws Exception {
        makeFriends();
        friend.getPostManager().postHome(content);

        assert account.getPostManager().getFriendPosts(
                account.getFriendManager().getFriendList().get(0).getActor()
        ).size() != 0;
    }

    @Override
    @Test
    public void getFriendPostsRejected() throws Exception {
        try {
            account.getPostManager().getFriendPosts(FRIEND);
            throw new Exception("Posts readable by non-friend.");
        } catch (NoSuchFriendException ignored) {
        }
    }

    @Override
    @Test
    public void removeRemotePost() throws Exception {
        makeFriends();
        PostManager postManager = account.getPostManager();
        postManager.postFriend(FRIEND, content_friend);
        int postCount = postManager.getFriendPosts(FRIEND).size();

        for (Post post : postManager.getFriendPosts(FRIEND)) {
            if (post.getSender().equals(account.getAccount())) {
                postManager.removePost(post.getId());
                break;
            }
        }
        assert postManager.getFriendPosts(FRIEND).size() != postCount;
    }

    @Override
    @Test
    public void streamPosts() throws Exception {
        makeFriends();
        friend.getPostManager().postHome(content_friend);
        assert account.getPostManager().getStreamPosts().size() != 0;
    }
}
