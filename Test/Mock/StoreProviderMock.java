package Mock;

import com.rduda.API.*;

public class StoreProviderMock implements StoreProvider {
    @Override
    public AccountStore getAccountStore() {
        return AccountStoreMock.getInstance();
    }

    @Override
    public Account getAccountModel(String username, String plaintextPassword, String firstName, String lastName) {
        return new AccountMock(username, plaintextPassword, firstName, lastName);
    }

    @Override
    public PostStore getPostStore() {
        return new PostStoreMock();
    }

    @Override
    public Post getPostModel(Account sender, Account receiver, String content, String date) {
        PostMock post = new PostMock();
        post.setContent(content);
        post.setReceiver(receiver);
        post.setSender(sender);
        post.setDate(date);
        return post;
    }

    @Override
    public Message getMessageModel(Account sender, Account receiver, String content) {
        return new MessageMock(sender, receiver, content);
    }

    @Override
    public MessageStore getMessageStore() {
        return new MessageStoreMock();
    }

    @Override
    public FriendStore getFriendStore() {
        return new FriendStoreMock();
    }
}
