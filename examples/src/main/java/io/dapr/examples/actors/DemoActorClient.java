/*
 * Copyright (c) Microsoft Corporation and Dapr Contributors.
 * Licensed under the MIT License.
 */

package io.dapr.examples.actors;

import io.dapr.actors.ActorId;
import io.dapr.actors.client.ActorClient;
import io.dapr.actors.client.ActorProxyBuilder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Client for Actor runtime to invoke actor methods.
 * 1. Build and install jars:
 * mvn clean install
 * 2. cd to [repo-root]/examples
 * 3. Run the client:
 * dapr run --components-path ./components/actors --app-id demoactorclient -- java -jar \
 * target/dapr-java-sdk-examples-exec.jar io.dapr.examples.actors.DemoActorClient
 */
public class DemoActorClient {

  private static int NUM_ACTORS = 3;

  /**
   * The main method.
   * @param args Input arguments (unused).
   * @throws InterruptedException If program has been interrupted.
   */
  public static void main(String[] args) throws InterruptedException, IOException {
    if (args.length == 1) {
      NUM_ACTORS = Integer.parseInt(args[0]);
    }

    try (ActorClient client = new ActorClient()) {
      ActorProxyBuilder<DemoActor> builder = new ActorProxyBuilder(DemoActor.class, client);
      FileWriter writer = new FileWriter("./experiment-latency.csv");
      writer.write("actorId;timeNs\n");

      System.out.println("Starting Actors.");

      // create single actor that is continioulsy doing something
      long nanos = System.nanoTime();
      ActorId actorId = new ActorId("Actor-0");
      DemoActor actor = builder.build(actorId);
      actor.doSomething();
      actor.registerReminder(1);
      long time = System.nanoTime() - nanos;

      writer.write(actorId + ";" + time + "\n");

      // Creates multiple actors.
      for (int i = 1; i < NUM_ACTORS; i++) {
        nanos = System.nanoTime();
        actorId = new ActorId("Actor-" + i);
        actor = builder.build(actorId);
        actor.doSomething();
        actor.registerReminder(5000);
        time = System.nanoTime() - nanos;

        writer.write(actorId + ";" + time + "\n");
      }

      writer.flush();
      writer.close();
      System.out.println("Done.");
    }
  }
}
