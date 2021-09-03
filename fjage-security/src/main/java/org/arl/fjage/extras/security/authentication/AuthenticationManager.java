package org.arl.fjage.extras.security.authentication;

import org.arl.fjage.extras.security.core.Authentication;

public interface AuthenticationManager {

  Authentication authenticate(Authentication authentication);
}
