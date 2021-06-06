package org.goafabric.personservice.logic;

import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import org.goafabric.personservice.persistence.PersonRepository;
import org.goafabric.personservice.service.Person;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public class PersonLogic {
    @Inject
    PersonMapper personMapper;

    @Inject
    PersonRepository personRepository;

    public Person getById(String id) {
        return personMapper.map(
                personRepository.findById(id));
    }

    public List<Person> findAll() {
        return personMapper.map(
                personRepository.findAll().list());
    }

    @CacheResult(cacheName = "persons")
    public List<Person> findByFirstName(String firstName) {
        return personMapper.map(
                personRepository.findByFirstName(firstName));
    }

    @CacheResult(cacheName = "persons")
    public List<Person> findByLastName(String lastName) {
        return personMapper.map(
                personRepository.findByLastName(lastName));
    }

    @CacheInvalidateAll(cacheName = "persons")
    public Person save(Person person) {
        return personMapper.map(
                personRepository.save(personMapper.map(person)));
    }

}
