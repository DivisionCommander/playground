/*
 * SecondaryAttributeImpl.java
 *
 * created at 2023-11-29 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.impl;

import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;

import bg.sarakt.attributes.AttributeFormula;
import bg.sarakt.attributes.AttributeGroup;
import bg.sarakt.attributes.IterableAttributeMap;
import bg.sarakt.attributes.SecondaryAttribute;

public final class SecondaryAttributeImpl extends AbstractAttribute implements SecondaryAttribute
{

    /** field <code>serialVersionUID</code> */
    private static final long serialVersionUID = 202401202125L;


    private final NavigableMap<Integer, AttributeFormula> formulas = new TreeMap<>();


    public SecondaryAttributeImpl(long id, String fullName, String abbreviation, AttributeGroup group, String description)
    {
        super(id, fullName, abbreviation, group, description);
    }

    public SecondaryAttributeImpl(String fullName, String abbreviation, AttributeGroup type, String description)
    {
        this(System.currentTimeMillis(), fullName, abbreviation, type, description);
    }

    /**
     * @see bg.sarakt.attributes.impl.AbstractAttribute#getId()
     */
    @Override
    public long getId() { return super.getId(); }

    public void addFormula(int level, AttributeFormula formula) {
        formulas.put(level, formula);
    }


    /**
     * @see bg.sarakt.attributes.SecondaryAttribute#getFormula()
     */
    @Override
    public AttributeFormula getFormula(int level) {
        return formulas.floorEntry(level).getValue();
    }

    public void putFormulas(SortedMap<Integer, AttributeFormula> formulas) {
        this.formulas.putAll(formulas);
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append('[').append(getId()).append(']').append(fullName()).append(" -[").append(abbreviation()).append("]-\t").append(group()).append(group().ordinal());

        return sb.toString();
    }

    /**
     * TODO: Generalize #toHibernateEntity extract somewhere else, maybe in the
     * 
     * AttributeFactory
     */
    /*
     * public SecondaryAttributeEntity toHibernateEntity() {
     * 
     * SecondaryAttributeEntity aae = new SecondaryAttributeEntity();
     * aae.setName(fullName()); aae.setAbbr(abbreviation()); aae.setGroup(group() );
     * List<AttributeFormulaEntity> preparedFormulas =
     * formulas.entrySet().stream().map(this::convert).collect(Collectors.toList());
     * aae.setDescription(description()); aae.setFormulas(preparedFormulas);
     * 
     * return aae; }
     */

    /*
     * private AttributeFormulaEntity convert(Entry<Integer, AttributeFormula>
     * formulaPerLevel) { int level = formulaPerLevel.getKey(); AttributeFormula
     * formula = formulaPerLevel.getValue(); byte[] byteFormula = null; try {
     * byteFormula = FormulaSerializer.getInstance().writeFormula(formula); } catch
     * (JsonProcessingException jpse) {
     * Logger.getLogger().debug("FIXME:LOGG AND process error!"); }
     * 
     * AttributeFormulaEntity aafe = new AttributeFormulaEntity();
     * aafe.setAttributeId(getId()); aafe.setLevel(level);
     * aafe.setFormula(byteFormula); return aafe; }
     */


    @Override
    public SecondaryAttributeEntry getEntry(IterableAttributeMap<PrimaryAttribute, PrimaryAttributeEntry> primaryAttribute) {
        return new SecondaryAttributeEntry(this, primaryAttribute);
    }

    /**
     * @deprecated since its use only in constructor from entity, it will be removed
     *             alongs it.
     */
    /*
     * @Deprecated(forRemoval = true, since = "0.0.8") private NavigableMap<Integer,
     * AttributeFormula> convert(List<AttributeFormulaEntity> formulas) {
     * NavigableMap<Integer, AttributeFormula> results = new TreeMap<>(); for (var
     * formula : formulas) { try { byte[] bytearrayFormula = formula.getFormula();
     * if (bytearrayFormula != null && bytearrayFormula.length > 0) {
     * AttributeFormula deseriallized =
     * FormulaSerializer.getInstance().deseriallize(bytearrayFormula);
     * results.put(formula.getLevel(), deseriallized);
     * 
     * } } catch (Exception e) {
     * System.err.println("FIXME:LOGG AND process error!"); } } return results; }
     */
}