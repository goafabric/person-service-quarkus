package org.goafabric.personservice.persistence.domain;

import jakarta.persistence.*;
import org.goafabric.personservice.persistence.encryption.StringEncryptor;
import org.goafabric.personservice.persistence.multitenancy.TenantAware;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "person")
@Where(clause = TenantAware.TENANT_FILTER)
public class PersonBo extends TenantAware {
    @Id
    @GeneratedValue(generator = "uuid") @GenericGenerator(name = "uuid", strategy = "uuid2")
    public String id;

    @Convert(converter = StringEncryptor.class) // be aware that colum size needs to be increased to hold encrypted value
    public String firstName;

    public String lastName;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    public AddressBo address;

    @Version //optimistic locking
    public Long version;

    @Override
    public String getId() {
        return id;
    }
}
