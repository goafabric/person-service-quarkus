package org.goafabric.personservice.repository.entity;

import jakarta.persistence.*;
import org.goafabric.personservice.repository.extensions.AuditListener;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "person")
public class PersonEo extends AuditListener.AuditAware {
    @Id
    @GeneratedValue(generator = "uuid") @GenericGenerator(name = "uuid", strategy = "uuid2")
    public String id;

    @Column(name = "first_name")
    public String firstName;

    @Column(name = "last_name")
    public String lastName;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    public AddressEo address;

    @Override
    public String getId() {
        return id;
    }
}
