package org.goafabric.personservice.logic;

import org.goafabric.personservice.persistence.domain.PersonBo;
import org.goafabric.personservice.controller.dto.Person;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "cdi")
public interface PersonMapper {
    Person map(PersonBo person);

    PersonBo map(Person person);

    List<Person> map(List<PersonBo> countries);
}
