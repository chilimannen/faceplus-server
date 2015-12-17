package com.rduda.API;

import com.rduda.Model.Exception.NoSuchFriendException;

import java.util.List;

/**
 * Created by Robin on 2015-11-20.
 *
 * Persistence store for Messages.
 */
public interface MessageStore {
    /**
     * Get messages where the sender and receiver is the actor OR the target,
     *      partial conversation between two Accounts should be returned.
     * @param actor the sender or receiver of the chat message.
     * @param target the sender or receiver of the chat message.
     * @param offset Returns all messages with a lower ID than the offset,
     *               the message history starts at the last message and backs
     *               up by lowering the offset. The messages returned when offset is set
     *               to minimum is always the first message. The messages returned when
     *               offset is set to maximum is always the last messages sent or
     *               received limited by an internal counter.
     * @return A chat conversation between two parties.
     * @throws NoSuchFriendException when the two accounts are not friends.
     */
    List<Message> getMessagesFrom(Account actor, Account target, long offset) throws NoSuchFriendException;

    /**
     * Sends a message to a friend.
     * @param message containing the actor, receiver and content.
     * @throws NoSuchFriendException when the receiver and sender is not friends.
     */
    void sendMessage(Message message) throws NoSuchFriendException;

    /**
     * Get a list of the last messages grouped by sender.
     * @param actor the receiver of the message.
     * @return a list of the latest messages received limited to one per sender.
     */
    public List<Message> getLatestMessages(Account actor);
}
