import javax.swing.*;
import java.awt.Graphics;

public abstract class GameObject
{
  protected int x, y;
  protected int w, h;
  protected ID id;
  protected int xSpd, ySpd;
  protected String direction = "";
  protected String action = "";
  protected boolean msgFocus;
  protected boolean normal;
  
  //ImageIcon iD = new ImageIcon("resources/playerD.gif");
  ImageIcon iD = new ImageIcon("resources/playerTemp2.png");
  ImageIcon iR = new ImageIcon("resources/playerR.gif");
  ImageIcon iL = new ImageIcon("resources/playerL.gif");
  ImageIcon iU = new ImageIcon("resources/playerU.gif");
  
  ImageIcon mD = new ImageIcon("resources/miningD.gif");
  ImageIcon mR = new ImageIcon("resources/miningR.gif");
  ImageIcon mL = new ImageIcon("resources/miningL.gif");
  ImageIcon mU = new ImageIcon("resources/miningU.gif");
  
  public GameObject(int x, int y, ID id, int w, int h)
  {
    this.x = x;
    this.y = y;
    this.id = id;
    this.w = w;
    this.h = h;
  }

  public abstract void setFocus(boolean focus);
  public abstract boolean has(int x, int y);
  public abstract boolean chatHas(int x, int y);
  public abstract void tick();
  public abstract void render(Graphics g);
  
  public void setAction(String x)
  {
    this.action = x;
  }
  
  public String getAction()
  {
    return this.action;
  }
  
  public void setDirection(String x)
  {
    this.direction = x;
  }
  
  public void setX(int x)
  {
    this.x = x;
  }
  public void setY(int y)
  {
    this.y = y;
  }
  
  public int getX()
  {
    return x;
  }
  public int getY()
  {
    return y;
  }
  
  public void setID(ID id)
  {
    this.id = id;
  }
  public ID getID()
  {
    return id;
  }
  
  public void setxSpd(int xSpd)
  {
    this.xSpd = xSpd;
  }
  public void setySpd(int ySpd)
  {
    this.ySpd = ySpd;
  }
  
  public int getxSpd()
  {
    return xSpd;
  }
  public int getySpd()
  {
    return ySpd;
  }
}