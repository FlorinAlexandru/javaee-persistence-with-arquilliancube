package org.happypanda.persistence.message;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class MessageDao {

    @PersistenceContext(name = "inmemory", unitName = "inmemory")
    private EntityManager em;

    public List<Message> findAll() {
        TypedQuery<Message> query = em.createQuery("select m from Message m", Message.class);
        return query.getResultList();
    }

    public void createMessage(Message msg) {
        em.merge(msg);
    }

    public String sayHello() {
        return "dodo fat";
    }
}   
