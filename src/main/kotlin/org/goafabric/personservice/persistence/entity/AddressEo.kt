package org.goafabric.personservice.persistence.entity

import jakarta.persistence.*
import org.goafabric.personservice.persistence.extensions.AuditTrailListener

@Entity
@Table(name = "address")
@EntityListeners(AuditTrailListener::class)
class AddressEo (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String?,

    var street: String,
    var city: String,

    @Version //optimistic locking
    var version: Long?
)

