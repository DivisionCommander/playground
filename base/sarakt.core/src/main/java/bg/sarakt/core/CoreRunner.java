
package bg.sarakt.core;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Optional;

import bg.sarakt.attributes.impl.AttributeMapImpl;
import bg.sarakt.attributes.levels.Level;
import bg.sarakt.attributes.levels.LevelNode;
import bg.sarakt.attributes.primary.PrimaryAttributeMap;
import bg.sarakt.attributes.services.AttributeService;
import bg.sarakt.attributes.services.LevelService;
import bg.sarakt.base.ApplicationContextProvider;
import bg.sarakt.base.utils.HibernateConf;
import bg.sarakt.core.window.AttributeWindow;
import bg.sarakt.logging.Logger;
import bg.sarakt.storing.conf.StoringConfiguration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/** Application runner for encapsulating the application. */

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(HibernateConf.class)
@Configuration(proxyBeanMethods = false)

@ComponentScan(basePackages =
    {
            "bg.sarakt.base", "bg.sarakt.core"
}, basePackageClasses = StoringConfiguration.class)
public class CoreRunner {
    
    /**
     * @formatter:off
            "bg.sarakt",
             "bg.sarakt.base", 
             "bg.sarakt.attributes.impl", 
             "bg.sarakt.storing", 
             "bg.sarakt.storing.hibernate",
            "bg.sarakt.attributes.impl",
             "bg.sarakt.attributes.secondary",
              "bg.sarakt.attributes.resource",
               "bg.sarakt.attributes.services",
            "bg.sarakt.attributes.util",
             "bg.sarakt.core"
            @formatter:on

     */
    public static final Logger LOG = Logger.getLogger();
    
    private static final int CONDITION = 2;
    
    double[]                 array =
        {
                1.0, 3, 1.1, 2
        };
        
    public static void main(String[] args) throws Exception {
        System.setProperty("java.awt.headless", "false");
        SpringApplication.run(CoreRunner.class, args);
        new CoreRunner().run();
        
    }
    
    private void run() throws InvocationTargetException, InterruptedException {
        switch (CONDITION)
        {
        case 1:
            test1();
            break;
        case 2:
            showAttributeWindow();
            break;
        case 3:
            break;
        case 4:
            break;
        case 5:
            break;
        default:
            LOG.log("unsupported");
            break;
        }
    }
    
    
    private void test1() {
        LOG.error("Level tests");
        ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
        LevelService serv = ctx.getBean(LevelService.class);
        Level level = serv.getLevel(1);
        LOG.log(level);
        LevelNode lvl = level.viewCurrentLevel();
        Optional<LevelNode> optional;
        do {
            LOG.log(lvl.getLevelNumber() + "\t" + lvl);
            optional = lvl.getNextNode();
            if (optional.isPresent()) {
                lvl = optional.get();
            }
        } while (optional.isPresent());
        
    }
    
    
    public static void showAttributeWindow() throws InterruptedException, InvocationTargetException {
        LOG.log("Test ground");
        ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
        LevelService serv = ctx.getBean(LevelService.class);
        Level level = serv.getLevel(1);
        

        PrimaryAttributeMap pam = new PrimaryAttributeMap(level, null);
        
        AttributeService service = ctx.getBean(AttributeService.class);
        AttributeMapImpl map = new AttributeMapImpl(pam, service.getResourceAttributes(), service.getSecondaryAttributes(), level);
        LOG.log(level);
        Optional<LevelNode> opt = level.getNextNode();
        LOG.log(opt);
        while (opt.isPresent()) {
            LevelNode node = opt.get();
            LOG.log(node);
            opt = node.getNextNode();
        }
        EventQueue.invokeAndWait(() -> new AttributeWindow(map));
    }
    
    @Override
    public String toString() {
        return "CoreRunner [array="
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
