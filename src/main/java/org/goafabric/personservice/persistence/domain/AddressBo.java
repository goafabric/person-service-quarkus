package org.goafabric.personservice.persistence.domain;

import org.goafabric.personservice.persistence.multitenancy.AuditAware;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;

@Entity
@Table(name="address")
public class AddressBo extends AuditAware {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    public String id;

    @Column(name = "company_id")
    public String companyId = "1";

    public String street;
    public String city;

    @Version //optimistic locking
    public Long version;

    @Override
    public String getId() {
        return id;
    }
}
