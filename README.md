# example-csv-file-consumer
Example: Spring Boot, CSV -> Spring Integration -> Spring Batch

An example of a Spring Boot application moving a CSV into a database entity using Spring Integration to restrict processing until a collection of new CSV files has arrived coupled with Spring Batch to parse, validate and store (JPA) the entities.

Database: H2
