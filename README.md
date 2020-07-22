# Project-k

This is a proof of concept implementation of a bigger project; it currently supports only a simple login/logout workflow.

# How to run this service

The contents of the full build is published to dockerhub as [dripolles/project-k](https://hub.docker.com/repository/docker/dripolles/project-k).

The image can be run without further configuration; you can try it in the command line with

```
docker run --rm dripolles/project-k:latest
```

This will start the server and listen on port 8080. Open http://localhost:8080 to see it in action.

Currently, there are two test users, with their corresponding passwords:

```
user1 password1
user2 password2
```

# Project structure

The code is written in Kotlin, using Gradle as the build tool. It is conceived as a "classic" web application, where the server uses templates to generate the HTML that will be sent to the client, and implements the necessary endpoints to provide the functionality.

[Ktor applications](https://ktor.io/servers/application.html) use a combination modules to define the endpoints, and features to enable extra functionalities. In this POC, there are only three modules defined:
* Login: provides login & logout functionality. Deals with setting and deleting the session used for authorization.
* Home: just a placeholder "home page", that only works for logged in users.
* Statics: this module is in charge of defining where are the static files (such as CSS).

I have used [Kodein DI](https://kodein.org/di/) to provide dependency injection in a "non intrusive" way. That is, the classes that participate in the injection don't have any dependency on the injector itself. This keeps them a bit simpler and makes testing straightforward.

In the places where "placeholder" classes have been used (such as the "fixed users" validation), the code is designed to be able to replace them with proper implementations seamlessly.

Tests use [JUnit5](https://junit.org/junit5/docs/current/user-guide/), the [Ktor test engine](https://ktor.io/servers/testing.html) and the [Mockk](https://mockk.io/) library for mocks. Note that this library does not need everything to be an interface to be able to replace it with a mock or stub, so the code does not create "extra" interfaces just to be able to replace them in tests.

The client side of the app is extemely barebones, and the style is very unpolished. This is something that should be improved as soon as possible.

## Future developments

There are a few things that I'd like to have but haven't included for lack of time:

* "Real" styling and not just a tiny CSS doing the bare minimum.
* Experiment with a JS framework like Vue, using webpack to generate the files and serving them as statics.
* Better integration of coverage automation; I haven't got to configure it properly yet.
