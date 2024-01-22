/*
 * AbstractWindow.java
 *
 * created at 2024-01-22 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.base.window;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public abstract class AbstractWindow {

    protected final JFrame window;
    protected JPanel       panel;

    protected AbstractWindow() {
        window = new JFrame("Attributes");
        window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        panel = new JPanel();
        panel.setLayout(new GridLayout(0, 3));

//        window.setLocationRelativeTo(null);
//        validate();
//        window.setVisible(true);
    }

protected void init() {
        populate();
        addListeners();
        validate();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

}
    protected void validate() {
        window.pack();
        window.validate();
        window.repaint();
    }

    protected abstract void populate();

    protected abstract void addListeners();
}



