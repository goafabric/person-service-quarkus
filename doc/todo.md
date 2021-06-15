- Working Integration Tests with H2
- CacheKey with Multi Tenancy Id and own Key Generator
- Open Points from features.md

-- Solutions

Multi Tenancy
    Interceptor: https://quarkus.io/guides/rest-json#http-filters-and-interceptors
    TenantResolver: https://quarkus.io/guides/hibernate-orm#multitenanc

    Statement Inspector: https://github.com/quarkusio/quarkus/issues/12724
    https://quarkus.io/guides/hibernate-orm#setting-up-and-configuring-hibernate-orm-with-a-persistence-xml
	Cache Key Generator: https://github.com/quarkusio/quarkus/issues/17879

Jasypt Properties
    https://github.com/chrisgleissner/microprofile-config-jasypt
    https://quarkus.io/guides/credentials-provider#custom-credentials-provider
    https://www.baeldung.com/java-base64-encode-and-decode
    str.substring(str.indexOf("[") + 1, str.indexOf("]"));

    https://www.baeldung.com/java-aes-encryption-decryption

