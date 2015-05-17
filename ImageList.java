import java.io.*;
import java.util.*;
import java.awt.FlowLayout;
import java.awt.Component;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.*;
import javax.swing.border.*;
import javax.accessibility.*;

import java.awt.event.*;
import java.awt.Color;
import java.awt.Dimension;

public class ImageList extends JPanel implements ActionListener{

	private List<ImageItem> items = new ArrayList<ImageItem>();
	private JButton reset;

	public ImageList(){
		// set layout
		this.setLayout(new FlowLayout());
		this.setPreferredSize(new Dimension(384, 768));

		// buttons
		reset = new JButton("Reset");
		this.add(reset);
		
		reset.addActionListener(this);

	}
	
	public void addImage(String label){
		items.add(new ImageItem(label));
		this.add(items.get(items.size() - 1));

		System.out.println("adding item #"+items.size());

		// refresh to display new item
		this.revalidate();
	}

	public void actionPerformed(ActionEvent e){
		if(e.getSource() == reset){
			this.addImage("Hello world");
		}
	}

}