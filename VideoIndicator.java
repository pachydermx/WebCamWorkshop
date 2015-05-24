import javax.swing.*;
import javax.swing.border.*;
import javax.accessibility.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.Point;
import java.util.Arrays;

public class VideoIndicator extends JPanel{

	int matrixWidth, matrixHeight;
	int margin_x, margin_y;
	int width, height;
	int[][] matrix;
	java.util.List<Point> clusters;
	int[] counts;

	int ori_x;

	public VideoIndicator(int width, int height, int block){
		this.width = width;
		this.height = height;

		ori_x = (640 - this.width) / 2;
		matrixWidth = width / block;
		matrixHeight = height / block;
		margin_x = block;
		margin_y = block;
		matrix = new int[matrixWidth][matrixHeight];
		clusters = new ArrayList<Point>();
		counts = new int[8];
	}

	public int[] grabCounts(){
		int[] result = new int[8];
		System.arraycopy(counts, 0, result, 0, counts.length);
		counts = new int[8];
		repaint();
		return result;
	}

	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
        g.setColor(Color.RED);
        g.setFont(new Font("Verdana", Font.BOLD, 16));

        // draw matrix 
        int x, y, value;
        int origin_x = ori_x;
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
        	origin_x = ori_x;
        	origin_y += margin_y;
        }

        // draw clusters
        g.setColor(Color.GREEN);
        for (Point i : clusters){
        	g.fillRect((int)i.getX() * margin_x + ori_x, (int)i.getY() * margin_y, margin_x, margin_y);
        }

        // draw counts
        g.setColor(Color.BLUE);
        g.drawString(Integer.toString(counts[0]), ori_x, 20);
        g.drawString(Integer.toString(counts[1]), ori_x + width/2-20, 20);
        g.drawString(Integer.toString(counts[2]), ori_x + width-40, 20);
        g.drawString(Integer.toString(counts[3]), ori_x + width-40, height/2-10);
        g.drawString(Integer.toString(counts[4]), ori_x + width-40, height-20);
        g.drawString(Integer.toString(counts[5]), ori_x + width/2-20, height-20);
        g.drawString(Integer.toString(counts[6]), ori_x, height-20);
        g.drawString(Integer.toString(counts[7]), ori_x, height/2-10);


	}

	public void setMatrix(int[][] mInput){
		for (int x = 0; x < matrixWidth; x++){
			for (int y = 0; y < matrixHeight; y++){
				matrix[x][y] = mInput[x][y];
			}
		}
		repaint();
	}

	public void addCount(int direction){
		counts[direction]++;
		//System.out.println(Arrays.toString(counts));
		repaint();
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