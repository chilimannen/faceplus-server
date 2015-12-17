package com.rduda.API;

import com.rduda.Model.Exception.NoSuchFriendException;

import java.util.List;

/**
 * Created by Robin on 2015-11-20.
 *
 * Handles the sending and receiving of messages.
 *
 * The MessageManager may only be accessed through
 * an existing Account-context from the AccountManager.
 */
public interface MessageManager {
    /**
     * Get all the messages from another user specified by username.
     * @param username of the friend to read messages from.
     * @param offset id of the last received message
     *               if set to 0 returns the last messages
     *               using an internal specified limit.
     * @return All messages matching the username sender
     *      which was directed as the account currently in context.
     * @throws NoSuchFriendException when username is not a friend.
     */
    List<Message> getMessagesFrom(String username, long offset) throws NoSuchFriendException;

    /**
     * Sends a message to another user that must be a friend.
     * @param username a friend to send a message to.
     * @param content the text contents of the message.
     * @throws NoSuchFriendException when username is not a friend.
     */
    void sendMessageTo(String username, String content) throws NoSuchFriendException;

    /**
     * Gets a list of the last received Messages limited to one per sender.
     * @return list of Messages.
     */
    List<Message> getLatestMessages();
}
