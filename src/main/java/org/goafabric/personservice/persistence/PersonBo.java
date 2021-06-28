package org.goafabric.personservice.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.goafabric.personservice.persistence.audit.AuditJpaListener;
import org.goafabric.personservice.persistence.audit.TenantAware;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity @Table(name = "person")
@EntityListeners(AuditJpaListener.class)
public class PersonBo extends TenantAware {
    @javax.persistence.Id @GeneratedValue(generator = "uuid") @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;
}
