import java.io.*;
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

import java.awt.event.*;
/**
 * 
 * @author Coenos 2010 www.coenos.com
 *
 */
public class GlassPanel extends JPanel implements MouseMotionListener,
		MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3162577207753229521L;

	public GlassPanel() {
		addMouseMotionListener(this);
		addMouseListener(this);
	}

	private Point oldPoint;
	private Point newPoint;

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.RED);
		g.drawRect(50, 50, 500, 500);

		g.setColor(Color.WHITE);
		/*
		for (Line2D line : lines) {
			g.drawLine((int) line.getX1(), (int) line.getY1(), (int) line
					.getX2(), (int) line.getY2());
		}
		*/
	}

	@Override
	public boolean isOpaque() {
		return false;
	}

	public void mouseDragged(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	public void mouseClicked(MouseEvent e) {		
		// set new starting Point
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	public void mousePressed(MouseEvent e) {		
		// set new starting Point
	}

	public void mouseReleased(MouseEvent e) {		
	}
}