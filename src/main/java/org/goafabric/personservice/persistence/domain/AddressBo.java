package org.goafabric.personservice.persistence.domain;

import jakarta.persistence.*;
import org.goafabric.personservice.persistence.extensions.AuditListener;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="address")
public class AddressBo extends AuditListener.AuditAware {
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
