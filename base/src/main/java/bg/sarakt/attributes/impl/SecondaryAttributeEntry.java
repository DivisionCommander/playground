/*
 * SecondaryAttributeEntry.java
 *
 * created at 2024-01-15 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeMapEntry;
import bg.sarakt.attributes.ModifierLayer;
import bg.sarakt.attributes.SecondaryAttribute;
import bg.sarakt.characters.Level;
import bg.sarakt.characters.attributes1.AttributeFormula;

public class SecondaryAttributeEntry extends AbstractAttributeMapEntry<SecondaryAttribute> {

    private final PrimaryAttributeMap primaryAttributes;

    public SecondaryAttributeEntry(SecondaryAttribute attribute, PrimaryAttributeMap primary, Level level) {
        super(attribute, level);
        this.primaryAttributes = primary;
    }

    /**
     * @see bg.sarakt.attributes.impl.AbstractAttributeMapEntry#getBaseValueForLayer(bg.sarakt.attributes.ModifierLayer)
     */
    @Override
    protected BigDecimal getBaseValueForLayer(ModifierLayer layer) {
        Map<Attribute, Number> map = new HashMap<>();
        for (AttributeMapEntry<PrimaryAttribute> pa : primaryAttributes) {
            BigDecimal valueForLayer = pa.getValueForLayer(layer);
            map.put(pa.getAttribute(), valueForLayer);
        }
        AttributeFormula formula = getAttribute().getFormula(level.getLevelNumber());
        return formula.calculate(map);
    }

    /**
     * @see bg.sarakt.attributes.AttributeMapEntry#levelUp(bg.sarakt.characters.Level)
     */
    @Override
    public void levelUp(Level level) {
        super.level = level;
        recalculate();
    }

    /**
     * @see bg.sarakt.attributes.impl.AbstractAttributeMapEntry#getBasicValue()
     */
    @Override
    public BigDecimal getBasicValue() { return getBaseValueForLayer(ModifierLayer.BASELINE_LAYER); }

}