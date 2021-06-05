package org.goafabric.personservice.service;

import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
//@QuarkusIntegrationTest //see https://quarkus.io/guides/getting-started-testing
public class NativePersonServiceIT extends PersonServiceTest {

    // Execute the same tests but in native mode.
}