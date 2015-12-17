package com.rduda.Model.Exception;

/**
 * Created by Robin on 2015-11-11.
 *
 * Throw when the account already exists for any
 * operation that requires the specified user to not exist.
 */
public class AccountAlreadyExistsException extends AuthenticationException {
}
