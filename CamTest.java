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
	private JPanel videoPanel = null;
	
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
				videoPanel = new JPanel();
				videoPanel.add(component, BorderLayout.NORTH);
				this.add(videoPanel);
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

    public int RGBtoGray(int rgb_value){
        int r, g, b;
        
        r = (rgb_value & 0x00FF0000) / 0x00010000;
        g = (rgb_value & 0x0000FF00) / 0x00000100;
        b = (rgb_value & 0x000000FF);
        
        return (int)(r * 0.299 + g * 0.587 + b * 0.114);
    }

    public void process(){
        try {
            // picture capture

            // wait for camera launching
            Thread.sleep(7000);
            System.out.println("captured");
            // grab a frame from cam
            FrameGrabbingControl frameGrabber = (FrameGrabbingControl)player.getControl("javax.media.control.FrameGrabbingControl");
            Buffer buf = frameGrabber.grabFrame();
            // convert frame to an buffered image
            Image img = (new BufferToImage((VideoFormat) buf.getFormat()).createImage(buf));
            int imgWidth = img.getWidth(null);
            int imgHeight = img.getHeight(null);
            BufferedImage buffImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = buffImg.createGraphics();

            /*
            // Overlay curent time on image
            g.setColor(Color.RED);
            g.setFont(new Font("Verdana", Font.BOLD, 16));
            g.drawString((new Date()).toString(), 10, 25);
            */

            // pixel processer
            /*
            MediaTracker mt = new MediaTracker();
            mt.addImage(img);
            */
            int[] pixelInput = new int[imgWidth * imgHeight];
            int[] pixelOutput = new int[imgWidth * imgHeight];
            int[] greyValue = new int[imgWidth * imgHeight];
            PixelGrabber pg = new PixelGrabber(img, 0, 0, imgWidth, imgHeight, pixelInput, 0, imgWidth);

            // grab pixel into grabber
            pg.grabPixels();

            // rgb to grey
            int x, y, value;
            for (y = 0; y < imgHeight; y++){
                for (x = 0; x < imgWidth; x++){
                    value = RGBtoGray(pixelInput[y*imgWidth + x]);
                    greyValue [y * imgWidth + x] = value;
                    pixelOutput[y*imgWidth + x] = 0xFF000000 + value * 0x00010101;
                }
            }

            Image imgOutput = createImage(new MemoryImageSource(imgWidth, imgHeight, pixelOutput, 0, imgWidth));

            g.drawImage(imgOutput, 0, 0, imgWidth, imgHeight, null, null);

            // avg filter
            int filterRadious = 80;
            int[] avgOutput = new int[(int)(imgWidth * imgHeight / (filterRadious * filterRadious))];
            int bx, by, avg;
            for (y = 0; y < imgHeight / filterRadious; y++){
                for (x = 0; x < imgWidth / filterRadious; x++){
                    by = y * filterRadious;
                    bx = x * filterRadious;
                    avg = 0;
                    for (by = y * filterRadious; by < ( y + 1 ) * filterRadious; by++){
                        for (bx = x * filterRadious; bx < ( x + 1 ) * filterRadious; bx++){
                            avg += greyValue[by * imgWidth + bx];
                        }
                    }
                    avg /= filterRadious * filterRadious;
                    avgOutput[y * (imgWidth / filterRadious) + x] = avg;
                    System.out.print(avg + "\t");
                }
                System.out.print("\n");
            }


            // save image to disk
            ImageIO.write(buffImg, "png", new File("c:/demo.jpg"));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        CamTest ct = new CamTest();
        ct.process();
    }
}