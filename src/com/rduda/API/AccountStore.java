package com.rduda.API;

import com.rduda.Model.Exception.AccountAlreadyExistsException;
import com.rduda.Model.Exception.AuthenticationException;
import com.rduda.Model.Exception.NoSuchAccountException;
import com.rduda.Model.Exception.NoSuchImageException;

import java.util.List;

/**
 * Created by Robin on 2015-11-11.
 * <p>
 * Entity store handling the storage of Accounts.
 */
public interface AccountStore {
    /**
     * @param account to be added.
     * @return the account that was added.
     * @throws AccountAlreadyExistsException
     */
    Account add(Account account) throws AccountAlreadyExistsException;

    /**
     * @param username of the account to be found.
     * @return the Account that was found in the store.
     * @throws NoSuchAccountException when no matching accounts were found.
     */
    Account findByUsername(String username) throws NoSuchAccountException;

    /**
     * Removes an account from the store.
     *
     * @param account to be removed from the store.
     * @throws NoSuchAccountException when the account could not be located.
     */
    void remove(Account account) throws NoSuchAccountException;

    /**
     * Finds accounts where the username, firstname or lastname contains
     * the query string.
     *
     * @param query search parameter.
     * @return A list of all accounts matching the query, the result is
     * ordered by match-score, descending.
     */
    List<Account> findByAny(String query);

    /**
     * Attempts to authenticate an account referenced by its username.
     *
     * @param username reference to the account to be authenticated.
     * @param password the assumed password of the referenced account.
     * @return account data of the authenticated account.
     * @throws AuthenticationException when the authentication fails.
     */
    Account authenticate(String username, String password) throws AuthenticationException;

    /**
     * Update accounts profile information.
     *
     * @param account   Account to be updated.
     * @param firstName First name.
     * @param lastName  Last name.
     * @param country   Country of residence.
     * @param age       of character.
     */
    void setProfile(Account account, String firstName, String lastName, String country, Integer age);

    /**
     * Sets a profile image for an account.
     *
     * @param account       Account to be updated.
     * @param base64encoded Image expressed as base64.
     */
    void setProfileImage(Account account, String base64encoded);

    /**
     * Get the profile image of an account.
     *
     * @param id of the Account to get the profile image of.
     * @return a profile image.
     * @throws NoSuchImageException when no image has been set.
     */
    Image getProfileImage(long id) throws NoSuchImageException;

    /**
     * Finds an Account by its id.
     *
     * @param id of the Account to return.
     * @return the found account.
     */
    Account findById(long id) throws NoSuchAccountException;
}
