package components;

import graphics.StatInterface;
import java.util.ArrayList;
import utility.Point;
import graphics.ExploredPointsInterface;

/**
 * Represents the explored area of a world.
 * @author kole
 * @since Nov 29, 2018
 */
public class Map implements StatInterface, ExploredPointsInterface {
    public static final int ESTIMATED_POINT = 0;
    public static final int EXPLORED_POINT = 1;
    private int exploredPointsCount = 0;
    private int revisitPointCount = 0;
    private int width = 0;
    private int height = 0;
    private boolean xMaxed = false;
    private boolean yMaxed = false;
    private ArrayList<ArrayList<Integer>> points;
    private ArrayList<Point> exploredPoints;
    /**
     * Initialize a new map. Maps start as a single point in space.
     */
    public Map(){
        points = new ArrayList<>();
        exploredPoints = new ArrayList<>();
    }
    /**
     * Register a point on the map as explored. The size of the map will expand
     * as needed.
     * @param point
     */
    public void addPoint(Point point){
        if(point.getX() >= width){
            expandX(point.getX()+1);
        }
        if(point.getY() >= height){
            expandY(point.getY()+1);
        }
        if(points.get(point.getX()).get(point.getY()) == ESTIMATED_POINT){
           ArrayList<Integer> temp = points.get(point.getX());
           temp.set(point.getY(), EXPLORED_POINT);
           points.set(point.getX(), temp);
           exploredPoints.add(point);
           exploredPointsCount++;
        }else{
           revisitPointCount++;
        } 
    }
    /**
     * Register a point on the left or right border.
     * @param point
     */
    public void addBorderPointX(Point point){
       addPoint(point);
       xMaxed = point.getX() > 0;
    }
    /**
     * Register a point on the top or bottom border.
     * @param point
     */
    public void addBorderPointY(Point point){
       addPoint(point);
       yMaxed = point.getY() > 0;
    }
    /**
     * Get the width of the map. Each point may not be explored, but this is
     * the minimum width the World can be considering the points that are explored.
     * @return width
     */
    public int getWidth(){
        return width;
    }
    /**
     * Get the height of the map. Each point may not be explored, but this is
     * the minimum height the World can be considering the points that are explored.
     * @return height
     */
    public int getHeight(){
        return height;
    }
    /**
     * Access the raw points of this map
     * @return 
     */
    public ArrayList<ArrayList<Integer>> getPoints(){
        return points;
    } 
    @Override
    public ArrayList<Point> getExploredPoints(){
        return exploredPoints;
    }
    /**
     * Get the percentage of the current known World size that is explored.
     * @return double
     */
    @Override
    public double percentExplored(){
        if(exploredPointsCount == 0){ return 0.0; }
        return (double) exploredPointsCount / (double) (width*height);
    }
    /**
     * Calculate how efficiently the map is being created by measuring
     * how many points were newly explored out of total number of points moved to.
     * @return 
     */
    @Override
    public double getEfficiency(){
        return (double) exploredPointsCount / (exploredPointsCount + revisitPointCount);
    }
    /**
     * Determine if the number of explored points is the same as the number of 
     * current points on the map.
     * @return 
     */
    public boolean isExplored(){
        return exploredPointsCount == (width*height);
    }
    /**
     * Determine if the passed point has already been explored;
     * @param point
     * @return bool
     */
    public boolean isExplored(Point point){
        if(point.getX() >= points.size() || point.getX() < 0){ return false; }
        else if(point.getY() >= points.get(point.getX()).size() || point.getY() < 0){ return false; }
        return points.get(point.getX()).get(point.getY()) == EXPLORED_POINT;
    }
    /**
     * Increase the width of the map to the specified integer value.
     * @param x width to expand to 
     */
    private void expandX(int x){
        for(int i = 0; i < x-width; i++){
            ArrayList<Integer> temp = new ArrayList<Integer>();
            for(int j = 0; j < height; j++){
                temp.add(ESTIMATED_POINT);
            }
            points.add(temp);
        }
        width = x;
    }
    /**
     * Increase the height of the map to the specified integer value.
     * @param y height to expand to 
     */
    private void expandY(int y){
        for(int i = 0; i < points.size(); i++){
            ArrayList<Integer> temp = points.get(i);
            for(int j = 0; j < y - height; j++){
                temp.add(ESTIMATED_POINT);
            }
            points.set(i, temp);
        }
        height = y;
    }
}
