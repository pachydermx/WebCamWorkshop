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

    // vi will be assigned by Main after launched
    public VideoIndicator vi = null;

    private int interval = 100;

    public ImageProcessor ip = null;

    private JLayeredPane layeredPane;

    private ProcessCircle pc;
    private static int[] imageMatrix;

	
    String deviceName = "vfw:Microsoft WDM Image Capture (Win32):0";
    // Creates a new instance of CameraTest 

	public CameraDisplay(){
		this.setPreferredSize( new Dimension(640, 480));

        Manager.setHint(Manager.LIGHTWEIGHT_RENDERER, new Boolean(true));

    	deviceInfo = CaptureDeviceManager.getDevice(deviceName);
    	mediaLocator = deviceInfo.getLocator();

        // create image processor
        ip = new ImageProcessor();
    	
		try{
			player = Manager.createRealizedPlayer(mediaLocator);
			component = player.getVisualComponent();
			if (component != null){
                layeredPane = new JLayeredPane();
                layeredPane.setPreferredSize(new Dimension(640, 480));
                videoPanel = new JPanel();
                videoPanel.add(component, BorderLayout.NORTH);
                this.add(videoPanel);

				player.start();

                // start process circle
                pc = new ProcessCircle();
                // use start() instead of run()
                pc.start();
			}
		}catch(Exception e){
            e.printStackTrace();
        }
	}

    private class ProcessCircle extends Thread{

        public Image grabImage(){
            FrameGrabbingControl frameGrabber = (FrameGrabbingControl)player.getControl("javax.media.control.FrameGrabbingControl");
            Buffer buf = frameGrabber.grabFrame();
            Image img = (new BufferToImage((VideoFormat) buf.getFormat()).createImage(buf));
            return img;
        }

        public void run(){

            try{
                Thread.sleep(5000);
                while (true){
                    // wait for next circle
                    Thread.sleep(interval);
                    imageMatrix = ip.process(this.grabImage());
                    if (vi != null){
                        vi.setMatrix(imageMatrix);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}