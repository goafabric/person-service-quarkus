package org.goafabric.personservice.persistence.domain;

import jakarta.persistence.*;
import org.goafabric.personservice.persistence.extensions.AuditListener;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "person")
public class PersonBo extends AuditListener.AuditAware {
    @Id
    @GeneratedValue(generator = "uuid") @GenericGenerator(name = "uuid", strategy = "uuid2")
    public String id;

    @Column(name = "company_id")
    public String companyId = "1";

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
