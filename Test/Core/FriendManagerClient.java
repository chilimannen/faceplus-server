package Core;

import com.rduda.Model.Exception.AuthenticationException;
import com.rduda.Model.Exception.FriendException;
import org.junit.Test;

/**
 * Created by Robin on 2015-11-28.
 */
public interface FriendManagerClient {
    String USER = "212";
    String FRIEND = "frnd";
    String PASS = "2";
    String FIRST = "first";
    String LAST = "last";

    @Test
    void sendFriendRequest() throws Exception;

    @Test
    void declineFriendRequest() throws Exception;

    @Test
    void acceptFriendRequest() throws Exception;

    @Test
    void verifyRequestConsumed() throws AuthenticationException, FriendException;


    @Test
    void terminateFriendship() throws Exception;

    @Test
    void requestSelfShouldFail() throws Exception;

    @Test
    void doubleRequestPrevention() throws AuthenticationException, FriendException;

    @Test
    void testIsFriends() throws Exception;

    @Test
    void testIsFriendRequested() throws Exception;
}
