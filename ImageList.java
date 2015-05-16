import java.io.*;
import java.util.*;
import java.awt.BorderLayout;
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

public class ImageList extends JPanel{

	private List<ImageItem> items = new ArrayList<ImageItem>();

	public ImageList(){
		this.setBackground(Color.GREEN);
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(384, 768));

		ImageItem testItem = new ImageItem();
		this.add(testItem, BorderLayout.NORTH);
	}
	
	public void addImage(){
		ImageItem newItem = new ImageItem();
		items.add(newItem);
		this.add(newItem, BorderLayout.NORTH);
	}

}