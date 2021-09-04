package org.arl.fjage.extras.security.provisioning;

import org.arl.fjage.extras.security.userdetails.UserDetails;
import org.arl.fjage.extras.security.userdetails.UserDetailsService;

/**
 * User provisioning and management interface.
 */
public interface UserDetailsManager
    extends UserDetailsService {

  /**
   * Creates a new user.
   *
   * @param user User.
   */
  void createUser(UserDetails user);

  /**
   * Deletes a user.
   *
   * @param username Username.
   */
  void deleteUser(String username);

  /**
   * Updates a user.
   *
   * @param user User.
   */
  void updateUser(UserDetails user);

  /**
   * Checks if a user exists.
   *
   * @param username Username.
   * @return <code>true</code> if the user exists, <code>false</code> otherwise.
   */
  boolean userExists(String username);
}
