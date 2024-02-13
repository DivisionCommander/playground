/*
 * Level.java
 *
 * created at 2024-01-31 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.levels;

import java.math.BigInteger;

import bg.sarakt.base.ApplicationContextProvider;
import bg.sarakt.base.utils.ForRemoval;

import org.springframework.lang.Nullable;

public interface Level extends LevelNode {
    
    /**
     * 
     * @deprecated use {@link Level#gainExperience(BigInteger)} instead
     */
    @Deprecated(since = "0.1.0-ALPHA", forRemoval = true)
    @ForRemoval(since = "0.1.0-ALPHA", expectedRemovalVersion = "0.1.5")
    boolean earnExperience(BigInteger amount);
    
    LevelUp gainExperience(BigInteger amount);
    
    BigInteger currentExperience();
    
    /**
     * @deprecated Use {@link LevelNode#getPreviousNode()} instead.
     */
    @Deprecated(since = "0.1.0-ALPHA", forRemoval = true)
    @ForRemoval(since = "0.1.0-ALPHA", expectedRemovalVersion = "0.1.5")
    @Nullable
    LevelNode viewPreviousLevel();
    
    LevelNode viewCurrentLevel();
    
    /**
     * @deprecated Use {@link LevelNode#getNextNode()} instead.
     */
    @Deprecated(since = "0.1.0-ALPHA", forRemoval = true)
    @ForRemoval(since = "0.1.0-ALPHA", expectedRemovalVersion = "0.1.5")
    @Nullable
    LevelNode viewNextLevel();
    
    public static final Level DEFAULT_LEVEL = ApplicationContextProvider.getApplicationContext().getBean(Level.class);
}