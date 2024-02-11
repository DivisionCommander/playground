/*
 * LevelService.java
 *
 * created at 2024-02-09 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.services;

import java.math.BigInteger;
import java.util.NavigableMap;
import java.util.SequencedCollection;

import bg.sarakt.attributes.levels.Level;
import bg.sarakt.attributes.levels.LevelNode;
import bg.sarakt.base.utils.ForRemoval;

public interface LevelService {
    
    /**
     * @deprecated Use {@link LevelService#maximalThreshold()}
     */
    @Deprecated(since = "0.1.0-ALPHA", forRemoval = true)
    @ForRemoval(since = "0.1.0-ALPHA", expectedRemovalVersion = "0.1.5")
    long EXPERIENCE_CAP = 1999L;
    
    /**
     * 
     * @deprecated Use the reversed implementation in
     *             {@link LevelService#getLevelThresholders()}
     */
    @Deprecated(since = "0.1.0-ALPHA", forRemoval = true)
    @ForRemoval(since = "0.1.0-ALPHA", expectedRemovalVersion = "0.1.5")
    NavigableMap<Long, Integer> getLevels();
    
    /**
     * Get {@link Level} structure for the provided unit class Id at first level;
     * 
     * @param classId
     * 
     * @return
     */
    Level getLevel(long classId);
    
    Level lookupLevel(long experience, long classId);
    
    /**
     * Get {@link Level} structure for the provided unit class Id at requested level
     * number.
     * 
     * @param classId
     * @param levelNumber
     * 
     * @return
     */
    Level getLevel(long classId, int levelNumber);
    
    LevelNode getLevelNode(long classId, int levelNumber);
    
    SequencedCollection<LevelNode> getAllLevelNodes(long classId);
    
    NavigableMap<Integer, Long> getLevelThresholders();
    
    BigInteger thresholdForLevel(int level);
    
    @Deprecated(since = "0.1.0-ALPHA", forRemoval = true)
    @ForRemoval(since = "0.1.0-ALPHA", expectedRemovalVersion = "0.1.5")
    long getNextThreshold(Long experience);
    
    BigInteger getNextThreshold(BigInteger currentThreshold);
    
    int maxLevelNumber();
    
    BigInteger maximalThreshold();
}
