import java.awt.*;
import java.awt.Graphics;

public class Player extends GameObject
{
  private Image player = iR.getImage();
  
  public Player(int x, int y, ID id, int w, int h)
  {
    super(x, y, id, w, h);
  }

  public void tick()
  {
    if(action.equals("mining"))
    {
      
    }
    else
    {
      if(direction.equals("r"))
      {
        player = iR.getImage();
      }
      else if(direction.equals("l"))
      {
        player = iL.getImage();
      }
      else if(direction.equals("u"))
      {
        player = iU.getImage();
      }
      else
      {
        player = iD.getImage();
      }
    }
  }

  public void setFocus(boolean focus)
  {
  }
  
  public void render(Graphics g)
  {
    x=w/2-23;
    y=h/2-92;
    g.drawImage(player, x, y, null);
  }

  public boolean has(int x, int y)
  {
    return false;
  }
  public boolean chatHas(int x, int y)
  {
    return false;
  }
}