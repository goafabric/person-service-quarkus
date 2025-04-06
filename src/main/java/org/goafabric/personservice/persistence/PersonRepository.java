package org.goafabric.personservice.persistence;

import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Find;
import jakarta.data.repository.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;
import org.goafabric.personservice.controller.dto.PersonSearch;
import org.goafabric.personservice.extensions.TenantContext;
import org.goafabric.personservice.persistence.entity.PersonEo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends CrudRepository<PersonEo, String>  {
    @Find
    List<PersonEo> findAllByOrganizationId(String organizationId);

    @Find
    List<PersonEo> findByFirstNameAndOrganizationId(String firstName, String organizationId);

    @Find
    List<PersonEo> findByLastNameAndOrganizationId(String lastName, String organizationId);

    default List<PersonEo> findByOptionalNames(EntityManager em, PersonSearch personSearch) {
        var cb = em.getCriteriaBuilder();
        var query = cb.createQuery(PersonEo.class);
        var root = query.from(PersonEo.class);
        var predicates = new ArrayList<>();

        predicates.add(cb.equal(root.get("organizationId"), TenantContext.getOrganizationId()));

        Optional.ofNullable(personSearch.getFirstName())
                .ifPresent(v -> predicates.add(cb.equal(root.get("firstName"), v)));

        Optional.ofNullable(personSearch.getLastName())
                .ifPresent(v -> predicates.add(cb.equal(root.get("lastName"), v)));

        query.where(cb.or(predicates.toArray(new Predicate[0])));

        return em.createQuery(query).getResultList();
    }

}
