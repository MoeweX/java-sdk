/*
 * Copyright (c) Microsoft Corporation and Dapr Contributors.
 * Licensed under the MIT License.
 */

package io.dapr.examples.actors;

import io.dapr.actors.ActorId;
import io.dapr.actors.client.ActorClient;
import io.dapr.actors.client.ActorProxyBuilder;

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
  public static void main(String[] args) throws InterruptedException {
    if (args.length == 1) {
      NUM_ACTORS = Integer.parseInt(args[0]);
    }

    try (ActorClient client = new ActorClient()) {
      ActorProxyBuilder<DemoActor> builder = new ActorProxyBuilder(DemoActor.class, client);
      List<Thread> threads = new ArrayList<>(NUM_ACTORS);

      // Creates multiple actors.
      for (int i = 0; i < NUM_ACTORS; i++) {
        ActorId actorId = ActorId.createRandom();
        DemoActor actor = builder.build(actorId);
        long nanos = System.nanoTime();
        actor.registerReminder();
        System.out.println("Actor registered");
        System.out.println(System.nanoTime() - nanos);
      }

      System.out.println("Done.");
    }
  }
}
