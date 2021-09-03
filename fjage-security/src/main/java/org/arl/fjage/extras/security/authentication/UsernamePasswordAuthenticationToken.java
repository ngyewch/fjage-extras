package org.arl.fjage.extras.security.authentication;

public class UsernamePasswordAuthenticationToken
    extends AbstractAuthenticationToken {

  private final String username;
  private final String password;

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
