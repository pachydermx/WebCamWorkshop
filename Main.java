import java.io.*;
import java.util.*;
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

import java.awt.*;
import java.awt.event.*;

public class Main extends JFrame{

    private CameraDisplay cd = null;
    private ControlPanel cp = null;
    private ImageList il = null;
    private VideoIndicator vi = null;

    public Main(){
        // init objects
        cd = new CameraDisplay();
        cp = new ControlPanel();
        il = new ImageList();
        vi = new VideoIndicator();

        JPanel lc = new JPanel();
        lc.setPreferredSize(new Dimension(640, 768));
        lc.setLayout(new BorderLayout());

        // set window
        this.setTitle("CamViewer");
        this.setSize(1024, 768);
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // set up components
        cd.vi = vi;

        lc.add(cd, BorderLayout.NORTH);
        lc.add(cp, BorderLayout.SOUTH);
        this.add(lc, BorderLayout.WEST);
        this.add(il, BorderLayout.EAST);

        this.setGlassPane(vi);
        vi.setVisible(true);
        // display window
        this.setVisible(true);

        // for test
        il.addImage();

    }

    public static void main(String[] args) {
        Main main = new Main();
    }
}