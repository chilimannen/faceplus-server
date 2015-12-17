package com.rduda.API;

import com.rduda.Model.Exception.FriendException;

import java.util.List;

/**
 * Created by Robin on 2015-11-18.
 *
 * Manages the friends of an Account, may only be accessed
 * through the existing Account-context from the AccountManager.
 */
public interface FriendManager {
    /**
     * Send a friend request to referenced Account.
     * @param username reference to an Account.
     * @throws FriendException when the reference is invalid
     *      or the two Accounts already are friends.
     */
    void request(String username) throws FriendException;

    /**
     * Accept an existing friend invite from another Account.
     * @param username reference to the Account to accept.
     * @throws FriendException when the referenced account
     *      does not have an invitation for the actor.
     */
    void accept(String username) throws FriendException;

    /**
     * Declines a friend request.
     * @param username reference to the Account to reject.
     */
    void decline(String username) throws FriendException;

    /**
     * Remove an existing friend.
     * @param username Reference to the friend to remove.
     * @throws FriendException when the Accounts are not already
     *      friends or the reference is invalid.
     */
    void terminate(String username) throws FriendException;

    /**
     * Get a list of friends for the current Account-context.
     * @return a List of the Accounts friends.
     * @throws FriendException when the Account-context is invalid.
     */
    List<Account> getFriendList() throws FriendException;

    /**
     * Get a list of friend requests for the current Account-context.
     * @return a List of the Accounts invitations.
     * @throws FriendException when the Account-context is invalid.
     */
    List<Account> getFriendRequests() throws FriendException;

    /**
     * Test if the Account is friends with Username.
     * @param username of the other account to test for.
     * @return A boolean indicating friend status.
     */
    boolean isFriendsWith(String username) throws FriendException;

    /**
     * Test if a friend request has been sent to another account.
     * @param username of the requested friend.
     * @return true if a request has been sent.
     */
    boolean isFriendRequested(String username) throws FriendException;
}
