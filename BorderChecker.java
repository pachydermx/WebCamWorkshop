public class BorderChecker{
	private int[] exBorder, inBorder;
	private boolean[] waiting, ignore;
	private int width, height, exBorderWidth, inBoarderWidth;
	public VideoIndicator vi;
	public CameraDisplay cd;

	public BorderChecker(int width, int height){
		this.width = width;
		this.height = height;
		exBorderWidth = 2;
		inBoarderWidth = 4;
		exBorder = new int[8];
		inBorder = new int[8];
		waiting = new boolean[8];
		ignore = new boolean[8];
	}

	public void addPoint(int x, int y){
		int state = 0;
		// 0 - normal, 1 - inborder, 2 - exborder
		// check in exborder
		if (x < exBorderWidth || x > width - exBorderWidth || y < exBorderWidth || y > height - exBorderWidth){
			state = 2;
		} else if (x < inBoarderWidth || x > width - inBoarderWidth || y < inBoarderWidth || y > height - inBoarderWidth){
			state = 1;
		}

		// check direction
		if (state > 0){
			int angle = (int)Math.toDegrees(Math.atan2(y - height / 2, x - width / 2)) + 180;
			String[] directions = {"Up-Left", "Up", "Up-Right", "Right", "Down-Right", "Down", "Down-Left", "Left"};
			for (int i = 0; i < directions.length - 1; i++){
				if (angle >= 23 + 45 * i && angle < 23 + 45 * ( i + 1 )){
					// event: border activitied
					if (state == 1){
						// set wait
						if (!ignore[i]){
							waiting[i] = true;
						} else {
							//System.out.println("Ignore Direction: "+directions[i]);
						}
						ignore[i] = false;
					} else {
						if (waiting[i]){
							//System.out.println("Direction: "+directions[i]);
							if (vi != null){
								cd.callCapture(i);
								vi.addCount(i);
							}
						} else {
							ignore[i] = true;
						}
						waiting[i] = false;
					}


				}
			}
		}

	}

}