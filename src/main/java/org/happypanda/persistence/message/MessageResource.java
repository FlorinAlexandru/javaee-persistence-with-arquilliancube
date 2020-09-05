package org.happypanda.persistence.message;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@RequestScoped
@Path("/messages")
public class MessageResource {

    @Inject
    MessageDao messageDao;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Message> messages() {
        return messageDao.findAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void create(Message message) {
        messageDao.createMessage(message);
    }
}
