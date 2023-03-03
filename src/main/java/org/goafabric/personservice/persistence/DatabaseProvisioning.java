package org.goafabric.personservice.persistence;

import io.quarkus.runtime.Quarkus;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.goafabric.personservice.crossfunctional.HttpInterceptor;
import org.goafabric.personservice.persistence.domain.AddressBo;
import org.goafabric.personservice.persistence.domain.PersonBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional
public class DatabaseProvisioning {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final String goals;

    private final PersonRepository personRepository;

    public DatabaseProvisioning(@ConfigProperty(name = "database.provisioning.goals", defaultValue = " ") String goals, PersonRepository personRepository) {
        this.goals = goals;
        this.personRepository = personRepository;
    }

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
                .firstName("Homer").lastName("Simpson")
                .address(createAddress("Evergreen Terrace 1"))
                .build());

        personRepository.save(PersonBo.builder()
                .firstName("Bart").lastName("Simpson")
                .address(createAddress("Everblue Terrace 1"))
                .build());

        personRepository.save(PersonBo.builder()
                .firstName("Monty").lastName("Burns")
                .address(createAddress("Monty Mansion"))
                .build());
    }

    private AddressBo createAddress(String street) {
        return AddressBo.builder()
                .street(street).city("Springfield " + HttpInterceptor.getTenantId())
                .build();
    }

}
