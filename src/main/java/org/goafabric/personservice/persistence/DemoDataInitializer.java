package org.goafabric.personservice.persistence;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class DemoDataInitializer {

    @Inject
    private PersonRepository personRepository;

    public void run() {
        //if (!personRepository.findAll().iterator().hasNext()) {
            personRepository.save(PersonBo.builder()
                    .firstName("Homer").lastName("Simpson")
                    .build());

            personRepository.save(PersonBo.builder()
                    .firstName("Bart").lastName("Simpson")
                    .build());

            personRepository.save(PersonBo.builder()
                    .firstName("Monty").lastName("Burns")
                    .build());
        //}
    }
}
