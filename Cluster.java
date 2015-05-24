import java.util.*;

class Cluster{
    private List<Point> points = new ArrayList<Point>();
    private double x=0;
    private double y=0;

    public void setXY(double a, double b){
        x=a;
        y=b;
    }

    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }

    public void addPoint(Point p){
        points.add(p);
        setXY(p.getX(),p.getY());
    }

    public List<Point> getPoints(){
        return points;
    }
}
