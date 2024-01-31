/*
 * ToolsRunner.java
 *
 * created at 2023-12-02 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.z.tools;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import bg.sarakt.storing.hibernate.HibernateConf;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(value = HibernateConf.class)
public class ToolsRunner {

    public static void main(String[] args) {
        SpringApplication.run(ToolsRunner.class, args);
        DBPopulator.getInstance()
//                 .populateLevels()
//                 .populateAttributes()
                .populateLevelNodes();
    }
}
