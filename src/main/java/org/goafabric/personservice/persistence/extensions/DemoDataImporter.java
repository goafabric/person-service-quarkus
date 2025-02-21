/*
package org.goafabric.personservice.persistence.extensions;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.goafabric.personservice.controller.dto.Address;
import org.goafabric.personservice.controller.dto.Person;
import org.goafabric.personservice.extensions.TenantContext;
import org.goafabric.personservice.logic.PersonLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@ApplicationScoped
public class DemoDataImporter {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final String goals;

    private final String tenants;

    private final PersonLogic personLogic;


    public DemoDataImporter(@ConfigProperty(name = "database.provisioning.goals") String goals, @ConfigProperty(name = "multi-tenancy.tenants")String tenants, PersonLogic personLogic) {
        this.goals = goals;
        this.tenants = tenants;
        this.personLogic = personLogic;
    }

    public void run() {
        if (goals.contains("-import-demo-data")) {
            log.info("Importing demo data ...");
            importDemoData();
            log.info("Demo data import done ...");
        }

        if (goals.contains("-terminate")) {
            log.info("Terminating app ...");
            System.exit(0);
        }
    }

    private void importDemoData() {
        Arrays.asList(tenants.split(",")).forEach(tenant -> {
            TenantContext.setTenantId(tenant);
            if (personLogic.findAll().isEmpty()) {
                insertData();
            }
        });
        TenantContext.setTenantId("0");
    }

    private void insertData() {
        IntStream.range(0, 1).forEach(i -> {
            personLogic.save(new Person(null, null, "Homer", "Simpson"
                    , List.of(createAddress("Evergreen Terrace No. " + i))));

            personLogic.save(new Person(null, null, "Bart", "Simpson"
                    , List.of(createAddress("Everblue Terrace No. " + i))));

            personLogic.save(new Person(null, null, "Monty", "Burns"
                    , List.of(createAddress("Mammon Street No. 1000 on the corner of Croesus"))));
        });

    }

    private Address createAddress(String street) {
        return new Address(null, null, street, "Springfield " + TenantContext.getTenantId());
    }

}


 */