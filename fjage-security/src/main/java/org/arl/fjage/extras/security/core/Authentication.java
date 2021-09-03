package org.arl.fjage.extras.security.core;

import java.security.Principal;

public interface Authentication
    extends Principal {

  Object getCredentials();

  Object getPrincipal();

  boolean isAuthenticated();

  void setAuthenticated(boolean authenticated);
}
