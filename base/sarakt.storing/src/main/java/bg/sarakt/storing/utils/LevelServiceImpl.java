/*
 * LevelServiceImpl.java
 *
 * created at 2024-02-11 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.storing.utils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.SequencedCollection;
import java.util.TreeMap;

import bg.sarakt.attributes.levels.Level;
import bg.sarakt.attributes.levels.LevelNode;
import bg.sarakt.attributes.services.LevelService;
import bg.sarakt.base.utils.ForRemoval;
import bg.sarakt.storing.hibernate.entities.LevelNodeEntity;
import bg.sarakt.storing.hibernate.interfaces.ILevelDAO;
import bg.sarakt.storing.hibernate.interfaces.ILevelNodeDAO;
import bg.sarakt.storing.hibernate.mapping.LevelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

@Service
@Scope(scopeName = BeanDefinition.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
@Primary
public class LevelServiceImpl implements LevelService {
    
    private ILevelNodeDAO nodes;
    private ILevelDAO     levelDAO;
    private LevelMapper   mapper;
    
    @Autowired
    public LevelServiceImpl(ILevelDAO dao, ILevelNodeDAO nodeDao) {
        nodes = nodeDao;
        levelDAO = dao;
        mapper = new LevelMapper();
    }
    
    /**
     * @see bg.sarakt.attributes.services.LevelService#getAllLevelNodes(long)
     */
    @Override
    public SequencedCollection<LevelNode> getAllLevelNodes(long classId) {
        List<LevelNodeEntity> nodesForClass = nodes.getClassNodes(classId);
        return mapper.mapNodes(nodesForClass);
    }
    
    /**
     * @see bg.sarakt.attributes.services.LevelService#getLevel(long)
     */
    @Override
    public Level getLevel(long classId) {
        return getLevel(classId, 1);
    }
    
    /**
     * @see bg.sarakt.attributes.services.LevelService#lookupLevel(long, long)
     * 
     *      TODO refactor query to be inclusive to current node
     */
    @Override
    public Level lookupLevel(long experience, long classId) {
        LevelNodeEntity currentNode = nodes.lookUpNode(experience, classId);
        List<LevelNodeEntity> nextNodes = nodes.lookUpNodes(experience, classId);
        List<LevelNodeEntity> allNodes = new ArrayList<>(nextNodes.size() + 1);
        allNodes.add(currentNode);
        allNodes.addAll(nextNodes);
        return mapper.mapLevel(allNodes, experience);
    }
    
    /**
     * @see bg.sarakt.attributes.services.LevelService#getLevel(long, int)
     */
    @Override
    public Level getLevel(long classId, int levelNumber) {
        List<LevelNodeEntity> classNodesFromLevel = nodes.getClassNodesFromLevel(classId, levelNumber);
        return mapper.mapLevel(classNodesFromLevel);
    }
    
    /**
     * @see bg.sarakt.attributes.services.LevelService#getLevelNode(long, int)
     */
    @Override
    public LevelNode getLevelNode(long classId, int levelNumber) {
        LevelNodeEntity v = nodes.getClassNode(classId, levelNumber);
        return mapper.mapNode(v);
    }
    
    /**
     * 
     * @see bg.sarakt.attributes.services.LevelService#getLevels()
     * 
     * @deprecated
     */
    @Override
    @Deprecated(since = "0.1.0-ALPHA", forRemoval = true)
    @ForRemoval(since = "0.1.0-ALPHA", expectedRemovalVersion = "0.1.5")
    public NavigableMap<Long, Integer> getLevels() {
        NavigableMap<Long, Integer> map = new TreeMap<>();
        levelDAO.findAll().stream().forEach(e -> map.put(e.getXp(), e.getLevel()));
        return map;
    }
    
    /**
     * @see bg.sarakt.attributes.services.LevelService#getLevelThresholders()
     */
    @Override
    public NavigableMap<Integer, Long> getLevelThresholders() {
        NavigableMap<Integer, Long> map = new TreeMap<>();
        levelDAO.findAll().stream().forEach(lvl -> map.put(lvl.getLevel(), lvl.getXp()));
        return map;
    }
    
    /**
     * @see bg.sarakt.attributes.services.LevelService#thresholdForLevel(int)
     */
    @Override
    public BigInteger thresholdForLevel(int level) {
        return levelDAO.thresholdForLevel(level);
    }
    
    /**
     * @see bg.sarakt.attributes.services.LevelService#getNextThreshold(java.lang.Long)
     * 
     * @deprecated
     */
    @Deprecated(since = "0.1.0-ALPHA", forRemoval = true)
    @ForRemoval(since = "0.1.0-ALPHA", expectedRemovalVersion = "0.1.5")
    @Override
    public long getNextThreshold(Long experience) {
        return levelDAO.nextThreshold(BigInteger.valueOf(experience)).longValue();
    }
    
    /**
     * @see bg.sarakt.attributes.services.LevelService#getNextThreshold(java.math.BigInteger)
     */
    @Override
    public BigInteger getNextThreshold(BigInteger currentThreshold) {
        return levelDAO.nextThreshold(currentThreshold);
    }
    
    /**
     * @see bg.sarakt.attributes.services.LevelService#maxLevelNumber()
     */
    @Override
    public int maxLevelNumber() {
        return levelDAO.getMaxlevel();
    }
    
    /**
     * @see bg.sarakt.attributes.services.LevelService#maximalThreshold()
     */
    @Override
    public BigInteger maximalThreshold() {
        return levelDAO.maximalThreshold();
    }
}
