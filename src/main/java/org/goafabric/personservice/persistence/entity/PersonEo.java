package org.goafabric.personservice.persistence.entity;

import jakarta.nosql.Entity;
import jakarta.nosql.Id;
import org.goafabric.personservice.extensions.TenantContext;

import java.util.List;

@Entity
public class PersonEo {
    @Id
    private String id;

    private String organizationId;

    private String firstName;

    private String lastName;

    private List<AddressEo> address;

    //@Version //optimistic locking
    private Long version;


    public PersonEo(String id, String firstName, String lastName, List<AddressEo> address, Long version) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.version = version;
        this.organizationId = TenantContext.getOrganizationId();
    }

    PersonEo() {}

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<AddressEo> getAddress() {
        return address;
    }

    public Long getVersion() {
        return version;
    }

    public String getOrganizationId() {
        return organizationId;
    }
}
