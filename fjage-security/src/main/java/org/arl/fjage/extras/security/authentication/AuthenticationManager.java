package org.arl.fjage.extras.security.authentication;

import org.arl.fjage.extras.security.core.Authentication;

/**
 * Processes an Authentication request.
 */
public interface AuthenticationManager {

  /**
   * Attempts to authenticate the passed Authentication object, returning a fully populated Authentication object if
   * successful.
   *
   * @param authentication Authentication request object.
   * @return A fully authenticated object.
   */
  Authentication authenticate(Authentication authentication);
}
