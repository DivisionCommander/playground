/*
 * AppRunner.java
 *
 * created at 2024-02-10 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes;

import static bg.sarakt.attributes.utils.Attributes.*;

import java.lang.reflect.Type;
import java.util.Map;

import bg.sarakt.attributes.impl.AttributeProvider;
import bg.sarakt.attributes.impl.SimpleAttributeFormulaImpl;
import bg.sarakt.attributes.levels.Level;
import bg.sarakt.attributes.primary.PrimaryAttribute;
import bg.sarakt.attributes.resources.impl.DefaultResourceAttributesProvider;
import bg.sarakt.attributes.secondary.SecondaryAttribute;
import bg.sarakt.attributes.secondary.SecondaryAttributeBuilder;
import bg.sarakt.attributes.services.AttributeProviderService;
import bg.sarakt.attributes.utils.Attributes;
import bg.sarakt.base.ApplicationContextProvider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages =
    {
            "bg.sarakt"
})
public class AppRunner implements Attributes {
    
    private static final int SWITCH = 4;
    ApplicationContext       ctx    = ApplicationContextProvider.getApplicationContext();
    
    public static void main(String[] args) {
        System.out.println("RUNNNN");
        SpringApplication.run(AppRunner.class, args);
        System.out.println("Running");
        new AppRunner().run();
    }
    
    public void run() {
        switch (SWITCH)
        {
        case 1:
            test1();
            break;
        case 2:
            test2();
            break;
        case 3:
            test3();
            break;
        case 4:
            test4();
            break;
        default:
            System.out.println("Unsupported");
            break;
        
        }
    }
    
    private void test4() {
        SimpleAttributeFormulaImpl formula = new SimpleAttributeFormulaImpl();
        formula.addAttributeFormula(PrimaryAttribute.STRENGTH, 10);
        SecondaryAttributeBuilder sab= new SecondaryAttributeBuilder();
        sab.setId(100).setName(NAME_ACCURACY).setAbbreviation(ABBR_ACCURACY).setGroup(AttributeGroup.PHYSICAL).setDescription(DESC_ACCURACY)
                .addFormula(1, formula);
        SecondaryAttribute secondaryAttribute = sab.build();
        System.out.println(secondaryAttribute);
    }
    
    private void test3() {
        Level bean = ctx.getBean(Level.class);
        System.out.println(bean);
        
    }
    
    private void test2() {
        AttributeProvider ap = ctx.getBean(AttributeProvider.class, 12);
        Map<String, Attribute> attributes = ap.getAttributeMap();
        for (var e : attributes.entrySet()) {
            System.out.println(e.getValue().getClass() + "\t" + e.getKey() + "\t" + e);
        }
        Attribute attribute = ap.getAttribute(NAME_MANA).get();
        System.out.println(attribute);
        // ap.refreshAttributes();
        // Attribute attribute = ap.getAttribute(NAME_STRENGTH);
        // System.out.println(attribute);
        // PrimaryAttribute attribute2 = ap.getAttribute(NAME_STRENGTH,
        // PrimaryAttribute.class);
        // System.out.println(attribute.equals(attribute2));
        //
        // var v = ap.getAttribute(NAME_MANA, ResourceAttribute.class);
        // System.err.println("var " + v);
    }
    
    private static void test1() {
        
        ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
        
        var map = ctx.getBeansOfType(AttributeProviderService.class);
        for (var entry : map.entrySet()) {
            AttributeProviderService<?> value = entry.getValue();
            
            Type[] types = value.getClass().getGenericInterfaces();
            for (Type t : types) {
                System.out.println(entry.getKey() + "\t=\t" + t);
            }
        }
        DefaultResourceAttributesProvider bean = ctx.getBean(DefaultResourceAttributesProvider.class);
        System.out.println(bean);
        System.out.println("=============");
        map = ctx.getBeansOfType(AttributeProviderService.class);
        for (var entry : map.entrySet()) {
            AttributeProviderService<?> value = entry.getValue();
            
            Type[] types = value.getClass().getGenericInterfaces();
            for (Type t : types) {
                System.out.println(entry.getKey() + "\t=\t" + t);
            }
        }
        //
        // AttributeProvider ap = ctx.getBean(AttributeProvider.class, 12);
        // Attribute attribute = ap.getAttribute(NAME_STRENGTH);
        // System.out.println(attribute);
        // PrimaryAttribute attribute2 = ap.getAttribute(NAME_STRENGTH,
        // PrimaryAttribute.class);
        // System.out.println(attribute.equals(attribute2));
    }
}
