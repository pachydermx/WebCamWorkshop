import vcc4.*;

class CameraControlSample{
	
	/* カメラ通信初期設定パラメータ（通常は変更する必要なし） */
	final static int CameraNum = 0;			//単独カメラを制御する場合は固定
	final static int BITRATE = 9600;		//カメラとの通信速度
	
	
	final static String PORT = "COM1";		//通信ポートの設定（ハードウェア構成により，変更の必要がある場合あり）
	
	/* ここまで */
	
	VCC4Control vcc4;						//カメラ制御用クラスの宣言（VCC5でも動作確認済）
	
	/* コンストラクタ */
	public CameraControlSample(){
		/* カメラの初期設定 */
		vcc4 = new VCC4Control(CameraNum, BITRATE, PORT);
		
		vcc4.cameraON();					//カメラの電源を入れる
	}
	
	/* 現在のパンとチルトの角度を出してみる */
	public void printPanTiltTest(){
		vcc4.pantiltAngleReq();			//パンとチルトの角度を取得
		System.out.println("カメラの角度" + vcc4.Status1() + "&" + vcc4.Status2());
	}
	
	public void cameraSwingTest(){
		int pan = 30;
		int tilt = 30;
		vcc4.pantiltAngleSet(pan, tilt);			//パンとチルトの角度を指定して動かしてみよう
	}
	
	public void cameraSwingTest2(){
		int pan = -30;
		int tilt = -30;
		vcc4.pantiltAngleSet(pan, tilt);			//パンとチルトの角度を指定して動かしてみよう
	}
	
	public void end(){
		vcc4.cameraOFF();					//カメラ電源OFF
	}
	
	public static void main(String[] args) throws Exception{
		System.out.println("***初期化***");
		CameraControlSample ccs = new CameraControlSample();	//コンストラクタの宣言
		
		ccs.printPanTiltTest();
		Thread.sleep(3000);
		
		System.out.println("***テストスタート***");
		ccs.cameraSwingTest();									//テストメソッドの呼び出し
		
		Thread.sleep(3000);
		
		System.out.println("***テスト2スタート***");
		ccs.cameraSwingTest2();									//テストメソッドの呼び出し
		
		Thread.sleep(3000);
		
		ccs.printPanTiltTest();
		System.out.println("***終了***");
		ccs.end();												//終了処理
	}
}
