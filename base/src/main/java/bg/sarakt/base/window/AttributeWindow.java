/*
 * AttributeWindow.java
 *
 * created at 2024-01-22 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.base.window;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.math.BigInteger;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.experience.impl.DummyLevelImpl;
import bg.sarakt.attributes.impl.AttributeFactory;
import bg.sarakt.attributes.impl.AttributeMapImpl;
import bg.sarakt.attributes.impl.PrimaryAttribute;
import bg.sarakt.base.utils.Dummy;
import bg.sarakt.logging.Logger;

@Dummy(description = "GUI for AttributeMap testing")
public class AttributeWindow extends AbstractWindow{

    private static final Logger LOGGER  = Logger.getLogger();
    private static final BigInteger DEFAULT = BigInteger.TEN;
    private final AtomicInteger freePoints;
    private final JTextField fldFreePoints;

    private final Map<Attribute, JTextField> map;
    private final DummyLevelImpl level;


    private final AttributeMapImpl attributes;
    private JTextField fldXP = new JTextField(8);
//    private JButton btnXP = new JButton("Add XP");

    public AttributeWindow(AttributeMapImpl attrMap, DummyLevelImpl lvl) {
        super();
        map  = new TreeMap<>();
        this.level = lvl;
        attributes= attrMap;
        freePoints = new AtomicInteger(lvl.getUnallocatedPonts());
        fldFreePoints  = new JTextField(4);
        fldFreePoints.setText(String.valueOf(freePoints));
        init();
        fldFreePoints.setEditable(false);
        fldFreePoints.setDisabledTextColor(Color.BLUE);
    }

    /**
     * @see bg.sarakt.base.window.AbstractWindow#populate()
     */
    @Override
    protected void populate() {
        for (Component component : panel.getComponents()) {
           rem(component);
        }
        panel.removeAll();
        panel.repaint();

        map.clear();
        iterate("Primary Attributes: ", PrimaryAttribute.getAllPrimaryAttributes());
        iterate("Resource Attributes", AttributeFactory.getInstance().getResourceAttribute());
        iterate("Secondary Attributes", AttributeFactory.getInstance().getSecondaryAttributes());

        JPanel p1 = new JPanel();
        p1.add(new JLabel("FP: "));
        p1.add(fldFreePoints);
        JPanel p2 = new JPanel();
        p2.add(new JLabel("Experience"));
        fldXP.setText(String.valueOf(DEFAULT));
        p2.add(fldXP);
        p2.add(createBtnXP());
        panel.add(p1);
        panel.add(p2);

        panel.validate();
        window.add(panel);
    }

    private void rem(Component c)
    {
        if(c instanceof Container ct)
        {
            for( Component com : ct.getComponents()) {
                rem(com);
            }
            ct.removeAll();
        }
    }

    private <A extends Attribute> void iterate(String category, Iterable<A> iterable) {
        panel.add(new JLabel(category));
        panel.add(new JLabel(level.getLevelNumber().toString()+" // "+level.getExperience().toString()));
        iterable.forEach(this::show);
        panel.add(new JSeparator());
        panel.add(new JSeparator());
    }

    private <A extends Attribute> void show(A a) {

        JPanel pnl = new JPanel();
        pnl.add(new JLabel(a.fullName()));
        JTextField fld = new JTextField(attributes.getCurrentAttributeValue(a).toString());
        fld.setEditable(false);
        pnl.add(fld);
        pnl.add(add(a));
        pnl.add(new JButton("-"));
        panel.add(pnl);
        map.put(a, fld);   JTextField fldDesc = new JTextField(a.description());
        fldDesc.setEnabled(false);
        fldDesc.setEditable(false);
        panel.add(fldDesc);

    }


    private JButton add(Attribute a) {
        JButton btn = new JButton("+");

        btn.addActionListener(l -> att(a));
        return btn;
    }


    // lambda err, move to stand-alone method
    private void att(Attribute a) {
        if (map.containsKey(a)) {
            JTextField field = map.get(a);
            String text = field.getText();
            int intV = Integer.parseInt(text);
            field.setText(String.valueOf(++intV));
            freePoints.decrementAndGet();
            fldFreePoints.setText(freePoints.toString());
        }
    }

    private JButton createBtnXP() {
       JButton xp  = new JButton("XP");
       xp.addActionListener(l->{

        BigInteger experience;
        String text = fldXP.getText();
        try
        {
            experience= new BigInteger(text);
//            experience = Integer.parseInt(text);
        }
        catch (NumberFormatException e)
        {
            LOGGER.error("Cannot parse " + text + ". Use default "+DEFAULT);
            e.printStackTrace();
            experience = DEFAULT;
        }
        System.out.println(experience);
        System.out.println("repopulate");
        attributes.earnExperience(experience);
        int unallocatedPonts = level.getUnallocatedPonts();
        freePoints.addAndGet(unallocatedPonts);
        populate();
       });
       return xp;
    }

    /**
     * @see bg.sarakt.base.window.AbstractWindow#addListeners()
     */
    @Override
    protected void addListeners() {
        // NO-OP for now

    }


}



