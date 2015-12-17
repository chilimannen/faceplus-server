package com.rduda.API;

/**
 * Created by Robin on 2015-11-20.
 *
 * Specifies the requirements on the messages stored by the persistence
 * manager. The Manager (business layer) and the View layer knows the
 * Message by this Interface. As the business layer does not know
 * the actual implementation messages may be encapsulated when
 * testing using Mock objects.
 */
public interface Message {
    /**
     * Get the store unique identifier, if no integer id is available the implementation
     * should provide a store-unique hash instead.
     * @return The unique identifier of the Message expressed as Long.
     */
    Long getId();

    /**
     * Get the text contents of the message.
     * @return The contents of the message as a String.
     */
    String getContent();

    /**
     * Get the date of sending.
     * @return The date of sending expressed as a String.
     */
    String getDate();

    /**
     * Returns the Account receiver of the Message. The receiver should
     * be re-instantiated as a bean as to not share reference with the
     * persistence layer.
     * @return The receiving account of the Message.
     */
    Account getReceiver();

    /**
     * Returns the Account sender of the Message. The sender should
     * be re-instantiated as a bean as to not share reference with
     * the persistence layer.
     * @return The sending account of the Message.
     */
    Account getSender();
}
