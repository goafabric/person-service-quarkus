package org.goafabric.personservice.persistence.extensions;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.goafabric.event.EventData;
import org.goafabric.personservice.extensions.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//	implementation("io.quarkus:quarkus-messaging-kafka")
//	implementation("io.smallrye.reactive:smallrye-reactive-messaging-kafka")

@ApplicationScoped
public class AuditTrailEventDispatcher {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Inject
    @Channel("person-out")
    Emitter<EventData> personEmitter;

    public void dispatchEvent(AuditTrailListener.AuditEvent auditTrail, Object payload) {
        log.info("emitting ...");
        personEmitter.send(new EventData(payload.getClass().getSimpleName(), auditTrail.operation().toString(), payload, UserContext.getAdapterHeaderMap()));
    }

    @Incoming("person-in")
    public void listen(EventData eventData) {
        log.info("loopback: " + eventData.toString());
    }
    /*
    @KafkaListener(topics = {"person"}, groupId = "person")
    public void listen(EventData eventData) { log.info("loopback event " + eventData.toString()); }

     */

}
