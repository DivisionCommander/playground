/*
 * LevelCalculator.java
 *
 * created at 2023-11-24 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.storing.utils;

import java.math.BigInteger;
import java.util.NavigableMap;
import java.util.SequencedCollection;
import java.util.TreeMap;
import java.util.stream.Collectors;

import bg.sarakt.attributes.levels.Level;
import bg.sarakt.attributes.levels.LevelNode;
import bg.sarakt.attributes.services.LevelService;
import bg.sarakt.base.utils.Dummy;
import bg.sarakt.base.utils.ForRemoval;
import bg.sarakt.logging.Logger;
import bg.sarakt.storing.hibernate.entities.LevelEntity;
import bg.sarakt.storing.hibernate.interfaces.ILevelDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

/**
 * @deprecated use {@link LevelService} instead
 */
@Dummy
@Component
@Deprecated(forRemoval = true)
@ForRemoval(expectedRemovalVersion = "0.1.1")
@Scope(scopeName = BeanDefinition.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.NO)
public class LevelCalculator implements LevelService {
    
    private static LevelCalculator            instance;
    private final NavigableMap<Long, Integer> levelMap;
    protected ILevelDAO                       levelDAO;
    private LevelService                      service;
    
    public static LevelCalculator getInstance() { return instance; }
    
    @Autowired
    protected LevelCalculator(ILevelDAO lvl, LevelService service) {
        this.levelDAO = lvl;
        this.levelMap = getAllLevels();
        this.service = service;
    }
    
    private NavigableMap<Long, Integer> getAllLevels() {
        
        NavigableMap<Long, Integer> levels = new TreeMap<>();
        levelDAO.findAll().forEach(l -> levels.put(l.getXp(), l.getLevel()));
        return levels;
    }
    
    /**
     * 
     * @deprecated
     */
    @Override
    @Deprecated(since = "0.1.0-ALPHA", forRemoval = true)
    @ForRemoval(since = "0.1.0-ALPHA", expectedRemovalVersion = "0.1.5")
    public NavigableMap<Long, Integer> getLevels() {
        NavigableMap<Long, Integer> levels = new TreeMap<>();
        
        levelDAO.findAll().stream().forEach(l -> levels.put(l.getXp(), l.getLevel()));
        return levels;
    }
    
    public int calculateLevel(long experience) {
        if (maximalThresholdAsLong() <= experience) {
            return levelMap.lastEntry().getValue();
        }
        return levelMap.get(levelMap.floorKey(experience));
    }
    
    public Long getThreshold(Long experience) {
        long maximalThresholdLong = maximalThresholdAsLong();
        if (experience >= maximalThresholdAsLong()) {
            return maximalThresholdLong;
        }
        return levelMap.ceilingKey(experience);
    }
    
    @Override
    public NavigableMap<Integer, Long> getLevelThresholders() {
        var map = levelDAO.findAll().stream().collect(Collectors.toMap(LevelEntity::getLevel, LevelEntity::getXp));
        return new TreeMap<>(map);
    }
    /**
     * 
     * @deprecated
     */
    @Override
    @Deprecated(since = "0.1.0-ALPHA", forRemoval = true)
    @ForRemoval(since = "0.1.0-ALPHA", expectedRemovalVersion = "0.1.5")
    public long getNextThreshold(Long experience) {
        long maximalThresholdLong = maximalThresholdAsLong();
        if (experience >= maximalThresholdAsLong()) {
            return maximalThresholdLong;
        }
        Long threshold = levelMap.higherKey(experience);
        return threshold == null ? maximalThresholdLong : threshold;
    }
    
    public long maximalThresholdAsLong() {
        return levelDAO.maximalThreshold().longValue();
    }
    
    @Override
    public BigInteger getNextThreshold(BigInteger current) {
        Integer value = getLevels().ceilingEntry(current.longValue()).getValue();
        return BigInteger.valueOf(value);
    }
    
    @Override
    public BigInteger maximalThreshold() {
        return levelDAO.maximalThreshold();
    }
    
    @Override
    public BigInteger thresholdForLevel(int level) {
        long xp = levelDAO.findOne(level).getXp();
        return BigInteger.valueOf(xp);
    }
    
    @Override
    public int maxLevelNumber() {
        return levelDAO.getMaxlevel();
    }
    
    @PostConstruct
    private void bindFactory() {
        instance = this; // NOSONAR
        Logger.getLogger().debug("Binding LevelCalculator#factory to" + this);
    }

    /**
     * @see bg.sarakt.attributes.services.LevelService#getLevel(long)
     */
    @Override
    public Level getLevel(long classId) {
        return service.getLevel(classId);
    }

    /**
     * @see bg.sarakt.attributes.services.LevelService#lookupLevel(long, long)
     */
    @Override
    public Level lookupLevel(long experience, long classId) {
        return service.lookupLevel(experience, classId);
    }

    /**
     * @see bg.sarakt.attributes.services.LevelService#getLevel(long, int)
     */
    @Override
    public Level getLevel(long classId, int levelNumber) {
        return service.getLevel(classId, levelNumber);
    }

    /**
     * @see bg.sarakt.attributes.services.LevelService#getLevelNode(long, int)
     */
    @Override
    public LevelNode getLevelNode(long classId, int levelNumber) {
        return service.getLevelNode(classId, levelNumber);
    }
    
    /**
     * @see bg.sarakt.attributes.services.LevelService#getAllLevelNodes(long)
     */
    @Override
    public SequencedCollection<LevelNode> getAllLevelNodes(long classId) {
        return service.getAllLevelNodes(classId);
    }
}
