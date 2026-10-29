package org.goafabric.personservice.logic

import io.mcarle.konvert.api.Konfig

import io.mcarle.konvert.api.Konverter
import io.mcarle.konvert.injector.cdi.KApplicationScoped

import org.goafabric.personservice.controller.dto.Address
import org.goafabric.personservice.controller.dto.Person
import org.goafabric.personservice.persistence.entity.AddressEo
import org.goafabric.personservice.persistence.entity.PersonEo

@Konverter([Konfig(key = "konvert.enforce-not-null", value = "true")])
@KApplicationScoped
interface PersonMapper {
    fun map(value: PersonEo): Person

    fun map(value: Person): PersonEo

    fun map(value: List<PersonEo>): List<Person>

    fun map(value: AddressEo): Address

    fun map(value: Address): AddressEo


}
