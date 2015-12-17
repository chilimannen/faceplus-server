package com.rduda.API;

/**
 * Created by Robin on 2015-11-18.
 *
 * Persistence store for Posts.
 */
public interface Post {
    /**
     * Get the unique identifier of the Post.
     * @return The ID expressed as a Long.
     */
    Long getId();

    /**
     * get the content of the Post.
     * @return the content of the Post as a String.
     */
    String getContent();

    /**
     * Get the date when the Post was sent.
     * @return The date of sending expressed as String.
     */
    String getDate();

    /**
     * Get the Receiver of the Post. The Receiver should
     * be re-instantiated as a bean before leaving the business layer.
     * @return The receiving Account of the Post.
     */
    Account getReceiver();

    /**
     * Get the Sender of the Post. The Sender should
     * be re-instantiated as a bean before leaving the business layer.
     * @return The sending Account of the Post.
     */
    Account getSender();
}
