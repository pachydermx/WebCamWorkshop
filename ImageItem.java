import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.BorderLayout;
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

	public ImageItem(){
		this.setBackground(Color.YELLOW);
		this.setPreferredSize(new Dimension(364, 120));
		this.setLayout(new BorderLayout());

		label = new JLabel("Hello World");
		this.add(label, BorderLayout.WEST);
	}
}