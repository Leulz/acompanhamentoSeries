import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static play.test.Helpers.callAction;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.fakeRequest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import models.*;
import models.dao.GenericDAO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.GlobalSettings;
import play.Logger;
import play.db.jpa.JPA;
import play.db.jpa.JPAPlugin;
import play.mvc.Result;
import play.test.FakeApplication;
import play.test.Helpers;
import play.twirl.api.Html;
import scala.Option;
import views.html.index;

public class ApplicationTest {
/*	GenericDAO dao = new GenericDAO();
	List<Serie> series;
    public EntityManager em;
    
    @Before
    public void setUp() {
        FakeApplication app = Helpers.fakeApplication();
        Helpers.start(app);
        Option<JPAPlugin> jpaPlugin = app.getWrappedApplication().plugin(JPAPlugin.class);
        em = jpaPlugin.get().em("default");
        JPA.bindForCurrentThread(em);
        em.getTransaction().begin();
    }
    
   @Test
    public void iniciaIndex(){
    	series = dao.findAllByClassName("Serie");
        Html html = views.html.index.render(series);
    	assertThat(contentAsString(html).contains("Onde eu parei?"));
    }
    
    @After
    public void tearDown() {
        em.getTransaction().commit();
        JPA.bindForCurrentThread(null);
        em.close();
    }*/
}
