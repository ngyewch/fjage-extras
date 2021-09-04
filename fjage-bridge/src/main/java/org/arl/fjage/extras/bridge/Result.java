package org.arl.fjage.extras.bridge;

import org.arl.fjage.Message;
import org.arl.fjage.Performative;

/**
 * Result of a fjage request.
 *
 * @param <T> Expected response message type.
 */
public class Result<T extends Message> {

  private final T message;
  private final Performative performative;

  private Result(T message) {
    super();

    this.message = message;
    this.performative = null;
  }

  private Result(Performative performative) {
    super();

    this.message = null;
    this.performative = performative;
  }

  /**
   * Returns the message.
   *
   * @return The message, <code>null</code> if not available.
   */
  public T getMessage() {
    return message;
  }

  /**
   * Returns the performative.
   *
   * @return The performative, <code>null</code> if not available.
   */
  public Performative getPerformative() {
    return performative;
  }

  /**
   * Returns <code>true</code> the request received an expected response.
   *
   * @return <code>true</code> the request received am expected response, <code>false</code> otherwise.
   */
  public boolean isOk() {
    return performative == null;
  }

  /**
   * Returns <code>true</code> the request did not receive an expected response.
   *
   * @return <code>true</code> the request did not receive an expected response, <code>false</code> otherwise.
   */
  public boolean isErr() {
    return performative != null;
  }

  /**
   * Instantiates an OK result.
   *
   * @param message Message.
   * @param <T>     Message type.
   * @return The instantiated result.
   */
  public static <T extends Message> Result<T> ok(T message) {
    return new Result<T>(message);
  }

  /**
   * Instantiates an error result.
   *
   * @param performative Performative.
   * @param <T>          Message type.
   * @return The instantiated result.
   */
  public static <T extends Message> Result<T> err(Performative performative) {
    return new Result<T>(performative);
  }
}
