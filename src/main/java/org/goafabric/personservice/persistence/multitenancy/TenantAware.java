package org.goafabric.personservice.persistence.multitenancy;

import org.goafabric.personservice.crossfunctional.HttpInterceptor;
import org.goafabric.personservice.persistence.audit.AuditAware;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class TenantAware extends AuditAware {
    @Access(AccessType.PROPERTY)
    @Column(name = "tenant_id")
    public String getTenantId() {
        return HttpInterceptor.getTenantId(); //this is for save operations only and this should also ensure that setting the wrong tenant is nearly impossible
    }

    public void setTenantId(String tenantId) {}
}
