package org.arl.fjage.extras.security.provisioning;

import org.apache.commons.lang3.StringUtils;
import org.arl.fjage.extras.security.userdetails.UserDetails;

import java.util.*;

public class InMemoryUserDetailsManager
    implements UserDetailsManager {

  private final Map<String, UserDetails> userMap = new LinkedHashMap<>();

  @Override
  public void createUser(UserDetails user) {
    userMap.put(user.getUsername(), user);
  }

  @Override
  public void deleteUser(String username) {
    userMap.remove(username);
  }

  @Override
  public void updateUser(UserDetails user) {
    userMap.put(user.getUsername(), user);
  }

  @Override
  public boolean userExists(String username) {
    return userMap.containsKey(username);
  }

  @Override
  public UserDetails loadUserByUsername(String username) {
    return userMap.get(username);
  }

  @Override
  public Collection<UserDetails> findUsersByPassword(String password) {
    final List<UserDetails> users = new ArrayList<>();
    for (final UserDetails user : userMap.values()) {
      if (StringUtils.equals(user.getPassword(), password)) {
        users.add(user);
      }
    }
    return users;
  }
}
