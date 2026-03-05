//package org.goafabric.personservice.extensions
//
//import io.smallrye.mutiny.Multi
//import io.smallrye.reactive.messaging.PublisherDecorator
//import io.smallrye.reactive.messaging.kafka.api.IncomingKafkaRecordMetadata
//import jakarta.enterprise.context.ApplicationScoped
//import org.apache.kafka.common.header.Headers
//import org.eclipse.microprofile.reactive.messaging.Message
//import org.goafabric.personservice.extensions.UserContext.setContext
//import java.nio.charset.StandardCharsets
//
//@ApplicationScoped
//class KafkaInterceptor : PublisherDecorator {
//    override fun decorate(
//        publisher: Multi<out Message<*>?>, channelName: MutableList<String?>,
//        isConnector: Boolean
//    ): Multi<out Message<*>?>? {
//        if (!isConnector) {
//            return publisher
//        }
//
//        return publisher.onItem().transform { message ->
//            message.getMetadata(IncomingKafkaRecordMetadata::class.java)
//                .ifPresent { metadata ->
//                    print(("## thread " + Thread.currentThread().id))
//                    setContext(
//                        "55", //getValue(metadata.headers, "X-TenantId"),
//                        getValue(metadata.headers, "X-OrganizationId"),
//                        getValue(metadata.headers, "X-Auth-Request-Preferred-Username"), null
//                    )
//                }
//            message
//        }
//
//    }
//
//    private fun getValue(headers: Headers, key: String): String {
//        return String(headers.lastHeader(key).value(), StandardCharsets.UTF_8)
//    }
//
//}