package org.goafabric.personservice.persistence.entity

import jakarta.persistence.*
import org.goafabric.personservice.extensions.UserContext

@Entity
@Table(name = "person") //@EntityListeners(AuditTrailListener.class)
class PersonEo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String? = null
        private set

    var organizationId: String? = null
        private set

    var firstName: String? = null
        private set

    var lastName: String? = null
        private set

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id")
    var address: MutableList<AddressEo?>? = null
        private set

    @Version //optimistic locking
    var version: Long? = null
        private set


    constructor(id: String?, firstName: String?, lastName: String?, address: MutableList<AddressEo?>?, version: Long?) {
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
        this.address = address
        this.version = version
        this.organizationId = UserContext.organizationId
    }

    internal constructor()
}
