# Tools Used
1. Podman for ActiveMQ: for spinning up ActiveMQ server
2. H2 Database on disk : for storing order data

## Frameworks and Libraries
1. Spring Boot Web : for building web applications/rest apis
2. Spring Boot Data JPA: for database access and reducing boilerplate code
3. Spring Boot JMS with ActiveMQ: for messaging platform
4. Gradle build tool : for building and managing dependencies
5. Swagger: for API documentation
6. Lombok: for reducing boilerplate code getters and setters

## Steps to create the projects
Step1: Create skeleton project with Spring initializer.
Step2: Add dependencies in spring initializer project for spring messaging activemq,
        Spring data jpa, lombok and h2 database.
Step3: Install Podman and run the ActiveMQ classic server and expose the ports appropriately
Step4: Add configuration for ActiveMQ in application.properties
Step5: Add configuration for H2 database in application.properties
Step6: Add annotations for JPA Entities and JPA Repositories
Step7: Create Order Entity Object
Step8: Create Order Entity Repository
Step9: Create JMS listener for consuming messages from ActiveMQ and Add code to save order to database
Step10: Create Order Controller with REST APIs for creating order by using jms client to send the message to Active MQ.

