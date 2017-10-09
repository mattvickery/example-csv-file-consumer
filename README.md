# sample-csv-file-consumer
Sample CSV -> SI -> SB

A demo of moving between CSV and database entity using Spring Integration to restrict processing until a collection of new files has arrived coupled with Spring Batch to parse, validate and store (JPA) the entities.

Some useful infrastructure code that could be abstracted into a Spring Batch executor utility..
