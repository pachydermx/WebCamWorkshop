import vcc4.*;

public class CameraControlCommunicator{
	final static int CamraNum = 0;
	final static int BITRATE = 9600;

	final static String PORT = "COM5";

	boolean enabled;
	int cooler;
	int cooler_default = 5;

	VCC4Control vcc4;

	public CameraControlCommunicator(boolean enabled){
		this.enabled = enabled;
		if (enabled){
			vcc4 = new VCC4Control(0, BITRATE, PORT);
			vcc4.cameraON();
		}
	}

	public void rotate(int x, int y){
		if (enabled && x == 0 && y == 0){
			vcc4.pantiltStartStop(0, 0);
		}
		if (enabled && cooler == 0){
			int output_x = 0;
			int output_y = 0;
			if (x < 0){
				// left
				output_x = 2;
			} else if (x > 0){
				// right
				output_x = 1;
			}
			if (y < 0){
				// up
				output_y = 1;
			} else if (y > 0){
				// down
				output_y = 2;
			}
			// perform 
			vcc4.pantiltStartStop(output_x, output_y);
		}
		cooler--;
	}

	public void setSpeed(int val){
		if (enabled && cooler == 0){
			vcc4.panSpSet(val);
			vcc4.tiltSpSet(Math.min(val, 622));
		}

	}
}