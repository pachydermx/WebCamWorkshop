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

    public Main(){
        // init objects
        cd = new CameraDisplay();
        cp = new ControlPanel();
        il = new ImageList();

        // set window
        this.setTitle("CamViewer");
        this.setSize(1024, 768);
        // display window
        this.setVisible(true);
    }

    public static void main(String[] args) {
        Main main = new Main();
    }
}