/*
 * SimpleLevelNode.java
 *
 * created at 2024-02-09 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.levels.simple;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.levels.LevelNode;
import bg.sarakt.attributes.primary.PrimaryAttribute;
import bg.sarakt.attributes.services.LevelService;
import bg.sarakt.base.ApplicationContextProvider;

import org.springframework.lang.Nullable;

class SimpleLevelNode implements LevelNode {
    
    private SimpleLevelNode previous;
    private SimpleLevelNode next;
    
    private final int                          points;
    private final int                          level;
    private final BigInteger                   threshold;
    private List<AttributeModifier<Attribute>> modifiers;
    
    protected SimpleLevelNode(SimpleLevelNode previous, BigInteger nextThreshold) {
        this(previous.level, nextThreshold);
        this.previous = previous;
    }
    
    protected SimpleLevelNode(int level, BigInteger threshold) {
        this.level = level;
        this.threshold = threshold;
        this.points = 8 + (level / 5);
        modifiers = new ArrayList<>();
    }
    
    @Override
    public final int getUnallocatedPoints() { return points; }
    
    /**
     * @see bg.sarakt.attributes.levels.LevelNode#experienceThreshold()
     */
    @Override
    public BigInteger experienceThreshold() {
        return this.threshold;
    }
    
    @Override
    public final Integer getLevelNumber() { return level; }
    
    protected SimpleLevelNode previous() {
        return null;
    }
    
    protected void generateNext() {
        LevelService service = ApplicationContextProvider.getApplicationContext().getBean(LevelService.class);
        BigInteger nextThreshold = service.getNextThreshold(threshold);
        if (nextThreshold != null) {
            SimpleLevelNode node = new SimpleLevelNode(this, nextThreshold);
            new SimpleLevelsHelper().injectModifiers(node);
            this.next = node;
        }
    }
    
    @Nullable
    protected SimpleLevelNode getPrevious() { return this.previous; }
    
    @Nullable
    protected SimpleLevelNode getNext() { return this.next; }
    
    @Override
    public List<AttributeModifier<Attribute>> getAllModifiers() { return modifiers; }
    
    void injectModifiers(Collection<AttributeModifier<Attribute>> mods) {
        this.modifiers.addAll(mods);
    }
    
    void injectModifier(AttributeModifier<Attribute> mod) {
        this.modifiers.add(mod);
    }
    
    /**
     * @see bg.sarakt.attributes.levels.LevelNode#getPreviousNode()
     */
    @Override
    public Optional<LevelNode> getPreviousNode() { return Optional.ofNullable(previous); }
    
    /**
     * @see bg.sarakt.attributes.levels.LevelNode#getNextNode()
     */
    @Override
    public Optional<LevelNode> getNextNode() { return Optional.ofNullable(next); }
    
    /**
     * @see bg.sarakt.attributes.levels.LevelNode#getPermanentBonuses()
     */
    @Override
    public Map<PrimaryAttribute, BigInteger> getPermanentBonuses() {// NOSONAR
        throw new UnsupportedOperationException("DEPRECATED!");
    }
}
