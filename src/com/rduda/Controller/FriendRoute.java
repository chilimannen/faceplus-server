package com.rduda.Controller;

import com.rduda.API.Request.RequestMapping;
import com.rduda.API.Request.AccountList;
import com.rduda.API.AccountManager;
import com.rduda.Model.Exception.AuthenticationException;
import com.rduda.Model.Exception.CannotFriendYourselfException;
import com.rduda.Model.Exception.FriendException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Robin on 2015-11-29.
 * <p>
 * REST routing for friend operations, the request
 * operations are mapped to a FriendHandler.
 * <p>
 * All routes requires that an "actor" (username) and "token" is specified.
 * <p>
 * The actor is the account requesting the operation
 * and is bound to the token. Whenever a token/actor
 * mismatch occur a NotAuthorized message will be sent.
 * <p>
 * The implementation uses request-based authentication to allow
 * for third-party clients to connect to the API. This infers that
 * every request made must contain authentication details is specified.
 * If host-based access is desired access-lists must be configured.
 */

@Path("/friend")
public class FriendRoute {

    /**
     * Lists all friends of the Account.
     *
     * @return AccountList containing all the friends as
     * Accounts.
     */
    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response friendList(@BeanParam RequestMapping request) {
        try {
            AccountManager manager = AccountRoute.session(request);

            return Response.ok(new AccountList(manager.getFriendManager().getFriendList())).build();
        } catch (AuthenticationException e) {
            return Response.status(401).build();
        } catch (FriendException e) {
            return Response.status(404).build();
        }
    }

    /**
     * Lists all friend-requests received.
     *
     * @return AccountList containing all the friends that
     * have sent a friend-request to the current account.
     */
    @GET
    @Path("/requests")
    @Produces(MediaType.APPLICATION_JSON)
    public Response requests(@BeanParam RequestMapping request) {
        try {
            AccountManager manager = AccountRoute.session(request);

            return Response.ok(new AccountList(manager.getFriendManager().getFriendRequests())).build();
        } catch (AuthenticationException e) {
            return Response.status(401).build();
        } catch (FriendException e) {
            return Response.status(404).build();
        }
    }

    /**
     * Sends a friend request.
     *
     * @param request contains the 'target' Account username.
     * @return OK on success, Missing when target does not exist.
     */
    @POST
    @Path("/request")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response request(RequestMapping request) {
        try {
            AccountManager manager = AccountRoute.session(request);
            manager.getFriendManager().request(request.getTarget());

            return Response.ok().build();
        } catch (AuthenticationException e) {
            return Response.status(401).build();
        } catch (CannotFriendYourselfException e) {
            return Response.status(403).build();
        } catch (FriendException e) {
            return Response.status(404).build();
        }
    }

    /**
     * Accept an existing friend request
     *
     * @param request 'target' as the username of the Account to
     *                accept an existing request from.
     * @return OK on success, Missing on invalid 'target'.
     */
    @POST
    @Path("/accept")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response accept(RequestMapping request) {
        try {
            AccountManager manager = AccountRoute.session(request);
            manager.getFriendManager().accept(request.getTarget());

            return Response.ok().build();
        } catch (AuthenticationException e) {
            return Response.status(401).build();
        } catch (FriendException e) {
            return Response.status(404).build();
        }
    }

    /**
     * Removes a friend from the friend list.
     *
     * @param request 'target' as the username of the friend to unfriend.
     * @return OK on success, Missing when 'target' is not a friend.
     */
    @POST
    @Path("/terminate")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response terminate(RequestMapping request) {
        try {
            AccountManager manager = AccountRoute.session(request);
            manager.getFriendManager().terminate(request.getTarget());

            return Response.ok().build();
        } catch (AuthenticationException e) {
            return Response.status(401).build();
        } catch (FriendException e) {
            return Response.status(404).build();
        }
    }

    /**
     * Declines a friend request.
     *
     * @param request containing the 'target' username of the account
     *                to reject an invitation from.
     * @return OK on success, Missing when there is no invitation from
     * 'target'.
     */
    @POST
    @Path("/decline")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response decline(RequestMapping request) {
        try {
            AccountManager manager = AccountRoute.session(request);
            manager.getFriendManager().decline(request.getTarget());

            return Response.ok().build();
        } catch (AuthenticationException e) {
            return Response.status(401).build();
        } catch (FriendException e) {
            return Response.status(404).build();
        }
    }

    /**
     * Check if another account is a friend or not.
     *
     * @param request contains the 'target' Accounts username.
     * @return result as True approves that the Account is
     * listed as friend, False declines.
     */
    @GET
    @Path("/is")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response isFriends(@BeanParam RequestMapping request) {
        try {
            AccountManager manager = AccountRoute.session(request);
            Boolean result = manager.getFriendManager().isFriendsWith(request.getTarget());

            return Response.ok(new RequestMapping().setResult(result)).build();
        } catch (FriendException e) {
            return Response.ok(new RequestMapping().setResult(false)).build();
        } catch (AuthenticationException e) {
            return Response.status(401).build();
        }
    }

    /**
     * Checks if a friend request has been sent.
     *
     * @param request 'target' username of the Account that was requested.
     * @return result as True approves that the Account is
     * listed as requested, False declines.
     */
    @GET
    @Path("/requested")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response isRequested(@BeanParam RequestMapping request) {
        try {
            AccountManager manager = AccountRoute.session(request);
            Boolean result = manager.getFriendManager().isFriendRequested(request.getTarget());

            return Response.ok(new RequestMapping().setResult(result)).build();
        } catch (FriendException e) {
            return Response.ok(new RequestMapping().setResult(false)).build();
        } catch (AuthenticationException e) {
            return Response.status(401).build();
        }
    }
}
