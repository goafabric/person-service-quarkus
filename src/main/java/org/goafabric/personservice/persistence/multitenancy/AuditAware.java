package org.goafabric.personservice.persistence.multitenancy;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
@EntityListeners(AuditListener.class)
public abstract class AuditAware {
    public abstract String getId();
}
