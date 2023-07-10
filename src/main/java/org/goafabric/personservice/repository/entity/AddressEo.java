package org.goafabric.personservice.repository.entity;

import jakarta.persistence.*;
import org.goafabric.personservice.repository.extensions.AuditListener;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="address")
@EntityListeners(AuditListener.class)
public class AddressEo {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    public String id;

    public String street;
    public String city;

    @Version //optimistic locking
    public Long version;
}
