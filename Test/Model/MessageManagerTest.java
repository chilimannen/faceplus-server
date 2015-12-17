package Model;

import Core.MessageManagerClient;
import Mock.ManagerProvider;
import com.rduda.API.AccountManager;
import com.rduda.Model.Exception.AuthenticationException;
import com.rduda.Model.Exception.FriendException;
import com.rduda.Model.Exception.NoSuchAccountException;
import com.rduda.Model.Exception.NoSuchFriendException;
import com.rduda.API.Message;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Robin on 2015-11-20.
 * <p>
 * Tests a MessageManager.
 */
public class MessageManagerTest implements MessageManagerClient {
    private AccountManager account;
    private AccountManager friend;
    private static final String ACCOUNT = "MMT";
    private static final String FRIEND = "USR_FR";
    private static final String PASS = "pass";
    private static final String HELLO_MSG = "hai friend";
    private static final String NEXT_MSG = "lets hang";

    @Before
    public void setUp() throws AuthenticationException {
        account = ManagerProvider.getAccountManager();
        account.register(ACCOUNT, PASS, "", "");

        friend = ManagerProvider.getAccountManager();
        friend.register(FRIEND, PASS, "", "");
    }

    @After
    public void tearDown() throws NoSuchAccountException {
        account.unregister();
        friend.unregister();
    }

    @Override
    @Test
    public void sendToStranger() throws Exception {
        try {
            account.getMessageManager().sendMessageTo("friend", "http://www.spam.com/click-me");
            throw new Exception("MessageManager did not throw when sending to stranger.");
        } catch (NoSuchFriendException ignored) {
        }
    }

    private void makeFriends() throws AuthenticationException, FriendException {
        account.getFriendManager().request(FRIEND);
        friend.getFriendManager().accept(ACCOUNT);

        assert account.getFriendManager().getFriendList().contains(friend.getAccount());
        assert friend.getFriendManager().getFriendList().contains(account.getAccount());
    }

    @Override
    @Test
    public void sendMessage() throws AuthenticationException, FriendException {
        makeFriends();
        account.getMessageManager().sendMessageTo(FRIEND, HELLO_MSG);
        account.getMessageManager().sendMessageTo(FRIEND, NEXT_MSG);
    }

    @Override
    @Test
    public void receiveMessage() throws AuthenticationException, FriendException {
        sendMessage();

        // verify that the messages has been received on the remote end.
        Message message = friend.getMessageManager().getMessagesFrom(
                ACCOUNT,
                Long.MAX_VALUE).get(0);
        assert message.getContent().equals(NEXT_MSG);

        Message nextMessage = friend.getMessageManager().getMessagesFrom(
                ACCOUNT,
                message.getId()).get(0);
        assert nextMessage.getContent().equals(HELLO_MSG);

        // verify that the messages are in the senders message history.
        Message history = account.getMessageManager().getMessagesFrom(
                FRIEND,
                message.getId()).get(0);
        assert history.getContent().equals(HELLO_MSG);

        // verify that the end of history has been reached and no more messages are returned.
        assert account.getMessageManager().getMessagesFrom(
                FRIEND,
                history.getId()).size() == 0;

        assert friend.getMessageManager().getMessagesFrom(
                ACCOUNT,
                history.getId()).size() == 0;
    }

    @Override
    @Test
    public void getLatestMessages() throws FriendException, AuthenticationException {
        makeFriends();

        friend.getMessageManager().sendMessageTo(ACCOUNT, "hello, message.");
        friend.getMessageManager().sendMessageTo(ACCOUNT, "latest");
        //assert account.getMessageManager().getLatestMessages().size() == 1;
        assert account.getMessageManager().getLatestMessages().get(0).getContent().equals("latest");
    }
}
