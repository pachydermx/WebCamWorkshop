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

	public ImageList(){
		// set layout
		this.setLayout(new FlowLayout());
		this.setPreferredSize(new Dimension(384, 768));

		// buttons
		reset = new JButton("Capture");
		this.add(reset);
		
		reset.addActionListener(this);

	}
	
	public void addImage(String label, Image img){
		items.add(new ImageItem(label, img));
		this.add(items.get(items.size() - 1));

		// refresh to display new item
		this.revalidate();
	}

	public void actionPerformed(ActionEvent e){
		if(e.getSource() == reset){
			String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
			this.addImage(timeStamp, resizeImage(cd.grabImage()));
		}
	}

	public Image resizeImage(Image input){
		BufferedImage thumb = new BufferedImage(160, 120, BufferedImage.TYPE_INT_RGB);
		thumb.getGraphics().drawImage(input.getScaledInstance(160, 120, Image.SCALE_AREA_AVERAGING), 0, 0, 160, 120, null);
		return thumb;
	}

}