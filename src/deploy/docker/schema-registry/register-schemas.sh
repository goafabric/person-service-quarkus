#!/bin/sh
# registers the json schemas of this folder as <topic>-value (payload) and <topic>-headers (kafka headers), a new version is only created if the schema has changed
# inside compose: runs automatically, on the host: SCHEMA_REGISTRY_URL=http://localhost:8081 ./register-schemas.sh
set -e

URL="${SCHEMA_REGISTRY_URL:-http://schema-registry:8081}"
DIR="$(cd "$(dirname "$0")" && pwd)/schemas"

register() {
  subject="$1"; file="$2"
  schema="$(sed 's/\\/\\\\/g; s/"/\\"/g' "$file" | tr -d '\n')"
  echo "registering $subject"
  curl -sf -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
    --data "{\"schemaType\":\"JSON\",\"schema\":\"$schema\"}" "$URL/subjects/$subject/versions"
  echo
}

register address-value "$DIR/address.json"
register person-value "$DIR/person.json"
register address-headers "$DIR/headers.json"
register person-headers "$DIR/headers.json"
