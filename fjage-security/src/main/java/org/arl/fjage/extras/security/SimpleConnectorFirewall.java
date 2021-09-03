package org.arl.fjage.extras.security;

import org.arl.fjage.AgentID;
import org.arl.fjage.extras.security.authentication.AuthenticationManager;
import org.arl.fjage.remote.JsonMessage;

public class SimpleConnectorFirewall
    extends AbstractConnectorFirewall {

  public SimpleConnectorFirewall(AuthenticationManager authenticationManager) {
    super(authenticationManager);
  }

  @Override
  protected boolean permit(Session session, JsonMessage rq) {
    return session.isAuthenticated();
  }

  @Override
  protected boolean permit(Session session, AgentID aid) {
    return session.isAuthenticated();
  }
}
