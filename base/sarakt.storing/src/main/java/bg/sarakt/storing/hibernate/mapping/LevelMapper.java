/*
 * LevelMapper.java
 *
 * created at 2024-02-13 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.storing.hibernate.mapping;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SequencedCollection;
import java.util.SortedSet;
import java.util.TreeSet;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeModifier;
import bg.sarakt.attributes.ModifierLayer;
import bg.sarakt.attributes.ModifierType;
import bg.sarakt.attributes.impl.AttributeModifierRecord;
import bg.sarakt.attributes.levels.Level;
import bg.sarakt.attributes.levels.LevelNode;
import bg.sarakt.attributes.primary.PrimaryAttribute;
import bg.sarakt.attributes.resources.ResourceAttribute;
import bg.sarakt.attributes.secondary.SecondaryAttribute;
import bg.sarakt.attributes.services.AttributeService;
import bg.sarakt.storing.hibernate.entities.AdditionalAttrValueEntity;
import bg.sarakt.storing.hibernate.entities.LevelEntity;
import bg.sarakt.storing.hibernate.entities.LevelNodeEntity;
import bg.sarakt.storing.hibernate.entities.PrimaryAttributeValuesEntity;

public class LevelMapper {
    
    private AttributeService provider;
    
    public LevelMapper(AttributeService service) {
        this.provider = service;
    }
    

    public Level mapLevel(Collection<LevelNodeEntity> nodes, long experience) {
        SequencedCollection<LevelNode> mapNodes = mapNodes(nodes);
        return new LevelImpl(mapNodes.getFirst(), experience);
    }
    
    public Level mapLevel(Collection<LevelNodeEntity> nodesArg) {
        return mapLevel(nodesArg, 0L);
    }
    
    public SequencedCollection<LevelNode> mapNodes(Collection<LevelNodeEntity> nodesArg) {
        SortedSet<LevelNodeEntity> nodes = sortNodes(nodesArg);
        SequencedCollection<LevelNode> levelNodes = new LinkedList<>();
        PreloadedLevelNodeImpl previous = null;
        Iterator<LevelNodeEntity> it = nodes.iterator();
        do {
            var node= it.next();
            PreloadedLevelNodeImpl currentNode = mapNodeInternal(node);
            currentNode.setPrevious(previous);
            if (previous != null) {
                previous.setNext(currentNode);
            }
            previous = currentNode;
            levelNodes.add(currentNode);
        }while(it.hasNext());
        
        return levelNodes;
    }
    
    public LevelNode mapNode(LevelNodeEntity node) {
        return mapNodeInternal(node);
    }
    
    private PreloadedLevelNodeImpl mapNodeInternal(LevelNodeEntity node) {
        LevelEntity level = node.getLevelEntity();
        
        PreloadedLevelNodeImpl mapped = new PreloadedLevelNodeImpl(level.getXp(), level.getFreePoints(), level.getLevel());
        PrimaryAttributeValuesEntity primary = node.getPrimary();
        Map<PrimaryAttribute, BigInteger> map = primary.toMap();
        mapped.addPrimaryBonuses(map);
        mapped.addModifiers(mapPrimary(map));
        List<AdditionalAttrValueEntity> additional = node.getAdditional();
        for (AdditionalAttrValueEntity add : additional) {
            Attribute attr = provider.ofName(add.getAttribute());
            BigDecimal value = add.getValue();
            if (attr != null && value != null) {
                AttributeModifier<Attribute> mod = switch (attr)
                {
                case PrimaryAttribute pa -> new AttributeModifierRecord<>(pa, value, ModifierType.PRIMARY_PERMANENT, ModifierLayer.CLASS_LAYER);
                case ResourceAttribute ra -> new AttributeModifierRecord<>(ra, value, ModifierType.COEFFICIENT, ModifierLayer.CLASS_LAYER);
                case SecondaryAttribute sa -> new AttributeModifierRecord<>(sa, value, ModifierType.FLAT_VALUE, ModifierLayer.CLASS_LAYER);
                default -> null;
                };
                mapped.addModifier(mod);
            }
        }
        
        return mapped;
    }
    
    private List<AttributeModifier<Attribute>> mapPrimary(Map<PrimaryAttribute, BigInteger> map) {
        
        ModifierType type = ModifierType.PRIMARY_PERMANENT;
        ModifierLayer layer = ModifierLayer.CLASS_LAYER;
        List<AttributeModifier<Attribute>> modifiers = new ArrayList<>(PrimaryAttribute.count());
        for (Entry<PrimaryAttribute, BigInteger> e : map.entrySet()) {
            BigInteger val = e.getValue();
            BigDecimal value = val == null ? BigDecimal.ZERO : new BigDecimal(e.getValue());
            modifiers.add(new AttributeModifierRecord<>(e.getKey(), value, type, layer));
        }
        return modifiers;
    }
    
    public SortedSet<LevelNodeEntity> sortNodes(Collection<LevelNodeEntity> toSort) {
        if (toSort instanceof SortedSet<LevelNodeEntity> sorted) {
            return sorted;
        }
        SortedSet<LevelNodeEntity> nodes = new TreeSet<>(compare());
        nodes.addAll(toSort);
        return nodes;
        
    }
    
    private static Comparator<? super LevelNodeEntity> compare() {
        return (n1, n2) -> Integer.compare(n1.getLevelEntity().getLevel(), n2.getLevelEntity().getLevel());
    }
    
}
