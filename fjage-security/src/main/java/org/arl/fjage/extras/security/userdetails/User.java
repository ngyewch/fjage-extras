package org.arl.fjage.extras.security.userdetails;

/**
 * Default UserDetails implementation.
 */
public class User
    implements UserDetails {

  private final String username;
  private final String password;

  /**
   * Constructs a new User.
   *
   * @param username Username.
   * @param password Password.
   */
  public User(String username, String password) {
    super();

    this.username = username;
    this.password = password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public String getPassword() {
    return password;
  }
}
