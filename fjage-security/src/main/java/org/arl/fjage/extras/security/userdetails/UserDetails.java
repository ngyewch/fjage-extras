package org.arl.fjage.extras.security.userdetails;

/**
 * Provides core user information.
 */
public interface UserDetails {

  /**
   * Returns the username.
   *
   * @return Username.
   */
  String getUsername();

  /**
   * Returns the user's password.
   *
   * @return Password.
   */
  String getPassword();
}
