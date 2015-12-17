package com.rduda.Model;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by Robin on 2015-11-28.
 * <p>
 * Generates a session id for session-based authentication
 * where it is not feasible for the client to store and
 * supply the password for each request.
 * <p>
 */
abstract class SessionHelper {
    private static int BIT_COUNT = 256;
    private static volatile SecureRandom random = new SecureRandom();

    /**
     * Generate a random string for session authentication.
     * All generated session ids must be used together with
     * another user-unique token as the generated ids are not
     * unique themselves.
     *
     * @return A string of 256 bits expressed as hexadecimal.
     */
    public static String getSessionId() {
        return new BigInteger(256, random).toString(16);
    }
}
