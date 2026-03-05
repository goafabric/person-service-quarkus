# top advantages
- quarkus was build from the ground up in 2019, with kubernetes and strong native support in mind
- lower memory usage during build and runtime
- dev-ui that features "kafka-ui", database lookup ...

# native mode
- lower memory usage in native mode vs spring (20mb vs 100mb)
- much better native image support
- (remote) tests can be  run in native mode by rather @easily with @QuarkusIntegrationTest
- much lower memory during build time (4G vs 7G) and faster builds

# unified structure
- quarkus was build from the ground up in 2019, with kubernetes and native in mind, while spring is starting to show its age

- unified library experience that are verified optimized and verified native 
  - vs different libraries from different vendors (hibernate, openapi, resilience ...)
  
- usually one simple solution to achieve a goal (e.g. rest calls) vs multiple confusing solutions (restclient, resttemplate, webclient ...)
- application properties can be cleanly defined for dev only, no surprise that dev defaults are activated in prod

# developer support
- dev-ui that features internal "kafka-ui", database lookup etc...
- if desired can automatically start (dev) containers for kafka, postgres ... 
              
# adoption
- already widely adopted within the company

# issue reaction time
- https://github.com/quarkusio/quarkus/issues/52524 (resolved 48 hours) vs https://github.com/spring-projects/spring-framework/issues/36001 (open for 2  months)

# migration path
- rather simple because very similar to spring boot in both annotations and properties (e.g. @inject vs @autowired, @Produces vs @Bean)
- similar spring data repository solution

# where spring still shines
- spring batch for batch processing (e.g. catalogs)
- feature richness of spring data jpa
- kafka support in spring is a little more dynamic
                                 




operational properties
- spring.datasource.url => quarkus.datasource.jdbc.url
- spring.datasource.username/password => quarkus.datasource.username/password
- spring.application.name => quarkus.application.name
- management.opentelemetry.tracing.export.otlp.endpoint => quarkus.otel.exporter.otlp.traces.endpoint
                                                           
- spring.config.locations => quarkus.config.locations
- /actuator/health/liveness => /actuator/health/live
- /actuator/health/readiness => /actuator/health/ready