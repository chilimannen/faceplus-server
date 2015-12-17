package com.rduda.API;

import com.rduda.Model.Exception.FriendException;

import java.util.List;

/**
 * Created by Robin on 2015-11-18.
 *
 * Storage implementation for friend relations.
 */
public interface FriendStore {
    /**
     * Creates a friend request from account to target, if the target
     * has invited account the existing invitation will instead be accepted.
     * @param account acting account.
     * @param target to be requested for friendship.
     * @throws FriendException when the accounts already are friends.
     */
    void friendRequest(Account account, Account target) throws FriendException;

    /**
     * Accepts an existing friend invitation.
     * @param account acting account.
     * @param target the sender of the request.
     * @throws FriendException when the friend request to be accepted does
     *      not exist.
     */
    void acceptRequest(Account account, Account target) throws FriendException;

    /**
     * Removes an existing friend invitation.
     * @param account acting account.
     * @param target to be declined.
     */
    void declineRequest(Account account, Account target);

    /**
     * Removes the friendship between two accounts.
     * @param account acting account.
     * @param target friend of the acting account to be removed.
     * @throws FriendException when the accounts are not already friends.
     */
    void terminateFriendship(Account account, Account target) throws FriendException;

    /**
     * Get the friends of the acting account.
     * @param account acting account.
     * @return a list of friends to the Account.
     * @throws FriendException when the Account context is invalid.
     */
    List<Account> getFriendList(Account account) throws FriendException;

    /**
     * Get the friend requests of the acting account.
     * @param account acting account.
     * @return a list of friend requests to the account.
     * @throws FriendException when the Account context is invalid.
     */
    List<Account> getFriendRequests(Account account) throws FriendException;

    /**
     * Tests if the Account and Target are friends.
     * @param account Account performing the lookup.
     * @param target of the comparison.
     * @return boolean indicating friend status.
     */
    boolean isFriendsWith(Account account, Account target) throws FriendException;

    /**
     * Test if another Account has a pending friend request from the current Account.
     * @param account to be tested from.
     * @param target of the comparison.
     * @return boolean indicating request existence.
     */
    boolean isFriendRequested(Account account, Account target) throws FriendException;
}
