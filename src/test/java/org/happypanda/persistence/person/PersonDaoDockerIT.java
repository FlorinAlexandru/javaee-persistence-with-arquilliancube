package org.happypanda.persistence.person;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.mongodb.MongoDbConfigurationBuilder;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import org.bson.Document;
import org.happypanda.persistence.MongoProducer;
import org.happypanda.persistence.cdi.Property;
import org.happypanda.persistence.cdi.PropertyProducer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(Arquillian.class)
public class PersonDaoDockerIT {

    @Rule
    public MongoDbRule mongoDbRule = new MongoDbRule(MongoDbConfigurationBuilder.mongoDb()
            .host("localhost")
            .port(27117)
            .databaseName("pandadb")
            .build());

    @Deployment
    public static WebArchive createDeployment() {
        JavaArchive[] javaArchives = Maven.resolver().resolve(
                "org.assertj:assertj-core:3.15.0",
                "org.arquillian.cube:arquillian-cube-docker:1.18.2",
                "org.mongodb:mongo-java-driver:3.4.3")
                .withTransitivity().as(JavaArchive.class);

        WebArchive war = ShrinkWrap.create(WebArchive.class, "app.war")

                .addClasses(PersonDao.class, Person.class)
                .addClasses(MongoProducer.class, PropertyProducer.class, Property.class)
                .addPackages(true, "com.lordofthejars.nosqlunit")
                .addAsLibraries(javaArchives)
                .addAsResource("test-persistence.xml", ArchivePaths.create("META-INF/persistence.xml"))
                .addAsResource("META-INF/application.properties", ArchivePaths.create("META-INF/application.properties"))
                .addAsResource("datasets/", ArchivePaths.create("datasets/"))
                .addAsManifestResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));
        System.out.println(war.toString(true));
        return war;
    }


    @Inject
    PersonDao personDao;

    @Inject
    MongoProducer producer;

    @Test
    public void injectionPointShouldBeNotNull() {
        assertThat(personDao).isNotNull();
    }

    @Test
    public void mongoProducerShouldBeNotNull() {
        assertThat(producer).isNotNull();
    }

    @Test
    @org.arquillian.ape.rdbms.UsingDataSet("datasets/persons.xml")
    public void shouldFindAll() {
        List<Person> messages = personDao.findAll();
        assertThat(messages.size()).isEqualTo(1);
    }

    @Test
    @UsingDataSet(locations = "/datasets/initialData.json")
    public void shouldGetAllFromMongo() {
        ArrayList<Document> documents = producer.getMongoClient().getDatabase("pandadb").getCollection("bears").find().into(new ArrayList<>());
        documents.forEach(System.out::println);
        assertThat(documents.size()).isEqualTo(7);
    }
}