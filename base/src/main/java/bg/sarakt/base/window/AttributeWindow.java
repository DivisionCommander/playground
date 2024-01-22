/*
 * AttributeWindow.java
 *
 * created at 2024-01-22 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.base.window;

import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeMap;
import bg.sarakt.attributes.impl.AttributeFactory;
import bg.sarakt.attributes.impl.PrimaryAttribute;
import bg.sarakt.base.utils.Dummy;
import bg.sarakt.logging.Logger;

@Dummy(description = "GUI for AttributeMap testing")
public class AttributeWindow extends AbstractWindow{

    private static final Logger LOGGER  = Logger.getLogger();
    private static final int    DEFAULT = 10;


    private final Map<Attribute, JComponent> map;
    private final AttributeMap<Attribute> attributes;
    private JTextField fldXP;
    private JButton btnXP;

    public AttributeWindow(AttributeMap<Attribute> attrMap) {
        super();
        map  = new TreeMap<>();
        attributes= attrMap;

        init();
    }

    /**
     * @see bg.sarakt.base.window.AbstractWindow#populate()
     */
    @Override
    protected void populate() {
        panel.removeAll();
        iterate("Primary Attributes: ", PrimaryAttribute.getAllPrimaryAttributes());
        iterate("Resource Attributes", AttributeFactory.getInstance().getResourceAttribute());
//        iterate("Secondary Attributes", AttributeFactory.getInstance().getSecondaryAttributes());

        panel.add(new JLabel("Experience"));
        fldXP = new JTextField(10);
        panel.add(fldXP);
        btnXP = new JButton("Add XP");
        panel.add(btnXP);

        window.add(panel);
    }

    private <A extends Attribute> void iterate(String category, Iterable<A> it) {
        panel.add(new JLabel(category));
        panel.add(new JLabel());
        panel.add(new JLabel());
        it.forEach(this::show);
        panel.add(new JSeparator());
        panel.add(new JSeparator());
        panel.add(new JSeparator());
    }

    private <A extends Attribute> void show(A a) {

        panel.add(new JLabel(a.fullName()));
        JTextField fld = new JTextField(attributes.getCurrentAttributeValue(a).toString());
        fld.setEditable(false);
        panel.add(fld);
        map.put(a, fld);   JTextField fldDesc = new JTextField(a.description());
        fldDesc.setEnabled(false);
        fldDesc.setEditable(false);
        panel.add(fldDesc);

    }



    /**
     * @see bg.sarakt.base.window.AbstractWindow#addListeners()
     */
    @Override
    protected void addListeners() {
        btnXP.addActionListener(l->{
            int experience;
            String text = fldXP.getText();
            try
            {
                experience = Integer.parseInt(text);
            }
            catch (NumberFormatException e)
            {
                LOGGER.error("Cannot parse " + text + ". Use default "+DEFAULT);
                experience = DEFAULT;
            }
//            pl.gainExperience(experience);
//            window.setTitle(pl.name()+"\t  Level="+pl.level()+"  EXP="+pl.currentExperience());
//            populate();
        });
        // TODO Auto-generated method stub

    }
}



