native mode
- lower memory usage in native mode vs spring (20mb vs 100mb)
- much better native image support
- tests are run in native mode by default (so native build is tested)

unified structure
- unified library experience that are verified optimized and verified native 
  - vs different libraries from different vendors (hibernate, openapi, resilience ...)
- quarkus was build from the ground up in 2019, with kubernetes and native in mind, while spring is starting to show its age
- usually one simple solution to achieve a goal (e.g. rest calls) vs multiple confusing solutions (restclient, resttemplate, webclient ...)
- application properties can be cleanly defined for dev only, no surprise that dev defaults are activated in prod
                                    
 
migration path
- rather simple because very similar to spring boot in both annotations and properties (e.g. @inject vs @autowired)
- similar spring data repository solution

where spring still shines
- spring batch for batch processing (e.g. catalogs)
- feature richness of spring data jpa
- kotlin support seems to be better
              

properties
- spring.datasource.url => quarkus.datasource.jdbc.url
- spring.datasource.username/password => quarkus.datasource.username/password
- spring.application.name => quarkus.application.name
- management.opentelemetry.tracing.export.otlp.endpoint => quarkus.otel.exporter.otlp.traces.endpoint
                                                           
- spring.config.locations => quarkus.config.locations
- /actuator/health/liveness => /actuator/health/live
- /actuator/health/readiness => /actuator/health/ready