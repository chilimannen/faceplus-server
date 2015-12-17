package com.rduda.Controller;

import com.rduda.API.AccountManager;
import com.rduda.API.Request.MessageList;
import com.rduda.API.Request.MessageMapping;
import com.rduda.Model.Exception.AuthenticationException;
import com.rduda.Model.Exception.NoSuchFriendException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Robin on 2015-11-29.
 * <p>
 * REST routing for message operations, the request
 * operations are mapped to a MessageHandler.
 * <p>
 * All routes requires that an "actor" (username) and "token" is specified.
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

@Path("/message")
public class MessageRoute {

    /**
     * Retrieves the latest messages from a specified Friend.
     *
     * @param message contains the 'target' as the username of the account
     *                to retrieve messages from.
     * @return MessageList of MessageMappings, when the 'target' is
     * not a friend returns Missing.
     */
    @GET
    @Path("/retrieve")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMessages(@BeanParam MessageMapping message) {
        try {
            AccountManager manager = AccountRoute.session(message);
            MessageList messages = MessageList.fromList(manager.getMessageManager().getMessagesFrom(message.getTarget(), Long.MAX_VALUE));

            return Response.ok(messages).build();
        } catch (AuthenticationException e) {
            return Response.status(401).build();
        } catch (NoSuchFriendException e) {
            return Response.status(404).build();
        }
    }

    /**
     * Sends a new message to a specified receiver.
     *
     * @param message Contains
     *                'target' - username of the Receiver Account.
     *                'content' - text contents of the message.
     * @return OK on success, Missing when the receiver is not a friend.
     */
    @POST
    @Path("/send")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendMessage(MessageMapping message) {
        try {
            AccountManager manager = AccountRoute.session(message);
            manager.getMessageManager().sendMessageTo(message.getTarget(), message.getContent());

            return Response.ok().build();
        } catch (NoSuchFriendException e) {
            return Response.status(404).build();
        } catch (AuthenticationException e) {
            return Response.status(401).build();
        }
    }

    /**
     * Lists recent conversations.
     *
     * @return the most recent 'threads' (conversations). The latest message in
     * each thread will be included with the sender account. A thread
     * exists only when messages have been received from the target.
     * Type is MessageList of MessageMapping.
     */
    @GET
    @Path("/threads")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getThreads(@BeanParam MessageMapping message) {
        try {
            AccountManager manager = AccountRoute.session(message);
            MessageList messages = MessageList.fromList(manager.getMessageManager().getLatestMessages());

            return Response.ok(messages).build();
        } catch (AuthenticationException e) {
            return Response.status(401).build();
        }
    }
}
