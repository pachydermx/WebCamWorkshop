import vcc4.*;

class CameraControlSample{
	
	/* �J�����ʐM�����ݒ�p�����[�^�i�ʏ�͕ύX����K�v�Ȃ��j */
	final static int CameraNum = 0;			//�P�ƃJ�����𐧌䂷��ꍇ�͌Œ�
	final static int BITRATE = 9600;		//�J�����Ƃ̒ʐM���x
	
	
	final static String PORT = "COM1";		//�ʐM�|�[�g�̐ݒ�i�n�[�h�E�F�A�\���ɂ��C�ύX�̕K�v������ꍇ����j
	
	/* �����܂� */
	
	VCC4Control vcc4;						//�J��������p�N���X�̐錾�iVCC5�ł�����m�F�ρj
	
	/* �R���X�g���N�^ */
	public CameraControlSample(){
		/* �J�����̏����ݒ� */
		vcc4 = new VCC4Control(CameraNum, BITRATE, PORT);
		
		vcc4.cameraON();					//�J�����̓d��������
	}
	
	/* ���݂̃p���ƃ`���g�̊p�x���o���Ă݂� */
	public void printPanTiltTest(){
		vcc4.pantiltAngleReq();			//�p���ƃ`���g�̊p�x���擾
		System.out.println("�J�����̊p�x" + vcc4.Status1() + "&" + vcc4.Status2());
	}
	
	public void cameraSwingTest(){
		int pan = 30;
		int tilt = 30;
		vcc4.pantiltAngleSet(pan, tilt);			//�p���ƃ`���g�̊p�x���w�肵�ē������Ă݂悤
	}
	
	public void cameraSwingTest2(){
		int pan = -30;
		int tilt = -30;
		vcc4.pantiltAngleSet(pan, tilt);			//�p���ƃ`���g�̊p�x���w�肵�ē������Ă݂悤
	}
	
	public void end(){
		vcc4.cameraOFF();					//�J�����d��OFF
	}
	
	public static void main(String[] args) throws Exception{
		System.out.println("***������***");
		CameraControlSample ccs = new CameraControlSample();	//�R���X�g���N�^�̐錾
		
		ccs.printPanTiltTest();
		Thread.sleep(3000);
		
		System.out.println("***�e�X�g�X�^�[�g***");
		ccs.cameraSwingTest();									//�e�X�g���\�b�h�̌Ăяo��
		
		Thread.sleep(3000);
		
		System.out.println("***�e�X�g2�X�^�[�g***");
		ccs.cameraSwingTest2();									//�e�X�g���\�b�h�̌Ăяo��
		
		Thread.sleep(3000);
		
		ccs.printPanTiltTest();
		System.out.println("***�I��***");
		ccs.end();												//�I������
	}
}
