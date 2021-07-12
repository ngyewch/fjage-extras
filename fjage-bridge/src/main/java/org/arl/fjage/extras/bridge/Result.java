package org.arl.fjage.extras.bridge;

import org.arl.fjage.Message;
import org.arl.fjage.Performative;

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

  public T getMessage() {
    return message;
  }

  public Performative getPerformative() {
    return performative;
  }

  public boolean isOk() {
    return performative == null;
  }

  public boolean isErr() {
    return performative != null;
  }

  public static <T extends Message> Result<T> ok(T message) {
    return new Result<T>(message);
  }

  public static <T extends Message> Result<T> err(Performative performative) {
    return new Result<T>(performative);
  }
}
