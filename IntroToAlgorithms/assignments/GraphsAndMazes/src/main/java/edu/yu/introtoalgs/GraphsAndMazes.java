package edu.yu.introtoalgs;

import java.util.*;

public class GraphsAndMazes {

  /** A immutable coordinate in 2D space.
   *
   * Students must NOT modify the constructor (or its semantics) in any way,
   * but can ADD whatever they choose.
   */
  public static class Coordinate { 
    public final int x, y;
    
    /** Constructor, defines an immutable coordinate in 2D space.
     *
     * @param x specifies x coordinate
     * @param y specifies x coordinate
     */
    public Coordinate(final int x, final int y) {
      this.x = x;
      this.y = y;
    }

    @Override
    public boolean equals(Object obj){
      if(this == obj){
        return true;
      }
      if(obj == null || obj.getClass()!= this.getClass()){
        return false;
      }
      Coordinate coordinate = (Coordinate) obj;
      return this.x == coordinate.x && this.y == coordinate.y;
    }

    @Override
    public String toString(){
      return "(" + this.x + "," + this.y + ")";
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.x, this.y);
    }

  } // Coordinate class
  public static boolean[][] tracker;
  public static boolean firstCall = true;
  public static int[] edgeTo;
  public static int mazeSize;
  public static int[][] maze1;
  /** Given a maze (specified by a 2D integer array, and start and end
   * Coordinate instances), return a path (beginning with the start
   * coordinate, and terminating wih the end coordinate), that legally
   * traverses the maze from the start to end coordinates.  If no such
   * path exists, returns an empty list.  The path need not be a
   * "shortest path".
   *
   * @param maze 2D int array whose "0" entries are interpreted as
   * "coordinates that can be navigated to in a maze traversal (can be
   * part of a maze path)" and "1" entries are interpreted as
   * "coordinates that cannot be navigated to (part of a maze wall)".
   * @param start maze navigation must begin here, must have a value
   * of "0"
   * @param end maze navigation must terminate here, must have a value
   * of "0"
   * @return a path, beginning with the start coordinate, terminating
   * with the end coordinate, and intervening elements represent a
   * legal navigation from maze start to maze end.  If no such path
   * exists, returns an empty list.  A legal navigation may only
   * traverse maze coordinates, may not contain coordinates whose
   * value is "1", may only traverse from a coordinate to one of its
   * immediate neighbors using one of the standard four compass
   * directions (no diagonal movement allowed).  A legal path may not
   * contain a cycle.  It is legal for a path to contain only the
   * start coordinate, if the start coordinate is equal to the end
   * coordinate.
   */
  public static List<Coordinate> searchMaze (final int[][] maze, final Coordinate start, final Coordinate end) {
    if (start.y >= maze.length || start.y < 0 || start.x >= maze[start.y].length || start.x < 0 || end.y >= maze.length || end.y < 0 || end.x >= maze[end.y].length || end.x < 0){
      throw new IllegalArgumentException("Out of bounds");
    }
    mazeSize = maze.length;
    int largestLine = maze.length;
    for (int i = 0; i < maze.length; i++){
      if (maze[i].length > largestLine){
        largestLine = maze[i].length;
      }
    }
    edgeTo = new int[largestLine*largestLine];
    maze1 = maze;
    if (maze1[start.y][start.x] == 1 || maze1[end.y][end.x] == 1){
      return new ArrayList<Coordinate>();
    }
    findPath(start);
    return pathTo(getPosition(end), getPosition(start));
  }

  private static int getPosition(Coordinate coordinate){
    return coordinate.y + (coordinate.x*mazeSize);
  }

  private static Coordinate getCoordinate(int position){
    return new Coordinate( (int) (position/mazeSize), position%mazeSize);
  }

  private static List<Coordinate> pathTo(int position, int start) {
    List<Coordinate> list = new ArrayList<Coordinate>();
    if(maze1[getCoordinate(position).y][getCoordinate(position).x] == 0){
      return list;
    }
    for (int x = position ; x != start; x = edgeTo[x]) {
      list.add(getCoordinate(x));
    }
    list.add(getCoordinate(start));
    Collections.reverse(list);
    return list;
  }

  private static void findPath(Coordinate start){
    maze1[start.y][start.x] = 1;
    Stack<Coordinate> stack = new Stack<>();
    stack.push(start);

    while(!stack.empty()){
      start = stack.pop();
      maze1[start.y][start.x] = 1;
      if (start.x+1 < maze1[start.y].length && maze1[start.y][start.x+1]==0){
        Coordinate newStart = new Coordinate(start.x+1, start.y);
        stack.push(newStart);
        edgeTo[getPosition(newStart)] = getPosition(start);
      }
      if (start.x-1 >= 0 && maze1[start.y][start.x-1]==0){
        Coordinate newStart = new Coordinate(start.x-1, start.y);
        stack.push(newStart);
        edgeTo[getPosition(newStart)] = getPosition(start);
      }
      if (start.y+1 < maze1.length && start.x < maze1[start.y+1].length && maze1[start.y+1][start.x]==0){
        Coordinate newStart = new Coordinate(start.x, start.y+1);
        stack.push(newStart);
        edgeTo[getPosition(newStart)] = getPosition(start);
      }
      if (start.y-1 >= 0 && start.x < maze1[start.y-1].length && maze1[start.y-1][start.x]==0){
        Coordinate newStart = new Coordinate(start.x, start.y-1);
        stack.push(newStart);
        edgeTo[getPosition(newStart)] = getPosition(start);
      }
    }
  }

  /** minimal main() demonstrates use of APIs
   */
  public static void main (final String [] args)
  {
    final int [][] exampleMaze = {
            {0 , 0 , 0} ,
            {0 , 1 , 0} ,
            {0 , 0 , 0} ,
            {0 , 1 , 0} ,
            {0 , 0 , 0} ,
            {0 , 1 , 0} ,
            {0 , 0 , 0} ,
            {0 , 1 , 0}
    };
    /*
    int[][] exampleMaze = new int[3][];
    exampleMaze[0] = new int[] {0, 0, 0};
    exampleMaze[1] = new int[] {0, 0, 0, 0};
    exampleMaze[2] = new int[] {0, 0};
    final int [][] exampleMaze = new int[500][500];
    for (int i = 0; i<exampleMaze.length; i++){
      for (int j = 0; j < exampleMaze[0].length; j++){
        exampleMaze[i][j] = 0;
      }
    }
    */

    final Coordinate start = new Coordinate (0 , 0) ;
    final Coordinate end = new Coordinate (1 , 2) ;
    final List < Coordinate > path = searchMaze(exampleMaze, start, end);
    System.out.println("path = " + path);
  }

}
