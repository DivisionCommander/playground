/*
 * AttributeConfiguration.java
 *
 * created at 2024-02-08 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.attributes.conf;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(
    {       "bg.sarakt.attributes",
            "bg.sarakt.attributes.impl", 
            "bg.sarakt.attributes.levels",
            "bg.sarakt.attributes.primary",
            "bg.sarakt.attributes.resource",
            "bg.sarakt.attributes.secondary",
            "bg.sarakt.attributes.services",
            "bg.sarakt.attributes.util"

})
public class AttributeConfiguration {
    
}



