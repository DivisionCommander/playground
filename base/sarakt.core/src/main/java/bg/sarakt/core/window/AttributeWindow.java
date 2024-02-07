/*
 * AttributeWindow.java
 *
 * created at 2024-01-22 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.core.window;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
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
import javax.swing.SwingConstants;

import bg.sarakt.attributes.Attribute;
import bg.sarakt.attributes.AttributeFactory;
import bg.sarakt.attributes.AttributeService;
import bg.sarakt.attributes.CharacterAttributeMap;
import bg.sarakt.attributes.ResourceAttribute;
import bg.sarakt.attributes.SecondaryAttribute;
import bg.sarakt.attributes.impl.AttributeMapImpl;
import bg.sarakt.attributes.impl.PrimaryAttribute;
import bg.sarakt.attributes.impl.ResourceAttributeEntry;
import bg.sarakt.attributes.levels.impl.DummyLevelImpl;
import bg.sarakt.base.utils.Dummy;
import bg.sarakt.base.windows.AbstractWindow;
import bg.sarakt.logging.Logger;

@Dummy(description = "GUI for AttributeMap testing")
public class AttributeWindow extends AbstractWindow{

    private static final Logger LOGGER  = Logger.getLogger();
    private static final BigInteger DEFAULT = BigInteger.TEN;
    private final AtomicInteger freePoints;
    private final JTextField fldFreePoints;

    private final Map<Attribute, JTextField> map;
    private final Map<ResourceAttribute, ResourceInfo> nfo;
    private final Set<JButton>                         buttonsPA;
    private JLabel                                     lbl1;
    private JLabel                                     lbl2;
    private JLabel                                     lbl3;
    
    private final DummyLevelImpl level;

    private final AttributeMapImpl attributes;
    private JTextField fldXP = new JTextField(8);
    
    private AttributeService attributeService = AttributeFactory.getInstance();

    public AttributeWindow(AttributeMapImpl attrMap, DummyLevelImpl lvl) {
        super();
        map  = new TreeMap<>(Attribute.getComparator());
        nfo = new HashMap<>();
        this.buttonsPA = new HashSet<>();
        this.freePoints = new AtomicInteger(lvl.getUnallocatedPonts());
        this.lbl1 = new JLabel("Free points: " + freePoints.get());
        this.lbl2 = new JLabel();
        this.lbl3 = new JLabel();
        this.level = lvl;
        attributes= attrMap;
        fldFreePoints  = new JTextField(4);
        fldFreePoints.setText(String.valueOf(freePoints));
        init();
        fldFreePoints.setEditable(false);
        fldFreePoints.setDisabledTextColor(Color.BLUE);
    }

    /**
     * @see bg.sarakt.attributes.window.AbstractWindow#populate()
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
        panelLeft.add(iterate(lbl1, "Primary Attributes: ", PrimaryAttribute.getAllPrimaryAttributes()));
        panelLeft.add(iterate(lbl2, "Resource Attributes", attributeService.getResourceAttribute()));
        panel.add(panelLeft);
        panel.add(iterate(lbl3, "Secondary Attributes", attributeService.getSecondaryAttributes()));
        
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

    private <A extends Attribute> JPanel iterate(JLabel lbl, String category, Iterable<A> iterable) {
        Set<A> set = new TreeSet<>();
        iterable.forEach(set::add);
        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel(category));
        panel.add(lbl);
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
        JPanel panelLeft = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        JPanel panelRight = new JPanel();
        JLabel lbl = new JLabel(ra.fullName() + ":");
        lbl.setToolTipText(ra.description());
        panelLeft.add(lbl);
        ResourceAttributeEntry e = attributes.get(ra);
        JTextField fieldCurrent = new JTextField(4);
        fieldCurrent.setText(e.getCurrentValue().toString());
        fieldCurrent.setEditable(false);
        JTextField fieldMax = new JTextField(4);
        fieldMax.setText(e.getMaxValue().toString());
        panelLeft.add(fieldCurrent);
        panelLeft.add(new JLabel("/"));
        panelLeft.add(fieldMax);
        fieldMax.setEnabled(false);
        fieldMax.setEditable(false);
        fieldMax.setBackground(Color.blue);
        panelRight.add(add(ra));
        panelRight.add(remove(ra));
        panel.add(panelLeft);
        panel.add(panelRight);
        map.put(ra, fieldCurrent);
        nfo.put(ra, new ResourceInfo(ra, fieldCurrent, fieldMax));
    }
    
    private void show(SecondaryAttribute sa, JPanel panel) {
        JPanel panelLeft = new JPanel(new FlowLayout(FlowLayout.RIGHT));
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

        JPanel panelLeft = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel panelRight = new JPanel();
        JLabel lbl = new JLabel("PA:" + pa.fullName());
        lbl.setToolTipText(pa.description());
        panelLeft.add(lbl);
        JTextField fld = new JTextField(attributes.getCurrentAttributeValue(pa).toString(), 4);
        fld.setEditable(false);
        if (pa != PrimaryAttribute.EXPERIENCE) {
            panelLeft.add(fld);
            panelRight.add(add(pa));
            panelRight.add(remove(pa));
        }
        else {
            fld.setColumns(7);
            fld.setHorizontalAlignment(SwingConstants.RIGHT);
            panelRight.add(fld);
        }
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
        buttonsPA.add(btn);

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
        for (var e : nfo.entrySet()) {
            ResourceAttributeEntry rae = attributes.get(e.getKey());
            e.getValue().fldCurrent.setText(rae.getCurrentValue().toString());
            e.getValue().fldmax.setText(rae.getMaxValue().toString());
        }
        Integer points = freePoints.get();
        fldFreePoints.setText(points.toString());
        lbl1.setText("Free points: " + freePoints.get());
        buttonsPA.forEach(b -> b.setEnabled(freePoints.get() > 0));
        window.setTitle("Attributes for level# " + attributes.getLevelNumber());
        
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
           // populate();
           refreshValues();
       });
       return xp;
    }

    /**
     * @see bg.sarakt.attributes.window.AbstractWindow#addListeners()
     */
    @Override
    protected void addListeners() {
        // NO-OP for now

    }
    
    private class ResourceInfo {
        
        private JTextField fldmax;
        private JTextField fldCurrent;
        
        private ResourceInfo(ResourceAttribute ra, JTextField current, JTextField max) {
            this.fldCurrent = current;
            this.fldmax = max;
        }
    }

}