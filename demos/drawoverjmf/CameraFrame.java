import java.io.*;
import java.awt.geom.Line2D;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.image.*;
import javax.imageio.*;
import javax.media.*;
import javax.media.CaptureDeviceInfo;
import javax.media.control.*;
import javax.media.format.*;
import javax.media.util.*;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.media.cdm.CaptureDeviceManager;
import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.*;
import javax.swing.border.*;
import javax.accessibility.*;

import java.awt.event.*;
/**
 * 
 * 
 */
public class CameraFrame extends JApplet {

    /**
     * 
     */
    private static final long serialVersionUID = 4154913998966529628L;

    public void init() {
        try {

            Manager.setHint(Manager.LIGHTWEIGHT_RENDERER, new Boolean(true));

            CaptureDeviceInfo deviceInfo = CaptureDeviceManager
                    .getDevice("vfw:Microsoft WDM Image Capture (Win32):0");
            Player player = Manager.createRealizedPlayer(deviceInfo
                    .getLocator());
            player.start();

            // Gets a component from the player that can show the actual
            // streaming from the
            // webcam.
            Component videoScreen = player.getVisualComponent();

            // adds the component that displays the streaming to the applet.
            videoScreen.setBounds(new Rectangle(10, 10, 100, 100));
            add(videoScreen);
            
            JPanel glassPane = new GlassPanel();
            glassPane.setBounds(new Rectangle(0, 0, 500, 500));

            this.setGlassPane(glassPane);
            glassPane.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public static void main(String[] args) {
        CameraFrame cf = new CameraFrame();
    }
}