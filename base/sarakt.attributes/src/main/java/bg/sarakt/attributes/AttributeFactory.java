/*
 * AttributeFactory.java
 *
 * created at 2024-02-06 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.attributes;

import bg.sarakt.base.ApplicationContextProvider;

public interface AttributeFactory {
    
    public static AttributeService getInstance() { return ApplicationContextProvider.getApplicationContext().getBean(AttributeService.class); }
}



