package com.rduda.Model;

import com.rduda.API.*;
import com.rduda.Model.Exception.NoSuchAccountException;
import com.rduda.Model.Exception.NoSuchFriendException;

import java.util.List;

/**
 * Created by Robin on 2015-11-20.
 *
 * Handler for messaging.
 */
class MessageHandler implements MessageManager {
    private Account account;
    private AccountStore accounts;
    private MessageStore messages;
    private StoreProvider provider;

    protected MessageHandler(StoreProvider provider, Account account) {
        this.account = account;
        accounts = provider.getAccountStore();
        messages = provider.getMessageStore();
        this.provider = provider;
    }

    @Override
    public List<Message> getMessagesFrom(String username, long offset) throws NoSuchFriendException {
        try {
            return MessageBean.FromList(messages.getMessagesFrom(account, accounts.findByUsername(username), offset));
        } catch (NoSuchAccountException e) {
            throw new NoSuchFriendException();
        }
    }

    @Override
    public void sendMessageTo(String target, String content) throws NoSuchFriendException {
        try {
            Message message = provider.getMessageModel(account, accounts.findByUsername(target), content);
            messages.sendMessage(message);
        } catch (NoSuchAccountException e) {
            throw new NoSuchFriendException();
        }
    }

    @Override
    public List<Message> getLatestMessages() {
        return messages.getLatestMessages(account);
    }
}
