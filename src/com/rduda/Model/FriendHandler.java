package com.rduda.Model;

import com.rduda.API.*;
import com.rduda.Model.Exception.CannotFriendYourselfException;
import com.rduda.Model.Exception.FriendException;
import com.rduda.Model.Exception.NoSuchAccountException;

import java.util.List;

/**
 * Created by Robin on 2015-11-18.
 * <p>
 * Handles friend logic.
 */
class FriendHandler implements FriendManager {
    private Account account;
    private FriendStore store;
    private AccountStore accounts;

    protected FriendHandler(StoreProvider provider, Account account) {
        this.account = account;
        this.store = provider.getFriendStore();
        this.accounts = provider.getAccountStore();
    }

    @Override
    public void request(String username) throws FriendException {
        try {
            if (!username.equals(account.getActor()))
                store.friendRequest(account, accounts.findByUsername(username));
            else
                throw new CannotFriendYourselfException();
        } catch (NoSuchAccountException e) {
            throw new FriendException();
        }
    }

    @Override
    public void accept(String username) throws FriendException {
        try {
            store.acceptRequest(account, accounts.findByUsername(username));
        } catch (NoSuchAccountException e) {
            throw new FriendException();
        }
    }

    @Override
    public void decline(String username) throws FriendException {
        try {
            store.declineRequest(account, accounts.findByUsername(username));
        } catch (NoSuchAccountException e) {
            throw new FriendException();
        }
    }

    @Override
    public void terminate(String username) throws FriendException {
        try {
            store.terminateFriendship(account, accounts.findByUsername(username));
        } catch (NoSuchAccountException e) {
            throw new FriendException();
        }
    }

    @Override
    public List<Account> getFriendList() throws FriendException {
        return AccountBean.fromList(store.getFriendList(account));
    }

    @Override
    public List<Account> getFriendRequests() throws FriendException {
        return AccountBean.fromList(store.getFriendRequests(account));
    }

    @Override
    public boolean isFriendsWith(String username) throws FriendException {
        try {
            return store.isFriendsWith(account, accounts.findByUsername(username));
        } catch (NoSuchAccountException e) {
            throw new FriendException();
        }
    }

    @Override
    public boolean isFriendRequested(String username) throws FriendException {
        try {
            return store.isFriendRequested(account, accounts.findByUsername(username));
        } catch (NoSuchAccountException e) {
            throw new FriendException();
        }
    }
}
