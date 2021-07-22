package org.goafabric.personservice.service;

import io.quarkus.test.junit.QuarkusTest;
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
    public void findAll() {
        List<Person> persons = personLogic.findAll();
        assertThat(persons).isNotNull().hasSize(3);
    }
}
