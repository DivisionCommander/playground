/*
 * PrimaryAttributeValuesEntity.java
 *
 * created at 2024-01-28 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.storing.hibernate.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

import bg.sarakt.attributes.primary.PrimaryAttribute;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "PrimaryAttributeValues")
@Table(name = "tbl_attributes_values_primary")
public class PrimaryAttributeValuesEntity implements Serializable {

    /** field <code>serialVersionUID</code> */
    private static final long serialVersionUID = 202401280256L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "values_id")
    private UUID id;

    @Column(name = "strength")
    private BigInteger strength;
    @Column(name = "agility")
    private BigInteger agility;
    @Column(name = "constitution")
    private BigInteger constitution;
    @Column(name = "intelligence")
    private BigInteger intelligence;
    @Column(name = "wisdom")
    private BigInteger wisdom;
    @Column(name = "psionic")
    private BigInteger psionic;
    @Column(name = "spirit")
    private BigInteger spirit;
    @Column(name = "will_power")
    private BigInteger will;

    public PrimaryAttributeValuesEntity fromIntegerMap(Map<PrimaryAttribute, BigInteger> values) {
        this.strength = values.get(PrimaryAttribute.STRENGTH);
        this.agility = values.get(PrimaryAttribute.AGILITY);
        this.constitution = values.get(PrimaryAttribute.CONSTITUTION);
        this.intelligence = values.get(PrimaryAttribute.INTELLIGENCE);
        this.wisdom = values.get(PrimaryAttribute.WISDOM);
        this.psionic = values.get(PrimaryAttribute.PSIONIC);
        this.spirit = values.get(PrimaryAttribute.SPIRIT);
        this.will = values.get(PrimaryAttribute.WILL);
        return this;
    }

    public PrimaryAttributeValuesEntity fromNumericMap(Map<PrimaryAttribute, Number> values) {
        this.strength = BigInteger.valueOf(values.get(PrimaryAttribute.STRENGTH).longValue());
        this.agility = BigInteger.valueOf(values.getOrDefault(PrimaryAttribute.AGILITY, 0).longValue());
        this.constitution = BigInteger.valueOf(values.getOrDefault(PrimaryAttribute.CONSTITUTION, 0).longValue());
        this.intelligence = BigInteger.valueOf(values.getOrDefault(PrimaryAttribute.INTELLIGENCE, 0).longValue());
        this.wisdom = BigInteger.valueOf(values.getOrDefault(PrimaryAttribute.WISDOM, 0).longValue());
        this.psionic = BigInteger.valueOf(values.getOrDefault(PrimaryAttribute.PSIONIC, 0).longValue());
        this.spirit = BigInteger.valueOf(values.getOrDefault(PrimaryAttribute.SPIRIT, 0).longValue());
        this.will = BigInteger.valueOf(values.getOrDefault(PrimaryAttribute.WILL, 0).longValue());
        return this;
    }

    public UUID getId() { return id; }

    public void setId(UUID id) { this.id = id; }

    public BigInteger getStrength() { return strength; }

    public BigInteger getAgility() { return agility; }

    public BigInteger getConstitution() { return constitution; }

    public BigInteger getIntelligence() { return intelligence; }

    public BigInteger getWisdom() { return wisdom; }

    public BigInteger getPsionic() { return psionic; }

    public BigInteger getSpirit() { return spirit; }

    public BigInteger getWill() { return will; }

    public void setStrength(BigInteger strength) { this.strength = strength; }

    public void setAgility(BigInteger agility) { this.agility = agility; }

    public void setConstitution(BigInteger constitution) { this.constitution = constitution; }

    public void setIntelligence(BigInteger intelligence) { this.intelligence = intelligence; }

    public void setWisdom(BigInteger wisdom) { this.wisdom = wisdom; }

    public void setPsionic(BigInteger psionic) { this.psionic = psionic; }

    public void setSpirit(BigInteger spirit) { this.spirit = spirit; }

    public void setWill(BigInteger will) { this.will = will; }


    public Map<PrimaryAttribute, BigInteger> toMap() {
        Map<PrimaryAttribute, BigInteger> map = new EnumMap<>(PrimaryAttribute.class);
        map.put(PrimaryAttribute.STRENGTH, strength);
        map.put(PrimaryAttribute.AGILITY, agility);
        map.put(PrimaryAttribute.CONSTITUTION, constitution);
        map.put(PrimaryAttribute.INTELLIGENCE, intelligence);
        map.put(PrimaryAttribute.WISDOM, wisdom);
        map.put(PrimaryAttribute.PSIONIC, psionic);
        map.put(PrimaryAttribute.SPIRIT, spirit);
        map.put(PrimaryAttribute.WILL, will);

        return map;
    }

    @Override
    public String toString() {
        return "ValuesPrimaryAttributesEntity [id="
               + id
               + ", strength="
               + strength
               + ", agility="
               + agility
               + ", constitution="
               + constitution
               + ", intelligence="
               + intelligence
               + ", wisdom="
               + wisdom
               + ", psionic="
               + psionic
               + ", spirit="
               + spirit
               + ", will="
               + will
               + "]";
    }

}
