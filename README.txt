Note:
Task definition states double is used as data type for money values. This lead to some data lost since double doesn't handle values like 0.1 well.
I raised this concert to Linda Post when she shared this task with me but she didn't reply.
If we need to preserve precision we should go with BigDecimal type but it will increase memory and performance consumption.
Since I didn't receive any reply I went with what was requested.

Run "gradle build" command to build the project and run unit tests.
Run "gradle integrationTest" command to run integration tests.
Run "java -jar build/libs/statistics-1.0-SNAPSHOT.jar" command to run the application.

TODO:
1 Improve parallelization of work. Now all business logic is formally single threaded because of synchronized blocks in TimedStatisticsCalculator.
I left it as is for now (kinda low priority) since there is no requirement for this.
Solution is to leave purging only for GET request. In this case POST will only write data to the end of the queue and GET will purge date from the beginning of the queue.
It's possible to separate both parties from each other and let them work at the same time.
The problem of this solution is that it will slow down GET requests and this was the biggest concern in task definition.