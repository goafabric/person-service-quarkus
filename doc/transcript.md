# intro

- quarkus is a microservice framework established in 2019
- similar to spring boot
- web pages

# demo application
- startup and usage

# layers
- controller (JaxRS)
- logic (applicationscope)

- persistence
- adapter

# kafka

- publisher, consumer, channels

# native images

docker run --pull always --name person-service-native --rm -p50800:50800 goafabric/person-service-native:4.0.5-SNAPSHOT

docker run --pull always --name person-service-quarkus --rm -p50800:50800 \
-e 'quarkus.datasource.jdbc.url=jdbc:postgresql://host.docker.internal:5432/postgres' -e 'quarkus.datasource.username=postgres' -e 'quarkus.datasource.password=postgres' goafabric/person-service-quarkus:3.32.2-SNAPSHOT

memory comparison

# build file


- in general
- openapi, resililience, hibernate
- smallrye libraries, Eclpise Microprofile

- reachability metadata, ApplicationBaseruntimehints

# advantages developer joy

# tests



