package org.happypanda.persistence.person;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

@RequestScoped
@Path("/persons")
public class PersonResource {

    @Inject
    private PersonDao personDao;

    @GET
    public List<Person> getAllPersons() {
        return personDao.findAll();
    }
}
