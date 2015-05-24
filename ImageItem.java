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

	private JLabel timeLabel, directionLabel;
	private JPanel labelPanel;
	private ImageIcon icon;

	public ImageItem(String direction, String timestamp, Image img){
		this.setBackground(Color.WHITE);
		this.setPreferredSize(new Dimension(364, 120));
		this.setLayout(new FlowLayout());

		// image
		if (img == null){
			icon = new ImageIcon("test.png");
		} else {
			icon = new ImageIcon(img);
		}
		this.add(new JLabel(icon));

		// label
		labelPanel = new JPanel();
		this.add(labelPanel);
		labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));

		directionLabel = new JLabel("Direction: " + direction);
		timeLabel = new JLabel("Time: " + timestamp);
		labelPanel.add(directionLabel);
		labelPanel.add(timeLabel);
	}
}