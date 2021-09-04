package org.arl.fjage.extras.security.authentication;

/**
 * An Authentication implementation that is designed for fjage connection credentials.
 */
public class CredentialsAuthenticationToken
    extends AbstractAuthenticationToken {

  private final String credentials;

  /**
   * Constructs a new CredentialsAuthenticationToken.
   *
   * @param credentials fjage connection credentials.
   */
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
