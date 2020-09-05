package org.happypanda.persistence.person;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class PersonDao {

    @PersistenceContext(unitName = "postgresDocker")
    private EntityManager em;


    public List<Person> findAll() {
        TypedQuery<Person> query = em.createQuery("select p from Person p", Person.class);
        return query.getResultList();
    }
}
