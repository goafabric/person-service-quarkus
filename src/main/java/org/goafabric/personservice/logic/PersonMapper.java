package org.goafabric.personservice.logic;

import org.goafabric.personservice.repository.entity.PersonEo;
import org.goafabric.personservice.controller.vo.Person;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;


@Mapper(componentModel = "jakarta", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PersonMapper {
    Person map(PersonEo person);

    PersonEo map(Person person);

    List<Person> map(List<PersonEo> countries);
}
