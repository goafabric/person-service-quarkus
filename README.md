#run native image
docker pull goafabric/callee-service-quarkus:1.0.1-SNAPSHOT && docker run --name calle-service-quarkus --rm -p50900:50900 goafabric/callee-service-quarkus:1.0.1-SNAPSHOT

#run native image arm64
docker run --name calle-service-quarkus --rm -p50900:50900 goafabric/callee-service-quarkus-arm64v8:1.0.1-SNAPSHOT

