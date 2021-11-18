/*
 * Copyright (c) Microsoft Corporation and Dapr Contributors.
 * Licensed under the MIT License.
 */

package io.dapr.examples.actors;

import io.dapr.actors.ActorId;
import io.dapr.actors.runtime.AbstractActor;
import io.dapr.actors.runtime.ActorRuntimeContext;
import io.dapr.actors.runtime.Remindable;
import io.dapr.utils.TypeRef;
import reactor.core.publisher.Mono;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Implementation of the DemoActor for the server side.
 */
public class DemoActorImpl extends AbstractActor implements DemoActor, Remindable<Integer> {

  /**
   * Format to output date and time.
   */
  private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

  /**
   * This is the constructor of an actor implementation, while also registering a timer.
   * @param runtimeContext The runtime context object which contains objects such as the state provider.
   * @param id             The id of this actor.
   */
  public DemoActorImpl(ActorRuntimeContext runtimeContext, ActorId id) {
    super(runtimeContext, id);
  }

  /**
   * Registers a reminder.
   */
  @Override
  public void registerReminder(String name) {
    super.registerReminder(
      name,
      (int) (Integer.MAX_VALUE * Math.random()),
      Duration.ofSeconds(10),
      Duration.ofMinutes(50)).block();
  }

  /**
   * Prints a message and appends the timestamp.
   * @param something Something to be said.
   * @return What was said appended with timestamp.
   */
  @Override
  public String say(String something) {
    Calendar utcNow = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    String utcNowAsString = DATE_FORMAT.format(utcNow.getTime());

    // Handles the request by printing message.
    System.out.println("Server say method for actor "
        + super.getId() + ": "
        + (something == null ? "" : something + " @ " + utcNowAsString));

    super.getActorStateManager().set("lastmessage", something).block();

    // Now respond with current timestamp.
    return utcNowAsString;
  }

  /**
   * Increments a persistent counter, saves and returns its updated value.
   * Example of method implemented with Reactor's Mono class.
   * This method could be rewritten with blocking calls in Mono, using block() method:
   *
   * <p>public int incrementAndGet(int delta) {
   *   int counter = 0;
   *   if (super.getActorStateManager().contains("counter").block()) {
   *     counter = super.getActorStateManager().get("counter", int.class).block();
   *   }
   *   counter = counter + 1;
   *   super.getActorStateManager().set("counter", counter).block();
   *   return counter;
   * }</p>
   * @param delta Amount to be added to counter.
   * @return Mono response for the incremented value.
   */
  @Override
  public Mono<Integer> incrementAndGet(int delta) {
    return super.getActorStateManager().contains("counter")
        .flatMap(exists -> exists ? super.getActorStateManager().get("counter", int.class) : Mono.just(0))
        .map(c -> c + delta)
        .flatMap(c -> super.getActorStateManager().set("counter", c).thenReturn(c));
  }

  /**
   * Method invoked by timer.
   * @param message Message to be printed.
   */
  @Override
  public void clock(String message) {
    Calendar utcNow = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    String utcNowAsString = DATE_FORMAT.format(utcNow.getTime());

    // Handles the request by printing message.
    System.out.println("Server timer for actor "
        + super.getId() + ": "
        + (message == null ? "" : message + " @ " + utcNowAsString));
  }

  /**
   * Method used to determine reminder's state type.
   * @return Class for reminder's state.
   */
  @Override
  public TypeRef<Integer> getStateType() {
    return TypeRef.INT;
  }

  /**
   * Method used be invoked for a reminder.
   * @param reminderName The name of reminder provided during registration.
   * @param state        The user state provided during registration.
   * @param dueTime      The invocation due time provided during registration.
   * @param period       The invocation period provided during registration.
   * @return Mono result.
   */
  @Override
  public Mono<Void> receiveReminder(String reminderName, Integer state, Duration dueTime, Duration period) {
    try {
      Thread.sleep(10);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return Mono.fromRunnable(() -> {
      if (reminderName.equals("silent")) {
        return;
      }

      Calendar utcNow = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
      System.out.println("Actor reminded: " + utcNow.get((Calendar.MINUTE)) + ":" + utcNow.get((Calendar.SECOND)) + "," + utcNow.get(Calendar.MILLISECOND));

      primeNumbersBruteForce(10000000);

      utcNow = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
      System.out.println("Actor finished: " + utcNow.get((Calendar.MINUTE)) + ":" + utcNow.get((Calendar.SECOND)) + "," + utcNow.get(Calendar.MILLISECOND));
    });
  }

  public static List<Integer> primeNumbersBruteForce(int n) {
    List<Integer> primeNumbers = new LinkedList<>();
    if (n >= 2) {
      primeNumbers.add(2);
    }
    for (int i = 3; i <= n; i += 2) {
      if (isPrimeBruteForce(i)) {
        primeNumbers.add(i);
      }
    }
    return primeNumbers;
  }
  private static boolean isPrimeBruteForce(int number) {
    for (int i = 2; i*i <= number; i++) {
      if (number % i == 0) {
        return false;
      }
    }
    return true;
  }
}
