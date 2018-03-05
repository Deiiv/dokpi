/*
 *
 *
 Colours:
 blue - 0x64A0C8
 orange (in logo) - 0xFAB00F
 orange (for background/glow) - 0xe8a81e








 1. aroundP add checks for other directions
 2. add diagonal movement for other directions
 3. add glow over highlighted available blocks after aroundP












 add an orange "glow" around map
 minimap will be on right/left, and item inventory+info will be other side

 in render() add flicker, use fade in http://zetcode.com/gfx/java2d/transparency/

 add bridge

 */
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.lang.*;
import java.awt.Graphics2D;
import java.net.URL;
import java.util.*;

public class Dokpi extends Canvas implements Runnable, KeyListener, MouseListener{

  //private static final int w = 1200, h = 900;
  private int w, h;
  private int bS, bSH, mS;
  private int dC; //diagonal cost
  private int currentLowestF, currentLowestCell;

  private JTextField userInput = new JTextField();
  private JTextArea chatWindow = new JTextArea();

  private int pathX, pathY;
  private Runnable pathThread;
  static boolean pathRun = false;

  String soundClick = "sfx/click.wav";

  private static Date date;
  private static Calendar calendar; // creates a new calendar instance

  private Cell[] aroundP = new Cell[8];

  private boolean breakMove = false;

  //later would get their current pos through db
  private int playerPosX = 0;
  private int playerPosY = 0;

  //path for character movement
  private LinkedList<Cell> path = new LinkedList<>();

  /*public int[][] map01grid = new int[][]
    {
      {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 11, 00, 00, 11, 12},
      {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 12, 00, 00, 00},
      {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 10, 10, 11, 00, 00},
      {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 10, 10, 13, 12, 00},
      {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 11, 00, 10, 00},
      {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 10, 00, 00, 00, 00},
      {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 11, 00, 10},
      {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 20, 00, 00, 00, 00, 00, 00, 10, 00, 00},
      {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
      {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
      {00, 00, 00, 00, 00, 00, 00, 00, 20, 20, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
      {00, 00, 00, 00, 00, 00, 00, 00, 20, 20, 20, 00, 00, 00, 00, 00, 00, 00, 00, 00},
      {20, 00, 00, 00, 00, 20, 20, 20, 20, 20, 20, 20, 00, 00, 00, 00, 00, 00, 00, 00},
      {20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 00, 00, 00, 20},
      {20, 20, 20, 20, 20, 00, 00, 00, 00, 00, 00, 00, 20, 20, 20, 20, 20, 20, 20, 20},
      {00, 00, 20, 20, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 20, 20, 20, 20, 20},
      {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 20},
      {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
      {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
      {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
    };*/

/*
BIG NOTE ON MAPS

map pathing has to have an answer
if it's too hard, shit will take too long and that's no bueno

 */
  //----------map 1
  private int[][] map01grid = new int[][]
      {
          {20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20},
          {20, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 11, 00, 00, 11, 12, 00, 20},
          {20, 00, 01, 01, 01, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 12, 00, 00, 00, 00, 20},
          {20, 00, 01, 01, 01, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 10, 10, 11, 00, 00, 00, 20},
          {20, 00, 01, 01, 01, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 10, 10, 13, 12, 00, 00, 20},
          {20, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 11, 00, 10, 00, 00, 20},
          {20, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 10, 00, 00, 00, 00, 00, 20},
          {20, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 11, 00, 10, 00, 20},
          {20, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 20, 00, 00, 00, 00, 00, 00, 10, 00, 00, 00, 20},
          {20, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 20},
          {20, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 20},
          {20, 00, 00, 00, 00, 00, 00, 00, 00, 20, 21, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 20},
          {20, 00, 00, 00, 00, 00, 00, 00, 00, 20, 20, 21, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 20},
          {20, 20, 00, 00, 00, 00, 00, 20, 20, 20, 20, 20, 21, 00, 00, 00, 00, 00, 00, 00, 00, 00, 20},
          {20, 20, 20, 20, 20, 21, 00, 22, 20, 20, 20, 20, 20, 00, 20, 20, 21, 00, 00, 00, 00, 00, 20},
          {20, 20, 20, 20, 20, 20, 00, 00, 00, 00, 00, 00, 00, 00, 22, 20, 20, 20, 20, 20, 21, 00, 20},
          {20, 00, 00, 22, 20, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 20, 20, 20, 20, 20, 00, 20},
          {20, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 20, 00, 20},
          {20, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 20},
          {20, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 20},
          {20, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 20},
          {20, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 20},
          {20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20},

          /*{00, 00, 00, 00, 00, 11, 00, 00, 11, 12, 00},
          {00, 00, 00, 00, 00, 00, 12, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 10, 10, 11, 00, 00, 00},
          {00, 00, 00, 00, 00, 10, 10, 13, 12, 00, 00},
          {00, 00, 00, 00, 00, 00, 11, 00, 10, 00, 00},
          {00, 00, 00, 00, 00, 10, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 11, 00, 10, 00},
          {20, 00, 00, 00, 00, 00, 00, 10, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {20, 00, 00, 00, 00, 00, 00, 10, 00, 00, 00},*/
      };
  private int[][] map01objects = new int[][]
      {
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          /*{00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},*/
      };
  private int[][] map01items = new int[][]
      {
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
      };
  private int[][] map01players = new int[][]
      {
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
          {00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
      };

  private Thread thread;
  private boolean running = false;

  private Image title, by, hL;

  private static Window window;

  private static Handler handler;
  private MapHandler maphandler;

  public Dokpi()
  {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    w = (int)screenSize.getWidth();
    h = (int)screenSize.getHeight();
    h=(int)(h/1.45); //temporary for easy reading of printing
    w=(int)(w/1.4);

    handler = new Handler();
    maphandler = new MapHandler(playerPosX, playerPosY, w, h);

    bS = maphandler.getbs();
    bSH = bS/2;
    mS = maphandler.getms();

    dC = (int)Math.sqrt((bSH*bSH)+(bSH/2*bSH/2));

   /* try {
      audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
    } catch (UnsupportedAudioFileException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      clip = AudioSystem.getClip();
    } catch (LineUnavailableException e) {
      e.printStackTrace();
    }
    try {
      clip.open(audioInputStream);
    } catch (LineUnavailableException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }*/

    ImageIcon i = new ImageIcon("resources/1.png");
    title = i.getImage();
    ImageIcon i2 = new ImageIcon("resources/by.png");
    by = i2.getImage();

    ImageIcon i3 = new ImageIcon("resources/hLight3.gif");
    hL = i3.getImage();

    addKeyListener(this);
    addMouseListener(this);

    window = new Window(w, h, this);

    //maphandler.addMap(new Map(w, h, map01grid, map01objects, map01items, map01players, ID.Map, -1, -1));
    //maphandler.addMap(new Map(w, h, map01grid, map01objects, map01items, map01players, ID.Map, 0, -1));
    //maphandler.addMap(new Map(w, h, map01grid, map01objects, map01items, map01players, ID.Map, 1, -1));
    //maphandler.addMap(new Map(w, h, map01grid, map01objects, map01items, map01players, ID.Map, -1, 0));
    maphandler.addMap(new Map(w, h, map01grid, map01objects, map01items, map01players, ID.Map, 0, 0));
    //maphandler.addMap(new Map(w, h, map01grid, map01objects, map01items, map01players, ID.Map, 1, 0));
    //maphandler.addMap(new Map(w, h, map01grid, map01objects, map01items, map01players, ID.Map, -1, 1));
    //maphandler.addMap(new Map(w, h, map01grid, map01objects, map01items, map01players, ID.Map, 0, 1));
    //maphandler.addMap(new Map(w, h, map01grid, map01objects, map01items, map01players, ID.Map, 1, 1));

    maphandler.mapStart();

    pathThread = new Runnable() {
      public void run() {
        movePlayer(pathX, pathY);
      }
    };

    //handler.addObject(new Player(50, h/2-325, ID.Player));
    handler.addObject(new Player(0, 0, ID.Player, w, h));

    handler.addObject(new InWindow(780, h/2-325, ID.InWindow, w, h));
  }

  public synchronized void start()
  {
    thread = new Thread(this);
    thread.start();
    running = true;
  }

  public synchronized void stop()
  {
    try{
      thread.join();
      running = false;
    }catch(Exception e){
      e.printStackTrace();
    }
  }

  public boolean login(){

    return true;
  }

  public void playSound(String soundName)
  {
    try
    {
      AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile( ));
      Clip clip = AudioSystem.getClip( );
      clip.open(audioInputStream);
      clip.start();
    }
    catch(Exception ex)
    {
      System.out.println("Error with playing sound.");
      ex.printStackTrace( );
    }
  }

  public static int getHour()
  {
    date = new Date();
    calendar = GregorianCalendar.getInstance();
    calendar.setTime(date);   // assigns calendar to given date
    return calendar.get(Calendar.HOUR_OF_DAY); //gets hour in 24h format
  }

  public static int getMinute()
  {
    date = new Date();
    calendar = GregorianCalendar.getInstance();
    calendar.setTime(date);   // assigns calendar to given date
    return calendar.get(Calendar.MINUTE); //gets current minutes
  }

  public void run()
  {
    //running = false;
    int frames = 0, ticks = 0;
    long lastTime = System.nanoTime();
    double unprocessed = 0;
    double nsPerSec = 1000000000/60.0; //60 corresponds to game speed
    long timer = System.currentTimeMillis();
//    System.out.println(timer);
    while(running)
    {
      long now = System.nanoTime();
      unprocessed += (now - lastTime)/nsPerSec;
      lastTime = now;

      if(unprocessed >= 1)
      {
        ticks++;
        tick();
        frames++;
        render();
        unprocessed--;
      }
      try {
        Thread.sleep(3); // old: 3
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      if(System.currentTimeMillis() - timer > 1000)
      {
//        System.out.println(System.currentTimeMillis() - timer);
//        System.out.println(timer);
//        System.out.println("-----------------");
//        System.out.println("Ticks: " + ticks);
//        System.out.println("FPS: " + frames);
        frames = 0;
        ticks = 0;
        timer += 1000;
      }
    }
  }

  private void tick()
  {
    handler.tick();
    maphandler.tick();
  }

  private void render()
  {
    BufferStrategy bs = this.getBufferStrategy();

    if(bs == null)
    {
      this.createBufferStrategy(3);
      return;
    }

    Graphics g = bs.getDrawGraphics();

    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    //BACKGROUND
    g.setColor(new Color(0xd1e0e0));
    g.fillRect(0, 0, w, h);

    //MAP BORDER
  /*  g.setColor(Color.black);
    g2.setStroke(new BasicStroke(6));
    RoundRectangle2D b = new RoundRectangle2D.Float(47, h/2-328, 686, 686, 10, 10);
    g2.draw(b);
    g2.setStroke(new BasicStroke(1));
*/
    //TITLE/CREDIT
    //TO BE ADDED TO LOGIN SCREEN INSTEAD
   // g.drawImage(title, (w/2) - 150, 20, null);
  //  g.drawImage(by, (w/2) - 84, 825, null);

    maphandler.render(g);
    handler.render(g);

    g.dispose();
    bs.show();
  }

  public static void setFocus(boolean focus)
  {
    handler.setFocus(focus);
    window.msgFocus = focus;
  }

  public static void main(String args[])
  {
    new Dokpi();
  }

//------------------------KEY LISTENER------------------------
  /*
   W = 87
   A = 65
   S = 83
   D = 68
   */
  public void keyPressed(KeyEvent e)
  {
    int key = e.getKeyCode();

    GameObject temp = handler.object.get(0);

    if (key == 87)
    {
      //makes sure you're altering the correct object
      if(temp.getID() == ID.Player)
      {
       // around = aroundP(temp.getX(), temp.getY());
      }
    }
  }

  public void keyReleased(KeyEvent e)
  {

  }
  public void keyTyped(KeyEvent e)
  {

  }

  /*
  How it works:

  movemap(x, y) receives generic movement direction using -1,0,1 as coordinates, corresponding to this diagram:

  (1, 0)  (1, 1)  (0, 1)

  (1, -1) <player>  (-1, 1)

  (0, -1)  (-1, -1)  (-1, 0)

  with player being on a tile AS SEEN IN GAME
  coordinates based off unedited view

  Points passed to movemap(x, y) are then processed into movement pattern in relation to the isometric drawing method (using 0, 1, 2, 4 for all different lengths of movement)
  */
  public void moveMap(int x, int y) {
    if(x == 0 && y == -1)
    {
      x = 2;
      y = -1;
    }
    else if(x == 1 && y == -1)
    {
      x = 4;
      y = 0;
    }
    else if(x == 1 && y == 0)
    {
      x = 2;
      y = 1;
    }
    else if(x == 1 && y == 1)
    {
      x = 0;
      y = 2;
    }
    else if(x == 0 && y == 1)
    {
      x = -2;
      y = 1;
    }
    else if(x == -1 && y == 1)
    {
      x = -4;
      y = 0;
    }
    else if(x == -1 && y == 0)
    {
      x = -2;
      y = -1;
    }
    else if(x == -1 && y == -1)
    {
      x = 0;
      y = -2;
    }

    for (int i = 0; i < bSH/2; i++) {
      maphandler.moveMap(x, y);
      if (x == 4 || x == -4) {
        try {
          thread.sleep(9);
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else {
        try {
          thread.sleep(6);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }
//------------------------CHECKS WHAT THE GRID HAS AT CLICK POINT and MOVES PLAYER------------------------
//returns int that is the block in the said location
//returns -1 if outofbounds

  public void movePlayer(int x, int y)
  {
    int playerX, playerY;
    int gridValue = 0;

    //GameObject player = handler.object.get(0);

    playerX = w/2;//player.getX()-24;
    playerY = h/2;//player.getY()-105;

    gridValue = maphandler.getGrid(x, y);

    if (gridValue >= 0 && gridValue <= 9) //walkable (later add option to move to a spot to commit action (maybe click, select action on object, and then move to the available spot next to the target
    {
      int[] click = maphandler.gridPos(x, y);
      maphandler.clickAnim(click[0], click[1], click[2], click[3]);

      Cell keep = null;
      if (path.size() != 0) {
        breakMove = true;
        keep = path.getFirst();
      }
      path = makePath(playerX, playerY, x, y);
      if (keep != null) {
        path.addFirst(keep);
      }
      //breakMove = false;
      while (path.size() != 0) {

        if (breakMove) {
          //System.out.println("breaking");
          breakMove = false;
          break;
        }
        // Cell temp = path.getFirst();
        int xD = path.getFirst().i; //x grid pos
        int yD = path.getFirst().j; //y grid pos
        int xPos = path.getFirst().x;
        int yPos = path.getFirst().y;
        int iD = path.getFirst().gI; //grid section I
        int jD = path.getFirst().gJ; //grid section J
        path.removeFirst();
        //System.out.println("From: (" + xD + ", " + yD + ")");
        if (path.size() != 0) {
            /*
              How it works:

              movemap(x, y) receives generic movement direction using -1,0,1 as coordinates, corresponding to this diagram:

              (1, 0)  (1, 1)  (0, 1)

              (1, -1) <player>  (-1, 1)

              (0, -1)  (-1, -1)  (-1, 0)

              with player being on a tile AS SEEN IN GAME
              coordinates based off unedited view

              Points passed to movemap(x, y) are then processed into movement pattern in relation to the isometric drawing method (using 0, 1, 2, 4 for all different lengths of movement)
            */
          //moving to another section
          if (iD != path.getFirst().gI || jD != path.getFirst().gJ) {
            //1st-side
            if (xD == 0 && yD != 0 && yD != mS - 1) {
              //System.out.println("1st-side");
              //up across
              if (path.getFirst().j < yD) {
                xD = 1;
                yD = 1;
              }
              //down across
              else if (path.getFirst().j > yD) {
                xD = 1;
                yD = -1;
              }
              //straight across
              else if (path.getFirst().j == yD) {
                xD = 1;
                yD = 0;
              }
            }
            //2nd-side
            else if (yD == 0 && xD != 0 && xD != mS - 1) {
              //System.out.println("2nd-side");
              //up across
              if (path.getFirst().i < xD) {
                xD = 1;
                yD = 1;
              }
              //down across
              else if (path.getFirst().i > xD) {
                xD = -1;
                yD = 1;
              }
              //straight across
              else if (path.getFirst().i == xD) {
                xD = 0;
                yD = 1;
              }
            }
            //3rd-side
            else if (xD == mS - 1 && yD != 0 && yD != mS - 1) {
              //System.out.println("3rd-side");
              //up across
              if (path.getFirst().j < yD) {
                xD = -1;
                yD = 1;
              }
              //down across
              else if (path.getFirst().j > yD) {
                xD = -1;
                yD = -1;
              }
              //straight across
              else if (path.getFirst().j == yD) {
                xD = -1;
                yD = 0;
              }
            }
            //4th-side
            else if (yD == mS - 1 && xD != 0 && xD != mS - 1) {
              //System.out.println("4th-side");
              //up across
              if (path.getFirst().i < xD) {
                xD = 1;
                yD = -1;
              }
              //down across
              else if (path.getFirst().i > xD) {
                xD = -1;
                yD = -1;
              }
              //straight across
              else if (path.getFirst().i == xD) {
                xD = 0;
                yD = -1;
              }
            }
            //5th-corner
            else if (xD == 0 && yD == mS - 1) {
              //System.out.println("5th-corner");
              //left
              if (path.getFirst().i == mS - 1 && path.getFirst().j == 0) {
                xD = 1;
                yD = -1;
              }
              //up left
              else if (path.getFirst().i == mS - 1 && path.getFirst().j == mS - 1) {
                xD = 1;
                yD = 0;
              }
              //down left
              else if (path.getFirst().i == 0 && path.getFirst().j == 0) {
                xD = 0;
                yD = -1;
              }
              //down
              else if (path.getFirst().i == 0 && path.getFirst().j != 0) {
                xD = -1;
                yD = -1;
              }
              //up
              else if (path.getFirst().i == mS - 1 && path.getFirst().j != mS - 1) {
                xD = 1;
                yD = 1;
              }
            }
            //6th-corner
            else if (xD == 0 && yD == 0) {
              //System.out.println("6th-corner");
              //up
              if (path.getFirst().i == mS - 1 && path.getFirst().j == mS - 1) {
                xD = 1;
                yD = 1;
              }
              //up left
              else if (path.getFirst().i == mS - 1 && path.getFirst().j == 0) {
                xD = 1;
                yD = 0;
              }
              //up right
              else if (path.getFirst().i == 0 && path.getFirst().j == mS - 1) {
                xD = 0;
                yD = 1;
              }
              //right
              else if (path.getFirst().i != 0 && path.getFirst().j == mS - 1) {
                xD = -1;
                yD = 1;
              }
              //left
              else if (path.getFirst().i == mS - 1 && path.getFirst().j != 0) {
                xD = 1;
                yD = -1;
              }
            }
            //7th-corner
            else if (xD == mS - 1 && yD == 0) {
              //System.out.println("7th-corner");
              //right
              if (path.getFirst().i == 0 && path.getFirst().j == mS - 1) {
                xD = -1;
                yD = 1;
              }
              //up right
              else if (path.getFirst().i == mS - 1 && path.getFirst().j == mS - 1) {
                xD = 0;
                yD = 1;
              }
              //down right
              else if (path.getFirst().i == 0 && path.getFirst().j == 0) {
                xD = -1;
                yD = 0;
              }
              //down
              else if (path.getFirst().i != 0 && path.getFirst().j == 0) {
                xD = -1;
                yD = -1;
              }
              //up
              else if (path.getFirst().i != mS - 1 && path.getFirst().j == mS - 1) {
                xD = 1;
                yD = 1;
              }
            }
            //8th-corner
            else if (xD == mS - 1 && yD == mS - 1) {
              //System.out.println("8th-corner");
              //down
              if (path.getFirst().i == 0 && path.getFirst().j == 0) {
                xD = -1;
                yD = -1;
              }
              //down left
              else if (path.getFirst().i == mS - 1 && path.getFirst().j == 0) {
                xD = 0;
                yD = -1;
              }
              //down right
              else if (path.getFirst().i == 0 && path.getFirst().j == mS - 1) {
                xD = -1;
                yD = 0;
              }
              //right
              else if (path.getFirst().i == 0 && path.getFirst().j != mS - 1) {
                xD = -1;
                yD = 1;
              }
              //left
              else if (path.getFirst().i != mS - 1 && path.getFirst().j == 0) {
                xD = 1;
                yD = -1;
              }
            }
          } else {
            xD -= path.getFirst().i;
            yD -= path.getFirst().j;
          }
          //System.out.println("Using: (" + xD + ", " + yD + ")");
          moveMap(xD, yD);
        }
      }
      //maphandler.setClickOff();
    }
    else {
      //System.out.println("...but can't walk there.");
      maphandler.setClickOff();
    }
  }

  static class Cell{
    //h cost is the estimated distance to goal, using manhattan method
    //vertical distance + horizontal distance
    //absolute because of clicks in other directions
    //g cost will be parent's g cost + nC (the normal cost of moving 1 cell UNLESS
    //its diagonal, in which case it would be more (based on pythagorean theorem shenanigans)
    //and we use dC
     int hcost = 0; //Heuristic cost
     int gcost = 0;
     int fcost = 0; //G+H
     int i, j, gI, gJ, val, x, y;

     Cell parent;

     Cell(int i, int j, int gI, int gJ, int value, int x, int y){
       this.i = i;
       this.j = j;
       this.gI = gI;
       this.gJ = gJ;
       this.val = value;
       this.x = x;
       this.y = y;
     }

     @Override
     public String toString(){
       return "["+this.i+", "+this.j+"]";
     }
  }

//------------------------GIVES BACK PATH------------------------

  /*
  Cancelling path sometimes makes player move back a cell first, before moving onto new path
  ¯\_(ツ)_/¯
   */
  public LinkedList<Cell> makePath(int xP, int yP, int x, int y)
  {
    LinkedList<Cell> open = new LinkedList<>();
    LinkedList<Cell> closed = new LinkedList<>();

    path.clear();

    //pos contains result from gridPos in maphandler, which gives the i,j of the tile, and the i,j of the section it's in
    //also gives the value of the cell, and the original coordinate of the click
    //xP+bSH/2 because player x and y are actually outside of the diamond tile that it's in
    int[] gridPos = maphandler.gridPos(xP, yP);
    Cell first = new Cell(gridPos[0], gridPos[1], gridPos[2], gridPos[3], gridPos[4], gridPos[5], gridPos[6]);
    open.addFirst(first);

    currentLowestF = 0;
    currentLowestCell = 0;

    closed = aStarPath(x, y, open, closed);

   // if(first.i == path.u)
    closed.addFirst(first);

    //get the actual path
    Cell current = closed.getLast();
    while(current != null) {
      path.addFirst(current);
      current = current.parent;
    }

    //System.out.println("From: " + Arrays.toString(maphandler.gridPos(xP, yP)));
    //System.out.println("To: " + Arrays.toString(maphandler.gridPos(x, y)));
    //System.out.println("Path: " + printPath(path));

    return path;
  }

  /*
  Method for generating path to x, y
  A* implementation
  returns linked list of cells
   */
  public LinkedList<Cell> aStarPath(int x, int y, LinkedList<Cell> open, LinkedList<Cell> closed) {
    while (open.size() != 0) {
      //System.out.println(closed.size());

      currentLowestF = 0;
      currentLowestCell = 0;
      for (int i = 0; i < open.size(); i++) {
        Cell temp = open.get(i);
        if (currentLowestF == 0) {
          currentLowestCell = i;
          currentLowestF = temp.fcost;
        } else {
          if (temp.fcost < currentLowestF) {
            currentLowestCell = i;
            currentLowestF = temp.fcost;
          }
        }
      }

      Cell q = open.get(currentLowestCell);
      //remove from open list
      open.remove(currentLowestCell);

      //if the solution is q
      int[] a1 = maphandler.gridPos(x, y);
      if (a1[0] == q.i && a1[1] == q.j && a1[2] == q.gI && a1[3] == q.gJ) {
        if(closed.size()==0)
        {
          closed.add(q);
        }
        return closed;
      }

      //put q on closed list
      //closed.addLast(q);
      //generate successors of q
      /*
      [8][1][2]
      [7][X][3]
      [6][5][4]
       */
      int[] cellVal = maphandler.gridPos(q.x + bS/2, q.y - bSH/2);
      aroundP[0] = new Cell(cellVal[0], cellVal[1], cellVal[2], cellVal[3], cellVal[4], cellVal[5], cellVal[6]);
      cellVal = maphandler.gridPos(q.x + bS, q.y);
      aroundP[1] = new Cell(cellVal[0], cellVal[1], cellVal[2], cellVal[3], cellVal[4], cellVal[5], cellVal[6]);
      cellVal = maphandler.gridPos(q.x + bS/2, q.y + bSH/2);
      aroundP[2] = new Cell(cellVal[0], cellVal[1], cellVal[2], cellVal[3], cellVal[4], cellVal[5], cellVal[6]);
      cellVal = maphandler.gridPos(q.x, q.y + bSH);
      aroundP[3] = new Cell(cellVal[0], cellVal[1], cellVal[2], cellVal[3], cellVal[4], cellVal[5], cellVal[6]);
      cellVal = maphandler.gridPos(q.x - bS/2, q.y + bSH/2);
      aroundP[4] = new Cell(cellVal[0], cellVal[1], cellVal[2], cellVal[3], cellVal[4], cellVal[5], cellVal[6]);
      cellVal = maphandler.gridPos(q.x - bS, q.y);
      aroundP[5] = new Cell(cellVal[0], cellVal[1], cellVal[2], cellVal[3], cellVal[4], cellVal[5], cellVal[6]);
      cellVal = maphandler.gridPos(q.x - bS/2, q.y - bSH/2);
      aroundP[6] = new Cell(cellVal[0], cellVal[1], cellVal[2], cellVal[3], cellVal[4], cellVal[5], cellVal[6]);
      cellVal = maphandler.gridPos(q.x, q.y - bSH);
      aroundP[7] = new Cell(cellVal[0], cellVal[1], cellVal[2], cellVal[3], cellVal[4], cellVal[5], cellVal[6]);

      //set parent of each to q
      aroundP[0].parent = q;
      aroundP[1].parent = q;
      aroundP[2].parent = q;
      aroundP[3].parent = q;
      aroundP[4].parent = q;
      aroundP[5].parent = q;
      aroundP[6].parent = q;
      aroundP[7].parent = q;

      //for each
      for (int i = 0; i < 8; i++) {
        //if valid to walk on
        if (aroundP[i].val >= 0 && aroundP[i].val <= 9) {
          //if its the goal, stop search
          int[] b1 = maphandler.gridPos(x, y);
          if (b1[0] == aroundP[i].i && b1[1] == aroundP[i].j && b1[2] == aroundP[i].gI && b1[3] == aroundP[i].gJ){
            if(closed.size()==0)
            {
              closed.add(aroundP[i]);
            }
            else
            {
              //check if parent is already in
              for(int j = 0; j < closed.size(); j++)
              {
                if(closed.get(j).i != aroundP[i].parent.i && closed.get(j).j != aroundP[i].parent.j && closed.get(j).gI != aroundP[i].gI && closed.get(j).gJ != aroundP[i].gJ) {
                  closed.addLast(aroundP[i].parent);
                }
              }
              closed.addLast(aroundP[i]);
            }
            return closed;
          }
          //g cost
         if((i == 0) || (i == 2) || (i == 4) || (i == 6))
         {
           //diagonal distance
           aroundP[i].gcost = q.gcost + dC;
         }
         else if ((i == 3) || (i == 7)) {
            //vertical distance
           aroundP[i].gcost = q.gcost + bSH;
         }
         else {
            //horizontal distance
           aroundP[i].gcost = q.gcost + bS;
         }

          //h cost
          aroundP[i].hcost = Math.abs(x - aroundP[i].x) + Math.abs(y - aroundP[i].y);
          //f cost
          aroundP[i].fcost = aroundP[i].gcost + aroundP[i].hcost;

          //if its in open list AND has a lower f than this one, skip this one
          if (isInItAndF(open, aroundP[i]) != -1) {
            //do nothing
          }
          //if its in the close list AND has a lower f than this one, skip this one
          else if (isInItAndF(closed, aroundP[i]) != -1) {
            closed.remove(isInItAndF(closed, aroundP[i]));
            open.addLast(aroundP[i]);
          }
          //otherwise, add the node to the open list
          else {
            open.addLast(aroundP[i]);
          }
        }
      }
      //put q on closed list
      closed.addLast(q);
    }
    return closed;
  }

  //returns true if the cell is in the list AND the f is lower than cell's f
  public int isInItAndF(LinkedList<Cell> list, Cell cell) {
    for(int i = 0; i < list.size(); i++) {
      if ((list.get(i).i == cell.i) && (list.get(i).j == cell.j) && (list.get(i).gI == cell.gI && (list.get(i).gJ == cell.gJ))) {
        if(list.get(i).fcost > cell.fcost) {
          return i;
        }
      }
    }
    return -1;
  }

  //for printing path
  public String printPath(LinkedList<Cell> path)
  {
    String string = "";

    int a = 0;
    while(a<path.size())
    {
      int x = path.get(a).i;
      int y = path.get(a).j;
      string += "[ >" + path.get(a).i + ", " + path.get(a).j + "< >" + path.get(a).gI + ", " + path.get(a).gJ + "< ] ";
      a++;
    }

    return string;
  }

//------------------------MOUSE LISTENER------------------------

  public void mousePressed(MouseEvent e)
  {
    if(e.getButton() == e.BUTTON1) {

      //check if it's hitting the UI
      if(handler.uiHas(e.getX(), e.getY()))
      {
        playSound(soundClick);
        if(handler.chatHas(e.getX(), e.getY()))
        {
          setFocus(true);
        }
        //System.out.println("Click is in UI.");
      }
      //else is move command
      else
      {
        setFocus(false);
        pathX = e.getX();
        pathY = e.getY();
        new Thread(pathThread).start();
      }
    }
  }

  public void mouseReleased(MouseEvent e)
  {

  }

  public void mouseEntered(MouseEvent e)
  {

  }

  public void mouseExited(MouseEvent e)
  {

  }

  public void mouseClicked(MouseEvent e)
  {

  }
}
