/*
 * LevelCalculator.java
 *
 * created at 2023-11-24 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.storing.utils;

import java.util.NavigableMap;
import java.util.TreeMap;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import bg.sarakt.attributes.services.LevelService;
import bg.sarakt.base.utils.Dummy;
import bg.sarakt.logging.Logger;
import bg.sarakt.storing.hibernate.LevelDAO;
import bg.sarakt.storing.hibernate.entities.LevelEntity;
import bg.sarakt.storing.hibernate.interfaces.ILevelDAO;

@Dummy
@Component
public final class LevelCalculator implements LevelService {

    private static LevelService               INSTANCE;
    private final NavigableMap<Long, Integer> LEVEL_MAP      = getAllLevels();
    private ILevelDAO                         levelDao;
    
    public static LevelService getInstance() { return INSTANCE; }


    @Autowired
    private LevelCalculator(SessionFactory sessionFactory, ILevelDAO lvl) {
        this.sessionFactory = sessionFactory;
        this.levelDao = lvl;
    }
    
    protected SessionFactory sessionFactory;

    private NavigableMap<Long, Integer> getAllLevels() {
        Session session = (sessionFactory != null) ? sessionFactory.getCurrentSession() : HibernateUtils.getSessionFactory().openSession();

        NavigableMap<Long, Integer> levels = new TreeMap<>();
        session.createQuery("SELECT l FROM Level l", LevelEntity.class).getResultList().forEach(l -> levels.put(l.getXp(), l.getLevel()));
        System.out.println(levels);
        return levels;
    }
    
    @Override
    public NavigableMap<Long, Integer> getLevels(){

        NavigableMap<Long, Integer> levels = new TreeMap<>();
        levelDao.findAll().stream().forEach(l -> levels.put(l.getXp(), l.getLevel()));
        return levels;
    }

    @Override
    public int calculateLevel(long experience) {
        if (EXPERIENCE_CAP <= experience) {
            return LEVEL_MAP.lastEntry().getValue();
        }
        return LEVEL_MAP.get(LEVEL_MAP.floorKey(experience));
    }

    @Override
    public Long getThreshold(Long experience) {
        if(experience >=EXPERIENCE_CAP)
        {
            return EXPERIENCE_CAP;
        }
        return LEVEL_MAP.ceilingKey(experience);
    }

    @Override
    public long getNextThreshold(Long experience) {
        if(experience>=EXPERIENCE_CAP)
        {
            return EXPERIENCE_CAP;
        }
        Long threshold = LEVEL_MAP.higherKey(experience);
        return threshold ==null? EXPERIENCE_CAP : threshold;
    }
    
    @PostConstruct
    private void bindFactory() {
        INSTANCE = this; // NOSONAR
        Logger.getLogger().debug("Binding AttributeFactory#factory to" + this);
    }
}
