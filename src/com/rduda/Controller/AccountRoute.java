package com.rduda.Controller;

import com.rduda.API.Request.MessageMapping;
import com.rduda.API.Request.PostMapping;
import com.rduda.API.Request.RequestMapping;
import com.rduda.API.Request.AccountList;
import com.rduda.API.AccountManager;
import com.rduda.API.Image;
import com.rduda.Model.AccountHandler;
import com.rduda.Model.Exception.AuthenticationException;
import com.rduda.Model.Exception.NoSuchImageException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 * Created by Robin on 2015-11-28.
 * <p>
 * REST routing for account operations, the request
 * operations are mapped to a AccountHandler.
 * <p>
 * All routes except those used for authentication
 * requires that an "actor" (username) and "token" is specified.
 * <p>
 * The actor is the account requesting the operation
 * and is bound to the token. Whenever a token/actor
 * mismatch occur a NotAuthorized message will be issued.
 * <p>
 * The implementation uses request-based authentication to allow
 * for third-party clients to connect to the API. This infers that
 * every request made must contain authentication details is specified.
 * If host-based access is desired access-lists must be configured.
 */

@Path("/account")
public class AccountRoute {

    /**
     * Login attempt for specified actor and password.
     *
     * @param request contains actor and password.
     * @return The authenticated account or in case
     * of failure a 401 error.
     */
    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(RequestMapping request) {
        AccountManager manager = new AccountHandler();
        try {
            if (request.getToken() == null)
                manager.login(request.getActor(), request.getPassword());
            else
                manager = session(request);

            return Response.ok().entity(new RequestMapping(manager.getAccount())).build();
        } catch (AuthenticationException e) {
            return Response.status(401).build();
        }
    }

    /**
     * Account registration request.
     *
     * @param request contains username, password, firstName and lastName.
     * @return The registered account or in case of failure
     * a 401 error.
     */
    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(RequestMapping request) {
        AccountManager manager = new AccountHandler();
        try {
            manager.register(
                    request.getActor(),
                    request.getPassword(),
                    request.getFirstName(),
                    request.getLastName());

            return Response.ok().entity(
                    new RequestMapping(manager.getAccount())).build();
        } catch (AuthenticationException e) {
            return Response.status(401).build();
        }
    }

    /**
     * Un-registers the authenticated account.
     *
     * @return OK or NotAuthorized.
     */
    @DELETE
    @Path("/unregister")
    @Produces(MediaType.APPLICATION_JSON)
    public Response unregister(@BeanParam RequestMapping request) {
        try {
            AccountManager manager = session(request);
            manager.unregister();

            return Response.ok().build();
        } catch (AuthenticationException e) {
            return Response.status(401).build();
        }
    }

    /**
     * "Logs out" the current user,
     * validates the authenticity but does not actually
     * destroy the session as the user may be logged in
     * with multiple devices.
     *
     * @param request containing [token+username] for
     *                session authentication.
     * @return OK or NotAuthorized.
     */
    @POST
    @Path("/logout")
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout(RequestMapping request) {
        try {
            session(request);
            return Response.ok(new RequestMapping()).build();
        } catch (AuthenticationException e) {
            return Response.status(401).build();
        }
    }

    /**
     * Returns a list of Accounts in the AccountStore
     * where the given query matches the a
     *
     * @param request Containing the query string and authentication.
     * @return a list of Accounts in the AccountStore
     * where the given query matches specified parts
     * of the Account details. @see AccountStore implementation.
     */
    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response search(@BeanParam RequestMapping request) {
        try {
            AccountManager manager = session(request);
            AccountList result = new AccountList(manager.findByAny(request.getSearch()));

            return Response.ok(result).build();
        } catch (AuthenticationException e) {
            return Response.status(401).build();
        }
    }

    /**
     * Updates the account details.
     *
     * @param request contains details to be updated.
     * @return The updated Account.
     */
    @POST
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(RequestMapping request) {
        try {
            AccountManager manager = session(request);
            manager.setProfile(
                    request.getFirstName(),
                    request.getLastName(),
                    request.getCountry(),
                    request.getAge()
            );

            return Response.ok(new RequestMapping(manager.getAccount())).build();
        } catch (AuthenticationException e) {
            return Response.status(401).build();
        }
    }

    /**
     * Method used to reload Account details or to check if a token
     * is valid.
     *
     * @param request contains token/actor pair.
     * @return The Account matching the token/actor pair.
     */
    @GET
    @Path("/load")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response load(@BeanParam RequestMapping request) {
        try {
            AccountManager manager = session(request);
            return Response.ok(new RequestMapping(manager.findByUsername(request.getTarget()))).build();
        } catch (AuthenticationException e) {
            return Response.status(401).build();
        }
    }

    /**
     * Upload an image to the server and set it as the profile-image.
     *
     * @param request contains the image data expressed as base64.
     * @return OK or NotAuthorized.
     */
    @PUT
    @Path("/image")
    @Produces(MediaType.APPLICATION_JSON)
    public Response upload(RequestMapping request) {
        try {
            AccountManager manager = session(request);
            manager.setProfileImage(request.getProfileImageData());
            return Response.ok().build();
        } catch (AuthenticationException e) {
            return Response.status(401).build();
        }
    }

    /**
     * Return the image associated with an AccountId.
     *
     * @param request specifies the account as id.
     * @return Image data and Image ID or NotAuthorized.
     */
    @GET
    @Path("/image")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getImage(@BeanParam RequestMapping request) {
        try {
            AccountManager manager = session(request);
            Image image = manager.getProfileImage(request.getId());

            RequestMapping result = new RequestMapping()
                    .setProfileImage(image.getId())
                    .setProfileImageData(image.getBase64());

            return Response.ok(result).build();
        } catch (AuthenticationException e) {
            return Response.status(401).build();
        } catch (NoSuchImageException e) {
            return Response.status(404).build();
        }
    }

    /**
     * Creates a RequestMapping from a Post and uses it to initialize
     * the AccountManager.
     *
     * @param post The post containing authentication details.
     * @return An AccountManager in the context of the Post authentication.
     * @throws AuthenticationException when the Post authentication details
     *                                 is invalid.
     */
    public static AccountManager session(PostMapping post) throws AuthenticationException {
        return session(new RequestMapping()
                .setActor(post.getActor())
                .setToken(post.getToken()));
    }


    /**
     * Creates a RequestMapping from a Message and uses it to initialize
     * the AccountManager.
     *
     * @param message The message containing authentication details.
     * @return An AccountManager in the context of the message authentication.
     * @throws AuthenticationException when the message authentication details
     *                                 is invalid.
     */
    public static AccountManager session(MessageMapping message) throws AuthenticationException {
        return session(new RequestMapping()
                .setActor(message.getActor())
                .setToken(message.getToken()));
    }

    /**
     * Creates a RequestMapping from a Message and uses it to initialize
     * the AccountManager.
     *
     * @param account The account containing authentication details.
     * @return An AccountManager in the context of the account authentication.
     * @throws AuthenticationException when the account authentication details
     *                                 is invalid.
     */
    public static AccountManager session(RequestMapping account) throws AuthenticationException {
        AccountManager handler = new AccountHandler();
        handler.session(account.getActor(), account.getToken());
        return handler;
    }

}
