# docker compose
go to /src/deploy/docker and do "./stack up"

# run native image
docker run --pull always --name person-service-quarkus --rm -p50800:50800 goafabric/person-service-quarkus:$(grep '^version=' gradle.properties | cut -d'=' -f2)

# run native image arm64
docker run --pull always --name person-service-quarkus --rm -p50800:50800 goafabric/person-service-quarkus-arm64v8:$(grep '^version=' gradle.properties | cut -d'=' -f2)

