package org.arl.fjage.extras.security.userdetails;

import java.util.Collection;

public interface UserDetailsService {

  UserDetails loadUserByUsername(String username);

  Collection<UserDetails> findUsersByPassword(String password);
}
