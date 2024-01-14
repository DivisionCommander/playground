
package bg.sarakt.base;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import bg.sarakt.glossary.entitites.TagEntity;
import bg.sarakt.storing.hibernate.HibernateConf;
import bg.sarakt.storing.hibernate.TagsDAO;

/** Application runner for encapsulating the application. */
@ComponentScan("bg.sarakt.storing.hibernate")
public final class AppRunner {

    @Autowired
    protected SessionFactory sessionFactory;
    double[]                 array =
        {
                1.0, 3, 1.1, 2
        };

    public static void main(String[] args) {
        System.out.println("HYA DAO");

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(HibernateConf.class);
        TagsDAO td = new TagsDAO();
System.out.println(ctx);
        List<TagEntity> findAll = td.findAll();
        findAll.forEach(System.out::println);

    }

}
