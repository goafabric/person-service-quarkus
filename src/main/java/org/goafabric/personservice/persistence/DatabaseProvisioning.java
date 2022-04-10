package org.goafabric.personservice.persistence;

import io.quarkus.runtime.Quarkus;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.goafabric.personservice.crossfunctional.HttpInterceptor;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional
@Slf4j
public class DatabaseProvisioning {
    @ConfigProperty(name = "database.provisioning.goals", defaultValue = " ")
    String goals;

    @Inject
    PersonRepository personRepository;

    public void run() {
        if (goals.contains("-import-demo-data")) {
            log.info("Importing demo data ...");
            importDemoData();
        }

        if (goals.contains("-terminate")) {
            log.info("Terminating app ...");
            Quarkus.asyncExit();
        }
    }

    public void importDemoData() {
        if (personRepository.findAll().list().isEmpty()) {
            HttpInterceptor.setTenantId("0");
            insertData();
            HttpInterceptor.setTenantId("5a2f");
            insertData();        }
    }

    private void insertData() {
        personRepository.save(PersonBo.builder()
                .firstName("Homer").lastName("Simpson").secret("SuperSecret")
                .build());

        personRepository.save(PersonBo.builder()
                .firstName("Bart").lastName("Simpson").secret("SuperSecret")
                .build());

        personRepository.save(PersonBo.builder()
                .firstName("Monty").lastName("Burns").secret("SuperSecret")
                .build());
    }
}
