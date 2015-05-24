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

import java.text.*;

public class ImageList extends JPanel implements ActionListener{

	private java.util.List<ImageItem> items = new ArrayList<ImageItem>();
	private JButton reset;
	public CameraDisplay cd;
	private int width = 640;
	private int height = 480;
	public VideoIndicator vi;

	public ImageList(){
		// set layout
		this.setLayout(new FlowLayout());
		this.setPreferredSize(new Dimension(384, 768));

		// buttons
		reset = new JButton("Reset and Save");
		this.add(reset);
		
		reset.addActionListener(this);

	}
	
	public void addImage(String direction, String timestamp, Image img){
		items.add(new ImageItem(direction, timestamp, img));

		configure();
	}

	private void configure(){
		// clear
		this.removeAll();

		// add button
		this.add(reset);
		// add items
		for (int i = items.size() - 1; i >= Math.max(items.size() - 6, 0); i--){
			this.add(items.get(i));
		}

		// refresh
		this.revalidate();

	}

	public void actionPerformed(ActionEvent e){
		if(e.getSource() == reset){
			items.clear();
			configure();

	        FileEdit fe = new FileEdit();
	        fe.setName("result.txt");
			String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());

	        fe.addLog(timeStamp + " " + Arrays.toString(vi.grabCounts()));
		}
	}

	public void captureImage(int direction){
		String[] directions = {"⇖", "⇑", "⇗", "⇒", "⇘", "⇓", "⇙", "⇐"};
		int[] x = {0, width/3, width/3*2, width/3*2, width/3*2, width/3, 0, 0};
		int[] y = {0, 0, 0, height/3, height/3*2, height/3*2, height/3*2, height/3};

		String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());

		// capture image
		BufferedImage img = convert(cd.grabImage());

		this.addImage(directions[direction], timeStamp, resizeImage(cropImage(img, x[direction], y[direction], width/3, height/3)));
	}

	public BufferedImage cropImage(BufferedImage input, int x, int y, int width, int height){
		BufferedImage output = input.getSubimage(x, y, width, height);
		return output;
	}

	public BufferedImage convert(Image img){
		BufferedImage bimg = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics2D bGr = bimg.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		return bimg;
	}

	public Image resizeImage(Image input){
		// resize
		BufferedImage thumb = new BufferedImage(160, 120, BufferedImage.TYPE_INT_RGB);
		thumb.getGraphics().drawImage(input.getScaledInstance(160, 120, Image.SCALE_AREA_AVERAGING), 0, 0, 160, 120, null);
		return thumb;
	}

}