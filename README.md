# docker compose
go to /src/deploy/docker and do "./stack up"

# run native image
POSTGRES_IP=$(container inspect postgres | jq -r '.[0].networks[0].ipv4Address | split("/")[0]')

"${(@z)${CRUNTIME:-docker run --pull always}}" --name person-service-quarkus --rm -p 50800:50800 \
-e "quarkus.datasource.jdbc.url=jdbc:postgresql://${POSTGRES_IP}:5432/postgres" -e 'quarkus.datasource.username=postgres' -e 'quarkus.datasource.password=postgres' goafabric/person-service-quarkus:$(grep '^version=' gradle.properties | cut -d'=' -f2)
