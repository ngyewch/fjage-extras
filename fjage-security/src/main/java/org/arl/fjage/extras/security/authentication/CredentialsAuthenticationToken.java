package org.arl.fjage.extras.security.authentication;

public class CredentialsAuthenticationToken
    extends AbstractAuthenticationToken {

  private final String credentials;

  public CredentialsAuthenticationToken(String credentials) {
    super();

    this.credentials = credentials;
  }

  @Override
  public Object getCredentials() {
    return credentials;
  }

  @Override
  public Object getPrincipal() {
    return null;
  }

  @Override
  public String getName() {
    return null;
  }
}
