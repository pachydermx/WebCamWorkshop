import java.util.*;
import java.math.BigDecimal;

class MedianClustering extends Clustering{
    protected void combineCluster(){
        double x,y;
        double sum = (cluster.get(p_x).getX() + cluster.get(p_y).getX());
        BigDecimal tmp_a = new BigDecimal(sum);
        BigDecimal tmp_b = new BigDecimal((double)2.0);
        x = tmp_a.divide(tmp_b, 5, BigDecimal.ROUND_HALF_UP).doubleValue();
        tmp_a = new BigDecimal(cluster.get(p_x).getY() + cluster.get(p_y).getY());
        y = tmp_a.divide(tmp_b, 5, BigDecimal.ROUND_HALF_UP).doubleValue();

        Cluster cl = new Cluster();
        cl.setXY(x,y);
        List<Point> points = cluster.get(p_x).getPoints();
        for(Point p : points){
            cl.addPoint(p);
        }
        points = cluster.get(p_y).getPoints();
        for(Point p : points){
            cl.addPoint(p);
        }
        Cluster c_tmp=cluster.get(p_x);
        cluster.remove(c_tmp);
        c_tmp=cluster.get(p_y);
        cluster.remove(c_tmp);
        cl.setXY(x,y);
        cluster.add(cl);
        int size = cluster.size();
        ary_d = new double[size][size];
        int i,j;
        for(i=1;i<size;i++){
            for(j=0;j<i;j++){
	            ary_d[i][j] = calDistance(cluster.get(i),cluster.get(j));
            }
        }
    }

        private void calMedian(Cluster cluster_a, Cluster cluster_b){
        BigDecimal tmp_a = new BigDecimal(cluster_a.getX() + cluster_b.getX());
        BigDecimal tmp_b = new BigDecimal((double)2.0);
        median_x = tmp_a.divide(tmp_b, 5, BigDecimal.ROUND_HALF_UP).doubleValue();
        tmp_a = new BigDecimal(cluster_a.getY() + cluster_b.getY());
        median_y = tmp_a.divide(tmp_b, 5, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
