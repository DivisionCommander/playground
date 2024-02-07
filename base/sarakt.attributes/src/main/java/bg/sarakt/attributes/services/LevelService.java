/*
 * LevelService.java
 *
 * created at 2024-02-06 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.services;

import java.util.NavigableMap;

/**
 * TODO short description for LevelService.
 * <p>
 * Long description for LevelService.
 * 
 * @author IceDragon
 */
public interface LevelService {
    
    long EXPERIENCE_CAP = 1999L;
    
    NavigableMap<Long, Integer> getLevels();
    
    int calculateLevel(long experience);
    
    Long getThreshold(Long experience);
    
    long getNextThreshold(Long experience);
    
}
