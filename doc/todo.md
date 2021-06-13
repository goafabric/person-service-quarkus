- Working Integration Tests with H2
- CacheKey with Multi Tenancy Id and own Key Generator

- Security, 
- Resilience, Tracing

- Jasypt, Multi Tenancy
- CalleeService call


-- Solutions

Configuration Props
    https://quarkus.io/guides/config
    @ConfigProperty(name = "greeting.suffix", defaultValue="!")

Logging Props
    kubectl create configmap ex1 --from-file ./application.properties -o yaml > test
    quarkus.log.category.\"org.goafabric\".level=INFO

Security
https://quarkus.io/guides/security-properties

Multi Tenancy
    Statement Inspector: https://github.com/quarkusio/quarkus/issues/12724
    https://quarkus.io/guides/hibernate-orm#setting-up-and-configuring-hibernate-orm-with-a-persistence-xml
	Cache Key Generator: https://github.com/quarkusio/quarkus/issues/17879

Jasypt Properties
    https://github.com/chrisgleissner/microprofile-config-jasypt
    https://quarkus.io/guides/credentials-provider#custom-credentials-provider
    https://www.baeldung.com/java-base64-encode-and-decode

CircuitBreaker
    https://quarkus.io/guides/smallrye-fault-tolerance#adding-resiliency-timeouts

CalleeService
https://quarkus.io/guides/rest-client

@Path("/v2")
@RegisterRestClient
public interface CountriesService {

    @GET
    @Path("/name/{name}")
    Set<Country> getByName(@PathParam String name);
}
...
# Your configuration properties
org.acme.rest.client.CountriesService/mp-rest/url=https://restcountries.eu/rest #
org.acme.rest.client.CountriesService/mp-rest/scope=javax.inject.Singleton #
...

@Inject
@RestClient
@RegisterClientHeaders(RequestUUIDHeaderFactory.class)
CountriesService countriesService;

...
https://stackoverflow.com/questions/58289303/quarkus-rest-client-never-timeouts

org.acme.ExampleClient/mp-rest/url=http://localhost:8081
org.acme.ExampleClient/mp-rest/connectTimeout=5000
org.acme.ExampleClient/mp-rest/readTimeout=5000
                     
