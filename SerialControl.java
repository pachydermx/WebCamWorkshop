/***************************************
RS232Cの通信を行う
by okushiro
***************************************/
import javax.comm.*;
import java.io.*;
import java.util.*;
import java.lang.Thread.*;

public class SerialControl implements SerialPortEventListener {
	protected SerialPort port;
	protected OutputStream writer;
	protected BufferedReader reader;
	protected PrintWriter comWriter;
	private int count=0;//ansDataの指標
	private byte[] ansData = new byte[14];//カメラからの帰り値
	private int readNum = 0;//readの制御
	private boolean flag = true;//送受信の制御
	//コンストラクタ
	public SerialControl(String portName){
		CommPortIdentifier portID = null;
		for(int i=0;i<14;i++){ansData[i]=0;}//初期化
			System.out.println("初期化しました");
        try{
            // CommPortIdentifier を取得
            portID = CommPortIdentifier.getPortIdentifier(portName);
			System.out.println("protName = "+portName);
        }catch(NoSuchPortException ex){
            ex.printStackTrace();
            System.exit(1);
        }

        try{
            // ポートのオープン
            port = (SerialPort)portID.open("SerialControl", 60000);	//タイムアウトまで1分
			System.out.println("port Open");
			
        }catch(PortInUseException ex){
            // タイムアウトを過ぎた場合
            ex.printStackTrace();
            System.exit(1);
        }
		
		try{
			// RS-232C 出力用の Writer を生成
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
		/**************************通信形式の設定********************************/
		try {
            // 通信条件の設定
            port.setSerialPortParams(bit_rate, 					//ビットレート
                                     data_bit, 					// データビット 8bit
                                     stop_bit, 					// ストップビット 1bit
                                     SerialPort.PARITY_NONE); 	// パリティ なし
			System.out.print("通信設定 : ");						 
			System.out.println("ビットレートis"+bit_rate+"  データビットis"+data_bit+"  ストップビットis"+stop_bit+"  パリティis"+SerialPort.PARITY_NONE);
            // フローコントロールの設定
            // ここではハードフローコントロール(RTS/CTS) を使用
            port.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN
                                    | SerialPort.FLOWCONTROL_RTSCTS_OUT);
        } catch (UnsupportedCommOperationException ex){
            ex.printStackTrace();
            System.exit(1);
        }
		
		/**************************送受信の設定********************************/
		try {
            // 出力用の Writer を生成
            writer = port.getOutputStream();
        } catch (IOException ex){
            ex.printStackTrace();
            System.exit(1);
        }
		try {
            // SerialPortEvent を受け取るためのリスナの登録
            port.addEventListener(this);
        } catch(TooManyListenersException ex){
            ex.printStackTrace();
            System.exit(1);
        }

        // Data Available イベントを受け取るようにする
        port.notifyOnDataAvailable(true);
        try {
            // 入力用の Reader を生成
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
			System.out.println("送信ロック");
			try{wait(10);}catch (InterruptedException e) { }
		}
	}
	private synchronized void kaijo(){
		notifyAll( );
	}
	
	// SerialPortEvent 処理ルーチン
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
   	                			 // データの読み込み
								temp = (byte)reader.read();
								if(temp == -3){count=0;}
								ansData[count] = temp;
								System.out.println("count = " + count +" "+ansData[count]);
   	                 			count++;
   	             			}
   	         			} catch (IOException e){}
   	     		}
				setFlag(true);
				kaijo();//ロックを解除する
    }
	
	public byte[] write(byte[] command){
		checkFlag(true);
		System.out.println("データ送信");
			try{
				writer.write(command);
				System.out.flush();
				writer.flush();
				setFlag(false);//ロック
			} catch(IOException e){
				e.printStackTrace();
			}
		checkFlag(true);
		System.out.println("解除成功");
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