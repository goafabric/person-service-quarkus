package org.goafabric.personservice.persistence.domain;

import org.goafabric.personservice.persistence.multitenancy.TenantAware;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;

@Entity
@Table(name="address")
public class AddressBo extends TenantAware {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    public String id;

    public String street;
    public String city;

    @Version //optimistic locking
    public Long version;

    @Override
    public String getId() {
        return id;
    }
}
