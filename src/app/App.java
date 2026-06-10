package app;

import view.DashboardFrame;
import com.formdev.flatlaf.FlatLightLaf;

import database.DatabaseInitializer;

import javax.swing.*;
import java.awt.Color;

/**
 * Main Application Class.
 * Contains the main method to start the application.
 */
public class App {

    public static void main(String[] args) {
        DatabaseInitializer.initialize();
        // Run GUI construction on the Event-Dispatching Thread (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Set FlatLaf Look and Feel
                    FlatLightLaf.setup();
                    // Customize some global UI properties
                    UIManager.put( "Button.arc", 10 );
                    UIManager.put( "Component.arc", 10 );
                    UIManager.put( "TextComponent.arc", 10 );
                    UIManager.put( "Table.selectionBackground", new Color(230, 240, 255) );
                    UIManager.put( "Table.selectionForeground", Color.BLACK );
                } catch (Exception e) {
                    e.printStackTrace();
                }

                DashboardFrame frame = new DashboardFrame();
                frame.setVisible(true);
            }
        });
    }
}
