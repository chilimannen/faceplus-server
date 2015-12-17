package com.rduda.API;


import com.rduda.Model.Exception.AuthenticationException;
import com.rduda.Model.Exception.NoSuchAccountException;
import com.rduda.Model.Exception.NoSuchImageException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Robin on 2015-11-11.
 * <p>
 * Account manager is the entry point for the UI/Controller.
 * An AccountManager in the authenticated state is required
 * to gain access to the other managers which uses the context
 * of the AccountManagers authenticated Account for operation.
 * <p>
 * It is important that the exposed managers does not have a
 * public constructor and that they are created within the
 * context of the currently authenticated user.
 * <p>
 * Caution must be taken when the AccountManager transitions
 * between authenticated and not authenticated, to prevent any
 * timing/synchronization attacks.
 */
public interface AccountManager extends Serializable {
    /**
     * Gives access to the authenticated Account´s messages.
     *
     * @return A MessageManager operating in the context of the authenticated Account.
     * @throws AuthenticationException when the AccountManager state is not authenticated.
     */
    MessageManager getMessageManager() throws AuthenticationException;

    /**
     * Gives access to the authenticated Account´s friends.
     *
     * @return a FriendManager operating in the context of the authenticated Account.
     * @throws AuthenticationException when the AccountManager state is not authenticated.
     */
    FriendManager getFriendManager() throws AuthenticationException;

    /**
     * Gives access to the authenticated Account´s wall/log - posts.
     *
     * @return a PostManager operating in the context of the authenticated Account.
     * @throws AuthenticationException when the AccountManager state is not authenticated.
     */
    PostManager getPostManager() throws AuthenticationException;

    /**
     * Registers a new account and transitions into authenticated on success.
     *
     * @param username  Has to be store-unique.
     * @param password  Account´s password.
     * @param firstName First name of the owner of the Account.
     * @param lastName  Last name of the owner of the Account.
     * @throws AuthenticationException when the registration failed.
     */
    void register(String username, String password, String firstName, String lastName) throws AuthenticationException;

    /**
     * Attempts to transition into the authenticated state and the context of
     * the Account referenced.
     *
     * @param username A reference to the Account that should be authenticated.
     * @param password The password of the Account referenced.
     * @throws AuthenticationException when the password mismatches the stored password or
     *                                 when the referenced Account does not exist.
     */
    void login(String username, String password) throws AuthenticationException;

    /**
     * Optional: Adds support for time-uncoupled authentication.
     * <p>
     * Implementing this method allows the AccountManager to
     * resume an existing session by using a token that was
     * supplied when authenticating.
     * <p>
     * The method should limit the TTL of a session.
     *
     * @param username specifying the user which may have an active session.
     * @param token    per-user unique value that has enough entropy to act as an
     *                 abstraction of the users password to avoid redistribution.
     * @throws AuthenticationException when a session could not be restored.
     */
    default void session(String username, String token) throws AuthenticationException {
        throw new NotImplementedException();
    }

    /**
     * Removes the currently authenticated account from the store and transitions
     * into the unauthenticated state.
     *
     * @throws NoSuchAccountException when the user in the current context
     *                                does not exist in the database.
     */
    void unregister() throws NoSuchAccountException;

    /**
     * Transitions into the unauthenticated state.
     */
    void logout();

    /**
     * @return indicating whether the AccountManager is in the authenticated state.
     */
    boolean isAuthenticated();

    /**
     * @return The account in the current context.
     * @throws AuthenticationException when there is no authenticated account
     *                                 in the current context.
     */
    Account getAccount() throws AuthenticationException;

    /**
     * Finds all accounts where the username, firstname or lastname
     * contains the query string. Results are ordered by match score.
     *
     * @param query the search parameter.
     * @return list of accounts matching the search parameter.
     */
    List<Account> findByAny(String query);

    /**
     * Finds an account from the store.
     *
     * @param username Uniquely identifying the user.
     * @return an Account with username matching parameter.
     * @throws NoSuchAccountException when an Account is not found.
     */
    Account findByUsername(String username) throws NoSuchAccountException;

    /**
     * Update account information.
     *
     * @param firstName first name.
     * @param lastName  last name.
     * @param country   of residence.
     * @param age       in years.
     */
    void setProfile(String firstName, String lastName, String country, Integer age) throws AuthenticationException;

    /**
     * Sets the profile image of the currently logged on user.
     *
     * @param base64encoded Image expressed as base64.
     */
    void setProfileImage(String base64encoded) throws AuthenticationException;

    /**
     * Get the profile image of a specified account..
     *
     * @return A base64 encoded image.
     */
    Image getProfileImage(long accountId) throws NoSuchImageException, AuthenticationException;
}
