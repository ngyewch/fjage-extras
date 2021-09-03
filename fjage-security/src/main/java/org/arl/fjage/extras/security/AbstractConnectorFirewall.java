package org.arl.fjage.extras.security;

import org.arl.fjage.AgentID;
import org.arl.fjage.auth.Firewall;
import org.arl.fjage.connectors.Connector;
import org.arl.fjage.extras.security.authentication.AuthenticationManager;
import org.arl.fjage.extras.security.authentication.CredentialsAuthenticationToken;
import org.arl.fjage.extras.security.core.Authentication;
import org.arl.fjage.remote.JsonMessage;

import java.util.logging.Logger;

public abstract class AbstractConnectorFirewall
    implements Firewall {

  private final AuthenticationManager authenticationManager;
  private final ThreadLocal<Session> sessionThreadLocal = new ThreadLocal<>();
  private final Logger log = Logger.getLogger(getClass().getName());

  public AbstractConnectorFirewall(AuthenticationManager authenticationManager) {
    super();

    this.authenticationManager = authenticationManager;
  }

  @Override
  public boolean authenticate(Connector conn, String creds) {
    if (creds == null) {
      final Session session = sessionThreadLocal.get();
      if (session != null) {
        log.info(String.format("[%s] session removed", session));
      }
      sessionThreadLocal.remove();
      return false;
    }

    final CredentialsAuthenticationToken credentialsAuthenticationToken = new CredentialsAuthenticationToken(creds);
    final Authentication authentication = authenticationManager.authenticate(credentialsAuthenticationToken);

    final Session session = new Session(conn, authentication);
    sessionThreadLocal.set(session);

    log.info(String.format("[%s] session created (authenticated=%s)",
        session, session.isAuthenticated(), creds));
    return session.isAuthenticated();
  }

  protected abstract boolean permit(Session session, JsonMessage rq);

  protected abstract boolean permit(Session session, AgentID aid);

  @Override
  public boolean permit(JsonMessage rq) {
    final Session session = sessionThreadLocal.get();
    if (session == null) {
      return true;
    }
    return permit(session, rq);
  }

  @Override
  public boolean permit(AgentID aid) {
    final Session session = sessionThreadLocal.get();
    if (session == null) {
      return true;
    }
    return permit(session, aid);
  }

  public static class Session {

    private final Connector connector;
    private final Authentication authentication;

    private Session(Connector connector, Authentication authentication) {
      super();

      this.connector = connector;
      this.authentication = authentication;
    }

    public Connector getConnector() {
      return connector;
    }

    public Authentication getAuthentication() {
      return authentication;
    }

    public boolean isAuthenticated() {
      return authentication.isAuthenticated();
    }

    @Override
    public String toString() {
      if (authentication.getPrincipal() != null) {
        return String.format("(%s) %s", authentication.getPrincipal(), connector);
      } else {
        return String.format("%s", connector);
      }
    }
  }
}
