
package bg.sarakt.base;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import bg.sarakt.attributes.AttributeService;
import bg.sarakt.attributes.impl.AttributeFactory;
import bg.sarakt.attributes.impl.AttributeMapImpl;
import bg.sarakt.attributes.impl.AttributeProvider;
import bg.sarakt.attributes.impl.Attributes;
import bg.sarakt.attributes.impl.PrimaryAttribute;
import bg.sarakt.attributes.levels.impl.DummyLevelImpl;
import bg.sarakt.base.window.AttributeWindow;
import bg.sarakt.base.window.CharacterAttributeWindow;
import bg.sarakt.storing.hibernate.HibernateConf;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/** Application runner for encapsulating the application. */

@SuppressWarnings("deprecation")
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(HibernateConf.class)
@Configuration(proxyBeanMethods =  false)
@EntityScan(basePackages = "bg.sarakt.storing.hibernate.entities")
@ComponentScan(basePackages = {"bg.sarakt.storing.hibernate", "bg.sarakt.base", "bg.sarakt.attributes.impl"})
public class AppRunner {

    private static final int CONDITION = 2;

    @Autowired
    protected SessionFactory sessionFactory;
    double[]                 array =
        {
                1.0, 3, 1.1, 2
        };

    public static void main(String[] args) throws Exception {
        System.setProperty("java.awt.headless", "false");
        SpringApplication.run(AppRunner.class, args);
        AppRunner runner = new AppRunner();
        switch (CONDITION)
        {
        case 0:
            attributeFactoryTest(args);
            break;
        case 1:
            main_1(args);
            break;
        case 2:
            showAttributeWindow();
            break;
        case 3:
            checkBeanAgainstFactory();
            break;
        case 4:
            bug4();
            break;
        case 5:
            runner.test1();
        default:
            System.out.println("unsupported");
            break;
        }
    }
    
    private static void bug4() {
        AttributeMapImpl ami = new AttributeMapImpl();
        
    }
    
    private void test1() {
        AttributeService factory = ApplicationContextProvider.getApplicationContext().getBean(AttributeFactory.class);
        factory.getResourceAttribute().forEach(System.out::println);
        System.out.println("Secondary");
    }
    


    public static void checkBeanAgainstFactory() {
        AttributeService bean = ApplicationContextProvider.getApplicationContext().getBean(AttributeFactory.class);
        AttributeService instance = AttributeFactory.getInstance();
        System.out.println(instance);// NOSONAR
        System.out.println(bean);// NOSONAR
        System.out.println(bean.equals(instance));// NOSONAR
        System.out.println(bean == instance);// NOSONAR
    }
    
    public static void showAttributeWindow() throws InterruptedException, InvocationTargetException {
        System.out.println("Test ground");
        DummyLevelImpl lvl = new DummyLevelImpl();
        AttributeMapImpl map = new AttributeMapImpl(lvl);
        EventQueue.invokeAndWait(()->new AttributeWindow(map, lvl ));
    }


    @Deprecated(forRemoval =  true, since ="0.0.7")
    public static void main_1(String[] args) throws Exception {
        EventQueue.invokeAndWait(CharacterAttributeWindow::new);
    }

    @Deprecated(forRemoval = true, since="0.0.7")
    public static void attributeFactoryTest(String[] args) {
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
    @Override
    public String toString() {
        return "AppRunner [sessionFactory="
               + sessionFactory
               + ",\n array="
               + Arrays.toString(array)
               + ",\n getClass()="
               + getClass()
               + ",\n hashCode()="
               + hashCode()
               + ",\n toString()="
               + super.toString()
               + "]";
    }

}
