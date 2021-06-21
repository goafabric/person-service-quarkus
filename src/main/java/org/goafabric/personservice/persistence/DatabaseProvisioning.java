package org.goafabric.personservice.persistence;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

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
            log.info("Importing demo data");
            importDemoData();
        }
    }

    private void importDemoData() {
        if (personRepository.findAll().list().isEmpty()) {
            personRepository.save(PersonBo.builder()
                    .firstName("Homer").lastName("Simpson")
                    .build());

            personRepository.save(PersonBo.builder()
                    .firstName("Bart").lastName("Simpson")
                    .build());

            personRepository.save(PersonBo.builder()
                    .firstName("Monty").lastName("Burns")
                    .build());
        }
    }
}
