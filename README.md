# docker compose
go to /src/deploy/docker and do "./stack up"

# run native image
docker run --pull always --name person-service-quarkus --rm -p50800:50800 \
-e 'quarkus.datasource.jdbc.url=jdbc:postgresql://host.docker.internal:5432/postgres' -e 'quarkus.datasource.username=postgres' -e 'quarkus.datasource.password=postgres' goafabric/person-service-quarkus:$(grep '^version=' gradle.properties | cut -d'=' -f2)
