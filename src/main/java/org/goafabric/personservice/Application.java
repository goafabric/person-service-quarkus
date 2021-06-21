package org.goafabric.personservice;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import lombok.extern.slf4j.Slf4j;
import org.goafabric.personservice.persistence.DatabaseProvisioning;

import javax.inject.Inject;

@QuarkusMain
@Slf4j
public class Application {

    public static void main(String... args) {
        Quarkus.run(MyApp.class, args);
    }

    public static class MyApp implements QuarkusApplication {

        @Inject
        DatabaseProvisioning databaseProvisioning;

        @Override
        public int run(String... args) throws Exception {
            databaseProvisioning.run();
            Quarkus.waitForExit();
            return 0;
        }
    }

}
