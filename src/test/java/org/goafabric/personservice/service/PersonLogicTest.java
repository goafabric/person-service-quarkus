package org.goafabric.personservice.service;

import io.quarkus.test.junit.QuarkusTest;
import org.goafabric.personservice.crossfunctional.TenantIdInterceptor;
import org.goafabric.personservice.logic.PersonLogic;
import org.goafabric.personservice.persistence.DatabaseProvisioning;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@QuarkusTest
public class PersonLogicTest {
    @Inject
    PersonLogic personLogic;

    @BeforeAll
    public static void init() {
        CDI.current().select(DatabaseProvisioning.class).get().importDemoData();
    }

    @Test
    public void findById() {
        TenantIdInterceptor.setTenantId("0");
        List<Person> persons = personLogic.findAll();
        assertThat(persons).isNotNull().hasSize(3);

        final Person person
                = personLogic.getById(persons.get(0).getId());
        assertThat(person).isNotNull();
        assertThat(person.getFirstName()).isEqualTo(persons.get(0).getFirstName());
        assertThat(person.getLastName()).isEqualTo(persons.get(0).getLastName());

        TenantIdInterceptor.setTenantId("5a2f");
        assertThat(personLogic.getById(persons.get(0).getId())).isNull();
    }

    @Test
    public void findAll() {
        TenantIdInterceptor.setTenantId("0");
        assertThat(personLogic.findAll()).isNotNull().hasSize(3);

        TenantIdInterceptor.setTenantId("5a2f");
        assertThat(personLogic.findAll()).isNotNull().hasSize(0);
    }

    @Test
    public void findByFirstName() {
        TenantIdInterceptor.setTenantId("0");
        List<Person> persons = personLogic.findByFirstName("Monty");
        assertThat(persons).isNotNull().hasSize(1);
        assertThat(persons.get(0).getFirstName()).isEqualTo("Monty");
        assertThat(persons.get(0).getLastName()).isEqualTo("Burns");

        TenantIdInterceptor.setTenantId("5a2f");
        assertThat(personLogic.findByFirstName("Monty")).isNotNull().hasSize(0);
    }

    @Test
    public void findByLastName() {
        TenantIdInterceptor.setTenantId("0");
        List<Person> persons = personLogic.findByLastName("Simpson");
        assertThat(persons).isNotNull().hasSize(2);
        assertThat(persons.get(0).getLastName()).isEqualTo("Simpson");

        TenantIdInterceptor.setTenantId("5a2f");
        assertThat(personLogic.findByFirstName("Simpson")).isNotNull().hasSize(0);
    }

    @Test
    public void countByLastName() {
        TenantIdInterceptor.setTenantId("0");
        assertThat(personLogic.countByLastName("Simpson")).isEqualTo(2);

        TenantIdInterceptor.setTenantId("5a2f");
        assertThat(personLogic.countByLastName("Simpson")).isEqualTo(0);
    }
}
