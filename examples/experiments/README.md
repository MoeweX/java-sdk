# Experiment Overview

## Actor scalability (exp1)

- Git commit: c934bc86 (addReminder must be commented)

Motivation: Check if the overhead of starting actors is linear

Setup:

- Start 10000 actors in a loop
- Each actors executes the doSomething method (does nothing but makes sure it is activated)
- Actor deactivation timeout is 5000 seconds

Result:
- Nothing is written to Redis (expected since we do not modify/store state)
- O(1)

![Exp1](./exp1.png)

## Registration scaleability (exp2)

- Git-commit: c934bc86

Motivation: Check if the overhead of registering reminders and starting actors is linear

Setup:

- Start 10000 actors in a loop
- Each actor registers a reminder on startup that will trigger in 5000 seconds
- Actor deactivation timeout is 5000 seconds

Result:

- Redis has a key for all reminders, values comprises a list with 10000 elements (none are missing)
- Program does not stop properly -> no print-out at the end
- O(n)

![Exp2](./exp2.png)

DemoActorClient log:
```bash
== APP == Starting Actors.
2021/11/10 13:02:09 failed to send the request: Post "http://localhost:9411/api/v2/spans": dial tcp [::1]:9411: connect: connection refused
2021/11/10 13:02:10 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:11 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:12 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:12 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:12 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:12 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:12 failed to send the request: Post "http://localhost:9411/api/v2/spans": read tcp [::1]:49613->[::1]:9411: read: connection reset by peer
2021/11/10 13:02:12 failed to send the request: Post "http://localhost:9411/api/v2/spans": read tcp [::1]:49615->[::1]:9411: read: connection reset by peer
2021/11/10 13:02:13 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:13 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:13 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:13 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:13 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:13 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:13 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:13 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:13 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:13 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:13 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:13 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:13 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:13 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:13 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:13 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:13 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:13 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:14 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:14 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:14 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:14 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:14 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:14 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:14 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:14 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:14 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:14 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:14 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:14 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:14 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:14 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:14 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:14 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:14 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:14 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:14 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:14 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:15 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:15 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:15 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:15 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:15 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:15 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:15 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:15 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:15 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
2021/11/10 13:02:15 failed to send the request: Post "http://localhost:9411/api/v2/spans": EOF
== APP == Done.
```

Repeated the experiment (exp2a) later with 5000 actors and a 10 second brake after 2500 actors to study if this has any effect on latency **Stopping does not change anything**

![Exp2a](./exp2a.png)

## Reminder scalability for deactivated actors that have reminders (exp3)

- Git-commit: 21974a72

Motivation: Check how the total number of reminders affects the scheduling of a single reminder. In other words: while I am adding more and more actors with reminders (which are not triggered), does a single reminder that is continiously triggered being slowed down?

Setup:
- Start 5000 actors in a loop
- The first actors registers a reminder on startup that will trigger each second
- All other actors register a reminder that will trigger in 5000 seconds (took 00:03:19)
- Actor deactivation is set to 2 seconds
- Let it run until 00:05:00

Results:

- Redis has a key for all reminders, values comprises a list with 5000 elements (none are missing)
- Redis has a fields for lastWakeup; version after run is 298
- Latency behaves similar to exp2

![Exp1](./exp3.png)

- Total number of reminders does not affect the scheduling of a single reminder

![Exp1](./exp3-ri.png)

## Findings so far

- Actors that do not have reminders do not slow us down
- Each actor with a reminder makes it more expensive to add new actors. Possible reasons
    - Caused by all reminders in same database key -> using partitioning would fix this
    - Caused by Dapr overloaded -> small brake while adding in exp2 would fix this, does not (exp2a)
- Number of (waiting) reminders in database does not impact reminder execution

If partitioning helps for writes, this means:
- Reminder execution is not slowed down by having many reminders (at least if they are waiting)
- Reminder creation is slowed down by having many reminders, even if they are wating