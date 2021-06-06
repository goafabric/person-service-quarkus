#run native image
docker pull goafabric/person-service-quarkus:1.0.0-SNAPSHOT && docker run --name person-service-quarkus --rm -p50700:50700 goafabric/person-service-quarkus:1.0.0-SNAPSHOT

#run native image arm64
docker run --name person-service-quarkus --rm -p50700:50700 goafabric/person-service-quarkus-arm64v8:1.0.0-SNAPSHOT

