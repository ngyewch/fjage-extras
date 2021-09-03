package org.arl.fjage.extras.security.provisioning;

import org.arl.fjage.extras.security.userdetails.UserDetails;
import org.arl.fjage.extras.security.userdetails.UserDetailsService;

public interface UserDetailsManager
    extends UserDetailsService {

  void createUser(UserDetails user);

  void deleteUser(String username);

  void updateUser(UserDetails user);

  boolean userExists(String username);
}
