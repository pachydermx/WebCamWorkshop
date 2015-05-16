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

public class ImageList extends JPanel{

	private List<ImageItem> items = new ArrayList<ImageItem>();

	public ImageList(){
		this.setBackground(Color.GREEN);
	}
	
	public void addImage(){
		ImageItem newItem = new ImageItem();
		items.add(newItem);
		newItem.setBounds(10, 10, 364, 30);
		System.out.println("Hello World");
		this.add(newItem);
	}

}