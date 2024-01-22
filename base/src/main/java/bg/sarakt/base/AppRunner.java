
package bg.sarakt.base;

import java.awt.EventQueue;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeMap;
import bg.sarakt.attributes.impl.AttributeFactory;
import bg.sarakt.attributes.impl.AttributeMapImpl;
import bg.sarakt.attributes.impl.PrimaryAttribute;
import bg.sarakt.base.window.AttributeWindow;
import bg.sarakt.base.window.CharacterAttributeWindow;
import bg.sarakt.storing.hibernate.HibernateConf;

/** Application runner for encapsulating the application. */
@ComponentScan("bg.sarakt.storing.hibernate")
public final class AppRunner {

    private static final int CONDITION =2;

    @Autowired
    protected SessionFactory sessionFactory;
    double[]                 array =
        {
                1.0, 3, 1.1, 2
        };

    public static void main(String[] args) throws Exception {
        switch (CONDITION)
        {
        case 0:
            main_0(args);
            return;
        case 1:
            main_1(args);
            return;
        case 2:
            main_2(args);
            return;
        default:
            return;
        }
    }
    public static <T extends Serializable>void main_2(String[] args) throws Exception {
        System.out.println("Test ground");

        AttributeMap<Attribute> map = new AttributeMapImpl();
        EventQueue.invokeAndWait(()->new AttributeWindow(map ));
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
