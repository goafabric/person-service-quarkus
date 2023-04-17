package org.goafabric.personservice.persistence.domain;

import org.goafabric.personservice.persistence.multitenancy.AuditAware;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;

@Entity
@Table(name = "person")
public class PersonBo extends AuditAware {
    @Id
    @GeneratedValue(generator = "uuid") @GenericGenerator(name = "uuid", strategy = "uuid2")
    public String id;

    @Column(name = "tenant_id")
    public String tenantId = "0";

    @Column(name = "first_name")
    public String firstName;

    @Column(name = "last_name")
    public String lastName;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    public AddressBo address;

    @Override
    public String getId() {
        return id;
    }
}
