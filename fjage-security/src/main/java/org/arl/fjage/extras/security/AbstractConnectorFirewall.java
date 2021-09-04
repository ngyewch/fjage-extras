package org.arl.fjage.extras.security;

import org.arl.fjage.AgentID;
import org.arl.fjage.auth.Firewall;
import org.arl.fjage.connectors.Connector;
import org.arl.fjage.extras.security.authentication.AuthenticationManager;
import org.arl.fjage.extras.security.authentication.CredentialsAuthenticationToken;
import org.arl.fjage.extras.security.core.Authentication;
import org.arl.fjage.remote.JsonMessage;

import java.util.logging.Logger;

/**
 * Abstract firewall that handles access per session/connection.
 */
public abstract class AbstractConnectorFirewall
    implements Firewall {

  private final AuthenticationManager authenticationManager;
  private final ThreadLocal<Session> sessionThreadLocal = new ThreadLocal<>();
  private final Logger log = Logger.getLogger(getClass().getName());

  /**
   * Constructs a new AbstractConnectorFirewall.
   *
   * @param authenticationManager Authentication manager.
   */
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

  /**
   * Checks whether a message intended for the specified agent/topic may be sent over this connection/session.
   *
   * @param session Session (non-null, may or may not be authenticated).
   * @param rq      Incoming JSON request.
   * @return <code>true</code> to accept, <code>false</code> to reject.
   */
  protected abstract boolean permit(Session session, JsonMessage rq);

  /**
   * Checks whether a message intended for the specified agent/topic may be sent over this connection/session.
   *
   * @param session Session (non-null, may or may not be authenticated).
   * @param aid     Recipient agent/topic for the message.
   * @return <code>true</code> to accept, <code>false</code> to reject.
   */
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

  /**
   * Connection session.
   */
  public static class Session {

    private final Connector connector;
    private final Authentication authentication;

    private Session(Connector connector, Authentication authentication) {
      super();

      this.connector = connector;
      this.authentication = authentication;
    }

    /**
     * Returns the connector.
     *
     * @return Connector.
     */
    public Connector getConnector() {
      return connector;
    }

    /**
     * Returns the authentication token.
     *
     * @return Authentication.
     */
    public Authentication getAuthentication() {
      return authentication;
    }

    /**
     * Returns <code>true</code> if authenticated, <code>false</code> otherwise.
     *
     * @return <code>true</code> if authenticated, <code>false</code> otherwise.
     */
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
