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

public class CameraDisplay extends JPanel{

    public static Player player = null;
    private CaptureDeviceInfo deviceInfo = null;
    private MediaLocator mediaLocator = null;
	private Component component = null;
    private JPanel videoPanel = null;

    public VideoIndicator vi = null;

    private int interval = 100;
    private int threshold = 10;


    private JLayeredPane layeredPane;
	
    String deviceName = "vfw:Microsoft WDM Image Capture (Win32):0";
    // Creates a new instance of CameraTest 

	public CameraDisplay(){
		this.setPreferredSize( new Dimension(640, 480));

        Manager.setHint(Manager.LIGHTWEIGHT_RENDERER, new Boolean(true));

    	deviceInfo = CaptureDeviceManager.getDevice(deviceName);
    	mediaLocator = deviceInfo.getLocator();
    	
		try{
			player = Manager.createRealizedPlayer(mediaLocator);// 利用mediaLocator 获取一个player
			component = player.getVisualComponent();
			if (component != null){

                layeredPane = new JLayeredPane();
                layeredPane.setPreferredSize(new Dimension(640, 480));
                JLabel testLabel = new JLabel("Hello World");
                testLabel.setBounds(0, 0, 640, 100);

                videoPanel = new JPanel();
                videoPanel.add(component, BorderLayout.NORTH);
                this.add(videoPanel);

				player.start();
			}
		}catch(Exception e){
            e.printStackTrace();
        }
	}

}