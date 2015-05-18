/***************************************
RS232Cを用いてVCC4のカメラを制御する
by okushiro
***************************************/
package vcc4;
public class VCC4Control extends SerialControl{
	private byte cn = 0;//カメラナンバー（通常は0）
	private int status1 = 0, status2 = 0, status3 = 0;
	byte[] ans;//アンサ
	private byte[] temp6 = {(byte)0xFF, (byte)0x30, cn,  (byte)0x00, (byte)0x00, (byte)0xEF};
	private byte[] temp7 = {(byte)0xFF, (byte)0x30, cn,  (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xEF};
	private byte[] temp8 = {(byte)0xFF, (byte)0x30, cn,  (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xEF};
	private byte[] temp9 = {(byte)0xFF, (byte)0x30, cn,  (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xEF};
	private byte[] temp10 = {(byte)0xFF, (byte)0x30, cn,  (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xEF};
	private byte[] temp12 = {(byte)0xFF, (byte)0x30, cn,  (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xEF};
	private byte[] temp13 = {(byte)0xFF, (byte)0x30, cn,  (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xEF};
	private byte[] temp14 = {(byte)0xFF, (byte)0x30, cn,  (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xEF};
	private byte[] temp15 = {(byte)0xFF, (byte)0x30, cn,  (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xEF};

	//CameraNum⇒操作対象となるカメラNOで通常は0でOK
	public VCC4Control(int CameraNum, int bitrate, String port){
		super(port);
		this.cn = henkan1(CameraNum);
		temp6[2] = cn;
		temp7[2] = cn;
		temp8[2] = cn;
		temp9[2] = cn;
		temp10[2] = cn;
		temp12[2] = cn;
		temp13[2] = cn;
		temp14[2] = cn;
		temp15[2] = cn;
		setSerial(bitrate);//RS-232C関連の設定
		try{Thread.currentThread().sleep(300);}catch(Exception e){}
		hostControlStep1();
		System.out.println("ControlMode Step1");
//		
	//	try{Thread.currentThread().sleep(1300);}catch(Exception e){}
		hostControlStep2();
		System.out.println("ControlMode Step2");
	//	try{Thread.currentThread().sleep(1300);}catch(Exception e){}
		hostControlStep3();
		System.out.println("ControlMode Step3");
	//	try{Thread.currentThread().sleep(1000);}catch(Exception e){}
	
	}
	public int Status1(){return status1;}
	public int Status2(){return status2;}
	public int Status3(){return status3;}

	//カメラON
	public int cameraON(){
		temp7[4] = (byte)0xA0;
		temp7[5] = (byte)0x31;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(200);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//カメラOFF
	public int cameraOFF(){
		temp7[4] = (byte)0xA0;
		temp7[5] = (byte)0x30;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(200);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//パン速度指定
	//8～800
	public int panSpSet(int para){
		byte[] data = henkan3(para);
		temp9[4] = (byte)0x50;
		temp9[5] = data[0];
		temp9[6] = data[1];
		temp9[7] = data[2];
		ans = write6(temp9);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp9);
		}
		return ansAnalysis();
	}
	//チルト速度指定
	//8～622
	public int tiltSpSet(int para){
		byte[] data = henkan3(para);
		temp9[4] = (byte)0x51;
		temp9[5] = data[0];
		temp9[6] = data[1];
		temp9[7] = data[2];
		ans = write6(temp9);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp9);
		}
		return ansAnalysis();
	}
	//パン速度要求
	public int panSpReq(){
		temp7[4] = (byte)0x52;
		temp7[5] = (byte)0x30;
		ans = write9(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write9(temp7);
		}
		status1 = dataPlus(change1(ans[5]), change1(ans[6]), change1(ans[7]));
		return ansAnalysis();
	}
	//チルト速度要求
	public int tiltSpReq(){
		temp7[4] = (byte)0x52;
		temp7[5] = (byte)0x31;
		ans = write9(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write9(temp7);
		}
		status1 = dataPlus(change1(ans[5]), change1(ans[6]), change1(ans[7]));
		return ansAnalysis();
	}
	//パン・チルトストップ
	public int pantiltStop(){
		temp7[4] = (byte)0x53;
		temp7[5] = (byte)0x30;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		try{Thread.currentThread().sleep(90);}catch(Exception e){}
		return ansAnalysis();
	}
	
	//パン右スタート
	public int panRStart(){
		temp7[4] = (byte)0x53;
		temp7[5] = (byte)0x31;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(900);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//パン左スタートsample
	public int panLStart(){
		temp7[4] = (byte)0x53;
		temp7[5] = (byte)0x32;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//チルト上スタート
	public int tiltUpStart(){
		temp7[4] = (byte)0x53;
		temp7[5] = (byte)0x33;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//チルト下スタート
	public int tiltDownStart(){
		temp7[4] = (byte)0x53;
		temp7[5] = (byte)0x34;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//ホームポジション  ok
	public int home(){
		temp6[4] = (byte)0x57;
		ans = write6(temp6);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp6);
		}
		try{Thread.currentThread().sleep(2000);}catch(Exception e){}
		return ansAnalysis();
	}
	//雲台イニシャライズ1
	public int platform1(){
		temp7[4] = (byte)0x58;
		temp7[5] = (byte)0x30;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//雲台イニシャライズ2
	public int platform2(){
		temp7[4] = (byte)0x58;
		temp7[5] = (byte)0x31;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	
	//パン最低速度要求
	public int panMinSpReq(){
		temp7[4] = (byte)0x59;
		temp7[5] = (byte)0x30;
		ans = write9(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write9(temp7);
		}
		status1 = dataPlus(change1(ans[5]), change1(ans[6]), change1(ans[7]));
		return ansAnalysis();
	}
	
	//パン最高速度要求
	public int panMaxSpReq(){
		temp7[4] = (byte)0x59;
		temp7[5] = (byte)0x31;
		ans = write9(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write9(temp7);
		}
		status1 = dataPlus(change1(ans[5]), change1(ans[6]), change1(ans[7]));
		return ansAnalysis();
	}
	
	//チルト最低速度要求
	public int tiltMinSpReq(){
		temp7[4] = (byte)0x59;
		temp7[5] = (byte)0x32;
		ans = write9(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write9(temp7);
		}
		status1 = dataPlus(change1(ans[5]), change1(ans[6]), change1(ans[7]));
		return ansAnalysis();
	}
	//チルト最高速度要求
	public int tiltMaxSpReq(){
		temp7[4] = (byte)0x59;
		temp7[5] = (byte)0x33;
		ans = write9(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write9(temp7);
		}
		status1 = dataPlus(change1(ans[5]), change1(ans[6]), change1(ans[7]));
		return ansAnalysis();
	}
	//パン角度変換係数要求
	//使用するときは結果を10000で割った値を使用してください
	public int panUnitSet(){
		temp7[4] = (byte)0x5B;
		temp7[5] = (byte)0x30;
		ans = write10(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write10(temp7);
		}
		status1 = dataPlus(change1(ans[5]),change1(ans[6]),change1(ans[7]),change1(ans[8]));
		return ansAnalysis();
	}
	//チルト角度変換係数要求
	//使用するときは結果を10000で割った値を使用してください
	public int tiltUnitSet(){
		temp7[4] = (byte)0x5B;
		temp7[5] = (byte)0x31;
		ans = write10(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write10(temp7);
		}
		status1 = dataPlus(change1(ans[5]),change1(ans[6]),change1(ans[7]),change1(ans[8]));
		return ansAnalysis();
	}
	//パン最低角度要求
	public int panMinAngleReq(){
		temp7[4] = (byte)0x5C;
		temp7[5] = (byte)0x30;
		ans = write10(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write10(temp7);
		}
		status1 = dataPlus(change1(ans[5]),change1(ans[6]),change1(ans[7]),change1(ans[8]));
		return ansAnalysis();
	}
	//パン最高角度要求
	public int panMaxAngelReq(){
		temp7[4] = (byte)0x5C;
		temp7[5] = (byte)0x31;
		ans = write10(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write10(temp7);
		}
		status1 = dataPlus(change1(ans[5]),change1(ans[6]),change1(ans[7]),change1(ans[8]));
		return ansAnalysis();
	}
	//チルト最低角度要求
	public int tiltMinAngelReq(){
		temp7[4] = (byte)0x5C;
		temp7[5] = (byte)0x32;
		ans = write10(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write10(temp7);
		}
		status1 = dataPlus(change1(ans[5]),change1(ans[6]),change1(ans[7]),change1(ans[8]));
		return ansAnalysis();
	}
	//チルト最高角度要求
	public int tiltMaxAngelReq(){
		temp7[4] = (byte)0x5C;
		temp7[5] = (byte)0x33;
		ans = write10(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write10(temp7);
		}
		status1 = dataPlus(change1(ans[5]),change1(ans[6]),change1(ans[7]),change1(ans[8]));
		return ansAnalysis();
	}
	//パン・チルトストップ
	public int pantiltStop2(){
		temp8[4] = (byte)0x60;
		temp8[5] = (byte)0x30;
		temp8[6] = (byte)0x30;
		ans = write6(temp8);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp8);
		}
		return ansAnalysis();
	}
	//パン・チルトスタートストップ
	public int pantiltStartStop(int p, int t){
		temp8[4] = (byte)0x60;
		temp8[5] = henkan1(p);
		temp8[6] = henkan1(t);
		ans = write6(temp8);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp8);
		}
		return ansAnalysis();
	}
	//パン･チルト角度指定
	//pan⇒-100～+100		tilt⇒-90～+100
	public int pantiltAngleSet(int p, int t){
		byte[] pan = henkan4(angelCreate(p));
		byte[] til = henkan4(angelCreate(t));
		temp14[4] = (byte)0x62;
		temp14[5] = pan[0];
		temp14[6] = pan[1];
		temp14[7] = pan[2];
		temp14[8] = pan[3];
		temp14[9] = til[0];
		temp14[10] = til[1];
		temp14[11] = til[2];
		temp14[12] = til[3];
		ans = write6(temp14);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp14);
		}
		return ansAnalysis();
	}
	//パン･チルト角度要求
	public int pantiltAngleReq(){
		temp6[4] = (byte)0x63;
		ans = write14(temp6);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write14(temp6);
		}
		try{Thread.currentThread().sleep(90);}catch(Exception e){}
		status1 = angelRecovery(dataPlus(change1(ans[5]), change1(ans[6]), change1(ans[7]), change1(ans[8])));
		status2 = angelRecovery(dataPlus(change1(ans[9]), change1(ans[10]), change1(ans[11]), change1(ans[12])));
		return ansAnalysis();
	}
	//パン可動範囲指定	引数は角度で
	public int panRangeSet(int min, int max){
		byte[] mi = henkan4(angelCreate(min));
		byte[] ma = henkan4(angelCreate(max));
		temp15[4] = (byte)0x64;
		temp15[5] = (byte)0x30;
		temp15[6] = mi[0];
		temp15[7] = mi[1];
		temp15[8] = mi[2];
		temp15[9] = mi[3];
		temp15[10] = ma[0];
		temp15[11] = ma[1];
		temp15[12] = ma[2];
		temp15[13] = ma[3];
		ans = write15(temp15);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write15(temp15);
		}
		return ansAnalysis();
	}
	//チルト可動範囲指定   引数は角度で
	public int tiltRangeSet(int min, int max){
		byte[] mi = henkan4(angelCreate(min));
		byte[] ma = henkan4(angelCreate(max));
		temp15[4] = (byte)0x64;
		temp15[5] = (byte)0x31;
		temp15[6] = mi[0];
		temp15[7] = mi[1];
		temp15[8] = mi[2];
		temp15[9] = mi[3];
		temp15[10] = ma[0];
		temp15[11] = ma[1];
		temp15[12] = ma[2];
		temp15[13] = ma[3];
		ans = write15(temp15);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write15(temp15);
		}
		return ansAnalysis();
	}
	//パン可動範囲要求
	public int panRangeReq(){
		temp7[4] = (byte)0x65;
		temp7[5] = (byte)0x30;
		ans = write14(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write14(temp7);
		}
		try{Thread.currentThread().sleep(90);}catch(Exception e){}
		status1 = angelRecovery(dataPlus(change1(ans[5]), change1(ans[6]), change1(ans[7]), change1(ans[8])));
		status2 = angelRecovery(dataPlus(change1(ans[9]), change1(ans[10]), change1(ans[11]), change1(ans[12])));
		return ansAnalysis();
	}
	//チルト可動範囲要求
	public int tiltRangeReq(){
		temp7[4] = (byte)0x65;
		temp7[5] = (byte)0x31;
		ans = write14(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write14(temp7);
		}
		try{Thread.currentThread().sleep(90);}catch(Exception e){}
		status1 = angelRecovery(dataPlus(change1(ans[5]), change1(ans[6]), change1(ans[7]), change1(ans[8])));
		status2 = angelRecovery(dataPlus(change1(ans[9]), change1(ans[10]), change1(ans[11]), change1(ans[12])));
		return ansAnalysis();
	}
	//フォーカスAF
	public int focusAuto(){
		temp7[4] = (byte)0xA1;
		temp7[5] = (byte)0x30;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//フォーカスMUNUAL
	public int focusManual(){
		temp7[4] = (byte)0xA1;
		temp7[5] = (byte)0x31;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//フォーカスNEAR
	public int focusNear(){
		temp7[4] = (byte)0xA1;
		temp7[5] = (byte)0x32;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//フォーカスFAR
	public int focusFar(){
		temp7[4] = (byte)0xA1;
		temp7[5] = (byte)0x33;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//フォーカス位置指定
	public int focusSet(int para){
		byte[] fo = henkan4(para);
		temp10[4] = (byte)0xB0;
		temp10[5] = fo[0];
		temp10[6] = fo[1];
		temp10[7] = fo[2];
		temp10[8] = fo[3];
		ans = write6(temp10);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp10);
		}
		return ansAnalysis();
	}
	//フォーカス位置要求
	public int focusReq(int para){
		temp7[4] = (byte)0xB1;
		temp7[5] = (byte)0x30;
		ans = write10(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write10(temp7);
		}
		status1 = dataPlus(change1(ans[5]), change1(ans[6]), change1(ans[7]), change1(ans[8]));
		return ansAnalysis();
	}
	//ワンプッシュAF
	public int focusOnePush(){
		temp7[4] = (byte)0xB1;
		temp7[5] = (byte)0x31;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//フォーカス範囲要求
	public int focusRange(){
		temp7[4] = (byte)0xB1;
		temp7[5] = (byte)0x32;
		ans = write14(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write14(temp7);
		}
		try{Thread.currentThread().sleep(90);}catch(Exception e){}
		status1 = dataPlus(change1(ans[5]), change1(ans[6]), change1(ans[7]), change1(ans[8]));
		status1 = dataPlus(change1(ans[9]), change1(ans[10]), change1(ans[11]), change1(ans[12]));
		return ansAnalysis();
	}
	//ズームSTOP
	public int zoomStop(){
		temp7[4] = (byte)0xA2;
		temp7[5] = (byte)0x30;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//ズームWIDE
	public int zoomWide(){
		temp7[4] = (byte)0xA2;
		temp7[5] = (byte)0x31;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//ズームTELE
	public int zoomTele(){
		temp7[4] = (byte)0xA2;
		temp7[5] = (byte)0x32;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//ズームHIWIDE
	public int zoomHiWide(){
		temp7[4] = (byte)0xA2;
		temp7[5] = (byte)0x33;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//ズームHITELE
	public int zoomHiTele(){
		temp7[4] = (byte)0xA2;
		temp7[5] = (byte)0x34;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//ズーム位置指定1	00～80
	public int zoomSet1(int para){
		byte[] pa = henkan2(para);
		temp8[4] = (byte)0xA3;
		temp8[5] = pa[0];
		temp8[6] = pa[1];
		ans = write6(temp8);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp8);
		}
		return ansAnalysis();
	}
	//ズーム位置指定2	00～80
	public int zoomSet2(int para){
		byte[] pa = henkan4(para);
		temp10[4] = (byte)0xB3;
		temp10[5] = pa[0];
		temp10[6] = pa[1];
		temp10[7] = pa[2];
		temp10[8] = pa[3];
		ans = write6(temp10);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp10);
		}
		return ansAnalysis();
	}
	//ズーム位置要求1	00～80
	public int zoomReq1(){
		temp6[4] = (byte)0xA4;
		ans = write10(temp6);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write10(temp6);
		}
		status1 = dataPlus(change1(ans[5]), change1(ans[6]));
		return ansAnalysis();
	}
	//ズーム位置要求2	00～80
	public int zoomReq2(){
		temp7[4] = (byte)0xB4;
		temp7[5] = (byte)0x30;
		ans = write10(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write10(temp7);
		}
		status1 = dataPlus(change1(ans[5]), change1(ans[6]), change1(ans[7]), change1(ans[8]));
		return ansAnalysis();
	}
	//ズーム速度指定
	public int zoomSpeedSet(int para){
		byte p = henkan1(para);
		temp8[4] = (byte)0xB4;
		temp8[5] = (byte)0x31;
		temp8[6] = p;
		ans = write6(temp8);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp8);
		}
		return ansAnalysis();
	}
	//ズーム速度要求
	public int zoomSpeedReq(){
		temp7[4] = (byte)0xB4;
		temp7[5] = (byte)0x32;
		ans = write7(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write7(temp7);
		}
		status1 = change1(ans[5]);
		return ansAnalysis();
	}
	//ズーム位置最大値要求
	public int zoomMaxReq(){
		temp7[4] = (byte)0xB4;
		temp7[5] = (byte)0x33;
		ans = write10(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write10(temp7);
		}
		status1 = dataPlus(change1(ans[5]), change1(ans[6]), change1(ans[7]), change1(ans[8]));
		return ansAnalysis();
	}
	//逆光補正OFF
	public int backlightModeOff(){
		temp7[4] = (byte)0xA5;
		temp7[5] = (byte)0x30;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//逆光補正ON
	public int backlightModeOn(){
		temp7[4] = (byte)0xA5;
		temp7[5] = (byte)0x31;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//露出モードAUTO
	public int exposureAuto(){
		temp7[4] = (byte)0xA5;
		temp7[5] = (byte)0x32;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//露出モードManual
	public int exposureManual(){
		temp7[4] = (byte)0xA5;
		temp7[5] = (byte)0x33;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//AEロックOFF
	public int aeRockOff(){
		temp8[4] = (byte)0xA5;
		temp8[5] = (byte)0x34;
		temp8[6] = (byte)0x30;
		ans = write6(temp8);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp8);
		}
		return ansAnalysis();
	}
	//AEロックON
	public int aeRockOn(){
		temp8[4] = (byte)0xA5;
		temp8[5] = (byte)0x34;
		temp8[6] = (byte)0x31;
		ans = write6(temp8);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp8);
		}
		return ansAnalysis();
	}
	//シャッタースピードプログラム
	public int shutterSp(){
		temp7[4] = (byte)0xA8;
		temp7[5] = (byte)0x30;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//シャッタースピード 1/60(PAL:1/50)
	public int shutterSpSecond(){
		temp7[4] = (byte)0xA8;
		temp7[5] = (byte)0x31;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//シャッタースピード 1/100(PAL:1/120)
	public int shutterSpCenti(){
		temp7[4] = (byte)0xA8;
		temp7[5] = (byte)0x32;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//シャッタースピード指定
	public int shutterSpSet(int para){
		byte[] p = henkan2(para);
		temp9[4] = (byte)0xA5;
		temp9[5] = (byte)0x35;
		temp9[6] = p[0];
		temp9[7] = p[1];
		ans = write6(temp9);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp9);
		}
		return ansAnalysis();
	}
	//シャッタースピード要求
	public int shutterSpReq(){
		temp7[4] = (byte)0xA5;
		temp7[5] = (byte)0x36;
		ans = write8(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write8(temp7);
		}
		status1 = dataPlus(change1(ans[5]), change1(ans[6]));
		return ansAnalysis();
	}
	//AGCゲイン指定
	public int agcSet(int para){
		byte[] p = henkan2(para);
		temp9[4] = (byte)0xA5;
		temp9[5] = (byte)0x37;
		temp9[6] = p[0];
		temp9[7] = p[1];
		ans = write6(temp9);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp9);
		}
		return ansAnalysis();
	}
	//AGCゲイン要求
	public int agcReq(){
		temp7[4] = (byte)0xA5;
		temp7[5] = (byte)0x38;
		ans = write8(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write8(temp7);
		}
		status1 = dataPlus(change1(ans[5]), change1(ans[6]));
		return ansAnalysis();
	}
	//絞り指定
	public int irisSet(int para){
		byte[] p = henkan2(para);
		temp9[4] = (byte)0xA5;
		temp9[5] = (byte)0x39;
		temp9[6] = p[0];
		temp9[7] = p[1];
		ans = write6(temp9);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp9);
		}
		return ansAnalysis();
	}
	//絞り要求
	public int irisReq(){
		temp7[4] = (byte)0xA5;
		temp7[5] = (byte)0x3A;
		ans = write8(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write8(temp7);
		}
		status1 = dataPlus(change1(ans[5]), change1(ans[6]));
		return ansAnalysis();
	}
	//AE目標値指定
	public int aeTargetSet(int para){
		byte[] p = henkan2(para);
		temp9[4] = (byte)0xA5;
		temp9[5] = (byte)0x3B;
		temp9[6] = p[0];
		temp9[7] = p[1];
		ans = write6(temp9);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp9);
		}
		return ansAnalysis();
	}
	//AE目標値要求
	public int aeTargetReq(){
		temp7[4] = (byte)0xA5;
		temp7[5] = (byte)0x3C;
		ans = write8(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write8(temp7);
		}
		status1 = dataPlus(change1(ans[5]), change1(ans[6]));
		return ansAnalysis();
	}
	//オートホワイトバランス ノーマル
	public int wbNormal(){
		temp7[4] = (byte)0xA7;
		temp7[5] = (byte)0x30;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//オートホワイトバランス ロック
	public int wbRock(){
		temp7[4] = (byte)0xA7;
		temp7[5] = (byte)0x31;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//ホワイトバランス マニュアルモード
	public int wbManual(){
		temp7[4] = (byte)0xA7;
		temp7[5] = (byte)0x32;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//ホワイトバランス マニュアル設定値指定
	public int wbSet(int para){
		byte[] p = henkan2(para);
		temp9[4] = (byte)0xA7;
		temp9[5] = (byte)0x34;
		temp9[6] = p[0];
		temp9[7] = p[1];
		ans = write6(temp9);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp9);
		}
		return ansAnalysis();
	}
	//ホワイトバランス マニュアル設定値要求
	public int wbReq(){
		temp7[4] = (byte)0xA7;
		temp7[5] = (byte)0x35;
		ans = write8(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write8(temp7);
		}
		status1 = dataPlus(change1(ans[5]), ans[6]);
		return ansAnalysis();
	}
	//フェード ノーマル
	public int fadeNomal(){
		temp7[4] = (byte)0xA9;
		temp7[5] = (byte)0x30;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//白フェード
	public int fadeWhite(){
		temp7[4] = (byte)0xA9;
		temp7[5] = (byte)0x31;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//高速白フェード
	public int fadeHiWhite(){
		temp7[4] = (byte)0xA9;
		temp7[5] = (byte)0x32;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//高速黒フェード
	public int fadeHiBlack(){
		temp7[4] = (byte)0xA9;
		temp7[5] = (byte)0x33;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//カメラリセット
	public int reset(){
		temp6[4] = (byte)0xAA;
		ans = write6(temp6);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp6);
		}
		return ansAnalysis();
	}
	//ズーム比要求
	public int zoomRateReq(){
		temp6[4] = (byte)0xAB;
		ans = write8(temp6);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write8(temp6);
		}
		status1 = dataPlus(change1(ans[5]), change1(ans[6]));
		return ansAnalysis();
	}
	//画素サイズ要求	statsu1 / status2 の値を使用すること
	public int pixelSizeReq(){
		temp6[4] = (byte)0xAC;
		ans = write8(temp6);
		status1 = change1(ans[5]);
		status2 = change1(ans[6]);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write8(temp6);
		}
		return ansAnalysis();
	}
	//製品バージョン要求
	public int verReq(){
		temp7[4] = (byte)0xBE;
		temp7[5] = (byte)0x30;
		ans = write8(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write8(temp7);
		}
		status1 = dataPlus(change1(ans[5]), change1(ans[6]));
		return ansAnalysis();
	}
	//EEPROMバージョン要求
	public int verEEPROMReq(){
		temp7[4] = (byte)0xBE;
		temp7[5] = (byte)0x31;
		ans = write8(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write8(temp7);
		}
		status1 = dataPlus(change1(ans[5]), change1(ans[6]));
		return ansAnalysis();
	}
	//リモコンON
	public int rcON(){
		temp7[4] = (byte)0x80;
		temp7[5] = (byte)0x30;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//リモコンOFF
	public int rcOff(){
		temp7[4] = (byte)0x80;
		temp7[5] = (byte)0x31;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//動作ステータス要求
	public int actionStatusReq(){
		temp6[4] = (byte)0x86;
		ans = write9(temp6);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write9(temp6);
		}
		status1 = dataPlus(change1(ans[5]), change1(ans[6]), change1(ans[7]));
		return ansAnalysis();
	}
	//拡張動作ステータス要求
	public int exActionStatusReq(){
		temp7[4] = (byte)0x86;
		temp7[5] = (byte)0x30;
		ans = write11(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write11(temp7);
		}
		try{Thread.currentThread().sleep(90);}catch(Exception e){}
		status1 = dataPlus(change1(ans[5]), change1(ans[6]), change1(ans[7]), change1(ans[8]), change1(ans[9]));
		return ansAnalysis();
	}
	//機種名要求
	public int nameReq(){
		temp6[4] = (byte)0x87;
		ans = write11(temp6);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write11(temp6);
		}
		try{Thread.currentThread().sleep(90);}catch(Exception e){}
		status1 = dataPlus(change1(ans[5]), change1(ans[6]), change1(ans[7]), change1(ans[8]), change1(ans[9]));
		return ansAnalysis();
	}
	//ROMバージョン要求
	public int romVerReq(){
		temp6[4] = (byte)0x88;
		ans = write11(temp6);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write11(temp6);
		}
		try{Thread.currentThread().sleep(90);}catch(Exception e){}
		status1 = dataPlus(change1(ans[5]), change1(ans[6]), change1(ans[7]), change1(ans[8]), change1(ans[9]));
		return ansAnalysis();
	}
	//プリセットセット
	public int statusSave(int para){
		byte p = henkan1(para);
		temp7[4] = (byte)0x89;
		temp7[5] = p;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//プリセット移動
	public int statusActivate(int para){
		byte p = henkan1(para);
		temp7[4] = (byte)0x8A;
		temp7[5] = p;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//プリセットステータス要求
	public int statusReq(){
		temp6[4] = (byte)0x8B;
		ans = write8(temp6);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write8(temp6);
		}
		status1 = dataPlus(change1(ans[5]), change1(ans[6]));
		return ansAnalysis();
	}
	//拡張プリセットステータス要求
	public int exStatusReq(){
		temp7[4] = (byte)0x8B;
		temp7[5] = (byte)0x30;
		ans = write9(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write9(temp7);
		}
		status1 = dataPlus(change1(ans[5]), change1(ans[6]), change1(ans[7]));
		return ansAnalysis();
	}
	//リモコンスルー設定
	public int rcThroughSet(int para){
		byte p = henkan1(para);
		temp7[4] = (byte)0x8D;
		temp7[5] = p;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//LEDノーマル表示
	public int ledNormal(){
		temp7[4] = (byte)0x8E;
		temp7[5] = (byte)0x30;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//LED強制制御
	public int ledCtrl(int para){
		byte p = henkan1(para);
		temp7[4] = (byte)0x8E;
		temp7[5] = p;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//カスケードOFF
	public int cascadeOff(){
		temp7[4] = (byte)0x8F;
		temp7[5] = (byte)0x30;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//カスケードON
	public int cascadeOn(){
		temp7[4] = (byte)0x8F;
		temp7[5] = (byte)0x31;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//ホスト制御モード
	public int hostCtrl(){
		temp7[4] = (byte)0x90;
		temp7[5] = (byte)0x30;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//ローカル制御モード
	public int rcCtrl(){
		temp7[4] = (byte)0x90;
		temp7[5] = (byte)0x31;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//スクリーン制御
	public int screenCtrl(int para){
		byte[] p = henkan2(para);
		temp8[4] = (byte)0x91;
		temp8[5] = p[0];
		temp8[6] = p[1];
		ans = write6(temp8);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp8);
		}
		return ansAnalysis();
	}
	//表示文字データの指定
	public int charSet(int para1, int para2, int para3){
		byte[] p1 = henkan2(para1);
		byte p2 = henkan1(para2);
		byte[] p3 = henkan2(para3);
		temp12[4] = (byte)0x91;
		temp12[5] = (byte)0x31;
		temp12[6] = p1[0];
		temp12[7] = p1[1];
		temp12[8] = p2;
		temp12[9] = p3[0];
		temp12[10] = p3[1];
		ans = write6(temp12);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp12);
		}
		return ansAnalysis();
	}
	//表示文字データの要求
	public int charReq(int para1, int para2){
		byte[] p1 = henkan2(para1);
		byte p2 = henkan1(para2);
		temp10[4] = (byte)0x91;
		temp10[5] = (byte)0x32;
		temp10[6] = p1[0];
		temp10[7] = p1[1];
		temp10[8] = p2;
		ans = write8(temp10);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write8(temp10);
		}
		status1 = dataPlus(change1(ans[5]), change1(ans[6]));
		return ansAnalysis();
	}
	//日付設定
	public int dateSet(int para1, int para2, int para3){
		byte[] p1 = henkan2(para1);
		byte[] p2 = henkan2(para2);
		byte[] p3 = henkan2(para3);
		temp13[4] = (byte)0x91;
		temp13[5] = (byte)0x33;
		temp13[6] = p1[0];
		temp13[7] = p1[1];
		temp13[8] = p2[0];
		temp13[9] = p2[1];
		temp13[10] = p3[0];
		temp13[11] = p3[1];
		ans = write6(temp13);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp13);
		}
		return ansAnalysis();
	}
	//日付要求
	public int dateReq(){
		temp7[4] = (byte)0x91;
		temp7[5] = (byte)0x34;
		ans = write12(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write12(temp7);
		}
		try{Thread.currentThread().sleep(90);}catch(Exception e){}
		status1 = dataPlus(change1(ans[5]), change1(ans[6]));
		status2 = dataPlus(change1(ans[7]), change1(ans[8]));
		status3 = dataPlus(change1(ans[9]), change1(ans[10]));
		return ansAnalysis();
	}
	//時刻設定
	public int timeSet(int para1, int para2, int para3){
		byte[] p1 = henkan2(para1);
		byte[] p2 = henkan2(para2);
		byte[] p3 = henkan2(para3);
		temp13[4] = (byte)0x91;
		temp13[5] = (byte)0x36;
		temp13[6] = p1[0];
		temp13[7] = p1[1];
		temp13[8] = p2[0];
		temp13[9] = p2[1];
		temp13[10] = p3[0];
		temp13[11] = p3[1];
		ans = write6(temp13);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp13);
		}
		return ansAnalysis();
	}
	//時刻要求
	public int timeReq(){
		temp7[4] = (byte)0x91;
		temp7[5] = (byte)0x36;
		ans = write12(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write12(temp7);
		}
		try{Thread.currentThread().sleep(90);}catch(Exception e){}
		status1 = dataPlus(change1(ans[5]), change1(ans[6]));
		status2 = dataPlus(change1(ans[7]), change1(ans[8]));
		status3 = dataPlus(change1(ans[9]), change1(ans[10]));
		return ansAnalysis();
	}
	//通電積算時間要求
	public int timeTotalReq(int para){
		byte p = henkan1(para);
		temp7[4] = (byte)0x92;
		temp7[5] = p;
		ans = write10(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write10(temp7);
		}
		status1 = dataPlus(change1(ans[5]), change1(ans[6]), change1(ans[7]), change1(ans[8]));
		return ansAnalysis();
	}
	//デフォルト設定
	public int initialize(){
		temp7[4] = (byte)0x93;
		temp7[5] = (byte)0x30;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//コマンド終了通知設定
	public int commandReportSet(int para){
		byte p = henkan1(para);
		temp7[4] = (byte)0x94;
		temp7[5] = p;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//グローバル通知設定
	public int globalReportSet(int para){
		byte p = henkan1(para);
		temp7[4] = (byte)0x95;
		temp7[5] = p;
		ans = write6(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp7);
		}
		return ansAnalysis();
	}
	//雲台モデル要求
	public int platformReq(){
		temp7[4] = (byte)0x9A;
		temp7[5] = (byte)0x30;
		ans = write7(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write7(temp7);
		}
		status1 = change1(ans[5]);
		return ansAnalysis();
	}
	//カメラモデル要求
	public int modelReq(){
		temp7[4] = (byte)0x9A;
		temp7[5] = (byte)0x31;
		ans = write7(temp7);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write7(temp7);
		}
		status1 = change1(ans[5]);
		return ansAnalysis();
	}
	private int ansAnalysis(){
		if(ans[3]==0x30){//正常
			System.out.println("正常");
			return 0;
		}else if(ans[3]==0x31){//ビジー
			System.out.println("ビジーエラー");
			return -1;
		}else if(ans[3]==0x35){//パラメータエラー
			System.out.println("パラメータエラー");
			return -2;
		}else if(ans[3]==0x39){//モードエラー
			System.out.println("モードエラー");
			return -3;
		}
		return 0;
	}
	
	/////////////   カメラ制御初期化処理   /////////////////////
	public void hostControlStep1(){
		exActionStatusReq();
	}
	public void hostControlStep2(){
		hostCtrl();
	}
	public void hostControlStep3(){
		exActionStatusReq();
	}
	
	/////////////	データ変換	10⇒16////////////////
	public byte henkan1(int x){
		byte result;
		result = toCameraCommand((byte)x);
		return result;
	}
	public byte[] henkan2(int x){
		byte result[] = new byte[2];
		int hs, ts;//商
		int ha, ta;//余
		hs = x / 256;
		ha = x % 256;
		ts = ha / 16;
		ta = ha % 16;
		result[0] = toCameraCommand((byte)ts);
		result[1] = toCameraCommand((byte)ta);
		return result;
	}
	public byte[] henkan3(int x){
//		System.out.println("byte x"+x);
		byte result[] = new byte[3];
		int hs, ts;//商
		int ha, ta;//余
		hs = x / 256;
		ha = x % 256;
		ts = ha / 16;
		ta = ha % 16;
		result[0] = toCameraCommand((byte)hs);
		result[1] = toCameraCommand((byte)ts);
		result[2] = toCameraCommand((byte)ta);
		return result;
	}
	
	public byte[] henkan4(int x){
		byte result[] = new byte[4];
		int ss, hs, ts;//商
		int sa, ha, ta;//余
		ss = x / 4096;
		sa = x % 4096;
		hs = sa / 256;
		ha = sa % 256;
		ts = ha / 16;
		ta = ha % 16;
		result[0] = toCameraCommand((byte)ss);
		result[1] = toCameraCommand((byte)hs);
		result[2] = toCameraCommand((byte)ts);
		result[3] = toCameraCommand((byte)ta);
		return result;
	}
	private byte toCameraCommand(byte x){
		byte result;
		if(x < 0xa){
			result = (new Integer( x + 0x30 )).byteValue();
		}else{
			result = (new Integer( x + 0x37 )).byteValue();
		}
		return result;
	}
	
	/////////////	データ変換	CameraMode⇒int10  ///////////////
	private int change1(int x){
		if(x <= 0x39){
			return x - 0x30;
		}else{
			return x - 0x37;
		}
	}
	
	/////// change1の帰り値を引数とすること
	private int dataPlus(int t, int o){
		return 16*t + o;
	}
	private int dataPlus(int h, int t, int o){
		return 256*h + 16*t + o;
	}
	private int dataPlus(int s, int h, int t, int o){
		return 4096*s + 256*h + 16*t + o;
	}
	private int dataPlus(int m, int s, int h, int t, int o){
		return 65536*m + 4096*s + 256*h + 16*t + o;
	}
	
	//10進数に変換したパラメータを入力すると角度に変換する
	//使用例:System.out.println(angelRecovery(dataPlus(7,14,15,5))); -30度の場合
	private int angelRecovery(int x){
		return (x - 32768) * 1125 / 10000;
	}
	private int angelCreate(int x){
		return x * 10000 / 1125 + 32768;
	}
	public static void main(String[] args){
		VCC4Control vcc = new VCC4Control(0, 9600, "COM1");
	}
}