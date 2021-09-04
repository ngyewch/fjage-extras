package org.arl.fjage.extras.security.userdetails;

import java.util.Collection;

/**
 * Core interface which loads user-specific data.
 */
public interface UserDetailsService {

  /**
   * Returns the user specified by the username.
   *
   * @param username Username.
   * @return The user, <code>null</code> if not found.
   */
  UserDetails loadUserByUsername(String username);

  /**
   * Returns the users that have the specified password.
   *
   * @param password Password.
   * @return The users.
   */
  Collection<UserDetails> findUsersByPassword(String password);
}
