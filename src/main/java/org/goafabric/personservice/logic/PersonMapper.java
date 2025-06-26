package org.goafabric.personservice.logic;

import jakarta.data.page.Page;
import org.goafabric.personservice.controller.dto.Person;
import org.goafabric.personservice.persistence.entity.PersonEo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;


@Mapper(componentModel = "jakarta", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PersonMapper {
    Person map(PersonEo value);

    PersonEo map(Person value);

    List<Person> map(List<PersonEo> value);

    List<Person> map(Page<PersonEo> value);
}
