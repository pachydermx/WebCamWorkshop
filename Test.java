import java.io.*;
import java.util.*;
import java.util.Random;

class Test{
    int[][] ary;
    List<Cluster> listCluster;
    List<Point>   listPoint;

    public static void main(String[] args){
        Test t = new Test();
    }

    public Test(){
        int x=10,y=10;
        resetArray(x,y);
        makeRandomArray(30,x,y);

        System.out.println("*****Median Clustering*****");
        MedianClustering ci = new MedianClustering();
        ci.setArray(ary);
        ci.setThreshold(2);
        ci.doClustering();
        ci.printCluster();

        //ファイルにクラスタリングの結果を出力
        FileEdit fe = new FileEdit();
        fe.setName("result.txt");
        listCluster = ci.getCluster();
        double cluster_x,cluster_y;
        int point_x,point_y;
        int i=0;
        String log = "";
        fe.addLog("*****Median Clustering");
        for(Cluster c : listCluster){
            listPoint = c.getPoints();
            cluster_x = c.getX();
            cluster_y = c.getY();
            log = "Cluster "+ (i+1)+" = ("+cluster_x+","+cluster_y+")";
            fe.addLog(log);
            log = "";
            for(Point p : listPoint){
                point_x = p.getX();
                point_y = p.getY();
                log += "("+point_x +","+point_y+"),";
            }
            fe.addLog(log);
            i++;
        }
        fe.addLog("*****END");

        // クラスタの分解及び，そのクラスタに属する点の分解
        /*
        listCluster = ci.getCluster();
        double cluster_x,cluster_y;
        double point_x,point_y;
        for(Cluster c : listCluster){
            listPoint = c.getPoints();
            cluster_x = c.getX();
            cluster_y = c.getY();
            for(Point p : listPoint){
                point_x = p.getX();
                point_y = p.getY();
            }
        }
        */
    }

    void resetArray(int x, int y){
        ary = new int[x][y];
        int i,j;
        for(i=0;i<x;i++){
            for(j=0;j<y;j++){
                ary[i][j]=0;
            }
        }
    }

    void makeRandomArray(int n, int x, int y){
        Random rnd = new Random();
        int a,b,i,j;
        for(i=0;i<n;i++){
            j=0;
            while(true){
                a=rnd.nextInt(x);
                b=rnd.nextInt(y);
                if(x*y<=i)
                    break;
                if(ary[a][b]==0)
                    break;
                if(x*y<=j)
                    break;
                j++;
            }
            ary[a][b]=1;
            /*
            System.out.printf("set %3d : ",(i+1));
            System.out.printf(" X = %3d ::: Y = %3d\n",a,b);
            */
        }
    }
}
