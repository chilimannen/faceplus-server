package Mock;

import com.rduda.API.Account;
import com.rduda.Model.Exception.*;
import com.rduda.API.FriendStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robin on 2015-11-18.
 */
class FriendStoreMock implements FriendStore {

    @Override
    public void friendRequest(Account actor, Account target) throws FriendException {
        AccountMock friend = (AccountMock) target;
        AccountMock account = (AccountMock) actor;

        // if already friends do not create a request
        if (account.getFriendList().contains(friend)) {
            throw new FriendAlreadyExistsException();
        } else if (friend.getFriendRequest().contains(account)) {
            // ignore the request as it already exists.
        }
        else {
            // the other party has already requested our friendship, accept the request.
            if (account.getFriendRequest().contains(friend)) {
                acceptRequest(account, friend);
            } else {
                friend.getFriendRequest().add(account);
            }
        }
    }

    @Override
    public void acceptRequest(Account actor, Account target) throws FriendException {
        boolean accepted = false;
        AccountMock account = (AccountMock) actor;
        AccountMock friend = (AccountMock) target;

        for (Account request : new ArrayList<>(account.getFriendRequest())) {
            if (request.equals(friend)) {

                friend.getFriendRequest().remove(account);
                account.getFriendRequest().remove(friend);

                friend.getFriendList().add(account);
                account.getFriendList().add(friend);
                accepted = true;
            }
        }

        if (!accepted)
            throw new FriendRequestMissingException();
    }

    @Override
    public void declineRequest(Account actor, Account target) {
        AccountMock account = (AccountMock) target;
        account.getFriendRequest().remove(actor);
    }

    @Override
    public void terminateFriendship(Account actor, Account target) throws FriendException {
        AccountMock friend = (AccountMock) target;
        AccountMock account = (AccountMock) actor;

        if (!friend.getFriendList().remove(account))
            throw new NoSuchFriendException();

        if (!account.getFriendList().remove(friend))
            throw new NoSuchFriendException();
    }

    @Override
    public List<Account> getFriendList(Account account) {
        return ((AccountMock) account).getFriendList();
    }

    @Override
    public List<Account> getFriendRequests(Account account) {
        return ((AccountMock)account).getFriendRequest();
    }

    @Override
    public boolean isFriendsWith(Account account, Account target) throws FriendException {
        return (getFriendList(account).contains(target) && getFriendList(target).contains(account));
    }

    @Override
    public boolean isFriendRequested(Account account, Account target) throws FriendException {
        return getFriendRequests(target).contains(account);
    }
}
