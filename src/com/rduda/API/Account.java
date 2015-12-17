package com.rduda.API;

/**
 * Created by Robin on 2015-11-13.
 * <p>
 * Entities implementing this interface may be passed out of
 * the persistent store and then back in by managers. May
 * also be returned from the manager to the UI.
 * <p>
 * When implementing equals the Id or the username
 * must be used as both are defined unique.
 */
public interface Account {
    /**
     * @return The unique identification number for the Account.
     */
    Long getId();

    /**
     * @return An username that is store-unique.
     */
    String getActor();

    /**
     * @return First name of the person owning the account.
     */
    String getFirstName();

    /**
     * @return Last name of the person owning the account.
     */
    String getLastName();

    /**
     * @return The country of residence.
     */
    String getCountry();

    /**
     * @return The age of the owner.
     */
    Integer getAge();

    /**
     * @return An ID reference to an Image.
     */
    Long getProfileImage();

    /**
     * Get the session token identifier for the account.
     *
     * @return A token string expressed in hexadecimal.
     */
    default String getToken() {
        return null;
    }
}
