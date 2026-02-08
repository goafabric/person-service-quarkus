package org.goafabric.personservice.persistence.entity

import jakarta.persistence.*

@Entity
@Table(name = "address") //@EntityListeners(AuditTrailListener.class)
class AddressEo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String? = null
        private set

    var street: String? = null
        private set
    var city: String? = null
        private set

    @Version //optimistic locking
    var version: Long? = null
        private set

    constructor(id: String?, street: String?, city: String?, version: Long?) {
        this.id = id
        this.street = street
        this.city = city
        this.version = version
    }

    internal constructor()
}
