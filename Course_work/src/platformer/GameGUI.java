package platformer;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import platformer.graphics.CompanyPanel;
import platformer.graphics.ConnectPanel;
import platformer.graphics.LoadingPanel;
import platformer.graphics.MenuPanel;
import platformer.graphics.MultiplayerPanel;
import platformer.graphics.PausePanel;
import platformer.graphics.StartServerPanel;
import platformer.graphics.TopPanel;



public class GameGUI extends JFrame{
  private JPanel cards;
  
  public static final int WIDTH = 270;
  public static final int HEIGHT = 192;
  public static final int SCALE = 3;
  
  public static String MENU ="MENU";
  public static String PAUSE = "PAUSE";
  public static String COMPANY = "COMPANY";
  public static String GAME = "GAME";
  public static String TOP = "TOP";
  public static String LOADING = "LOADING";
  
  public static String CONNECT = "CONNECT";
  public static String MULTIPLAYER = "MULTIPLAYER";
  public static String START_SERVER = "START_SERVER";
  
  GameGUI(){
    setSize(WIDTH*SCALE,HEIGHT*SCALE);
    Game game = new Game(this);
    
    cards = new JPanel(new CardLayout());
    cards.add(new MenuPanel(this),MENU);
    cards.add(new PausePanel(this),PAUSE);
    cards.add(game,GAME);
    cards.add(new CompanyPanel(this),COMPANY);
    cards.add(new TopPanel(this),TOP);
    cards.add(new ConnectPanel(this),CONNECT);
    cards.add(new MultiplayerPanel(this),MULTIPLAYER);
    cards.add(new StartServerPanel(this),START_SERVER);
    cards.add(new LoadingPanel(),LOADING);
    game.start();
    


    add(cards,BorderLayout.CENTER);
   // getContentPane().add(new MenuPanel(this),BorderLayout.CENTER);
    //getContentPane().add(bp,BorderLayout.CENTER);
    pack();
  }
  
  public void changeLayout(String name){
    CardLayout layout = (CardLayout)cards.getLayout();
    layout.show(cards,name);
  }
  
  public static void main(String[] args){
    GameGUI game = new GameGUI();
    game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   game.setLocationRelativeTo(null);
    game.setSize(WIDTH*SCALE,HEIGHT*SCALE);
    game.changeLayout(MENU);
    //game.setResizable(false);
    game.setVisible(true);
    game.changeLayout("LOADING");
    try{
    Thread.sleep(500);
    }catch(InterruptedException e){}
    game.changeLayout("MENU");
  }
  
  
}
