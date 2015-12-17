package Model;

import Core.FriendManagerClient;
import Mock.ManagerProvider;
import com.rduda.API.AccountManager;
import com.rduda.API.FriendManager;
import com.rduda.Model.Exception.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Robin on 2015-11-18.
 * <p>
 * Tests a FriendManager and/or FriendStore.
 */
public class FriendManagerTest implements FriendManagerClient {
    private static final String ACCOUNT = ".";
    private static final String PASS = "-1";
    private static final String FRIEND = ";";
    private static final String PASS_FRIEND = "-1";
    private AccountManager account = ManagerProvider.getAccountManager();
    private AccountManager friend = ManagerProvider.getAccountManager();

    @Before
    public void setUp() throws AuthenticationException, FriendException {
        account.register(ACCOUNT, PASS, "", "");
        friend.register(FRIEND, PASS_FRIEND, "", "");

        assert account.getFriendManager().getFriendList().size() == 0;
        assert friend.getFriendManager().getFriendList().size() == 0;

        assert account.getFriendManager().getFriendRequests().size() == 0;
        assert friend.getFriendManager().getFriendRequests().size() == 0;
    }

    @After
    public void tearDown() throws NoSuchAccountException {
        account.unregister();
        friend.unregister();
    }

    @Override
    @Test
    public void sendFriendRequest() throws Exception {
        account.getFriendManager().request(FRIEND);
        assert account.getFriendManager().isFriendRequested(FRIEND);
    }

    @Override
    @Test
    public void declineFriendRequest() throws Exception {
        account.getFriendManager().request(FRIEND);
        assert friend.getFriendManager().getFriendRequests().contains(account.getAccount());

        friend.getFriendManager().decline(ACCOUNT);
        assert !friend.getFriendManager().getFriendList().contains(account.getAccount());
    }

    @Override
    @Test
    public void acceptFriendRequest() throws Exception {
        account.getFriendManager().request(friend.getAccount().getActor());

        assert friend.getFriendManager().getFriendRequests().contains(account.getAccount());
        friend.getFriendManager().accept(ACCOUNT);
        assert !(friend.getFriendManager().getFriendRequests().contains(account.getAccount()));
    }

    @Override
    @Test
    public void verifyRequestConsumed() throws AuthenticationException, FriendException {
        account.getFriendManager().request(FRIEND);
        friend.getFriendManager().request(ACCOUNT);

        assert account.getFriendManager().isFriendsWith(FRIEND);
        assert friend.getFriendManager().isFriendsWith(ACCOUNT);
    }

    @Override
    @Test
    public void terminateFriendship() throws Exception {
        account.getFriendManager().request(FRIEND);
        friend.getFriendManager().accept(ACCOUNT);

        account.getFriendManager().terminate(FRIEND);

        assert !(account.getFriendManager().getFriendList().contains(friend.getAccount()));
        assert !(friend.getFriendManager().getFriendList().contains(account.getAccount()));

        try {
            account.getFriendManager().terminate("NX_USER");
            throw new RuntimeException("FriendMissingException: Not thrown for non-existent user.");
        } catch (FriendException ignored) {
        }
    }

    @Override
    @Test
    public void requestSelfShouldFail() throws Exception {
        try {
            account.getFriendManager().request(ACCOUNT);
            throw new Exception("Operation did not fail: self-friending does not throw.");
        } catch (CannotFriendYourselfException ignored) {
        }
    }

    @Override
    @Test
    public void doubleRequestPrevention() throws AuthenticationException, FriendException {
        account.getFriendManager().request(FRIEND);
        account.getFriendManager().request(FRIEND);
        assert friend.getFriendManager().getFriendRequests().size() == 1;
    }

    @Test
    public void testIsFriends() throws Exception {
        assert !account.getFriendManager().isFriendsWith(FRIEND);
        account.getFriendManager().request(FRIEND);
        friend.getFriendManager().accept(ACCOUNT);
        assert account.getFriendManager().isFriendsWith(FRIEND);
    }

    @Test
    public void testIsFriendRequested() throws Exception {
        FriendManager friends = account.getFriendManager();

        assert !friends.isFriendRequested(FRIEND);
        friends.request(FRIEND);
        assert friends.isFriendRequested(FRIEND);
    }
}
