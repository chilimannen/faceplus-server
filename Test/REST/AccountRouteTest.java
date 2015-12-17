package REST;

import Core.AccountManagerClient;
import com.rduda.API.Request.RequestMapping;
import com.rduda.API.Request.AccountList;
import com.rduda.Model.Exception.AuthenticationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.client.Entity;

/**
 * Created by Robin on 2015-11-28.
 * <p>
 * Tests the REST API /account route.
 */
public class AccountRouteTest implements AccountManagerClient {
    private RequestMapping account = null;

    @Before
    public void setUp() {
        account = register(USER);
        assert account.getAuthenticated();
    }

    public static RequestMapping register(String username) {
        RequestMapping request = new RequestMapping()
                .setActor(username)
                .setPassword(PASS)
                .setFirstName(FIRST)
                .setLastName(LAST);

        return REST.getClient()
                .path(REST.Account.REGISTER)
                .request(REST.TYPE)
                .post(Entity.entity(request, REST.TYPE), RequestMapping.class);
    }

    @After
    public void tearDown() {
        try {
            unregisterAccount();
        } catch (Exception ignored) {
        }
    }


    @Test
    public void unregisterAccount() throws Exception {
        unregister(account);
    }

    public static void unregister(RequestMapping account) throws Exception {
        RequestMapping request = new RequestMapping(account);

        account = REST.getClient()
                .path(REST.Account.UNREGISTER)
                .queryParam("actor", account.getActor())
                .queryParam("token", account.getToken())
                .request(REST.TYPE)
                .delete(RequestMapping.class);

        try {
            // verify that de-registering the account makes it unavailable.
            account = REST.getClient()
                    .path(REST.Account.LOGIN)
                    .request(REST.TYPE)
                    .post(Entity.entity(request.setToken(request.getToken()), REST.TYPE), RequestMapping.class);

            throw new Exception("Attempt to login on unregistered account did not fail.");
        } catch (NotAuthorizedException ignored) {
        }
    }

    @Test
    public void loginWithoutAccount() throws Exception {
        try {
            RequestMapping request = new RequestMapping()
                    .setActor(USER_NX)
                    .setPassword("null&void");

            REST.getClient()
                    .path(REST.Account.LOGIN)
                    .request(REST.TYPE)
                    .post(Entity.entity(request, REST.TYPE), RequestMapping.class);

            throw new Exception("Did not throw exception on failed login.");
        } catch (NotAuthorizedException ignored) {
        }
    }

    @Test
    public void registerLogoutAndLogin() throws Exception {
        account = login();

        assert account.getActor().equals(USER);
        assert account.getToken() != null;
        assert account.getFirstName().equals(FIRST);
        assert account.getLastName().equals(LAST);

        account = REST.getClient()
                .path(REST.Account.LOGOUT)
                .request(REST.TYPE)
                .post(Entity.entity(account.setToken(account.getToken()), REST.TYPE), RequestMapping.class);

        // must be authenticated to unregister (cleanup)
        account = login();
    }

    private RequestMapping login() {
        RequestMapping request = new RequestMapping()
                .setActor(USER)
                .setPassword(PASS)
                .setFirstName(FIRST)
                .setLastName(LAST);

        return REST.getClient()
                .path(REST.Account.LOGIN)
                .request(REST.TYPE)
                .post(Entity.entity(request, REST.TYPE), RequestMapping.class);
    }

    @Test
    public void loginWithWrongPassword() throws Exception {
        RequestMapping request = new RequestMapping()
                .setActor(USER)
                .setPassword(PASS_WRONG);

        try {
            account = REST.getClient()
                    .path(REST.Account.LOGIN)
                    .request(REST.TYPE)
                    .post(Entity.entity(request, REST.TYPE), RequestMapping.class);

            throw new Exception("Response was not status 401.");
        } catch (NotAuthorizedException ignored) {
        }
    }

    @Test
    public void overwriteExistingUsername() throws Exception {
        RequestMapping request = new RequestMapping()
                .setActor(USER)
                .setPassword(PASS)
                .setFirstName(FIRST)
                .setLastName(LAST);
        try {
            account = REST.getClient()
                    .path(REST.Account.REGISTER)
                    .request(REST.TYPE)
                    .post(Entity.entity(request, REST.TYPE), RequestMapping.class);

            throw new Exception("Attempt to overwrite username did not throw Unauthorized.");
        } catch (NotAuthorizedException ignored) {
        }
    }

    @Test
    public void findByAny() throws AuthenticationException {
        RequestMapping request = new RequestMapping()
                .setActor(USER_NX)
                .setPassword(PASS)
                .setFirstName("friend")
                .setLastName("dneirf");

        // register a new friend as searching does not return actor.
        RequestMapping friend =
                REST.getClient()
                        .path(REST.Account.REGISTER)
                        .request(REST.TYPE)
                        .post(Entity.entity(request, REST.TYPE),
                                RequestMapping.class);

        // perform the search query.
        AccountList result =
                REST.getClient()
                        .path(REST.Account.SEARCH)
                        .queryParam("actor", account.getActor())
                        .queryParam("token", account.getToken())
                        .queryParam("search", "friend")
                        .request(REST.TYPE)
                        .get(AccountList.class);

        // remove the extraneous Account.
        REST.getClient()
                .path(REST.Account.UNREGISTER)
                .queryParam("actor", friend.getActor())
                .queryParam("token", friend.getToken())
                .request(REST.TYPE)
                .delete(RequestMapping.class);

        assert result.getList().size() != 0;
    }

    @Test
    public void findByUsername() throws Exception {
        RequestMapping request = new RequestMapping(account)
                .setTarget(USER);

        RequestMapping result = REST.getClient()
                .path(REST.Account.LOAD)
                .queryParam("actor", request.getActor())
                .queryParam("token", request.getToken())
                .queryParam("target", request.getTarget())
                .request(REST.TYPE)
                .get(RequestMapping.class);

        // assert authentication required.
        try {
            REST.getClient()
                    .path(REST.Account.LOAD)
                    .queryParam("target", request.getTarget())
                    .request(REST.TYPE)
                    .get(RequestMapping.class);
            throw new Exception("account/load does not require token.");
        } catch (NotAuthorizedException ignored) {
        }
    }

    @Test
    public void updateProfile() throws Exception {
        RequestMapping request = new RequestMapping(account)
                .setFirstName("first")
                .setLastName("second")
                .setCountry("country")
                .setAge(52);

        account = REST.getClient()
                .path(REST.Account.UPDATE)
                .request(REST.TYPE)
                .post(Entity.entity(request, REST.TYPE), RequestMapping.class);

        assert account.getFirstName().equals("first");
        assert account.getLastName().equals("second");
        assert account.getCountry().equals("country");
        assert account.getAge().equals(52);
    }

    @Test
    public void downloadImage() throws Exception {
        RequestMapping request = new RequestMapping(account)
                .setProfileImageData("image-data.png/jpg");

        REST.getClient()
                .path(REST.Account.IMAGE)
                .request(REST.TYPE)
                .put(Entity.entity(request, REST.TYPE));

        RequestMapping image = REST.getClient()
                .path(REST.Account.IMAGE)
                .queryParam("actor", account.getActor())
                .queryParam("token", account.getToken())
                .queryParam("id", account.getId())
                .request(REST.TYPE)
                .get(RequestMapping.class);

        assert image.getProfileImageData().equals("image-data.png/jpg");
    }

    @Test
    public void authenticationRequiredForImages() throws Exception {
        try {
            account = REST.getClient()
                    .path(REST.Account.IMAGE)
                    .queryParam("actor", account.getActor())
                    .queryParam("token", "void")
                    .queryParam("id", account.getId())
                    .request(REST.TYPE)
                    .get(RequestMapping.class);

            throw new Exception("Image access not restricted.");
        } catch (NotAuthorizedException ignored) {
        }

    }

    @Test
    public void tokenBasedAuthentication() throws Exception {
        account = REST.getClient()
                .path(REST.Account.LOAD)
                .queryParam("actor", account.getActor())
                .queryParam("token", account.getToken())
                .request(REST.TYPE)
                .get(RequestMapping.class);

        assert account.getAuthenticated();
        assert account.getToken() != null;
    }
}
