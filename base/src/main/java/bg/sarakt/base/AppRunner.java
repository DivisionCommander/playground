
package bg.sarakt.base;

import java.awt.EventQueue;
import java.io.Serializable;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import bg.sarakt.attributes.experience.impl.DummyLevelImpl;
import bg.sarakt.attributes.impl.AttributeFactory;
import bg.sarakt.attributes.impl.AttributeMapImpl;
import bg.sarakt.attributes.impl.PrimaryAttribute;
import bg.sarakt.base.window.AttributeWindow;
import bg.sarakt.base.window.CharacterAttributeWindow;
import bg.sarakt.storing.hibernate.HibernateConf;

/** Application runner for encapsulating the application. */

@SuppressWarnings("deprecation")
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(HibernateConf.class)
@EntityScan(basePackages = "bg.sarakt.storing.hibernate.entities")
public class AppRunner {

    private static final int CONDITION =2;

    @Autowired
    protected SessionFactory sessionFactory;
    double[]                 array =
        {
                1.0, 3, 1.1, 2
        };

    public static void main(String[] args) throws Exception {
        System.setProperty("java.awt.headless", "false");
         SpringApplication.run(AppRunner.class, args);
        switch (CONDITION)
        {
        case 0:
            main_0(args);
            break;
        case 1:
            main_1(args);
            break;
        case 2:
            main_2(args);
            break;
        default:
            break;
        }
    }
    public static <T extends Serializable>void main_2(String[] args) throws Exception {
        System.out.println("Test ground");
        DummyLevelImpl lvl = new DummyLevelImpl();
        AttributeMapImpl map = new AttributeMapImpl(lvl);
        EventQueue.invokeAndWait(()->new AttributeWindow(map, lvl ));
    }


    public static void main_1(String[] args) throws Exception {
        EventQueue.invokeAndWait(() -> new CharacterAttributeWindow());
    }

    public static void main_0(String[] args) {
        System.out.println("HYA DAO");
        System.out.println(PrimaryAttribute.values());
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(HibernateConf.class);
        // TagsDAO td = new TagsDAO();
        System.out.println(ctx);
        // List<TagEntity> findAll = td.findAll();
        // findAll.forEach(System.out::println);

        AttributeFactory.getInstance().getResourceAttribute().stream().forEach(System.out::println);
        AttributeFactory.getInstance().getSecondaryAttributes().stream().forEach(System.err::println);
    }

}
