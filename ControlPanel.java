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

public class ControlPanel extends JPanel implements MouseMotionListener, MouseListener{

	private JLabel joystick;
	private JLabel center;
	private ImageIcon icon;

	private JLabel state_display;

	private int[] origin = {270, 94};
	private int[] loc = {220, 44};

	private int deadzone = 20;

	private CameraControlCommunicator ccc;

	public ControlPanel(){
		this.setPreferredSize(new Dimension(640, 288));
		this.setLayout(null);

		// configure center
		center = new JLabel(new ImageIcon("center.png"));
		center.setSize(50, 50);
		center.setLocation(origin[0] + 25, origin[1] + 25);

		// configure joystick
		icon = new ImageIcon("joystick.png");
		joystick = new JLabel(icon);
		this.add(joystick);
		this.add(center);
		joystick.setSize(100, 100);
		joystick.setLocation(origin[0], origin[1]);
		joystick.addMouseMotionListener(this);
		joystick.addMouseListener(this);

		// configure state_display
		state_display = new JLabel();
		this.add(state_display);
		state_display.setSize(150, 16);
		state_display.setLocation(0, 40);

		// configure ccc
		ccc = new CameraControlCommunicator(false);
	}

	public void mouseDragged(MouseEvent e){
		if (e.getSource() == joystick){
			// move joystick
			loc[0] += e.getX() - 50;
			loc[1] += e.getY() - 50;
			joystick.setLocation(loc[0], loc[1]);

			// set display
			state_display.setText(translate_location_to_state(loc[0], loc[1]));
		}
	}

	public void mouseReleased(MouseEvent e){
		joystick.setLocation(origin[0], origin[1]);
		ccc.rotate(0, 0);
		state_display.setText(translate_location_to_state(origin[0], origin[1]));
	}

	public String translate_location_to_state(int x, int y){
		int delta_x = x - origin[0];
		int delta_y = y - origin[1];
		int distance = (int)Math.sqrt(delta_x * delta_x + delta_y * delta_y);
		int angle = (int)Math.toDegrees(Math.atan2(delta_y, delta_x)) + 180;
		String[] directions = {"Up-Left", "Up", "Up-Right", "Right", "Down-Right", "Down", "Down-Left", "Left"};
		int[][] commands = {{-1, -1}, {0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}};

		// set pan
		if (distance < deadzone){
			return "Center";
		} else {
			// set speed
			int val = distance * 2;
			ccc.setSpeed(val);
			int startPoint = 23;
			for (int i = 0; i < directions.length - 1; i++){
				if (angle >= startPoint + 45 * i && angle < startPoint + 45 * ( i + 1 )){
					ccc.rotate(commands[i][0], commands[i][1]);
					return directions[i];
				}
			}
			return directions[directions.length - 1];
		}
	}

	// useless methods
	public void mouseMoved(MouseEvent e){
		// do nothing
	}

	public void mouseExited(MouseEvent e){
		// do nothing
	}

	public void mouseEntered(MouseEvent e){
		// do nothing
	}

	public void mousePressed(MouseEvent e){
		// do nothing
	}

	public void mouseClicked(MouseEvent e){
		// do nothing
	}

}