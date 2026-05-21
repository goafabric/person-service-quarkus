package org.goafabric.personservice.persistence.entity

import jakarta.persistence.*
import org.goafabric.personservice.persistence.extensions.AuditTrailListener
import org.goafabric.personservice.persistence.extensions.KafkaPublisher
import org.hibernate.annotations.TenantId

@Entity
@Table(name = "person")
@EntityListeners(AuditTrailListener::class, KafkaPublisher::class)
class PersonEo (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String?,

    var organizationId: String?,

    @TenantId
    var tenantId: String?,

    var firstName: String,
    var lastName: String,

    @OneToMany(cascade = [CascadeType.ALL])
    @JoinColumn(name = "person_id")
    var address: MutableList<AddressEo> = mutableListOf(),

    @Version //optimistic locking
    var version: Long
)
