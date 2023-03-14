package org.goafabric.personservice.logic;

import lombok.NonNull;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.goafabric.personservice.adapter.CalleeServiceAdapter;
import org.goafabric.personservice.controller.dto.Person;
import org.goafabric.personservice.crossfunctional.DurationLog;
import org.goafabric.personservice.persistence.PersonRepository;
import org.goafabric.personservice.persistence.domain.PersonBo;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
@DurationLog
public class PersonLogic {
    private final PersonMapper personMapper;

    private final PersonRepository personRepository;

    private final CalleeServiceAdapter calleeServiceAdapter;

    public PersonLogic(PersonMapper personMapper, PersonRepository personRepository, @RestClient CalleeServiceAdapter calleeServiceAdapter) {
        this.personMapper = personMapper;
        this.personRepository = personRepository;
        this.calleeServiceAdapter = calleeServiceAdapter;
    }

    public Person getById(String id) {
        return personMapper.map(
                (PersonBo) personRepository.findById(id).firstResult());
    }

    public List<Person> findAll() {
        return personMapper.map(
                personRepository.findAll().list());
    }

    public List<Person> findByFirstName(@NonNull String firstName) {
        return personMapper.map(
                personRepository.findByFirstName(firstName));
    }

    public List<Person> findByLastName(@NonNull String lastName) {
        return personMapper.map(
                personRepository.findByLastName(lastName));
    }

    public long countByLastName(@NonNull String lastName) {
        return personRepository.countByLastName(lastName);
    }

    public Person save(@NonNull Person person) {
        return personMapper.map(
                personRepository.save(personMapper.map(person)));
    }

    public Person sayMyName(@NonNull String name) {
        return new Person(null,
                calleeServiceAdapter.sayMyName(name).message(), "", null);    }
}
