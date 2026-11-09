# docker compose
go to /src/deploy/docker and do "./stack up"

# kafka + schema registry (confluent, json schema)
starts a local kafka (`localhost:9092`, plaintext) and the schema registry (`http://localhost:8081`), the schemas in `src/deploy/docker/schema-registry/schemas` are the event contract and are registered as `person-value` and `address-value` on startup.
it uses the same ports as the kafka of the stack, so don't run both at the same time
```
cd src/deploy/docker/schema-registry && ./stack up     #or ./cstack up for the socktainer container runtime, also: down, restart, pull, update, prune
curl http://localhost:8081/subjects
curl http://localhost:8081/subjects/person-value/versions/latest
```
after changing a schema, register it again (a new version is only created if the schema changed, incompatible changes are rejected)
```
SCHEMA_REGISTRY_URL=http://localhost:8081 ./register-schemas.sh
```
the registry alone as a single command, against the (SASL secured) kafka of the stack
```
docker run -d --name schema-registry -p 8081:8081 --add-host host.docker.internal:host-gateway \
-e SCHEMA_REGISTRY_HOST_NAME=schema-registry \
-e SCHEMA_REGISTRY_LISTENERS=http://0.0.0.0:8081 \
-e SCHEMA_REGISTRY_KAFKASTORE_BOOTSTRAP_SERVERS=host.docker.internal:9093 \
-e SCHEMA_REGISTRY_KAFKASTORE_TOPIC_REPLICATION_FACTOR=1 \
-e SCHEMA_REGISTRY_KAFKASTORE_SECURITY_PROTOCOL=SASL_PLAINTEXT \
-e SCHEMA_REGISTRY_KAFKASTORE_SASL_MECHANISM=PLAIN \
-e 'SCHEMA_REGISTRY_KAFKASTORE_SASL_JAAS_CONFIG=org.apache.kafka.common.security.plain.PlainLoginModule required username="admin" password="supersecret";' \
confluentinc/cp-schema-registry:8.4.0
```

# run native image
container image pull goafabric/person-service-quarkus:$(grep '^version=' gradle.properties | cut -d'=' -f2)
"${(@z)${CRUNTIME:-docker run --pull always}}" --name person-service-quarkus --rm -p 50800:50800 \
-e "quarkus.datasource.jdbc.url=jdbc:postgresql://$(container ls | grep postgres | awk '{print $6}' | cut -d/ -f1):5432/postgres" -e 'quarkus.datasource.username=postgres' -e 'quarkus.datasource.password=postgres' goafabric/person-service-quarkus:$(grep '^version=' gradle.properties | cut -d'=' -f2)