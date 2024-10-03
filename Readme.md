# AWS Redshift performance comparison

AWS Redshift is a fully managed data warehouse service in the cloud, optimized for running complex queries on large datasets. It enables scalable, high-performance data analysis and integrates seamlessly with AWS tools and services.

AWS Redshift supports two APIs to retrieve/manipulate data:

1. **JDBC API**: The JDBC (Java Database Connectivity) API allows direct connections to Redshift, enabling standard SQL queries and database operations. It is a widely-used protocol for traditional database access.
   
2. **Data API**: AWS Redshift Data API is a REST-based API that provides a simple way to run queries without managing persistent connections. It’s useful for applications that need to query Redshift without the overhead of JDBC configuration.

The project aims to evaluate the performance implications of using either the JDBC API or the Data API for interacting with Redshift.

## Prerequisites

- JDK 17
- Docker 
- Jmeter

## Sample applications

This project evaluates two sample applications written in Java(JDK 17) with SpringBoot(v3.3.4). 

1. `redshift-jdbc-demo`: application using JDBC API
2. `redshift-dataapi-demo`: application using Data API

### Building and running the applications

1. Update the configurations in the relevant `application.properties` file.

2. Go into the application directory and run the following command.

```sh
    ./mvnw spring-boot:build-image
```

3. Run the docker container.

```sh
    docker run -p 9080:8080 --cpus="2" --memory="8g" -d <docker-image>
```

**Note**
Following are the docker images for respective applications:
- redshift-jdbc-demo : `sample/redshift-jdbc-demo:0.0.1-SNAPSHOT`
- redshift-dataapi-demo : `sample/redshift-dataapi-demo:0.0.1-SNAPSHOT`

## Running the test

Execute the Jmeter script located in `jmeter` directory using following command.

```sh
    jmeter -n -t jmeter/aws-redshift-tests.jmx -l results/result.jtl -Jusers="<number-of-concurrent-users>" -Jduration=<duration-in-seconds> -Jport=9080
```
