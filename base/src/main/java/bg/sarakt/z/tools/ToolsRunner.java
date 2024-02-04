/*
 * ToolsRunner.java
 *
 * created at 2023-12-02 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.z.tools;

import java.util.Arrays;

import bg.sarakt.base.ApplicationContextProvider;
import bg.sarakt.storing.hibernate.HibernateConf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(value = HibernateConf.class)
@EntityScan(basePackages = "bg.sarakt.storing.hibernate.entities")
@ComponentScan(basePackages =
    {
            "bg.sarakt.storing.hibernate", "bg.sarakt.base", "bg.sarakt.attributes.impl", "bg.sarakt.z.tools"
})
public class ToolsRunner {
    
    public static void main(String[] args) {
        Arrays.stream(args).forEach(System.err::println);
        SpringApplication.run(ToolsRunner.class, args);
        new ToolsRunner().run();
    }
    
    public void run() {
        
        Environment environment = ApplicationContextProvider.getApplicationContext().getEnvironment();
        System.err.println("environment" + environment);
        
        for (String profileName : environment.getActiveProfiles()) {
            System.out.println("Currently active profile - " + profileName);
        }
        DBPopulator bean = ApplicationContextProvider.getApplicationContext().getBean(DBPopulator.class);
        System.out.println(bean);
        // DBPopulator.getInstance()
        bean.populateLevels().populateAttributes().populateLevelNodes();
    }
}
