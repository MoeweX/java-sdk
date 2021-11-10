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

![Exp1](./exp2.png)

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

## Reminder scalability for deactivated actors that have reminders (exp3)

- Git-commit: XXX

Motivation: Check how the total number of reminders affects the scheduling of a single reminder. In other words: while I am adding more and more actors with reminders (which are not triggered), does a single reminder that is continiously triggered being slowed down?

Setup:
- Start 5000 actors in a loop
- The first actors registers a reminder on startup that will trigger each second
- All other actors register a reminder that will trigger in 5000 seconds (took 00:03:19)
- Actor deactivation is set to 2 seconds
- Let it run until 00:05:00

Results:

- Redis has a key for all reminders, values comprises a list with 10000 elements (none are missing)
- Redis has a fields for lastWakeup; version after run is 298
- Latency behaves similar to exp2

![Exp1](./exp3.png)

- Total number of reminders does not affect the scheduling of a single reminder

![Exp1](./exp3-ri.png)