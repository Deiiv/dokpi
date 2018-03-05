import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.Color;
import java.awt.Graphics; 
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import java.util.LinkedList;

public class InWindow extends GameObject
{
  public InWindow(int x, int y, ID id, int w, int h)
  {
    super(x, y, id, w, h);
  }

  private RoundRectangle2D inside, border, inside2, border2;
  private static LinkedList<Message> chat = new LinkedList<>();

  static class Message{
    int x = 0; //x and y of the player who said it
    int y = 0;
    String player = ""; //player's name
    String message = ""; //message
    String time = ""; //timestamp

    Message(String message, String player, int hour, int min, int x, int y){
      this.message = message;
      this.player = player;
      if(min < 10)
      {
        if(hour < 10) {
          this.time = "0" + hour + ":0" + min;
        }
        else
        {
          this.time = hour + ":0" + min;
        }
      }
      else
      {
        if(hour < 10) {
          this.time = "0" + hour + ":" + min;
        }
        else
        {
          this.time = hour + ":" + min;
        }      }
      this.x = x;
      this.y = y;
    }

    @Override
    public String toString(){
      return "[" + this.time + "] " + this.player + ": " + this.message;
    }
  }

  public void tick()
  {

  }

  public void setFocus(boolean focus)
  {
    if(focus)
    {
      Window.userInput.grabFocus();
    }
    msgFocus = focus;
  }

  //returns true if click is in the UI
  public boolean has(int x, int y)
  {
    return (inside.contains(x, y) || inside2.contains(x, y));
  }

  //returns true if click is in chat
  public boolean chatHas(int x, int y)
  {
    return (inside2.contains(x, y));
  }

  public static void addText(Message msg)
  {
    chat.addFirst(msg);
  }

  public void render(Graphics g)
  {
    Graphics2D g2 = (Graphics2D) g;

    float width = (float)(w/4.2);
    float arch = 75;

    //inventory
    border = new RoundRectangle2D.Float(w-width, (float)(h*0.32), width+arch, h+arch, arch, arch);
    inside = new RoundRectangle2D.Float(w-width, (float)(h*0.32), width+arch, h+arch, arch, arch);

    normal = true;
    //chat
    if(normal) {
      border2 = new RoundRectangle2D.Float(0 - arch, (float) (h * 0.75), (float) (w * 0.4), h + arch, arch, arch);
      inside2 = new RoundRectangle2D.Float(0 - arch, (float) (h * 0.75), (float) (w * 0.4), h + arch, arch, arch);
    }
    else
    {
      border2 = new RoundRectangle2D.Float(0 - arch, (float) (h * 0.68), (float) (w * 0.4), (float) (h * 0.75) + arch, arch, arch);
      inside2 = new RoundRectangle2D.Float(0 - arch, (float) (h * 0.68), (float) (w * 0.4), (float) (h * 0.75) + arch, arch, arch);
    }

    g.setColor(new Color(163, 144, 102, 180));
    g2.fill(inside);
   // g.setColor(new Color(0xe8a81e));
    if(msgFocus) {
      g.setColor(new Color(163, 144, 102, 180));
      //g.setColor(new Color(0.9f, 0.5f, 0.01f, 0.75f));
    }
    else {
      g.setColor(new Color(163, 144, 102, 120));
      //g.setColor(new Color(0.9f, 0.5f, 0.01f, 0.5f));
    }
    g2.fill(inside2);

    g2.setStroke(new BasicStroke(2));
    g2.setColor(Color.black);

    g2.draw(border);
    g2.draw(border2);

    //draw the chat content
    for(int i = 0; i < chat.size(); i++)
    {
      g.drawString(chat.get(i).toString(), 5, h - 70 - (15 * i));
    }
  }
}