package Core;

import com.rduda.Model.Exception.AuthenticationException;
import com.rduda.Model.Exception.FriendException;
import org.junit.Test;

/**
 * Created by Robin on 2015-11-28.
 * <p>
 * Tests a MessageManager.
 */
public interface MessageManagerClient {
    public static final String USER = "msg";
    public static final String FRIEND = "msf";

    /**
     * Verifies that it is not possible to send a message to an Account
     * not the Friendlist.
     */
    @Test
    void sendToStranger() throws Exception;

    /**
     * Verifies that it is possible to send messages.
     */
    @Test
    void sendMessage() throws AuthenticationException, FriendException;

    /**
     * Tests that it is possible to receive messages.
     */
    @Test
    void receiveMessage() throws AuthenticationException, FriendException;

    /**
     * Tests that the latest threads/messages may be retrieved.
     */
    @Test
    void getLatestMessages() throws FriendException, AuthenticationException;
}
