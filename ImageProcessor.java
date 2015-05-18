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

public class ImageProcessor extends JPanel{
	private int firstRun = 1;

	private int imgWidth, imgHeight;
	private BufferedImage buffImg;
	private Graphics2D g;

	private int[] pixelInput;
	private int[] pixelOutput;
	private int[] greyValue;

	private PixelGrabber pg;

	private int x, y, value, bx, by, avg, delta;
	private Image imgOutput;

	private int filterRadious = 40;
    private int threshold = 10;

    private int[] avgOutput;
    private int[] deltaOutput;

	public int[] process(Image img){
		if (firstRun == 1){
			// get basic info of image
			imgWidth = img.getWidth(null);
			imgHeight = img.getHeight(null);
			System.out.println("Size: " + imgWidth + " x " + imgHeight);
			// grub pratical objects from img
			buffImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
			g = buffImg.createGraphics();

			pixelInput = new int[imgWidth * imgHeight];
			pixelOutput = new int[imgWidth * imgHeight];
			greyValue = new int[imgWidth * imgHeight];

			avgOutput = new int[imgWidth * imgHeight / filterRadious / filterRadious];
			deltaOutput = new int[imgWidth * imgHeight / filterRadious / filterRadious];

			// set counter
			firstRun = 0;
		}
		
		pg = new PixelGrabber(img, 0, 0, imgWidth, imgHeight, pixelInput, 0, imgWidth);

        // grab pixel into grabber
		try{
			pg.grabPixels();
		} catch (Exception e){
			e.printStackTrace();
		}

        // rgb to grey
		for (y = 0; y < imgHeight; y++){
			for (x = 0; x < imgWidth; x++){
				value = RGBtoGray(pixelInput[y*imgWidth + x]);
				greyValue [y * imgWidth + x] = value;
				pixelOutput[y*imgWidth + x] = 0xFF000000 + value * 0x00010101;
			}
		}

		imgOutput = createImage(new MemoryImageSource(imgWidth, imgHeight, pixelOutput, 0, imgWidth));

		g.drawImage(imgOutput, 0, 0, imgWidth, imgHeight, null, null);

        // avg filter
        for (y = 0; y < imgHeight / filterRadious; y++){
        	for (x = 0; x < imgWidth / filterRadious; x++){
        		by = y * filterRadious;
        		bx = x * filterRadious;
        		avg = 0;
        		for (by = y * filterRadious; by < ( y + 1 ) * filterRadious; by++){
        			for (bx = x * filterRadious; bx < ( x + 1 ) * filterRadious; bx++){
        				avg += greyValue[by * imgWidth + bx];
        			}
        		}
        		avg /= filterRadious * filterRadious;
        		delta = avg - avgOutput[y * (imgWidth / filterRadious) + x];
        		if (delta < threshold){
        			deltaOutput[y * (imgWidth / filterRadious) + x] = 0;
        		} else {
        			deltaOutput[y * (imgWidth / filterRadious) + x] = delta;
        		}

        		avgOutput[y * (imgWidth / filterRadious) + x] = avg;
        	}
        }

        return deltaOutput;
	}

    public int RGBtoGray(int rgb_value){
        int r, g, b;
        
        r = (rgb_value & 0x00FF0000) / 0x00010000;
        g = (rgb_value & 0x0000FF00) / 0x00000100;
        b = (rgb_value & 0x000000FF);
        
        return (int)(r * 0.299 + g * 0.587 + b * 0.114);
    }
}