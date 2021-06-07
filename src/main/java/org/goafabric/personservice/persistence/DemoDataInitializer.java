package org.goafabric.personservice.persistence;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional
public class DemoDataInitializer {

    @Inject
    PersonRepository personRepository;

    public void run() {
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
