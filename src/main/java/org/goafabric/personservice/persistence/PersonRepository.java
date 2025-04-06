package org.goafabric.personservice.persistence;

import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Find;
import jakarta.data.repository.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;
import org.goafabric.personservice.controller.dto.PersonSearch;
import org.goafabric.personservice.persistence.entity.PersonEo;

import java.util.ArrayList;
import java.util.List;

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

        List<Predicate> predicates = new ArrayList<>();

        if (personSearch.getFirstName()!= null) {
            predicates.add(cb.equal(root.get("firstName"), personSearch.getFirstName()));
        }

        if (personSearch.getLastName() != null) {
            predicates.add(cb.equal(root.get("lastName"), personSearch.getLastName()));
        }

        if (!predicates.isEmpty()) {
            query.where(cb.or(predicates.toArray(new Predicate[0])));
        }

        return em.createQuery(query).getResultList();
    }

}
