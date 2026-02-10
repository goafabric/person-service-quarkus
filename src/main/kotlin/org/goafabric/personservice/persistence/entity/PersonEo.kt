package org.goafabric.personservice.persistence.entity

import jakarta.persistence.*
import org.goafabric.personservice.extensions.UserContext
import org.goafabric.personservice.persistence.extensions.AuditTrailListener

@Entity
@Table(name = "person")
@EntityListeners(AuditTrailListener::class) //, KafkaPublisher::class)
class PersonEo (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String?,

    var organizationId: String?= UserContext.organizationId,

    var firstName: String?,
    var lastName: String?,

    @OneToMany(cascade = [CascadeType.ALL])
    @JoinColumn(name = "person_id")
    var address: List<AddressEo>?,

    @Version //optimistic locking
    var version: Long?
)
