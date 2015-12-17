package Model; /**
 * Created by Robin on 2015-11-13.
 *
 */

import Core.AccountManagerClient;
import Mock.ManagerProvider;
import com.rduda.API.Account;
import com.rduda.API.AccountManager;
import com.rduda.Model.Exception.AccountAlreadyExistsException;
import com.rduda.Model.Exception.AuthenticationException;
import com.rduda.Model.Exception.NoSuchImageException;
import org.junit.Before;
import org.junit.Test;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Temporally coupled test-methods for an account ensures that the tests
 * are executed in the correct order.
 */
public class AccountManagerTest implements AccountManagerClient {
    private AccountManager handler;


    @Before
    public void setUp() throws AuthenticationException {
        this.handler = ManagerProvider.getAccountManager();
    }

    @Override
    @Test
    public void loginWithoutAccount() throws Exception {
        try {
            handler.login(AccountManagerTest.USER_NX, AccountManagerTest.PASS);
            throw new Exception("Expected AuthenticationException not thrown.");
        } catch (AuthenticationException ignored) {
        }
    }

    @Override
    @Test
    public void registerLogoutAndLogin() throws Exception {
        handler.register(USER, PASS, FIRST, LAST);
        try {
            handler.logout();
            assert !handler.isAuthenticated();
            handler.login(USER, PASS);
            getAccountData();
            assert handler.isAuthenticated();
        } finally {
            handler.unregister();
        }
    }

    private void getAccountData() throws Exception {
        AssertNotEmpty(handler.getAccount().getFirstName());
        AssertNotEmpty(handler.getAccount().getLastName());
        AssertNotEmpty(handler.getAccount().getActor());
    }

    private void AssertNotEmpty(String string) throws Exception {
        if (string == null || string.length() == 0)
            throw new Exception("Assertion Failed: String is empty.");
    }

    @Override
    @Test
    public void loginWithWrongPassword() throws Exception {
        handler.register(USER, PASS, FIRST, LAST);
        try {
            handler.login(USER, PASS_WRONG);
            throw new Exception("Expected AuthenticationException not thrown.");
        } catch (AuthenticationException ignored) {
        } finally {
            handler.login(USER, PASS);
            handler.unregister();
        }
    }

    @Override
    @Test
    public void overwriteExistingUsername() throws Exception {
        handler.register(USER, PASS, FIRST, LAST);
        try {
            handler.register(USER, PASS_WRONG, FIRST, LAST);
            throw new Exception("Expected AccountAlreadyExistsException not thrown.");
        } catch (AccountAlreadyExistsException ignored) {
        } finally {
            handler.login(USER, PASS);
            handler.unregister();
        }
    }

    @Override
    @Test
    public void unregisterAccount() throws Exception {
        handler.register(USER, PASS, FIRST, LAST);
        handler.unregister();
        try {
            handler.login(USER, PASS);
            throw new Exception("Account not unregistered!");
        } catch (AuthenticationException ignored) {
        }
    }

    @Override
    @Test
    public void findByAny() throws AuthenticationException {
        // should not return a handle to self.
        assert handler.findByAny(USER).size() == 0;

        String username = "friend";
        String firstname = "fre";
        String lastname = "nemy";
        AccountManager friend = ManagerProvider.getAccountManager();
        friend.register(username, "", firstname, lastname);

        // should return account for any param.
        assert handler.findByAny(username).size() != 0;
        assert handler.findByAny(firstname).size() != 0;
        assert handler.findByAny(lastname).size() != 0;

        friend.unregister();
    }

    @Override
    public void findByUsername() throws AuthenticationException {
        assert handler.findByUsername(USER) != null;
    }

    @Override
    @Test
    public void updateProfile() throws Exception {
        handler.register("", "", "", "");
        try {
            handler.setProfile("test", "test", "test", 64);
            Account account = handler.getAccount();
            assert account.getFirstName().equals("test");
            assert account.getLastName().equals("test");
            assert account.getCountry().equals("test");
            assert account.getAge() == 64;
        } finally {
            handler.unregister();
        }

        try {
            handler.setProfile("", "", "", -1);
            throw new Exception("Setting profile without authentication does not throw.");
        } catch (AuthenticationException ignored) {
        }
    }

    @Override
    @Test
    public void downloadImage() throws Exception {
        handler.register("", "", "", "");

        try {
            handler.getProfileImage(handler.getAccount().getId());
            // else
            handler.unregister();
            throw new Exception("No exception thrown when image is missing.");
        } catch (NoSuchImageException ignored) {
        }

        try {
            handler.setProfileImage("base64");
            assert handler.getProfileImage(handler.getAccount().getId()).getBase64().equals("base64");
        } finally {
            handler.unregister();
        }
    }

    @Override
    @Test
    public void authenticationRequiredForImages() throws Exception {
        try {
            handler.setProfileImage("null");
            throw new Exception("No exception thrown when authentication is none.");
        } catch (AuthenticationException ignored) {
        }
    }

    @Override
    @Test
    public void tokenBasedAuthentication() throws Exception {
        try {
            handler.register(USER, PASS, FIRST, LAST);

            Account account = handler.getAccount();
            handler.session(account.getActor(), account.getToken());

            try {
                handler.session(account.getActor(), "invalid-session-token");
                throw new Exception("AccountManager did not reject invalid session token.");
            } catch (AuthenticationException ignored) {
            }
        } catch (NotImplementedException ignored) {
        } finally {
            handler.unregister();
        }
    }
}
