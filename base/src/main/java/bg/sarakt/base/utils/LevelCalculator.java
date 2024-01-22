/*
 * LevelCalculator.java
 *
 * created at 2023-11-24 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.base.utils;

import java.util.NavigableMap;
import java.util.TreeMap;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import bg.sarakt.storing.hibernate.LevelDAO;
import bg.sarakt.storing.hibernate.entities.LevelEntity;

public final class LevelCalculator {

    public static final long                  EXPERIENCE_CAP = 1999L;
    private final NavigableMap<Long, Integer> LEVEL_MAP      = getAllLevels();

    @Autowired
    protected SessionFactory sessionFactory;

    private NavigableMap<Long, Integer> getAllLevels() {
        Session session = (sessionFactory != null) ? sessionFactory.getCurrentSession() : HibernateUtils.getSessionFactory().openSession();
        // session.beginTransaction();
//        try {

            NavigableMap<Long, Integer> levels = new TreeMap<>();
            session.createQuery("SELECT l FROM Level l", LevelEntity.class).getResultList().forEach(l -> levels.put(l.getXp(), l.getLevel()));
            System.out.println(levels);
            return levels;
//        } finally {
//            session.flush();
//        }
    }

    private NavigableMap<Long, Integer> getLevels(){

        NavigableMap<Long, Integer> levels = new TreeMap<>();
        new LevelDAO().findAll().stream().forEach(l->levels.put(l.getXp()   , l.getLevel()));
        return levels;
    }

    public int calculateLevel(long experience) {
        if (EXPERIENCE_CAP <= experience) {
            return LEVEL_MAP.lastEntry().getValue();
        }
        return LEVEL_MAP.get(LEVEL_MAP.floorKey(experience));
    }
}
