/*
 * ApplicationContextProvider.java
 *
 * created at 2024-01-31 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.base;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider implements ApplicationContextAware{

    private static ApplicationContext applicationContext;

    /**
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextProvider.applicationContext = applicationContext; //NOSONAR
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    // @Bean
    // public LevelNodeDAO levelNodeDAO() {
    // return new LevelNodeDAO();
    // }

    // @Bean
    // public LevelDAO levelDAO() {
    // return new LevelDAO();
    // }
    //
    // @Bean
    // public GenericHibernateDAO<TagEntity> generigTagDAO(){
    // GenericHibernateDAO<TagEntity> dao = new
    // GenericHibernateDAO<>(TagEntity.class);
    //// dao.setClazz(TagEntity.class);
    // return dao;
    //
    // }
    //
    // @Bean
    // public GenericHibernateDAO<UnitClassEntity> unitClassDAO() {
    // return new GenericHibernateDAO<>(UnitClassEntity.class);
    // }

}



