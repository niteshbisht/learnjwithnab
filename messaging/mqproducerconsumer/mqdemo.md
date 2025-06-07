## Podman steps for MQ/Active MQ
step1: podman pull apache/activemq-classic
step2: podman run -d --name activemq -p 8161:8161 -p 61616:61616 apache/activemq-classic

## Rabbit MQ
podman pull rabbitmq:3-management
podman run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management


links:
https://www.svix.com/resources/guides/rabbitmq-docker-setup-guide/
