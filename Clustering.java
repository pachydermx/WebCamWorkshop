import java.io.*;
import java.util.*;
import java.math.BigDecimal;

abstract class Clustering{
    protected List<Point> points    = new ArrayList<Point>();
    protected List<Cluster> cluster   = new ArrayList<Cluster>();
    protected Point p;
    protected Cluster c;
    protected int[][] ary;
    protected double[][] ary_d;
    protected double threshold=1;
    protected double median_x=0;
    protected double median_y=0;
    protected int p_x=0;
    protected int p_y=0;

    abstract void combineCluster();

    public void setThreshold(double d){
        threshold = d;
    }

    public void setArray(int[][] tmpAry){
        ary = new int[tmpAry.length][tmpAry[0].length];
        int i,j;
        for(i=0;i<tmpAry.length;i++){
            for(j=0;j<tmpAry[0].length;j++){
                if(tmpAry[i][j]!=0){
                    p = new Point();
                    p.setXY(i,j);
                    points.add(p);
                }
            }
        }
        createCluster();
        createDistanceArray();
    }

    public List<Cluster> getCluster(){
        return cluster;
    }

    public List<Point> getPointList(Cluster c){
        return c.getPoints();
    }

    public double[] getPointXY(Cluster c){
        double[] result = new double[2];
        result[0] = c.getX();
        result[1] = c.getY();
        return result;
    }

    private void createCluster(){
        for(Point p : points){
            c = new Cluster();
            c.setXY(p.getX(),p.getY());
            c.addPoint(p);
            cluster.add(c);
        }
    }

    private void createDistanceArray(){
        int i,j,k,size=cluster.size();
        ary_d = new double[size][size];
        for(i=1;i<size;i++){
            for(j=0;j<i;j++){
	            ary_d[i][j] = calDistance(cluster.get(i),cluster.get(j));
            }
        }
	    //printPoint();
	    //printDistanceArray();
    }

	protected double calDistance(Cluster cluster_a, Cluster cluster_b){
	    double x=Math.abs(cluster_a.getX()-cluster_b.getX());
	    double y=Math.abs(cluster_a.getY()-cluster_b.getY());
	    return Math.sqrt((x*x)+(y*y));
	}

	protected double calDistance(Point point_a, Point point_b){
	    double x=Math.abs(point_a.getX()-point_b.getX());
	    double y=Math.abs(point_a.getY()-point_b.getY());
	    return Math.sqrt((x*x)+(y*y));
	}

    public void doClustering(){
        int size = cluster.size();
        while(true){
            //printCluster();
            if(findMinimum() == false || size <=1){
                break;
            }else{
                combineCluster();
                size--;
            }
        }
    }

    private boolean findMinimum(){
        boolean result = false;
        int i,j,size=cluster.size();
        int x=0,y=0;
        double k=99999999999.0;
        for(i=1;i<size;i++){
            for(j=0;j<i;j++){
                if( (ary_d[i][j] < k) && (ary_d[i][j] <= threshold) ){
                    k = ary_d[i][j];
                    x = i;
                    y = j;
                    result = true;
                }
            }
        }
        p_x = x;
        p_y = y;
        return result;
    }

    /* for debug */
    public void printCluster(){
        int i=0;
        List<Point> listPoint;
        for(Cluster c : cluster){
            listPoint = c.getPoints();
            System.out.println("Cluster "+(i+1));
            for(Point tmp_p : listPoint){
                System.out.print("("+tmp_p.getX()+","+tmp_p.getY()+"), ");
            }
            System.out.print("\n\n");
            i++;
        }
    }

	private void printPoint(){
	    for(Point p : points){
	        System.out.println("X: "+p.getX()+" Y: "+p.getY());
	    }
	}

	private void printDistanceArray(){
	    int n=0;
	    int i,j,size=cluster.size();
	    for(i=1;i<size;i++){
	        for(j=0;j<i;j++){
	            System.out.print(n++ +("\t:"));
	            System.out.printf("%5.3f\n",ary_d[i][j]);
	        }
	    }
	}
}
