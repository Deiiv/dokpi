import java.awt.*;
import java.util.LinkedList;

public abstract class MapObject
{
  protected int bS = 128;   //block width
  protected int bSH = 64; //block height
  protected int mS = 23;   //grid size (mSxmS)
  protected int x, y;
  protected int offSetX, offSetY;
  protected ID id;
  protected Polygon area;
  protected int ii, jj, val;
  protected int[] xx = new int[4];
  protected int[] yy = new int[4];
  protected LinkedList<AddArray> drawOntop;
  protected AddArray add;
  protected int clickBorder = 0;
  protected boolean drawClick = false;
  protected int[][] grid = new int[mS][mS];
  protected int[] points = new int[2];
  protected Polygon[][] poly = new Polygon[mS][mS];
  protected int[][] objects = new int[mS][mS];
  protected int[][] items = new int[mS][mS];
  protected int numX, numY; //the maps id and position (numX=0, numY=1 -> first row, second column)
  protected int[][] player = new int[mS][mS];
  protected boolean mapDraw = true; //to be used later to only draw visible maps
  protected boolean clickDraw = false;
  protected int[] click = new int[2];

  public MapObject(int x, int y, int[][] grid, int[][] objects, int[][] items, int[][] player, ID id, int numX, int numY)
  {
    this.x = x;
    this.y = y;
    this.grid = grid;
    this.objects = objects;
    this.items = items;
    this.player = player;
    this.id = id;
    this.numX = numX;
    this.numY = numY;
  }

  static class AddArray{
    int i, j, value;

    AddArray(int value, int i, int j){
      this.value = value;
      this.i = i;
      this.j = j;
    }
  }


  public abstract void setClickOff();
  public abstract void clickAnim(int x, int y, Graphics g);
  public abstract int[] getClickPos(int x, int y);
  public abstract boolean isInArea(int x, int y);
  public abstract void setArea(int[] x, int[] y, int gridX, int gridY);
  public abstract int getGridValueAt(int x, int y);
  public abstract int getNumX();
  public abstract int getNumY();
  public abstract void setBsMs(int x, int y);
  public abstract void tick();
  public abstract void moveMap(int x, int y);
  public abstract void mapStart(int x, int y);
  public abstract void render(Graphics g);
}
  