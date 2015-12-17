package Mock;

import com.rduda.API.AccountManager;
import com.rduda.Model.AccountHandler;

/**
 * Created by Robin on 2015-11-21.
 * <p>
 * Toggles the testing mode to use the mocked provider or default implementation.
 */
public class ManagerProvider {
    private static final boolean MOCK_PROVIDER = false;

    /**
     * Provides implementations of a StoreProvider.
     *
     * @return a Mocked Provider if internal is set
     * return the implementations DefaultProvider if unset.
     */
    public static AccountManager getAccountManager() {
        if (MOCK_PROVIDER)
            return new AccountHandler(new StoreProviderMock());
        else
            return new AccountHandler();
    }

}
