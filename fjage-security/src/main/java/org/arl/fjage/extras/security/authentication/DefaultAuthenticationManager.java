package org.arl.fjage.extras.security.authentication;

import org.arl.fjage.extras.security.core.Authentication;
import org.arl.fjage.extras.security.userdetails.UserDetails;
import org.arl.fjage.extras.security.userdetails.UserDetailsService;

import java.util.Collection;

public class DefaultAuthenticationManager
    implements AuthenticationManager {

  private final UserDetailsService userDetailsService;

  public DefaultAuthenticationManager(UserDetailsService userDetailsService) {
    super();

    this.userDetailsService = userDetailsService;
  }

  @Override
  public Authentication authenticate(Authentication authentication) {
    if (authentication instanceof CredentialsAuthenticationToken) {
      final CredentialsAuthenticationToken token = (CredentialsAuthenticationToken) authentication;
      final Collection<UserDetails> users = userDetailsService.findUsersByPassword((String) token.getCredentials());
      return users.stream().findFirst().map(userDetails -> {
        final UsernamePasswordAuthenticationToken newToken = new UsernamePasswordAuthenticationToken(
            userDetails.getUsername(), userDetails.getPassword());
        newToken.setAuthenticated(true);
        return (Authentication) newToken;
      }).orElse(authentication);
    }
    return authentication;
  }
}
