/*
 * StorngConfiguration.java
 *
 * created at 2024-02-06 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(
    {
            "bg.sarakt.storing", "bg.sarakt.storing.hibernate"
})
public class StoringConfiguration {
    
}
