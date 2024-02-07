/*
 * EntityProvider.java
 *
 * created at 2024-02-06 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.storing;

import bg.sarakt.base.IHibernateDAO;
import bg.sarakt.storing.hibernate.GenericHibernateDAO;
import bg.sarakt.storing.hibernate.entities.UnitClassEntity;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class EntityProvider {
    
    @Bean
    public IHibernateDAO<UnitClassEntity> unitClassDAO() {
        return new GenericHibernateDAO<>(UnitClassEntity.class);
    }
    
}



