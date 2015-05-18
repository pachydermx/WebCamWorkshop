/***************************************
RS232C�̒ʐM���s��
by okushiro
***************************************/
package vcc4;
import javax.comm.*;
import java.io.*;
import java.util.*;
import java.lang.Thread.*;

public class SerialControl implements SerialPortEventListener {
	protected SerialPort port;
	protected OutputStream writer;
	protected BufferedReader reader;
	protected PrintWriter comWriter;
	private int count=0;//ansData�̎w�W
	private byte[] ansData = new byte[14];//�J��������̋A��l
	private int readNum = 0;//read�̐���
	private boolean flag = true;//����M�̐���
	//�R���X�g���N�^
	public SerialControl(String portName){
		CommPortIdentifier portID = null;
		for(int i=0;i<14;i++){ansData[i]=0;}//������
			System.out.println("���������܂���");
        try{
            // CommPortIdentifier ���擾
            portID = CommPortIdentifier.getPortIdentifier(portName);
			System.out.println("protName = "+portName);
        }catch(NoSuchPortException ex){
            ex.printStackTrace();
            System.exit(1);
        }

        try{
            // �|�[�g�̃I�[�v��
            port = (SerialPort)portID.open("SerialControl", 60000);	//�^�C���A�E�g�܂�1��
			System.out.println("port Open");
			
        }catch(PortInUseException ex){
            // �^�C���A�E�g���߂����ꍇ
            ex.printStackTrace();
            System.exit(1);
        }
		
		try{
			// RS-232C �o�͗p�� Writer �𐶐�
            comWriter = new PrintWriter(new BufferedWriter(
                     new OutputStreamWriter(port.getOutputStream())));
		} catch (IOException ex){
            ex.printStackTrace();
            System.exit(1);
        }
		
	}
	
	public void setSerial(int bit_rate){
		setSerial(bit_rate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1);
	}
	public void setSerial(int bit_rate, int data_bit, int stop_bit){
		/**************************�ʐM�`���̐ݒ�********************************/
		try {
            // �ʐM�����̐ݒ�
            port.setSerialPortParams(bit_rate, 					//�r�b�g���[�g
                                     data_bit, 					// �f�[�^�r�b�g 8bit
                                     stop_bit, 					// �X�g�b�v�r�b�g 1bit
                                     SerialPort.PARITY_NONE); 	// �p���e�B �Ȃ�
			System.out.print("�ʐM�ݒ� : ");						 
			System.out.println("�r�b�g���[�gis"+bit_rate+"  �f�[�^�r�b�gis"+data_bit+"  �X�g�b�v�r�b�gis"+stop_bit+"  �p���e�Bis"+SerialPort.PARITY_NONE);
            // �t���[�R���g���[���̐ݒ�
            // �����ł̓n�[�h�t���[�R���g���[��(RTS/CTS) ���g�p
            port.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN
                                    | SerialPort.FLOWCONTROL_RTSCTS_OUT);
        } catch (UnsupportedCommOperationException ex){
            ex.printStackTrace();
            System.exit(1);
        }
		
		/**************************����M�̐ݒ�********************************/
		try {
            // �o�͗p�� Writer �𐶐�
            writer = port.getOutputStream();
        } catch (IOException ex){
            ex.printStackTrace();
            System.exit(1);
        }
		try {
            // SerialPortEvent ���󂯎�邽�߂̃��X�i�̓o�^
            port.addEventListener(this);
        } catch(TooManyListenersException ex){
            ex.printStackTrace();
            System.exit(1);
        }

        // Data Available �C�x���g���󂯎��悤�ɂ���
        port.notifyOnDataAvailable(true);
        try {
            // ���͗p�� Reader �𐶐�
            reader = new BufferedReader(
                     new InputStreamReader(port.getInputStream()));
        } catch (IOException ex){
            ex.printStackTrace();
            System.exit(1);
        }
	}
	private void setFlag(boolean f){
		this.flag = f;
	}
	
	private synchronized void checkFlag(boolean f){
		if(flag != f){
			System.out.println("���M���b�N");
			try{wait(10);}catch (InterruptedException e) { }
		}
	}
	private synchronized void kaijo(){
		notifyAll( );
	}
	
	// SerialPortEvent �������[�`��
    public  void serialEvent(SerialPortEvent event) {
		byte temp;
   	     		switch(event.getEventType()) {
   	       			case SerialPortEvent.BI:
 		  	        case SerialPortEvent.OE:
  		 	        case SerialPortEvent.FE:
   	       			case SerialPortEvent.PE:
   	       			case SerialPortEvent.CD:
   	       			case SerialPortEvent.CTS:
   	       			case SerialPortEvent.DSR:
   	       			case SerialPortEvent.RI:
   	       			case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
   	       			case SerialPortEvent.DATA_AVAILABLE:
    	         		try {
							count = 0;
   	             			while (reader.ready() || count <= readNum) {
								if(count >= readNum){count = 0;break;}
   	                			 // �f�[�^�̓ǂݍ���
								temp = (byte)reader.read();
								if(temp == -3){count=0;}
								ansData[count] = temp;
								System.out.println("count = " + count +" "+ansData[count]);
   	                 			count++;
   	             			}
   	         			} catch (IOException e){}
   	     		}
				setFlag(true);
				kaijo();//���b�N����������
    }
	
	public byte[] write(byte[] command){
		checkFlag(true);
		System.out.println("�f�[�^���M");
			try{
				writer.write(command);
				System.out.flush();
				writer.flush();
				setFlag(false);//���b�N
			} catch(IOException e){
				e.printStackTrace();
			}
		checkFlag(true);
		System.out.println("��������");
			System.out.println("ansData = " + ansData[0]+" "+ansData[1]+" "+ansData[2]+" "+ansData[3]+" "+ansData[4]+" "+ansData[5]+" "+ansData[6]+" "+ansData[7]+" "+ansData[8]+" "+ansData[9]+" "+ansData[10]+" "+ansData[11]);

		return ansData;
    }
	public byte[] write6(byte[] command){
		readNum = 5;
		return write(command);
    }
	public byte[] write7(byte[] command){
		readNum = 6;
		return write(command);
	}
	public byte[] write8(byte[] command){
		readNum = 7;
		return write(command);
    }
	public byte[] write9(byte[] command){
		readNum = 8;
		return write(command);
	}
	public byte[] write10(byte[] command){
		readNum = 9;
		return write(command);
    }
	public byte[] write11(byte[] command){
		readNum = 10;
		return write(command);
	}
	public byte[] write12(byte[] command){
		readNum = 11;
		return write(command);
    }
	public byte[] write13(byte[] command){
		readNum = 12;
		return write(command);
	}
	public byte[] write14(byte[] command){
		readNum = 13;
		return write(command);
    }
	public byte[] write15(byte[] command){
		readNum = 14;
		return write(command);
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
	
	public byte[] read(){
		return ansData;
	}
	
	public void close(){
        port.close();
		close();
    }
}
