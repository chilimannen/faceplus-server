package Mock;

import com.rduda.API.Account;
import com.rduda.Model.Exception.NoSuchFriendException;
import com.rduda.API.Message;
import com.rduda.API.MessageStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Robin on 2015-11-20.
 */
class MessageStoreMock implements MessageStore {
    private static final int READ_LIMIT = 10;

    @Override
    public List<Message> getMessagesFrom(Account actor, Account target, long offset) throws NoSuchFriendException {
        AccountMock account = (AccountMock) actor;
        AccountMock friend = (AccountMock) target;
        List<Message> messages = allMessagesOrderDescendingById(account, friend);
        List<Message> conversation = new ArrayList<>();

        if (account.getFriendList().contains(friend)) {
            for (Message message : messages) {
                // get messages in the conversation between two accounts.
                if (message.getReceiver().equals(friend) && message.getSender().equals(account) ||
                        message.getReceiver().equals(account) && message.getSender().equals(friend)) {
                    // get all messages starting from the latest that have an id less than offset.
                    if (message.getId() < offset) {
                        conversation.add(message);
                    }
                    // limit the amount of messages returned.
                    if (conversation.size() == READ_LIMIT)
                        break;
                }
            }
        } else
            throw new NoSuchFriendException();
        return conversation;
    }

    // Get all messages and order descending by id (time) getting the latest messages first.
    private ArrayList<Message> allMessagesOrderDescendingById(AccountMock account, AccountMock friend) {
        ArrayList<Message> messages = new ArrayList<>();

        messages.addAll(account.getMessages());
        messages.addAll(friend.getMessages());
        messages.sort(new Comparator<Message>() {
            public int compare(Message first, Message other) {
                return ((Long) first.getId()).compareTo(other.getId()) * -1;
            }
        });

        return messages;
    }

    @Override
    public void sendMessage(Message message) throws NoSuchFriendException {
        AccountMock account = (AccountMock) message.getSender();
        AccountMock friend = (AccountMock) message.getReceiver();

        if (account.getFriendList().contains(friend)) {
            friend.getMessages().add(message);
        } else
            throw new NoSuchFriendException();
    }

    @Override
    public List<Message> getLatestMessages(Account actor) {
        AccountMock account = (AccountMock) actor;
        List<Message> result = new ArrayList<>();
        List<Message> messages = new ArrayList<>();
        messages.addAll(account.getMessages());

        Collections.reverse(messages);

        for (Message message : messages) {
            if (!(containsUser(result, message.getSender()))) {
                result.add(message);
            }
        }
        return result;
    }

    private boolean containsUser(List<Message> messages, Account account) {
        boolean contains = false;

        for (Message message : messages) {
            if (message.getSender().equals(account))
                contains = true;
        }

        return contains;
    }
}
