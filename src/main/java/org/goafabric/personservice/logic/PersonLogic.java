package org.goafabric.personservice.logic;

import jakarta.data.page.PageRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.goafabric.personservice.adapter.CalleeServiceAdapter;
import org.goafabric.personservice.controller.dto.Person;
import org.goafabric.personservice.controller.dto.PersonSearch;
import org.goafabric.personservice.extensions.TenantContext;
import org.goafabric.personservice.persistence.PersonRepository;

import java.util.List;

@ApplicationScoped
@Transactional
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
                personRepository.findById(id).get());
    }


    public Person save(Person person) {
        return personMapper.map(
                personRepository.save(personMapper.map(person)));
    }

    public void delete(String id) {
        personRepository.deleteById(id);
    }

    public Person sayMyName(String name) {
        return new Person(null, null,
                calleeServiceAdapter.sayMyName(name).message(), "", null);
    }
    
    public List<Person> search(PersonSearch personSearch) {
        if (personSearch.getFirstName() != null) {
            return personMapper.map(
                    personRepository.findByFirstNameAndOrganizationId(personSearch.getFirstName(), TenantContext.getOrganizationId()
                    , PageRequest.ofPage(1, 3, true)));
        } else if (personSearch.getLastName() != null) {
            return personMapper.map(
                    personRepository.findByLastNameAndOrganizationId(personSearch.getLastName(), TenantContext.getOrganizationId()
                            , PageRequest.ofPage(1, 3, true)));
        }  else {
            return personMapper.map(
                    personRepository.findAllByOrganizationId(TenantContext.getOrganizationId()
                    , PageRequest.ofPage(1, 3, true)));
        }
    }
}
