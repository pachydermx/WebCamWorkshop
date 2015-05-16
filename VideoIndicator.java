import javax.swing.*;
import javax.swing.border.*;
import javax.accessibility.*;

import java.awt.*;
import java.awt.event.*;

public class VideoIndicator extends JPanel{

	/*
	int matrixWidth, matrixHeight;
	int margin_x, margin_y;
	int[] matrix;

	public VideoIndicator(){
		matrixWidth = 16;
		matrixHeight = 12;
		margin_x = 40;
		margin_y = 40;
		matrix = new int[matrixWidth * matrixHeight];
	}

	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
        g.setColor(Color.RED);
        g.setFont(new Font("Verdana", Font.BOLD, 16));

        // draw matrix label
        int x, y, value;
        int origin_x = 10;
        int origin_y = 30;
        for (y = 0; y < matrixHeight; y++){
        	for (x = 0; x < matrixWidth; x++){
        		value = matrix[y * matrixWidth + x];
        		// text display
        		//g.drawString(""+value, origin_x, origin_y);
        		// graphical display
        		if (value > 0){
        			g.fillRect(origin_x, origin_y, 40, 40);
        		}

        		origin_x += margin_x;
        	}
        	origin_x = 10;
        	origin_y += margin_y;
        }
	}

	public void setMatrix(int[] mInput){
		for (int i = 0; i < matrixWidth * matrixHeight; i++){
			matrix[i] = mInput[i];
		}
	}

	public void setMatrix(int[] mInput){
		for (int i = 0; i < matrixWidth * matrixHeight; i++){
			matrix[i] = mInput[i];
		}
	}



	*/

	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
        g.setColor(Color.RED);
        g.setFont(new Font("Verdana", Font.BOLD, 16));

        g.fillRect(100, 100, 320, 240);

	}
	@Override
	public boolean isOpaque() {
		return false;
	}
}