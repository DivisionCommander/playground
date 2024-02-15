/*
 * StorngConfiguration.java
 *
 * created at 2024-02-06 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.storing.conf;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EntityScan(basePackages =
    {
            "bg.sarakt.storing.hibernate.entities",
            "bg.sarakt.glossary.entities"
})
@ComponentScan(basePackages =
    {
            "bg.sarakt",
            "bg.sarakt.base",
            "bg.sarakt.storing",
            "bg.sarakt.storing.hibernate",
            "bg.sarakt.storing.hibernate.entities",
            "bg.sarakt.storing.hibernate.interfaces",
            "bg.sarakt.glossary",
            "bg.sarakt.storing.hibernate"
})
public class StoringConfiguration {
    
}