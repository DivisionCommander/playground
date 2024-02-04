/*
 * LevelImpl.java
 *
 * created at 2024-01-30 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.levels.impl;

import java.math.BigInteger;

import bg.sarakt.attributes.levels.LevelNode;
import bg.sarakt.characters.attributes1.UnitClass;
import bg.sarakt.storing.hibernate.LevelDAO;
import bg.sarakt.storing.hibernate.entities.LevelEntity;

public abstract class LevelImpl {

    private BigInteger   currentExperience;
    private final String classID;

    private LevelNode previousLevel;
    private LevelNode currentLevel;
    private LevelNode nextLevel;

    protected LevelImpl() {
        this(1);
    }

    protected LevelImpl(int level) {
        this(level, UnitClass.getDefault());
    }

    protected LevelImpl(int level, UnitClass unitClassArg) {
        this.classID = unitClassArg.unitClassId();
        this.previousLevel = getLevelNode(level - 1);
        this.currentLevel = getLevelNode(level);
        this.nextLevel = getLevelNode(level + 1);
    }

    public boolean earnExperience(BigInteger amount) {
        if (amount == null || amount.signum() < 0) {
            throw new IllegalArgumentException("Invalid amount of experience!");
        }
        BigInteger experience = currentExperience.add(amount);
        while (experience.compareTo(nextLevel.experienceThreshold()) >= 1) {
            LevelNode oldPrevious = previousLevel;
            previousLevel = currentLevel;
            currentLevel = nextLevel;
            nextLevel = getLevelNode(nextLevel.getLevelNumber() + 1);
            // collet modifiers to add and remove. Move to if-statement with previous and
            // collect only permanents. When reach max level to this xp, then apply
        }

        return false;
    }

    public BigInteger getCurrentExperience() { return this.currentExperience; }

    protected abstract LevelNode getLevelNode(int level);

    public LevelNode viewPreviousLevel() {
        return this.previousLevel;
    }

    public LevelNode viewCurrentLevel() {
        return this.currentLevel;
    }

    public LevelNode viewNextLevel() {
        return this.nextLevel;
    }
}