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
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.CharacterAttributeMap;
import bg.sarakt.attributes.ResourceAttribute;
import bg.sarakt.attributes.SecondaryAttribute;
import bg.sarakt.attributes.impl.AttributeFactory;
import bg.sarakt.attributes.impl.AttributeMapImpl;
import bg.sarakt.attributes.impl.PrimaryAttribute;
import bg.sarakt.attributes.levels.impl.DummyLevelImpl;
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


    private final CharacterAttributeMap attributes;
    private JTextField fldXP = new JTextField(8);

    public AttributeWindow(AttributeMapImpl attrMap, DummyLevelImpl lvl) {
        super();
        map  = new TreeMap<>(Attribute.getComparator());
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
        JPanel panelLeft = new JPanel();
        panelLeft.setLayout(new BoxLayout(panelLeft, BoxLayout.Y_AXIS));
        panelLeft.add(iterate("Primary Attributes: ", PrimaryAttribute.getAllPrimaryAttributes()));
        panelLeft.add(iterate("Resource Attributes", AttributeFactory.getInstance().getResourceAttribute()));
        panel.add(panelLeft);
        panel.add(iterate("Secondary Attributes", AttributeFactory.getInstance().getSecondaryAttributes()));
        
        JPanel p1 = new JPanel();
        p1.add(new JLabel("FP: "));
        p1.add(fldFreePoints);
        p1.add(new JLabel("Experience"));
        fldXP.setText(String.valueOf(DEFAULT));
        p1.add(fldXP);
        p1.add(createBtnXP());
        
        panel.validate();
        contentPane.add(panel);
        contentPane.add(p1);
        window.setTitle("Attributes for level# " + attributes.getLevelNumber());
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

    private <A extends Attribute> JPanel iterate(String category, Iterable<A> iterable) {
        Set<A> set = new TreeSet<>();
        iterable.forEach(set::add);
        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel(category));
        panel.add(new JLabel(level.getLevelNumber().toString()+" // "+level.getExperience().toString()));
        panel.add(new JSeparator());
        panel.add(new JSeparator());
        set.forEach(a -> this.show(a, panel));
        panel.add(new JSeparator());
        panel.add(new JSeparator());
        return panel;
    }


    private void show(Attribute a, JPanel panel) {
         switch (a)
         {
         case PrimaryAttribute pa:
             show(pa, panel);
             break;
         case SecondaryAttribute sa:
             show(sa, panel);
             break;
         case ResourceAttribute ra:
             show(ra, panel);
             break;
         default:
             break;
         }
     }
    private void show(ResourceAttribute ra, JPanel panel) {
        JPanel panelLeft = new JPanel();
        JPanel panelRight = new JPanel();
        JLabel lbl = new JLabel("RA:" + ra.fullName());
        lbl.setToolTipText(ra.description());
        panelLeft.add(lbl);
        JTextField fld = new JTextField(8);
        fld.setText(attributes.getCurrentAttributeValue(ra).toString());
        fld.setEditable(false);
        panelLeft.add(fld);
        panelRight.add(add(ra));
        panelRight.add(remove(ra));
        panel.add(panelLeft);
        panel.add(panelRight);
        map.put(ra, fld);
    }
    
    private void show(SecondaryAttribute sa, JPanel panel) {
        JPanel panelLeft = new JPanel();
        JPanel panelRight = new JPanel();
        JLabel lbl = new JLabel("SA:" + sa.fullName());
        lbl.setToolTipText(sa.description());
        panelLeft.add(lbl);
        JTextField fld = new JTextField(8);
        fld.setText(attributes.getCurrentAttributeValue(sa).toString());
        fld.setEditable(false);
        fld.setEnabled(false);
        panelLeft.add(fld);
        panelRight.add(add(sa));
        panelRight.add(remove(sa));
        panel.add(panelLeft);
        panel.add(panelRight);
        map.put(sa, fld);
    }
    
    private void show(PrimaryAttribute pa, JPanel panel) {

        JPanel panelLeft = new JPanel();
        JPanel panelRight = new JPanel();
        JLabel lbl = new JLabel("PA:" + pa.fullName());
        lbl.setToolTipText(pa.description());
        panelLeft.add(lbl);
        JTextField fld = new JTextField(3);
        fld.setText(attributes.getCurrentAttributeValue(pa).toString());
        fld.setEditable(false);
        panelLeft.add(fld);
        panelRight.add(add(pa));
        panelRight.add(remove(pa));
        panel.add(panelLeft);
        panel.add(panelRight);
        map.put(pa, fld);
        
    }

    private JButton remove(Attribute a) {
        JButton button = new JButton("-") ;
        button.addActionListener(l -> LOGGER.debug("NO-OP!"));
        return button;
    }

    private JButton add(Attribute a) {
        JButton btn = new JButton("+");
        
        btn.addActionListener(l -> LOGGER.debug("NO-OP!"));
        return btn;
    }
    
    private JButton add(PrimaryAttribute pa) {
        JButton btn = new JButton("+");

        btn.addActionListener(l -> addListener(pa, btn));
        return btn;
    }


    private void addListener(PrimaryAttribute primaryAttribute, JButton button) {
        if (map.containsKey(primaryAttribute)) {
            JTextField field = map.get(primaryAttribute);
            String text = field.getText();
            try {
                var intV = Integer.parseInt(text);
                field.setText(String.valueOf(++intV));
                
            } catch (Exception e) {
                var intV = Double.parseDouble(text);
                field.setText(String.valueOf(++intV));
            }
            freePoints.decrementAndGet();
            fldFreePoints.setText(freePoints.toString());
            attributes.addPermanentBonus(primaryAttribute, BigInteger.ONE);
            button.setEnabled(freePoints.get() > 0);
            refreshValues();
        }
    }
    
    private void refreshValues() {
        for (Entry<Attribute, JTextField> e : map.entrySet()) {
            BigDecimal currentAttributeValue = attributes.getCurrentAttributeValue(e.getKey());
            e.getValue().setText(currentAttributeValue.toString());
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
        }
        catch (NumberFormatException e)
        {
            LOGGER.error("Cannot parse " + text + ". Use default "+DEFAULT);
            e.printStackTrace();
            experience = DEFAULT;
        }
        attributes.earnExperience(experience);
        int unallocatedPonts = level.getUnallocatedPonts();
        freePoints.addAndGet(unallocatedPonts);
           populate();
           // refreshValues();
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