/*
 * FormulaSerializer.java
 *
 * created at 2023-11-29 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.base.utils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bg.sarakt.characters.attributes1.AttributeFormula;
import bg.sarakt.characters.attributes1.impls.SimpleAttributeFormulaImpl;
import bg.sarakt.logging.Logger;
import bg.sarakt.logging.LoggerFactory;

public final class FormulaSerializer
{

    private static final Logger LOGGER = LoggerFactory.getLogger();

    private static final FormulaSerializer INSTANCE = new FormulaSerializer();
    private final ObjectMapper             MAPPER   = new ObjectMapper();
    File                                   baseDirectory;

    public static FormulaSerializer getInstance()
    {
        return INSTANCE;
    }

    public String seriallizeToString(AttributeFormula formula)
    {
        try
        {
            return MAPPER.writeValueAsString(formula);
        }
        catch (Exception e)
        {
            LOGGER.error("Cannot seriallize " + formula + " due to: " + e.getMessage());
        }
        return formula.toString();
    }

    public byte[] seriallize(AttributeFormula formula)
    {
        try
        {
            return MAPPER.writeValueAsBytes(formula);
        }
        catch (Exception e)
        {
            LOGGER.error("Cannot seriallize " + formula + " due to: " + e.getMessage());
        }
        return null;
    }

    public AttributeFormula deseriallize(byte[] formula)
    {
        try
        {
            return MAPPER.readValue(formula, SimpleAttributeFormulaImpl.class);
        }
        catch (Exception e)
        {
            {
                LOGGER.error(e);
            }
            return null;
        }
    }

    public AttributeFormula deseriallize(String formula)
    {
        try
        {
            return MAPPER.readValue(formula, SimpleAttributeFormulaImpl.class);
        }
        catch (Exception e)
        {
            LOGGER.error(e);
        }
        return null;
    }

    public <T extends AttributeFormula> T deseriallize(String formula, Class<T> clazz)
    {
        try
        {
            return MAPPER.readValue(formula,clazz);
        }
        catch (Exception e)
        {
            LOGGER.error(e);
        }
        return null;
    }

    // public String writeFormula(AdvancedAttributeFormulaImpl formula) throws
    // JacksonException
    // {
    // return MAPPER.writeValueAsString(formula);
    // }
    //
    // public File writeToFile(AdvancedAttributeFormulaImpl formula) throws
    // IOException
    // {
    // String formulaName = formula.getName();
    // File output = new File(baseDirectory, "Formula-" + formulaName + ".json");
    // if ( !output.exists())
    // {
    // output.createNewFile();
    // }
    // MAPPER.writeValue(output, formula);
    // return output;
    // }

    @SuppressWarnings("unchecked")
    public <T> T readFormula(Class<?> clazz, String str) throws JacksonException
    {
        return (T) MAPPER.readValue(str, clazz);
    }

    private FormulaSerializer()
    {
        Path p = Paths.get(System.getProperty("user.dir"), "formulas");
        p.toFile().mkdirs();

        baseDirectory = p.toFile();// new File(System.getProperty("user.dir"), "formulas");
    }

    /**
     * @param formula
     * @throws JsonProcessingException
     */
    public byte[] writeFormula(AttributeFormula formula) throws JsonProcessingException
    {
        return MAPPER.writeValueAsBytes(formula);
    }
}
