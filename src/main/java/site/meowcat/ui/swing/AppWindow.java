package site.meowcat.ui.swing;

import javax.swing.*;
import java.awt.*;

/**
 * A wrapper for JFrame that provides a fluent API for window configuration.
 */
public class AppWindow {
    private final JFrame frame;

    /**
     * Constructs an AppWindow wrapping the given JFrame.
     * @param frame the JFrame to wrap
     */
    AppWindow(JFrame frame) {
        this.frame = frame;
    }

    /**
     * Creates a new AppWindow instance wrapping the given JFrame.
     * @param frame the JFrame to wrap
     * @return a new AppWindow instance
     */
    public static AppWindow wrap(JFrame frame) {
        return new AppWindow(frame);
    }

    /**
     * Makes the window visible on the Event Dispatch Thread.
     * @return this AppWindow instance
     */
    public AppWindow show() {
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
        return this;
    }

    /**
     * Hides the window on the Event Dispatch Thread.
     * @return this AppWindow instance
     */
    public AppWindow hide() {
        SwingUtilities.invokeLater(() -> frame.setVisible(false));
        return this;
    }

    /**
     * Sets the window title.
     * @param title the new title
     * @return this AppWindow instance
     */
    public AppWindow title(String title) {
        frame.setTitle(title);
        return this;
    }

    /**
     * Sets the window size.
     * @param w width
     * @param h height
     * @return this AppWindow instance
     */
    public AppWindow size(int w, int h) {
        frame.setSize(w, h);
        return this;
    }

    /**
     * Centers the window on the screen.
     * @return this AppWindow instance
     */
    public AppWindow center() {
        frame.setLocationRelativeTo(null);
        return this;
    }

    /**
     * Returns the underlying JFrame.
     * @return the raw JFrame
     */
    public JFrame raw() {
        return frame;
    }

    /**
     * Adds a component to the window.
     * @param c the component to add
     * @return this AppWindow instance
     */
    public AppWindow add(Component c) {
        frame.add(c);
        return this;
    }

    /**
     * Sets the layout manager for the window.
     * @param lm the layout manager to use
     * @return this AppWindow instance
     */
    public AppWindow layout(LayoutManager lm) {
        frame.setLayout(lm);
        return this;
    }


}
