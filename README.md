#docker compose
go to /src/deploy/docker and do "./stack up"

#run native image
docker pull goafabric/person-service-quarkus:1.2.3 && docker run --name person-service-quarkus --rm -p50800:50800 goafabric/person-service-quarkus:1.2.3

#run native image arm64
docker run --name person-service-quarkus --rm -p50800:50800 goafabric/person-service-quarkus-arm64v8:1.2.3

