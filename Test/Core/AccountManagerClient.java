package Core;

import com.rduda.Model.Exception.AuthenticationException;
import org.junit.Test;

/**
 * Created by Robin on 2015-11-28.
 * <p>
 * Defines the operations/tests for an AccountManager.
 */
public interface AccountManagerClient {
    // account setup for the tests in-scope.
    String USER = "313";
    String USER_NX = "???";
    String PASS = "2";
    String FIRST = "first";
    String LAST = "last";
    String PASS_WRONG = "???";

    /**
     * Tests that login attempts where the target account does
     * not exist should fail.
     */
    @Test
    void loginWithoutAccount() throws Exception;

    /**
     * Tests the ability to register an account, logout from it
     * and then log back in.
     */
    @Test
    void registerLogoutAndLogin() throws Exception;

    /**
     * Verifies that attempts to login where an username exists
     * but the password is wrong should fail.
     */
    @Test
    void loginWithWrongPassword() throws Exception;

    /**
     * Verifies that it is not possible to register an account
     * with a taken username.
     */
    @Test
    void overwriteExistingUsername() throws Exception;

    /**
     * Tests that it is possible to unregister an account.
     * Crucial for test cleanup!
     *
     * @throws Exception
     */
    @Test
    void unregisterAccount() throws Exception;

    /**
     * Tests the ability to find Accounts by their properties and
     * that authentication is required. Also verifies that the
     * searching account is not returned.
     */
    @Test
    void findByAny() throws AuthenticationException;

    /**
     * Verifies that an Account may be found by its username and that
     * authentication is required.
     */
    @Test
    void findByUsername() throws Exception;

    /**
     * Verifies that profile information may be updated and that
     * authentication is required.
     */
    @Test
    void updateProfile() throws Exception;

    /**
     * Tests that it is possible to retrieve the profile image of an account.
     */
    @Test
    void downloadImage() throws Exception;

    /**
     * Verifies that authentication is required to retrieve images.
     */
    @Test
    void authenticationRequiredForImages() throws Exception;

    /**
     * Verifies that it is possible to authenticate using a token
     * acquired in the login process.
     */
    @Test
    void tokenBasedAuthentication() throws Exception;
}
