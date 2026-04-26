package site.meowcat.ui.swing;

import javax.swing.*;

/**
 * Utilities for creating and running Swing windows.
 */
public final class WindowKit {
    private WindowKit() {}

    /**
     * Creates a new wrapped JFrame with the specified title and size.
     * @param title the window title
     * @param w the window width
     * @param h the window height
     * @return an AppWindow instance wrapping the created JFrame
     */
    public static AppWindow window(String title, int w, int h) {
        JFrame frame = new JFrame(title);
        frame.setSize(w, h);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return AppWindow.wrap(frame);
    }
    
    /**
     * Creates a simple wrapped JFrame with a title and a content component.
     * The frame is packed to fit its content.
     * @param title the window title
     * @param content the JComponent to add to the frame
     * @return an AppWindow instance wrapping the created JFrame
     */
    public static AppWindow simple(String title, JComponent content) {
        JFrame frame = new JFrame(title);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(content);
        frame.pack();
        
        return AppWindow.wrap(frame);
    }
    
    /**
     * Runs a Runnable on the Event Dispatch Thread (EDT).
     * @param ui the UI task to run
     */
    public static void run(Runnable ui) {SwingUtilities.invokeLater(ui);}
    
}
