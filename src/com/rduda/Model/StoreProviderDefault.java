package com.rduda.Model;

import com.rduda.API.*;

import java.util.Date;

/**
 * Created by Robin on 2015-11-11.
 * <p>
 * Default store provider.
 */
class StoreProviderDefault implements StoreProvider {

    @Override
    public AccountStore getAccountStore() {
        return new AccountDB();
    }


    @Override
    public MessageStore getMessageStore() {
        return new MessageDB();
    }

    @Override
    public FriendStore getFriendStore() {
        return new FriendDB();
    }


    @Override
    public PostStore getPostStore() {
        return new PostDB();
    }

    @Override
    public Account getAccountModel(String username, String plaintextPassword, String firstName, String lastName) {
        AccountMapping account = new AccountMapping()
                .setUsername(username)
                .setFirstName(firstName)
                .setLastName(lastName);
        account.setPassword(plaintextPassword);

        return account;
    }

    @Override
    public Message getMessageModel(Account sender, Account receiver, String content) {
        MessageMapping message = new MessageMapping();
        message.setSender((AccountMapping) sender);
        message.setReceiver((AccountMapping) receiver);
        message.setContent(content);
        message.setDate(new Date().toString());
        return message;
    }

    @Override
    public Post getPostModel(Account sender, Account receiver, String content, String date) {
        PostMapping post = new PostMapping();
        post.setSender((AccountMapping) sender);
        post.setReceiver((AccountMapping) receiver);
        post.setContent(content);
        post.setDate(date);
        return post;
    }
}
