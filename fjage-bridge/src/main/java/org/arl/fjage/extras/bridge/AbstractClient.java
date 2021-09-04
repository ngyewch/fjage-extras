package org.arl.fjage.extras.bridge;

import org.arl.fjage.*;

import java.util.UUID;
import java.util.concurrent.*;

/**
 * Abstract client for communicating with a specific fjage agent/service.
 */
public abstract class AbstractClient
    implements Client {

  private final Container container;
  private final String recipientAgentName;
  private final Enum<?> service;
  private final Agent proxyAgent;
  private final AgentID proxyAgentID;
  private Integer timeout = null;
  private TimeUnit timeoutUnit = null;

  /**
   * Constructs a new AbstractClient.
   *
   * @param container          fjage container.
   * @param recipientAgentName Recipient agent name (if specified, service is ignored).
   * @param service            Service.
   */
  public AbstractClient(Container container, String recipientAgentName, Enum<?> service) {
    super();

    this.container = container;
    this.recipientAgentName = recipientAgentName;
    this.service = service;

    proxyAgent = new ProxyAgent();
    proxyAgentID = container.add(String.format("clientProxyAgent-%s", UUID.randomUUID()), proxyAgent);
  }

  private AgentID getRecipientAgentID() {
    if (recipientAgentName != null) {
      return proxyAgent.agent(recipientAgentName);
    } else if (service != null) {
      return proxyAgent.agentForService(service);
    } else {
      return null;
    }
  }

  /**
   * Set request timeout.
   *
   * @param timeout Timeout.
   * @param unit    Time unit.
   */
  public void setRequestTimeout(int timeout, TimeUnit unit) {
    this.timeout = timeout;
    this.timeoutUnit = unit;
  }

  /**
   * Performs an asynchronous request.
   *
   * @param req Request.
   * @return Message future.
   */
  protected Future<Message> doAsyncRequest(Message req) {
    req.setSender(proxyAgentID);
    req.setRecipient(getRecipientAgentID());
    return new RequestFuture(req);
  }

  /**
   * Performs a send operation.
   *
   * @param msg Message.
   */
  protected void doSend(Message msg) {
    msg.setSender(proxyAgentID);
    msg.setRecipient(getRecipientAgentID());
    container.send(msg);
  }

  /**
   * Performs a synchronous request.
   *
   * @param req  Request.
   * @param type Expected response message type.
   * @param <T>  Response message type.
   * @return Result.
   * @throws InterruptedException If the synchronous request was interrupted.
   * @throws TimeoutException     If the synchronous request timed out.
   */
  protected <T extends Message> Result<T> doRequest(Message req, Class<T> type)
      throws InterruptedException, TimeoutException {
    final Future<Message> requestFuture = doAsyncRequest(req);
    try {
      if ((timeout != null) && (timeoutUnit != null)) {
        return postProcess(requestFuture.get(timeout, timeoutUnit), type);
      } else {
        return postProcess(requestFuture.get(), type);
      }
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Performs a synchronous request.
   *
   * @param req          Request.
   * @param performative Expected performative.
   * @return Result.
   * @throws InterruptedException If the synchronous request was interrupted.
   * @throws TimeoutException     If the synchronous request timed out.
   */
  protected Result<Message> doRequest(Message req, Performative performative)
      throws InterruptedException, TimeoutException {
    final Future<Message> requestFuture = doAsyncRequest(req);
    try {
      if ((timeout != null) && (timeoutUnit != null)) {
        return postProcess(requestFuture.get(timeout, timeoutUnit), performative);
      } else {
        return postProcess(requestFuture.get(), performative);
      }
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  private <T extends Message> Result<T> postProcess(Message rsp, Class<T> type) {
    if (type.isInstance(rsp)) {
      return Result.ok((T) rsp);
    } else if (rsp == null) {
      return Result.err(null);
    } else {
      return Result.err(rsp.getPerformative());
    }
  }

  private Result<Message> postProcess(Message rsp, Performative performative) {
    if (rsp == null) {
      return Result.err(null);
    } else if (rsp.getPerformative() == ((performative != null) ? performative : Performative.AGREE)) {
      return Result.ok(rsp);
    } else {
      return Result.err(rsp.getPerformative());
    }
  }

  private static class ProxyAgent
      extends Agent {

    @Override
    protected void init() {
      super.init();

      add(new TickerBehavior(1000, () -> {
        // do nothing
      }));
    }
  }

  private class RequestFuture
      implements Future<Message> {

    private final MessageListener messageListener;
    private final Object lock = new Object();
    private boolean done = false;
    private boolean cancelled = false;
    private Message rsp = null;

    public RequestFuture(Message req) {
      super();

      messageListener = new MessageListener() {

        @Override
        public boolean onReceive(Message msg) {
          if (req.getMessageID().equals(msg.getInReplyTo())) {
            rsp = msg;
            container.removeListener(this);
            done = true;
            synchronized (lock) {
              lock.notifyAll();
            }
            return true;
          }
          return false;
        }
      };
      container.addListener(messageListener);
      container.send(req);
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
      container.removeListener(messageListener);
      cancelled = true;
      done = true;
      synchronized (lock) {
        lock.notifyAll();
      }
      return true;
    }

    @Override
    public boolean isCancelled() {
      return cancelled;
    }

    @Override
    public boolean isDone() {
      return done;
    }

    @Override
    public Message get()
        throws InterruptedException {
      if (cancelled) {
        throw new CancellationException();
      }
      if (done) {
        return rsp;
      }
      synchronized (lock) {
        lock.wait();
      }
      if (cancelled) {
        throw new CancellationException();
      }
      return rsp;
    }

    @Override
    public Message get(long timeout, TimeUnit unit)
        throws InterruptedException, TimeoutException {
      if (cancelled) {
        throw new CancellationException();
      }
      if (done) {
        return rsp;
      }
      synchronized (lock) {
        lock.wait(unit.toMillis(timeout));
      }
      if (cancelled) {
        throw new CancellationException();
      }
      if (done) {
        return rsp;
      }
      throw new TimeoutException();
    }
  }
}
