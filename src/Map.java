import java.awt.*;
import java.awt.Color;
import javax.swing.*;
import java.awt.Graphics;
import java.util.LinkedList;

public class Map extends MapObject
{
  private Image rock, copper, water, waterL, waterR, tin, silver, dirt, grass, border;

  public Map(int x, int y, int[][] map, int[][] objects, int[][] items, int[][] players, ID id, int numX, int numY)
  {
    super(x, y, map, objects, items, players, id, numX, numY);

    ImageIcon iDirt = new ImageIcon("resources/dirt.png");
    dirt = iDirt.getImage();

    ImageIcon iGrass = new ImageIcon("resources/dirtGrass.png");
    grass = iGrass.getImage();

    ImageIcon iBorder = new ImageIcon("resources/border.png");
    border = iBorder.getImage();

    ImageIcon iCopper = new ImageIcon("resources/copper.png");
    copper = iCopper.getImage();
    
    ImageIcon iTin = new ImageIcon("resources/tin4.png");
    tin = iTin.getImage();
    
    ImageIcon iSilver = new ImageIcon("resources/silver4.png");
    silver = iSilver.getImage();
    
    ImageIcon iRock = new ImageIcon("resources/rock.png");
    rock = iRock.getImage();
    
    ImageIcon iWater = new ImageIcon("resources/water.png");
    water = iWater.getImage();
    ImageIcon iWaterL = new ImageIcon("resources/waterL.png");
    waterL = iWaterL.getImage();
    ImageIcon iWaterR = new ImageIcon("resources/waterR.png");
    waterR = iWaterR.getImage();

    drawOntop = new LinkedList<>();

    /*
      Note: offSetX and offSetY account for the displacement of the maps in relation to their coordinates (numX and numY)
    */
    offSetX = (x/2-bSH) + bS * mS * numX + (bS/2*mS); //- (x/2-(int)(bS*mS*0.25));
    if(numY > 0 && numY%2 != 0) {
      offSetY = bSH * mS * numY - (bSH/2*mS) + (y/2-bSH*mS/2);
    }
    else if(numY < 0 && numY%2 != 0) {
      offSetY = bSH * mS * numY + (bSH/2*mS) + (y/2-bSH*mS/2);
    }
    else {
      offSetX = (x/2-bSH) + bS * mS * numX;// - (x/2-(int)(bS*mS*0.25));
      offSetY = bSH * mS * numY + (y/2-bSH*mS/2);
    }

    for(int i = 0; i < mS; i++)
    {
      for(int j = 0; j < mS; j++)
      {
        poly[i][j] = new Polygon();
      }
    }

  }

  public int getNumX()
  {
    return numX;
  }
  public int getNumY()
  {
    return numY;
  }

  public int getGridValueAt(int x, int y)
  {
    return grid[x][y];
  }

  public void setArea(int[] x, int[] y, int gridX, int gridY)
  {
    poly[gridX][gridY] = new Polygon(x, y, 4);
  }

  public boolean isInArea(int x, int y)
  {
    for(int i = 0; i < mS; i++)
    {
      for(int j = 0; j < mS; j++)
      {
        if(poly[i][j].contains(x, y))
        {
          points[0] = i;
          points[1] = j;
          return true;
        }
      }
    }
    return false;
  }

  public int[] getClickPos(int x, int y)
  {
    return points;
  }

  public void setBsMs(int b, int m)
  {
    bS = b;
    mS = m;
  }

  public void moveMap(int x, int y)
  {
    offSetX+=x;
    offSetY+=y;
  }

  public void mapStart(int x, int y)
  {
    offSetX+=x*bS;
    offSetY+=y*bS;
  }

  public void tick()
  {
    if(clickBorder == 30)
    {
      drawClick = true;
    }
    if(clickBorder == 00)
    {
      clickDraw = false;
      drawClick = false;
    }
    if(drawClick) {
      clickBorder--;
    }
    else {
      clickBorder++;
    }
    //change numbers in relation to what happens
  }

  public void clickAnim(int x, int y, Graphics g)
  {
    clickDraw = true;
    click[0] = x;
    click[1] = y;
  }

  public void setClickOff()
  {
    clickDraw = false;
  }

  public void render(Graphics g)
  {
    if(mapDraw) {
      drawOntop.clear();
      for (int i = 0; i < mS; i++) {
        for (int j = 0; j < mS ; j++) {
          //dirt
          if (grid[i][j] == 00) {
            g.drawImage(dirt, offSetX + ((i - j) * bS/2), offSetY + ((j + i) * bSH/2), null);

            xx[0] = offSetX + ((i - j) * bS / 2) + bS / 2;
            xx[1] = offSetX + ((i - j) * bS / 2) + bS;
            xx[2] = offSetX + ((i - j) * bS / 2) + bS / 2;
            xx[3] = offSetX + ((i - j) * bS / 2);
            yy[0] = offSetY + ((j + i) * bSH / 2);
            yy[1] = offSetY + ((j + i) * bSH / 2) + bSH / 2;
            yy[2] = offSetY + ((j + i) * bSH / 2) + bSH;
            yy[3] = offSetY + ((j + i) * bSH / 2) + bSH / 2;
            setArea(xx, yy, i, j);

          }
          //grass
          if (grid[i][j] == 01) {
            g.drawImage(grass, offSetX + ((i - j) * bS/2), offSetY + ((j + i) * bSH/2), null);

            xx[0] = offSetX + ((i - j) * bS / 2) + bS / 2;
            xx[1] = offSetX + ((i - j) * bS / 2) + bS;
            xx[2] = offSetX + ((i - j) * bS / 2) + bS / 2;
            xx[3] = offSetX + ((i - j) * bS / 2);
            yy[0] = offSetY + ((j + i) * bSH / 2);
            yy[1] = offSetY + ((j + i) * bSH / 2) + bSH / 2;
            yy[2] = offSetY + ((j + i) * bSH / 2) + bSH;
            yy[3] = offSetY + ((j + i) * bSH / 2) + bSH / 2;
            setArea(xx, yy, i, j);

          }
          //water
          if (grid[i][j] == 20) {
            g.drawImage(water, offSetX + ((i - j) * bS/2), offSetY + ((j + i) * bSH/2), null);

            xx[0] = offSetX + ((i - j) * bS / 2) + bS / 2;
            xx[1] = offSetX + ((i - j) * bS / 2) + bS;
            xx[2] = offSetX + ((i - j) * bS / 2) + bS / 2;
            xx[3] = offSetX + ((i - j) * bS / 2);
            yy[0] = offSetY + ((j + i) * bSH / 2);
            yy[1] = offSetY + ((j + i) * bSH / 2) + bSH / 2;
            yy[2] = offSetY + ((j + i) * bSH / 2) + bSH;
            yy[3] = offSetY + ((j + i) * bSH / 2) + bSH / 2;
            setArea(xx, yy, i, j);
          }
          //waterL
          if (grid[i][j] == 21) {
            add = new AddArray(21, i, j);
            drawOntop.add(add);
          }
          //waterR
          if (grid[i][j] == 22) {
            add = new AddArray(22, i, j);
            drawOntop.add(add);
          }
          //Copper
          if (grid[i][j] == 11) {
            add = new AddArray(11, i, j);
            drawOntop.add(add);
          }
          //Tin
          if (grid[i][j] == 12) {
            add = new AddArray(12, i, j);
            drawOntop.add(add);
          }
          //Silver
          if (grid[i][j] == 13) {
            add = new AddArray(13, i, j);
            drawOntop.add(add);
          }

          //border
          g.drawImage(border, offSetX + ((i - j) * bS/2), offSetY + ((j + i) * bSH/2), null);
        }
      }

      while(drawOntop.size() != 0) {
        ii = drawOntop.getFirst().i;
        jj = drawOntop.getFirst().j;
        val = drawOntop.getFirst().value;
        drawOntop.removeFirst();

        //water left corner ground
        if (val == 21) {
          g.drawImage(waterL, offSetX + ((ii - jj) * bS / 2), offSetY + ((jj + ii) * bSH / 2), null);

          xx[0] = offSetX + ((ii - jj) * bS / 2) + bS / 2;
          xx[1] = offSetX + ((ii - jj) * bS / 2) + bS;
          xx[2] = offSetX + ((ii - jj) * bS / 2) + bS / 2;
          xx[3] = offSetX + ((ii - jj) * bS / 2);
          yy[0] = offSetY + ((jj + ii) * bSH / 2);
          yy[1] = offSetY + ((jj + ii) * bSH / 2) + bSH / 2;
          yy[2] = offSetY + ((jj + ii) * bSH / 2) + bSH;
          yy[3] = offSetY + ((jj + ii) * bSH / 2) + bSH / 2;
          setArea(xx, yy, ii, jj);
        }
        //water right corner ground
        if (val == 22) {
          g.drawImage(waterR, offSetX + ((ii - jj) * bS / 2), offSetY + ((jj + ii) * bSH / 2), null);

          xx[0] = offSetX + ((ii - jj) * bS / 2) + bS / 2;
          xx[1] = offSetX + ((ii - jj) * bS / 2) + bS;
          xx[2] = offSetX + ((ii - jj) * bS / 2) + bS / 2;
          xx[3] = offSetX + ((ii - jj) * bS / 2);
          yy[0] = offSetY + ((jj + ii) * bSH / 2);
          yy[1] = offSetY + ((jj + ii) * bSH / 2) + bSH / 2;
          yy[2] = offSetY + ((jj + ii) * bSH / 2) + bSH;
          yy[3] = offSetY + ((jj + ii) * bSH / 2) + bSH / 2;
          setArea(xx, yy, ii, jj);
        }
        //Copper
        //copper image is atm 128 by 128
        //so the top val drawn needs to be minus 64
        if (val == 11) {
          g.drawImage(copper, offSetX + ((ii - jj) * bS / 2), offSetY + ((jj + ii) * bSH / 2) - 64, null);

          xx[0] = offSetX + ((ii - jj) * bS / 2) + bS / 2;
          xx[1] = offSetX + ((ii - jj) * bS / 2) + bS;
          xx[2] = offSetX + ((ii - jj) * bS / 2) + bS / 2;
          xx[3] = offSetX + ((ii - jj) * bS / 2);
          yy[0] = offSetY + ((jj + ii) * bSH / 2);
          yy[1] = offSetY + ((jj + ii) * bSH / 2) + bSH / 2;
          yy[2] = offSetY + ((jj + ii) * bSH / 2) + bSH;
          yy[3] = offSetY + ((jj + ii) * bSH / 2) + bSH / 2;
          setArea(xx, yy, ii, jj);
        }
      }


    if(clickDraw)
      {
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(new Color(0, 255, 255));
        g2.setStroke(new BasicStroke(clickBorder/5));
        g.drawPolygon(poly[click[0]][click[1]]);
      }
    }
  }
}