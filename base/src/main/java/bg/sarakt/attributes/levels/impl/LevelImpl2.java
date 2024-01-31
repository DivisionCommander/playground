/*
 * LevelImpl.java
 *
 * created at 2024-01-27 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.levels.impl;

import java.math.BigInteger;

import bg.sarakt.attributes.levels.LevelNode;

public abstract class LevelImpl2 {

    private boolean capped;
    private BigInteger threshold;
    private BigInteger currentExperience;


    private LevelNode previousLevel;
    private LevelNode currentLevel;
    private LevelNode nextLevel;


    LevelImpl2() {
        this(BigInteger.TEN, BigInteger.ZERO);
    }

    public LevelImpl2(BigInteger threshold, BigInteger currentExperience) {
        this.threshold = threshold;
        this.currentExperience = currentExperience;
    }

    public void gainExperience(BigInteger amount) {
        if (amount == null || amount.signum() < 0) {
            throw new IllegalArgumentException("Invalid amount of experience!");
        }
        if(amount.intValue() ==0) {
            return;
        }
        currentExperience = currentExperience.add(amount);
        //Rethinks it
        while(capped || threshold.compareTo(currentExperience)<=0) {
            levelUp();
        }

    }

    public  void levelUp() {
        LevelNode temp = previousLevel;
        previousLevel = currentLevel;
        nextLevel = getNextLevel();

    }

    /**
     * @return
     */
    protected abstract LevelNode getNextLevel();

    public int getCurrentLevelNumber() { return currentLevel.getLevelNumber(); }
}
