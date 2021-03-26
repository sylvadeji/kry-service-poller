# kry-service-poller

##PROBLEM

As a part of scaling the number of services running within a modern health
tech company we need a way to make sure that all are running smoothly.
None of the tools that we have been looking for are quite doing the right
thing for us so we decided that we need to build it ourselves. What we
want you to do is to build a simple service poller that keeps a list of
services (defined by a URL), and periodically does a HTTP GET to each and
saves the response ("OK" or "FAIL"). Apart from the polling logic we want to
have all the services visualised and easily managed in a basic UI presenting
the all services together with their status.

##SOLUTION
1. Implemented a Rest Service using Spring Boot for CRUD operation and also a Spring MVC for the UI.
2. Spring Scheduler with a delay for polling services


#EXTRA NOTE

This would have been totally completed awesomely if there is more time.
