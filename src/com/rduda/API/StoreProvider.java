package com.rduda.API;

/**
 * Created by Robin on 2015-11-11.
 * <p>
 * Specifies the provider of Stores (persistent-storage).
 * Specifies the implementations to be used, should be passed to
 * the model entry point to override the default implementation
 * when testing.
 * <p>
 * The stores provided must be compatible with another if they
 * share data.
 */
public interface StoreProvider {
    /**
     * @return an AccountStore implementation.
     */
    AccountStore getAccountStore();

    /**
     * @return an implementation of the FriendStore.
     */
    FriendStore getFriendStore();

    /**
     * @return a PostStore implementation.
     */
    PostStore getPostStore();

    /**
     * @return an implementation of the MessageStore.
     */
    MessageStore getMessageStore();

    /**
     * Instantiates an Account using the provider-hidden implementation.
     *
     * @param username          username of the Account to create.
     * @param plaintextPassword password of the Account to create.
     * @param firstName         first name of the Account owner.
     * @param lastName          last name of the Account owner.
     * @return an object implementing the Account interface.
     */
    Account getAccountModel(String username, String plaintextPassword, String firstName, String lastName);

    /**
     * Instantiate a Post using the provider-hidden implementation.
     *
     * @param sender   The sending Account of the post.
     * @param receiver The receiving Account.
     * @param content  The textual content of the Post.
     * @param date     Date of sending expressed as a String.
     * @return an object implementing the Post interface.
     */
    Post getPostModel(Account sender, Account receiver, String content, String date);

    /**
     * Instantiate a message using the provider-hidden implementation.
     *
     * @param sender   Account sending the message.
     * @param receiver Username of Account receiving the message.
     * @param content  The textual content of the message.
     * @return an object implementing the Message interface.
     */
    Message getMessageModel(Account sender, Account receiver, String content);
}
