package Mock;

import com.rduda.API.Account;
import com.rduda.API.AccountStore;
import com.rduda.Model.Exception.AccountAlreadyExistsException;
import com.rduda.Model.Exception.AuthenticationException;
import com.rduda.Model.Exception.NoSuchAccountException;
import com.rduda.Model.Exception.NoSuchImageException;
import com.rduda.API.Image;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Robin on 2015-11-13.
 * <p>
 * AccountStore implementation without persistent storage.
 */
class AccountStoreMock implements AccountStore {
    private static AccountStoreMock instance = null;
    private ArrayList<Account> accounts = new ArrayList<>();

    private AccountStoreMock() {
    }

    public synchronized static AccountStore getInstance() {
        if (instance == null)
            instance = new AccountStoreMock();

        return instance;
    }

    @Override
    public Account add(Account account) throws AccountAlreadyExistsException {
        if (accounts.contains(account))
            throw new AccountAlreadyExistsException();

        accounts.add(account);
        return account;
    }

    @Override
    public Account findByUsername(String username) throws NoSuchAccountException {
        Account found = null;

        for (Account account : accounts) {
            if (account.getActor().equals(username))
                found = account;
        }

        if (found == null)
            throw new NoSuchAccountException();

        return found;
    }

    @Override
    public void remove(Account account) throws NoSuchAccountException {
        if (!accounts.remove(account))
            throw new NoSuchAccountException();
    }

    @Override
    public List<Account> findByAny(String query) {
        List<Account> result = new ArrayList<>();

        for (Account account : accounts) {
            if (account.getActor().contains(query) ||
                    account.getFirstName().contains(query) ||
                    account.getLastName().contains(query))
                result.add(account);
        }
        result.sort(new Comparator<Account>() {
            @Override
            public int compare(Account first, Account other) {
                return (first.getFirstName() + first.getLastName()).compareTo(other.getFirstName() + other.getLastName());
            }
        });
        return result;
    }

    @Override
    public Account authenticate(String username, String password) throws AuthenticationException {
        Account authenticated = null;

        for (Account account : accounts) {
            if (account.getActor().equals(username) && ((AccountMock) account).getPassword().equals(password))
                authenticated = account;
        }

        if (authenticated != null)
            return authenticated;
        else
            throw new AuthenticationException();
    }

    @Override
    public void setProfile(Account account, String firstName, String lastName, String country, Integer age) {
        AccountMock mock = (AccountMock) account;
        mock.setAge(age);
        mock.setCountry(country);
        mock.setFirstName(firstName);
        mock.setLastName(lastName);
    }

    @Override
    public void setProfileImage(Account account, String base64encoded) {
        AccountMock mock = (AccountMock) account;
        mock.setProfileImage(new ImageMock(base64encoded));
    }

    @Override
    public Image getProfileImage(long id) throws NoSuchImageException {

        for (Account account : accounts) {
            AccountMock mock = (AccountMock) account;

            if (account.getId() == id && mock.getProfileImage() != null)
                return mock.getProfileImageMock();
        }
        throw new NoSuchImageException();
    }

    @Override
    public Account findById(long id) throws NoSuchAccountException {
        for (Account account : accounts) {
            if (account.getId().equals(id))
                return account;
        }
        throw new NoSuchAccountException();
    }
}
