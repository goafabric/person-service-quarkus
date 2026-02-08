//package org.goafabric.personservice.logic
//
//import org.goafabric.personservice.controller.dto.Person
//import org.goafabric.personservice.persistence.entity.PersonEo
//import org.mapstruct.Mapper
//import org.mapstruct.ReportingPolicy
//
//@Mapper(componentModel = "jakarta", unmappedTargetPolicy = ReportingPolicy.IGNORE)
//interface PersonMapper {
//    fun map(value: PersonEo): Person
//
//    fun map(value: Person): PersonEo
//
//    fun map(value: List<PersonEo>): List<Person>
//}
