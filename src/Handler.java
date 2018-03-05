import java.awt.*;
import java.util.*;

public class Handler
{
  static LinkedList<GameObject> object = new LinkedList<GameObject>();
  
  public void tick()
  {
    for (int i = 0; i < object.size(); i++)
    {
      GameObject tempObject = object.get(i);
      
      tempObject.tick();
    }
  }
  
  public void render(Graphics g)
  {
    for (int i = 0; i < object.size(); i++)
    {
      GameObject tempObject = object.get(i);
      
      tempObject.render(g);
    }
  }

  public boolean uiHas(int x, int y)
  {
    for (int i = 0; i < object.size(); i++)
    {
      GameObject temp = object.get(i);
      if(temp.id == ID.InWindow)
      {
        return temp.has(x, y);
      }
    }
    return false;
  }

  public boolean chatHas(int x, int y)
  {
    for (int i = 0; i < object.size(); i++)
    {
      GameObject temp = object.get(i);
      if(temp.id == ID.InWindow)
      {
        return temp.chatHas(x, y);
      }
    }
    return false;
  }

  public static void setFocus(boolean focus)
  {
    for (int i = 0; i < object.size(); i++)
    {
      GameObject temp = object.get(i);
      if(temp.id == ID.InWindow)
      {
        temp.setFocus(focus);
      }
    }
  }
  
  public void addObject(GameObject object)
  {
    this.object.add(object);
  }
  
  public void removeObject(GameObject object)
  {
    this.object.remove(object);
  }
}