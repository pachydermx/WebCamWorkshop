import javax.swing.*;
import javax.swing.border.*;
import javax.accessibility.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.Point;

public class VideoIndicator extends JPanel{

	int matrixWidth, matrixHeight;
	int margin_x, margin_y;
	int[][] matrix;
	java.util.List<Point> clusters;
	int[] counts;

	public VideoIndicator(){
		matrixWidth = 16;
		matrixHeight = 12;
		margin_x = 40;
		margin_y = 40;
		matrix = new int[matrixWidth][matrixHeight];
		clusters = new ArrayList<Point>();
		counts = new int[8];
	}

	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
        g.setColor(Color.RED);
        g.setFont(new Font("Verdana", Font.BOLD, 16));

        // draw matrix 
        int x, y, value;
        int origin_x = 0;
        int origin_y = 0;

        for (y = 0; y < matrixHeight; y++){
        	for (x = 0; x < matrixWidth; x++){
        		value = matrix[x][y];
       			//g.drawString(""+value, origin_x, origin_y);

        		// graphical display
        		if (value > 0){
        			g.fillRect(origin_x, origin_y, margin_x, margin_y);
        		}

        		origin_x += margin_x;
        	}
        	origin_x = 10;
        	origin_y += margin_y;
        }

        // draw clusters
        g.setColor(Color.GREEN);
        for (Point i : clusters){
        	g.fillRect((int)i.getX() * margin_x, (int)i.getY() * margin_y, margin_x, margin_y);
        }

        // draw counts
        g.setColor(Color.BLUE);
        g.drawString(Integer.toString(counts[0]), 0, 0);
        g.drawString(Integer.toString(counts[1]), 270, 0);
        g.drawString(Integer.toString(counts[2]), 540, 0);
        g.drawString(Integer.toString(counts[3]), 540, 230);
        g.drawString(Integer.toString(counts[4]), 540, 460);
        g.drawString(Integer.toString(counts[5]), 270, 460);
        g.drawString(Integer.toString(counts[6]), 0, 460);
        g.drawString(Integer.toString(counts[7]), 0, 230);


	}

	public void setMatrix(int[][] mInput){
		for (int x = 0; x < matrixWidth; x++){
			for (int y = 0; y < matrixHeight; y++){
				matrix[x][y] = mInput[x][y];
			}
		}
	}

	public void addCount(int direction){
		counts[direction]++;
	}

	public void reset(){
		clusters.clear();
	}

	public void setClusterMark(int x, int y){
		clusters.add(new java.awt.Point(x, y));
	}

	@Override
	public boolean isOpaque() {
		return false;
	}
}