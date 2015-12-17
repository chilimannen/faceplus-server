package REST;

import Core.FriendManagerClient;
import com.rduda.API.Request.AccountList;
import com.rduda.API.Request.RequestMapping;
import com.rduda.Model.Exception.AuthenticationException;
import com.rduda.Model.Exception.FriendException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.client.Entity;

/**
 * Created by Robin on 2015-11-28.
 * <p>
 * Tests the REST-API /friend route.
 */
public class FriendRouteTest implements FriendManagerClient {
    private static RequestMapping account = null;
    private static RequestMapping friend = null;

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
    public void sendFriendRequest() throws Exception {
        sendRequest(account, friend);
        assert getRequests(friend).getList().contains(account);
    }


    private static void sendRequest(RequestMapping from, RequestMapping to) {
        RequestMapping request = new RequestMapping(from)
                .setTarget(to.getActor());

        REST.getClient()
                .path(REST.Friend.REQUEST)
                .request(REST.TYPE)
                .post(Entity.entity(request, REST.TYPE), RequestMapping.class);
    }

    private AccountList getRequests(RequestMapping account) {
        return REST.getClient()
                .path(REST.Friend.REQUESTS)
                .queryParam("actor", account.getActor())
                .queryParam("token", account.getToken())
                .request(REST.TYPE)
                .get(AccountList.class);
    }

    @Test
    public void declineFriendRequest() throws Exception {
        sendRequest(account, friend);
        declineRequest(friend, account);

        assert !getRequests(friend).getList().contains(account);
    }

    private void declineRequest(RequestMapping actor, RequestMapping target) {
        RequestMapping termination = new RequestMapping(actor)
                .setTarget(target.getActor());

        REST.getClient()
                .path(REST.Friend.DECLINE)
                .request(REST.TYPE)
                .post(Entity.entity(termination, REST.TYPE), RequestMapping.class);
    }


    @Test
    public void acceptFriendRequest() throws Exception {
        makeFriends(account, friend);

        assert getFriends(account).getList().contains(friend);
        assert getFriends(friend).getList().contains(account);
    }

    public static void makeFriends(RequestMapping account, RequestMapping friend) {
        sendRequest(account, friend);
        acceptRequest(friend, account);

        assert getFriends(account).getList().contains(friend);
        assert getFriends(friend).getList().contains(account);
    }

    private static void acceptRequest(RequestMapping from, RequestMapping to) {
        RequestMapping accept = new RequestMapping(from)
                .setTarget(to.getActor());

        REST.getClient()
                .path(REST.Friend.ACCEPT)
                .request(REST.TYPE)
                .post(Entity.entity(accept, REST.TYPE), RequestMapping.class);
    }

    private static AccountList getFriends(RequestMapping account) {
        return REST.getClient()
                .path(REST.Friend.LIST)
                .queryParam("actor", account.getActor())
                .queryParam("token", account.getToken())
                .request(REST.TYPE)
                .get(AccountList.class);
    }


    @Test
    public void verifyRequestConsumed() throws AuthenticationException, FriendException {
        sendRequest(account, friend);
        sendRequest(friend, account);

        assert getRequests(friend).getList().size() == 0;
        assert getRequests(account).getList().size() == 0;
    }


    @Test
    public void terminateFriendship() throws Exception {
        makeFriends(account, friend);
        RequestMapping terminate = new RequestMapping(account)
                .setTarget(FRIEND);

        REST.getClient()
                .path(REST.Friend.TERMINATE)
                .request(REST.TYPE)
                .post(Entity.entity(terminate, REST.TYPE), RequestMapping.class);
    }

    @Test
    public void requestSelfShouldFail() throws Exception {
        try {
            sendRequest(account, account);
            throw new Exception("Friend request to self does not throw Forbidden.");
        } catch (ForbiddenException ignored) {
        }
    }

    @Test
    public void doubleRequestPrevention() throws AuthenticationException, FriendException {
        sendRequest(account, friend);
        sendRequest(account, friend);

        assert getRequests(friend).getList().size() == 1;
    }

    @Test
    public void testIsFriends() throws Exception {
        assert !isFriends();
        makeFriends(account, friend);
        assert isFriends();
    }

    private Boolean isFriends() {
        return REST.getClient()
                .path(REST.Friend.IS_FRIENDS)
                .queryParam("actor", account.getActor())
                .queryParam("token", account.getToken())
                .queryParam("target", FRIEND)
                .request(REST.TYPE)
                .get(RequestMapping.class).getResult();
    }

    @Test
    public void testIsFriendRequested() throws Exception {
        assert !isFriendRequested();
        sendFriendRequest();
        assert isFriendRequested();

    }

    private Boolean isFriendRequested() {
        return REST.getClient()
                .path(REST.Friend.IS_REQUESTED)
                .queryParam("actor", account.getActor())
                .queryParam("token", account.getToken())
                .queryParam("target", FRIEND)
                .request(REST.TYPE)
                .get(RequestMapping.class).getResult();
    }
}