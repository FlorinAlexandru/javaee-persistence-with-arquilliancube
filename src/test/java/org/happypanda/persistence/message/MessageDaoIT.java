package org.happypanda.persistence.message;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(Arquillian.class)
public class MessageDaoIT {

    @Deployment
    public static WebArchive createDeployment() {
        JavaArchive[] javaArchives = Maven.resolver().resolve("org.assertj:assertj-core:3.15.0").withTransitivity().as(JavaArchive.class);
        WebArchive war = ShrinkWrap.create(WebArchive.class, "app.war")
                .addClasses(MessageDao.class, Message.class)
                .addAsLibraries(javaArchives)
                .addAsResource("test-persistence.xml", ArchivePaths.create("META-INF/persistence.xml"))
                .addAsResource("test-insert.sql", ArchivePaths.create("insert.sql"))
                .addAsManifestResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));
        System.out.println(war.toString(true));
        return war;

    }

    @Inject
    MessageDao messageDao;

    @Test
    public void shouldBeNotNull() {
        assertThat(messageDao).isNotNull();
    }

    @Test
    public void shouldSayHello() {
        String message = messageDao.sayHello();
        assertThat(message).isEqualTo("dodo fat");
    }

    @Test
    public void shouldFindAll() {
        List<Message> messages = messageDao.findAll();
        assertThat(messages.size()).isEqualTo(1);
    }

}