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
public class CamTest extends JFrame{
    public  static Player player = null;
    private CaptureDeviceInfo deviceInfo = null;
    private MediaLocator mediaLocator = null;
	private Component component = null;
	private JPanel vedioPanel = null;
	
    String   deviceName   =   "vfw:Microsoft WDM Image Capture (Win32):0";
    // Creates a new instance of CameraTest 
    public CamTest() {
        init();
    }
    public void init(){
    	deviceInfo = CaptureDeviceManager.getDevice(deviceName);	//根据字符串获取采集设备（摄像头）的引用
     //   System.out.println(deviceInfo);         //显示采集设备(摄像头)的信息
     //   System.out.println(deviceInfo.getName());     //显示采集设备（摄像头）的设备名称
    	mediaLocator = deviceInfo.getLocator();	//获取采集设备的定位器的引用，需要根据此引用来创建视频播放器
    	
		try{
			player = Manager.createRealizedPlayer(mediaLocator);// 利用mediaLocator 获取一个player
			component = player.getVisualComponent();
			if (component != null){
				vedioPanel = new JPanel();
				vedioPanel.add(component, BorderLayout.NORTH);
				this.add(vedioPanel);
				this.pack();	// 自动分配窗体大小
				this.setResizable(false);
				this.setDefaultCloseOperation(EXIT_ON_CLOSE);
				this.setVisible(true);
				player.start();
			}
		}catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        new CamTest();

        try {
            // picture capture

            // wait for camera launching
            Thread.sleep(10000);
            System.out.println("captured");
            // grab a frame from cam
            FrameGrabbingControl frameGrabber = (FrameGrabbingControl)player.getControl("javax.media.control.FrameGrabbingControl");
            Buffer buf = frameGrabber.grabFrame();
            // convert frame to an buffered image
            Image img = (new BufferToImage((VideoFormat) buf.getFormat()).createImage(buf));
            BufferedImage buffImg = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = buffImg.createGraphics();
            g.drawImage(img, null, null);

            /*
            // Overlay curent time on image
            g.setColor(Color.RED);
            g.setFont(new Font("Verdana", Font.BOLD, 16));
            g.drawString((new Date()).toString(), 10, 25);
            System.out.println("Woww.. Captured Image");
            */

            // save image to disk
            ImageIO.write(buffImg, "png", new File("c:/demo.jpg"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}