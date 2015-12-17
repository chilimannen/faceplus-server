package REST;

import Core.MessageManagerClient;
import com.rduda.API.Request.MessageList;
import com.rduda.API.Request.MessageMapping;
import com.rduda.API.Request.RequestMapping;
import com.rduda.Model.Exception.AuthenticationException;
import com.rduda.Model.Exception.FriendException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Entity;

/**
 * Created by Robin on 2015-11-28.
 * <p>
 * Tests the /message service in REST api.
 */


public class MessageRouteTest implements MessageManagerClient {
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
    public void sendToStranger() throws Exception {
        MessageMapping message = new MessageMapping(account)
                .setContent("message :>")
                .setTarget("user-nx");

        try {
            message(message);
            throw new Exception("Does not throw 404 when friend is missing.");
        } catch (NotFoundException ignored) {
        }
    }

    private void message(MessageMapping message) {
         REST.getClient()
                .path(REST.Message.SEND)
                .request(REST.TYPE)
                .post(Entity.entity(message, REST.TYPE), MessageMapping.class);
    }

    @Test
    public void sendMessage() throws AuthenticationException, FriendException {
        FriendRouteTest.makeFriends(account, friend);
        MessageMapping message = new MessageMapping(account)
                .setContent("message :>")
                .setTarget(FRIEND);

        message(message);
    }

    @Test
    public void receiveMessage() throws AuthenticationException, FriendException {
        FriendRouteTest.makeFriends(account, friend);

        MessageMapping toFriend = new MessageMapping(account)
                .setContent("message :>")
                .setTarget(FRIEND);
        message(toFriend);

        MessageMapping fromFriend = new MessageMapping(friend)
                .setContent("message :<")
                .setTarget(USER);

        message(fromFriend);

        assert getMessages(account, FRIEND).getList().size() == 2;
        assert getMessages(friend, USER).getList().size() == 2;
    }

    public MessageList getMessages(RequestMapping account, String from) throws FriendException, AuthenticationException {
        return REST.getClient()
                .path(REST.Message.GET)
                .queryParam("actor", account.getActor())
                .queryParam("token", account.getToken())
                .queryParam("target", from)
                .request(REST.TYPE)
                .get(MessageList.class);
    }

    @Test
    public void getLatestMessages() throws FriendException, AuthenticationException {
        FriendRouteTest.makeFriends(account, friend);

        assert getThreads().getList().size() == 0;
        receiveMessage();
        assert getThreads().getList().size() == 1;
    }

    private MessageList getThreads() {
        return REST.getClient()
                .path(REST.Message.THREADS)
                .queryParam("actor", account.getActor())
                .queryParam("token", account.getToken())
                .queryParam("target", FRIEND)
                .request(REST.TYPE)
                .get(MessageList.class);
    }
}
