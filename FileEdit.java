import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

class FileEdit{
    private String name = "log.csv";

    public FileEdit(){
        String log = "LOG TEST";
    }

    public void setName(String n){
        name = n;
        createFile();
    }

    private void createFile(){
        File newfile = new File(name);
        try{
            if(newfile.createNewFile()){
                //System.out.println("ファイルの作成に成功しました");
            }else{
                //System.out.println("ファイルの作成に失敗しました");
            }
        }catch(IOException e){
            System.out.println(e);
        }
    }

    public void addLog(String log){
        File outputFile = new File(name);
        try{
            FileOutputStream fos = new FileOutputStream(outputFile,true);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            PrintWriter pw = new PrintWriter(osw);
            pw.println(log);
            pw.close();
        }catch(Exception e){
              e.printStackTrace();
        }
    }
}
