package REST;

import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 * Created by Robin on 2015-11-28.
 * <p>
 * Contains configuration and documentation for the REST service.
 * <p>
 * All routes except /register & /login requires an active session token
 * and the username of the owning account.
 */
abstract class REST {
    public static final String URL = "http://localhost:5001/api";
    public static final MediaType TYPE = MediaType.APPLICATION_JSON_TYPE;

    public static WebTarget getClient() {
        return ClientBuilder.newBuilder().register(JacksonFeature.class).build().target(REST.URL);
    }

    /**
     * Specifies in/out params for the Account routes.
     * <p>
     * throws NotAuthenticatedException(401) for authenticity based exceptions.
     */
    abstract class Account {

        /**
         * INPUT
         * - username
         * - password
         * <p>
         * OUPUT
         * - Account
         */
        public final static String LOGIN = "account/login";

        /**
         * INPUT
         * - username
         * - password
         * - firstName
         * - lastName
         * <p>
         * OUTPUT
         * - Account
         */
        public final static String REGISTER = "account/register";

        /**
         * Destroys the active session token.
         */
        public final static String LOGOUT = "account/logout";

        /**
         * De-registers the account owning the token.
         */
        public final static String UNREGISTER = "account/unregister";

        /**
         * OUTPUT
         * - List<Account> [.image, .name, username, age, country]
         */
        public final static String SEARCH = "account/search";

        /**
         * INPUT
         * firstName, lastName, age (int), country
         * <p>
         * OUTPUT
         * - Account
         */
        public final static String UPDATE = "account/update";

        /**
         * OUTPUT
         * - AccountJSON [.*]
         */
        public final static String LOAD = "account/load";

        /**
         * INPUT
         * - profileImage (Long) [GET]
         * - profileImageData (base64) [POST] - updates the profile image with a png/jpg image.
         * <p>
         * OUTPUT
         * - profileImage (Long) [GET]
         * - profileImageData - image as base64. [GET]
         */
        public final static String IMAGE = "account/image";
    }

    abstract class Friend {

        /**
         * INPUT
         * param - username of the account to send friend request to.
         */
        public final static String REQUEST = "friend/request";

        /**
         * INPUT
         * param - username of the account to accept a request from.
         */
        public final static String ACCEPT = "friend/accept";

        /**
         * INPUT
         * param - username of the account to remove friendship with.
         */
        public final static String TERMINATE = "friend/terminate";

        /**
         * OUTPUT
         * list of accounts that the token owner is friends with.
         */
        public final static String LIST = "friend/list";

        /**
         * OUTPUT
         * list of friend requests pending that the token owner has.
         */
        public final static String REQUESTS = "friend/requests";

        /**
         * INPUT
         * param - name of the username to decline invitation from.
         */
        public final static String DECLINE = "friend/decline";


        /**
         * INPUT
         * target - username of the friend to test if friends with.
         * <p>
         * OUTPUT
         * result - boolean
         */
        public final static String IS_FRIENDS = "friend/is";

        /**
         * INPUT
         * target - username of the friend to test if request sent.
         * <p>
         * OUTPUT
         * result - boolean
         */
        public final static String IS_REQUESTED = "friend/requested";
    }

    abstract class Post {

        /**
         * INPUT
         * content [POST]
         * <p>
         * OUTPUT
         * list of posts on own wall. [GET]
         */
        public final static String POST_HOME = "post/home";

        /**
         * INPUT
         * content [POST]
         * username [POST, GET]
         * <p>
         * OUTPUT
         * List of posts of specified friend. [GET]
         */
        public final static String POST_FRIEND = "post/friend";

        /**
         * OUTPUT
         * list of posts made by friends on their own wall.
         */
        public final static String STREAM_POSTS = "post/stream";

        /**
         * INPUT
         * id [POST]
         * specifies post to be removed, must be created by session owner
         * or on the session owners wall.
         */
        public final static String REMOVE = "post/remove";
    }

    abstract class Message {
        /**
         * INPUT [GET]
         * param - sender username.
         * <p>
         * OUTPUT
         * list of message history between sender and param.
         */
        public final static String GET = "message/retrieve";

        /**
         * INPUT [POST]
         * param - receiver username.
         * content - text content.
         */
        public final static String SEND = "message/send";

        /**
         * INPUT [GET]
         * param - friend name.
         * content - text content.
         */
        public final static String THREADS = "message/threads";
    }
}
