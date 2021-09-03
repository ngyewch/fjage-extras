package org.arl.fjage.extras.security.authentication;

import org.arl.fjage.extras.security.core.Authentication;

public abstract class AbstractAuthenticationToken
    implements Authentication {

  private boolean authenticated = false;

  @Override
  public boolean isAuthenticated() {
    return authenticated;
  }

  @Override
  public void setAuthenticated(boolean authenticated) {
    this.authenticated = authenticated;
  }
}
