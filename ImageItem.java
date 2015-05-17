import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.FlowLayout;
import java.awt.Component;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.accessibility.*;

import java.awt.*;
import java.awt.event.*;

public class ImageItem extends JPanel{

	private JLabel label;
	private ImageIcon icon;

	public ImageItem(String text){
		this.setBackground(Color.WHITE);
		this.setPreferredSize(new Dimension(364, 120));
		this.setLayout(new FlowLayout());

		// image
		icon = new ImageIcon("test.png");
		this.add(new JLabel(icon));

		// label
		label = new JLabel("Time: " + text);
		this.add(label);
	}
}