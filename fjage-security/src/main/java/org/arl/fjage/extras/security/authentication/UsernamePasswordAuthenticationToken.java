package org.arl.fjage.extras.security.authentication;

/**
 * An Authentication implementation that is designed for simple presentation of a username and password.
 */
public class UsernamePasswordAuthenticationToken
    extends AbstractAuthenticationToken {

  private final String username;
  private final String password;

  /**
   * Constructs a new UsernamePasswordAuthenticationToken.
   *
   * @param username Username.
   * @param password Password.
   */
  public UsernamePasswordAuthenticationToken(String username, String password) {
    super();

    this.username = username;
    this.password = password;
  }

  @Override
  public Object getCredentials() {
    return password;
  }

  @Override
  public Object getPrincipal() {
    return username;
  }

  @Override
  public String getName() {
    return username;
  }
}
