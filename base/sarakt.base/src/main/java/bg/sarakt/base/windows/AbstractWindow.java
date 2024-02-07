/*
 * AbstractWindow.java
 *
 * created at 2024-01-22 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */
package bg.sarakt.base.windows;

import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public abstract class AbstractWindow {

    protected final JFrame window;
    protected JPanel       contentPane;
    protected JPanel       panel;

    protected AbstractWindow() {
        window = new JFrame();
        contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        window.setContentPane(contentPane);
        window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2));

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



