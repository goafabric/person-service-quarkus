package org.goafabric.personservice.persistence.audit;

import org.goafabric.personservice.crossfunctional.TenantFilter;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
//@Where(clause = TenantAware.TENANT_FILTER)
public abstract class TenantAware {
    public static final String TENANT_FILTER = "TENANT_FILTER";

    @Access(AccessType.PROPERTY)
    @Column(name = "tenant_id")
    public String getTenantId() {
        return TenantFilter.getTenantId(); //this is for save operations only and this should also ensure that setting the wrong tenant is nearly impossible
    }

    public void setTenantId(String tenantId) {
        //should never be set because we alway use the ThreadLocal
    }

    public abstract String getId();
}
