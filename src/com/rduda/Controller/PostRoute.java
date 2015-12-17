package com.rduda.Controller;

import com.rduda.API.AccountManager;
import com.rduda.API.Request.PostList;
import com.rduda.API.Request.PostMapping;
import com.rduda.Model.Exception.AuthenticationException;
import com.rduda.Model.Exception.FriendException;
import com.rduda.Model.Exception.PostInvalidArgumentsException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Robin on 2015-11-29.
 * <p>
 * REST routing for log-post operations, the request
 * operations are mapped to a PostHandler.
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

@Path("/post")
public class PostRoute {

    /**
     * Get all posts on the home wall.
     *
     * @param account authentication details.
     * @return PostList of PostMessages on the home wall.
     */
    @GET
    @Path("/home")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHome(@BeanParam PostMapping account) {
        try {
            AccountManager manager = AccountRoute.session(account);
            PostList posts = PostList.fromList(manager.getPostManager().getHomePosts());

            return Response.ok(posts).build();
        } catch (AuthenticationException e) {
            return Response.status(401).build();
        }
    }

    /**
     * Posts a message to the home wall.
     *
     * @param post contains the 'content' of the message.
     * @return PostList of PostMessages, on invalid
     * message headers returns InvalidArguments.
     */
    @POST
    @Path("/home")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postHome(PostMapping post) {
        try {
            AccountManager manager = AccountRoute.session(post);
            manager.getPostManager().postHome(post.getContent());
            return Response.ok().build();
        } catch (AuthenticationException e) {
            return Response.status(401).build();
        } catch (PostInvalidArgumentsException e) {
            return Response.status(405).build();
        }
    }

    /**
     * Get the posts on a friends wall.
     *
     * @param post containing the 'target' username of a Friend.
     * @return PostList of PostMessages on the friends wall, where
     * posts made by a shared friend is visible but not those who are
     * exclusive to the wall owner. When the 'target' is not a friend
     * of the in-context Account Missing is returned.
     */
    @GET
    @Path("/friend")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFriendPosts(@BeanParam PostMapping post) {
        try {
            AccountManager manager = AccountRoute.session(post);
            PostList posts = PostList.fromList(manager.getPostManager().getFriendPosts(post.getTarget()));

            return Response.ok(posts).build();
        } catch (AuthenticationException e) {
            return Response.status(401).build();
        } catch (FriendException e) {
            return Response.status(404).build();
        }
    }

    /**
     * Posts a message on the wall of a friend.
     *
     * @param post Specifying the message 'contents' and the 'target'
     *             receiver as Account username.
     * @return OK on success, Missing when the 'target' is not a friend.
     */
    @POST
    @Path("/friend")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postFriend(PostMapping post) {
        try {
            AccountManager manager = AccountRoute.session(post);
            manager.getPostManager().postFriend(post.getTarget(), post.getContent());

            return Response.ok().build();
        } catch (AuthenticationException e) {
            return Response.status(401).build();
        } catch (FriendException e) {
            return Response.status(404).build();
        }
    }

    /**
     * Get the posts in the stream.
     *
     * @param post contains authentication.
     * @return A PostList of PostMappings made by friends on
     * their own wall. Any friend-to-friend post will not be visible
     * the stream.
     */
    @GET
    @Path("/stream")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStream(@BeanParam PostMapping post) {
        try {
            AccountManager manager = AccountRoute.session(post);
            PostList posts = PostList.fromList(manager.getPostManager().getStreamPosts());

            return Response.ok(posts).build();
        } catch (AuthenticationException e) {
            return Response.status(401).build();
        }
    }

    /**
     * Removes an existing post. Users only have the authority to remove
     * posts that they are the receiver of (home wall) or the sender (friend-wall).
     *
     * @param post specified by 'id'.
     * @return OK on success, NotAuthorized when the id of the post
     * is not removable by the current user.
     */
    @POST
    @Path("/remove")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removePost(PostMapping post) {
        try {
            AccountManager manager = AccountRoute.session(post);
            manager.getPostManager().removePost(post.getId());

            return Response.ok().build();
        } catch (AuthenticationException e) {
            return Response.status(401).build();
        }
    }
}
