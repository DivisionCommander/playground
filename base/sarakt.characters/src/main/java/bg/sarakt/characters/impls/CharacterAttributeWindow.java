/*
 * DefaultWindow.java
 *
 * created at 2023-11-28 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.characters.impls;

import java.awt.GridLayout;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import bg.sarakt.characters.GameCharacter;
import bg.sarakt.characters.attributes.AttributeValuePair;
import bg.sarakt.characters.attributes1.AttributeMap;
import bg.sarakt.logging.Logger;

/**
 *
 * @deprecated still need better version. New version will be implemented when
 *             the new version of {@link GameCharacter} is live, then this class
 *             will be removed
 */
@Deprecated(forRemoval = false)
public class CharacterAttributeWindow
{

    private static final Logger LOGGER  = Logger.getLogger();
    private static final int    DEFAULT = 10;

    private final JFrame        window;
    private JPanel              panel;
    private PlayerCharacterImpl pl;
    private JTextField          field;
    private JButton             btnLevel;

    public CharacterAttributeWindow()
    {
        window = new JFrame("Main Window");
        pl = new PlayerCharacterImpl("Player");
        window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2));

        btnLevel = new JButton("Level Up");
        field = new JTextField("10");
        populate();
        asd();
        window.setSize(600, 800);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setVisible(true);
    }

    private void populate()
    {
        panel.removeAll();
        AttributeMap attributeMap = pl.getAttributeMap();
        Collection<AttributeValuePair> allAtributes = attributeMap.getAllAtributes();
        for (AttributeValuePair a : allAtributes)
        {
            panel.add(new JLabel(a.attribute().fullName()));
            panel.add(new JLabel(String.valueOf(a.value())));
        }
        panel.add(field);
        panel.add(btnLevel);
        window.add(panel);
        window.validate();
        window.repaint();
    }

    private void asd()
    {
        btnLevel.addActionListener((a) -> {
            int experience;
            String text = field.getText();
            try
            {
                experience = Integer.parseInt(text);
            }
            catch (NumberFormatException e)
            {
                LOGGER.error("Cannot parse " + text + ". Use default "+DEFAULT);
                experience = DEFAULT;
            }
            pl.gainExperience(experience);
            window.setTitle(pl.name()+"\t  Level="+pl.level()+"  EXP="+pl.currentExperience());
            populate();
        });

    }
}
