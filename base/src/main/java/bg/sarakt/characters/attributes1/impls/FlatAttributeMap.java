/*
 * FlatAttributeMap.java
 *
 * created at 2024-01-09 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.characters.attributes1.impls;

import java.util.HashMap;
import java.util.Map;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.base.Pair;

public class FlatAttributeMap {

    private final Map<Attribute, Integer> map;

    FlatAttributeMap() {
        this.map = new HashMap<>();
    }

    public Integer getValue(Attribute attribute) {
        map.putIfAbsent(attribute, 0);
        return map.get(attribute);
    }

     class FlatPair implements Pair<Attribute, Integer> {

        private final Attribute attribute;
        private Integer         value;

        private FlatPair(Attribute attr) {
            this(attr, 0);
        }

        private FlatPair(Attribute attr, Integer value) {
            this.attribute = attr;
            this.value = value;
        }

        /**
         * @see bg.sarakt.base.Pair#left()
         */
        @Override
        public Attribute left() {
            return attribute;
        }

        @Override
        public Integer right() {
            return value;
        }

    }
}
