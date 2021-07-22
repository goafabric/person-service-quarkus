package org.goafabric.personservice.persistence.multitenancy;

import org.goafabric.personservice.crossfunctional.TenantIdInterceptor;
import org.goafabric.personservice.persistence.audit.AuditJpaListener;

import javax.persistence.*;

@MappedSuperclass
@EntityListeners(AuditJpaListener.class)
public abstract class TenantAware {
    @Access(AccessType.PROPERTY)
    @Column(name = "tenant_id")
    public String getTenantId() {
        return TenantIdInterceptor.getTenantId(); //this is for save operations only and this should also ensure that setting the wrong tenant is nearly impossible
    }

    public void setTenantId(String tenantId) {}

    public abstract String getId();
}
