package org.goafabric.personservice.logic;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.goafabric.personservice.adapter.CalleeServiceAdapter;
import org.goafabric.personservice.crossfunctional.DurationLog;
import org.goafabric.personservice.persistence.PersonRepository;
import org.goafabric.personservice.service.Person;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
@DurationLog
public class PersonLogic {
    @Inject
    PersonMapper personMapper;

    @Inject
    PersonRepository personRepository;

    @Inject
    @RestClient
    CalleeServiceAdapter calleeServiceAdapter;

    public Person getById(String id) {
        return personMapper.map(
                personRepository.findById(id));
    }

    public List<Person> findAll() {
        return personMapper.map(
                personRepository.findAll().list());
    }

    public List<Person> findByFirstName(String firstName) {
        return personMapper.map(
                personRepository.findByFirstName(firstName));
    }

    public List<Person> findByLastName(String lastName) {
        return personMapper.map(
                personRepository.findByLastName(lastName));
    }

    public Person save(Person person) {
        return personMapper.map(
                personRepository.save(personMapper.map(person)));
    }

    public Person sayMyName(String name) {
        return Person.builder().firstName(
                calleeServiceAdapter.sayMyName(name).getMessage()).build();
    }
}
