/***************************************
RS232C��p����VCC4�̃J�����𐧌䂷��
by okushiro
***************************************/
package vcc4;
public class VCC4Control extends SerialControl{
	private byte cn = 0;//�J�����i���o�[�i�ʏ��0�j
	private int status1 = 0, status2 = 0, status3 = 0;
	byte[] ans;//�A���T
	private byte[] temp6 = {(byte)0xFF, (byte)0x30, cn,  (byte)0x00, (byte)0x00, (byte)0xEF};
	private byte[] temp7 = {(byte)0xFF, (byte)0x30, cn,  (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xEF};
	private byte[] temp8 = {(byte)0xFF, (byte)0x30, cn,  (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xEF};
	private byte[] temp9 = {(byte)0xFF, (byte)0x30, cn,  (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xEF};
	private byte[] temp10 = {(byte)0xFF, (byte)0x30, cn,  (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xEF};
	private byte[] temp12 = {(byte)0xFF, (byte)0x30, cn,  (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xEF};
	private byte[] temp13 = {(byte)0xFF, (byte)0x30, cn,  (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xEF};
	private byte[] temp14 = {(byte)0xFF, (byte)0x30, cn,  (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xEF};
	private byte[] temp15 = {(byte)0xFF, (byte)0x30, cn,  (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xEF};

	//CameraNum�ˑ���ΏۂƂȂ�J����NO�Œʏ��0��OK
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
		setSerial(bitrate);//RS-232C�֘A�̐ݒ�
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

	//�J����ON
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
	//�J����OFF
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
	//�p�����x�w��
	//8�`800
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
	//�`���g���x�w��
	//8�`622
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
	//�p�����x�v��
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
	//�`���g���x�v��
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
	//�p���E�`���g�X�g�b�v
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
	
	//�p���E�X�^�[�g
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
	//�p�����X�^�[�gsample
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
	//�`���g��X�^�[�g
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
	//�`���g���X�^�[�g
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
	//�z�[���|�W�V����  ok
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
	//�_��C�j�V�����C�Y1
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
	//�_��C�j�V�����C�Y2
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
	
	//�p���Œᑬ�x�v��
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
	
	//�p���ō����x�v��
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
	
	//�`���g�Œᑬ�x�v��
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
	//�`���g�ō����x�v��
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
	//�p���p�x�ϊ��W���v��
	//�g�p����Ƃ��͌��ʂ�10000�Ŋ������l���g�p���Ă�������
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
	//�`���g�p�x�ϊ��W���v��
	//�g�p����Ƃ��͌��ʂ�10000�Ŋ������l���g�p���Ă�������
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
	//�p���Œ�p�x�v��
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
	//�p���ō��p�x�v��
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
	//�`���g�Œ�p�x�v��
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
	//�`���g�ō��p�x�v��
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
	//�p���E�`���g�X�g�b�v
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
	//�p���E�`���g�X�^�[�g�X�g�b�v
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
	//�p����`���g�p�x�w��
	//pan��-100�`+100		tilt��-90�`+100
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
	//�p����`���g�p�x�v��
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
	//�p�����͈͎w��	�����͊p�x��
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
	//�`���g���͈͎w��   �����͊p�x��
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
	//�p�����͈͗v��
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
	//�`���g���͈͗v��
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
	//�t�H�[�J�XAF
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
	//�t�H�[�J�XMUNUAL
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
	//�t�H�[�J�XNEAR
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
	//�t�H�[�J�XFAR
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
	//�t�H�[�J�X�ʒu�w��
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
	//�t�H�[�J�X�ʒu�v��
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
	//�����v�b�V��AF
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
	//�t�H�[�J�X�͈͗v��
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
	//�Y�[��STOP
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
	//�Y�[��WIDE
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
	//�Y�[��TELE
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
	//�Y�[��HIWIDE
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
	//�Y�[��HITELE
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
	//�Y�[���ʒu�w��1	00�`80
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
	//�Y�[���ʒu�w��2	00�`80
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
	//�Y�[���ʒu�v��1	00�`80
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
	//�Y�[���ʒu�v��2	00�`80
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
	//�Y�[�����x�w��
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
	//�Y�[�����x�v��
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
	//�Y�[���ʒu�ő�l�v��
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
	//�t���␳OFF
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
	//�t���␳ON
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
	//�I�o���[�hAUTO
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
	//�I�o���[�hManual
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
	//AE���b�NOFF
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
	//AE���b�NON
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
	//�V���b�^�[�X�s�[�h�v���O����
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
	//�V���b�^�[�X�s�[�h 1/60(PAL:1/50)
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
	//�V���b�^�[�X�s�[�h 1/100(PAL:1/120)
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
	//�V���b�^�[�X�s�[�h�w��
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
	//�V���b�^�[�X�s�[�h�v��
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
	//AGC�Q�C���w��
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
	//AGC�Q�C���v��
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
	//�i��w��
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
	//�i��v��
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
	//AE�ڕW�l�w��
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
	//AE�ڕW�l�v��
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
	//�I�[�g�z���C�g�o�����X �m�[�}��
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
	//�I�[�g�z���C�g�o�����X ���b�N
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
	//�z���C�g�o�����X �}�j���A�����[�h
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
	//�z���C�g�o�����X �}�j���A���ݒ�l�w��
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
	//�z���C�g�o�����X �}�j���A���ݒ�l�v��
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
	//�t�F�[�h �m�[�}��
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
	//���t�F�[�h
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
	//�������t�F�[�h
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
	//�������t�F�[�h
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
	//�J�������Z�b�g
	public int reset(){
		temp6[4] = (byte)0xAA;
		ans = write6(temp6);
		if(ans[3]==0x31){
			try{Thread.currentThread().sleep(90);}catch(Exception e){}
			ans = write6(temp6);
		}
		return ansAnalysis();
	}
	//�Y�[����v��
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
	//��f�T�C�Y�v��	statsu1 / status2 �̒l���g�p���邱��
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
	//���i�o�[�W�����v��
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
	//EEPROM�o�[�W�����v��
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
	//�����R��ON
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
	//�����R��OFF
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
	//����X�e�[�^�X�v��
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
	//�g������X�e�[�^�X�v��
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
	//�@�햼�v��
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
	//ROM�o�[�W�����v��
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
	//�v���Z�b�g�Z�b�g
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
	//�v���Z�b�g�ړ�
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
	//�v���Z�b�g�X�e�[�^�X�v��
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
	//�g���v���Z�b�g�X�e�[�^�X�v��
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
	//�����R���X���[�ݒ�
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
	//LED�m�[�}���\��
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
	//LED��������
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
	//�J�X�P�[�hOFF
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
	//�J�X�P�[�hON
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
	//�z�X�g���䃂�[�h
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
	//���[�J�����䃂�[�h
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
	//�X�N���[������
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
	//�\�������f�[�^�̎w��
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
	//�\�������f�[�^�̗v��
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
	//���t�ݒ�
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
	//���t�v��
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
	//�����ݒ�
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
	//�����v��
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
	//�ʓd�ώZ���ԗv��
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
	//�f�t�H���g�ݒ�
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
	//�R�}���h�I���ʒm�ݒ�
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
	//�O���[�o���ʒm�ݒ�
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
	//�_�䃂�f���v��
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
	//�J�������f���v��
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
		if(ans[3]==0x30){//����
			System.out.println("����");
			return 0;
		}else if(ans[3]==0x31){//�r�W�[
			System.out.println("�r�W�[�G���[");
			return -1;
		}else if(ans[3]==0x35){//�p�����[�^�G���[
			System.out.println("�p�����[�^�G���[");
			return -2;
		}else if(ans[3]==0x39){//���[�h�G���[
			System.out.println("���[�h�G���[");
			return -3;
		}
		return 0;
	}
	
	/////////////   �J�������䏉��������   /////////////////////
	public void hostControlStep1(){
		exActionStatusReq();
	}
	public void hostControlStep2(){
		hostCtrl();
	}
	public void hostControlStep3(){
		exActionStatusReq();
	}
	
	/////////////	�f�[�^�ϊ�	10��16////////////////
	public byte henkan1(int x){
		byte result;
		result = toCameraCommand((byte)x);
		return result;
	}
	public byte[] henkan2(int x){
		byte result[] = new byte[2];
		int hs, ts;//��
		int ha, ta;//�]
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
		int hs, ts;//��
		int ha, ta;//�]
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
		int ss, hs, ts;//��
		int sa, ha, ta;//�]
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
	
	/////////////	�f�[�^�ϊ�	CameraMode��int10  ///////////////
	private int change1(int x){
		if(x <= 0x39){
			return x - 0x30;
		}else{
			return x - 0x37;
		}
	}
	
	/////// change1�̋A��l�������Ƃ��邱��
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
	
	//10�i���ɕϊ������p�����[�^����͂���Ɗp�x�ɕϊ�����
	//�g�p��:System.out.println(angelRecovery(dataPlus(7,14,15,5))); -30�x�̏ꍇ
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