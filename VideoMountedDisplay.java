import javax.swing.*;
import javax.swing.border.*;
import javax.accessibility.*;

import java.awt.*;
import java.awt.event.*;

public class VideoMountedDisplay extends JPanel{
	int matrixWidth, matrixHeight;
	int margin_x, margin_y;
	int[] matrix;

	public VideoMountedDisplay(){
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
        int x, y;
        int origin_x = 10;
        int origin_y = 30;
        for (y = 0; y < matrixHeight; y++){
        	for (x = 0; x < matrixWidth; x++){
        		g.drawString(""+matrix[y * matrixWidth + x], origin_x, origin_y);
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

	@Override
	public boolean isOpaque() {
		return false;
	}
}