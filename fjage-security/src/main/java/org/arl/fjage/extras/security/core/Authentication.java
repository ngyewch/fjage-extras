package org.arl.fjage.extras.security.core;

import java.security.Principal;

/**
 * Represents the token for an authentication request or for an authenticated principal once the request has been
 * processed by the {@link org.arl.fjage.extras.security.authentication.AuthenticationManager#authenticate(Authentication)} AuthenticationManager.authenticate(Authentication) method.
 */
public interface Authentication
    extends Principal {

  /**
   * The credentials that prove the principal is correct.
   *
   * @return The credentials that prove the identity of the Principal
   */
  Object getCredentials();

  /**
   * The identity of the principal being authenticated.
   * In the case of an authentication request with username and password, this would be the username.
   * Callers are expected to populate the principal for an authentication request.
   *
   * @return The Principal being authenticated or the authenticated principal after authentication.
   */
  Object getPrincipal();

  /**
   * Returns <code>true</code> if this represents a successfully authenticated token, <code>false</code> otherwise.
   *
   * @return <code>true</code> if this represents a successfully authenticated token, <code>false</code> otherwise.
   */
  boolean isAuthenticated();

  /**
   * Sets the authenticated status of this token.
   *
   * @param authenticated <code>true</code> if this represents a successfully authenticated token, <code>false</code> otherwise.
   */
  void setAuthenticated(boolean authenticated);
}
