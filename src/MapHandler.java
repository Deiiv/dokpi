import java.awt.*;
import java.util.*;

public class MapHandler
{
  LinkedList<MapObject> maps = new LinkedList<MapObject>();
  //taken off playerposX and y in dokpi (which is taken from db later)
  //updated to hold current position offset of the maps
  protected int offsetX, offsetY;
  protected int w, h;
  protected Graphics gIn;
  protected int bS = 128;   //block width
  protected int bSH = 64; //block height
  protected int mS = 23;   //grid size (mSxmS)

  public MapHandler(int x, int y, int w, int h)
  {
    this.offsetX = x;
    this.offsetY = y;
    this.w = w;
    this.h = h;
  }

  //change the starting point for all maps based on saved previous
  public void mapStart()
  {
    for (int i = 0; i < maps.size(); i++)
    {
      MapObject tempMap = maps.get(i);
      tempMap.mapStart(offsetX, offsetY);
    }
  }

  public void tick()
  {
    for (int i = 0; i < maps.size(); i++)
    {
      MapObject tempMap = maps.get(i);
      tempMap.tick();
    }
  }

  public void moveMap(int x, int y)
  {
    offsetX+=x;
    offsetY+=y;
    for (int i = 0; i < maps.size(); i++)
    {
      MapObject tempMap = maps.get(i);
      tempMap.moveMap(x, y);
    }
  }

  public void clickAnim(int x, int y, int i, int j)
  {
    for (int k = 0; k < maps.size(); k++)
    {
      MapObject tempMap = maps.get(k);
      tempMap.setClickOff();
      if(tempMap.numX == i && tempMap.numY == j)
      {
        tempMap.clickAnim(x, y, gIn);
      }
    }
  }

  public void setClickOff()
  {
    for (int i = 0; i < maps.size(); i++)
    {
      MapObject tempMaps = maps.get(i);
      tempMaps.setClickOff();
    }
  }

  public void render(Graphics g)
  {
    for (int i = 0; i < maps.size(); i++)
    {
      MapObject tempMaps = maps.get(i);
      tempMaps.render(g);
    }
  }

  public int getbs()
  {
    return bS;
  }
  public int getms()
  {
    return mS;
  }
  
  public void addMap(MapObject map)
  {
    this.maps.add(map);
  }

  //returns the grid position with what section it is in
  public int[] gridPos(int x, int y)
  {
    int[] temp = new int[7];
    temp[4] = -1;

    for (int i = 0; i < maps.size(); i++)
    {
      MapObject tempMaps = maps.get(i);
      //find the correct map section
      if(tempMaps.isInArea(x, y))
      {
        int[] pos = tempMaps.getClickPos(x, y);
        temp[0] = pos[0];
        temp[1] = pos[1];
        temp[2] = tempMaps.numX;
        temp[3] = tempMaps.numY;
        temp[4] = tempMaps.grid[pos[0]][pos[1]];
        temp[5] = x;
        temp[6] = y;
      }
    }

    return temp;
  }

  //returns int value of the grid at that point
  public int getGrid(int x, int y)
  {
    int value = -1;

    //now get the value of it
    for (int i = 0; i < maps.size(); i++)
    {
      MapObject tempMaps = maps.get(i);
      //find the correct map section
      if(tempMaps.isInArea(x, y))
      {
        int[] points = tempMaps.getClickPos(x, y);
        value = tempMaps.getGridValueAt(points[0], points[1]);
        //System.out.println("In Grid: (" + tempMaps.numX + ", " + tempMaps.numY + ") i: " + points[0] + " j: " + points[1] + " Value: " + value);
      }
    }

   // System.out.println("Original: (" + x + ", " + y + ") Map: (" + sectionX + ", " + sectionY + ") Grid pos: (" + gX + ", " + gY + ")");
   // System.out.println("Value at point: " + value);
    return value;
  }
  
  public void removeMap(MapObject map)
  {
    this.maps.remove(map);
  }
}