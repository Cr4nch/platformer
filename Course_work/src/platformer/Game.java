package platformer;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;

import platformer.Entity.Entity;
import platformer.Entity.Player;
import platformer.graphics.sprites.Sprite;
import platformer.graphics.sprites.SpriteSheet;
import platformer.input.KeyInput;
import platformer.input.MouseInput;
import platformer.net.Client;

public class Game  extends Canvas implements Runnable{
  public static final int WIDTH = 270;
  public static final int HEIGHT = 192;
  public static final int SCALE = 3;
  public static final String  TITLE = "GAME";
  public static String username="";
  
  public static GameGUI guiParent;
  
  public static  int coins = 0;
  public static int lives = 3;
  public static int deathScreenTime = 0;
  public static int level = 0;
  
  public static int spawnX=0;
  public static int spawnY=0;
  
  public static boolean showDeathScreen = true;
  public static boolean gameOver = false;
  public static boolean playing = false;
  
  public static Handler handler;
  public static SpriteSheet sheet;
  public static Camera cam;
//  public static Launcher launcher;
  public static MouseInput mouse;
  
  public static Sprite grass;
  public static Sprite magma;
  public static Sprite fireball;
  public static Sprite flower;
  public static Sprite powerUp;
  public static Sprite usedPowerUp;
  public static Sprite star;
  public static Sprite mushroom;
  public static Sprite liveMushroom;
  public static Sprite[] player;
  public static Sprite[] goomba;
  public static Sprite[] flag;
  public static Sprite[] particle;
  public static Sprite coin[];
  
  public static Song jump;
  public static Song stomp;
  public static Song complete;
  public static Song loselife;
  public static Song maintheme;
  
  public static Client client;
  public static boolean multi=false;
  public static boolean localServer=false;
  
  private Thread  thread;
  private boolean running = false;
  public  static BufferedImage image[];
  private BufferedImage background;
  public static BufferedImage menuBackground;
  
  private void init(){
    handler = new Handler();
    
    jump = new Song("/platformer/res/jump.wav");
    stomp = new Song("stomp.wav");
    complete = new Song("complete.wav");
    loselife = new Song("die.wav");
    maintheme = new Song("main.wav");
    
    try{
    image = new  BufferedImage[5];
    menuBackground = ImageIO.read(getClass().getResource("/platformer/res/menu_background.png"));
    background = ImageIO.read(getClass().getResource("/platformer/res/background.png"));
    image[0] = ImageIO.read(getClass().getResource("/platformer/res/velel01_final.png"));
    image[1] = ImageIO.read(getClass().getResource("/platformer/res/velel02_final.png"));
    image[2] = ImageIO.read(getClass().getResource("/platformer/res/velel03_final.png"));
    image[3] = ImageIO.read(getClass().getResource("/platformer/res/velel04_final.png"));
    image[4] = ImageIO.read(getClass().getResource("/platformer/res/velel05_final.png"));
    }catch(IOException e){e.printStackTrace();}
    
    cam = new Camera();
  //  launcher = new Launcher();
    mouse = new MouseInput();
    
    sheet = new SpriteSheet("/platformer/res/developed_spritesheet.png");
    magma = new Sprite(sheet,8,1);
    fireball = new Sprite(sheet,4,5);
    flower = new Sprite(sheet,8,1);
    grass = new Sprite(sheet,8,3);
    star = new Sprite(sheet,3,5);
    powerUp = new Sprite(sheet,5,5);
    usedPowerUp = new Sprite(sheet,6,5);
    mushroom = new Sprite(sheet,7,5);
    liveMushroom = new Sprite(sheet,8,5);
    player = new  Sprite[7];
    goomba = new Sprite[7];
    flag = new Sprite[3];
    particle = new Sprite[6];
    coin = new Sprite[8];
    for(int i=0;i<coin.length;i++)
      coin[i]= new Sprite(sheet,i+1,4);
    for(int i=0;i<player.length;i++)
      player[i]= new Sprite(sheet,i+1,1);
    for(int i =0;i<goomba.length;i++)
      goomba[i]= new Sprite(sheet,i+1,3);
    for(int i=0;i<flag.length;i++)
      flag[i]= new Sprite(sheet,i+1,5);
    flag[2] =new Sprite(sheet,7,2);
    for(int i=0;i<particle.length;i++){
      particle[i] = new Sprite(sheet,i+1,2);
    }
    
    addKeyListener(new KeyInput());
    addMouseListener(mouse);
    addMouseMotionListener(mouse);
  }
  
  public synchronized  void start(){
    if(running)return;
    running=true;
    thread = new Thread(this,"Thread");
    thread.start();
  }
  
  public synchronized  void stop(){
    if(!running)return;
    running=false;
    try{
      thread.join();
    }catch(InterruptedException e){
      e.printStackTrace();
    }
  }
  
  @Override
  public void run(){
    init();
    requestFocus();
    long lastTime = System.nanoTime();
    long timer = System.currentTimeMillis();
    double delta = 0;
    double ns = 1000000000.0/60.0;
    int frames = 0;
    int ticks = 0;
    while(running){
      try{
      Thread.sleep(20);
    }catch(InterruptedException e){
      e.printStackTrace();
    }
      long  now =  System.nanoTime();
      delta+=(now-lastTime)/ns;
      lastTime=now;
      while(delta>=1){
        tick();
        ticks++;
        delta--;
      }
      render();
      frames++;
      if(System.currentTimeMillis()-timer>1000){
        timer+=1000;
        System.out.println(frames+"/"+ticks);
        frames=0;
        ticks=0;
        
      }
    }
    stop();
  }
  
  public void render(){
    BufferStrategy bs =  getBufferStrategy();
    if(bs==null){
      createBufferStrategy(3);
      return;
    }
    Graphics g = bs.getDrawGraphics();
   /* g.setColor(new Color(0,0,0));
    g.fillRect(0,0,getWidth(),getHeight());*/
    g.drawImage(background,0,0,getWidth(),getHeight(),null);
  
    if(!showDeathScreen){
    }else{
      if(!gameOver){
      g.drawImage(player[1].getBufferedImage(),HEIGHT*SCALE/2,WIDTH*SCALE/4,100,100,null);
      g.setColor(Color.WHITE);
      g.setFont(new Font("Courier",Font.BOLD,50));
      g.drawString("x"+lives,HEIGHT*SCALE/2+50,WIDTH*SCALE/4+80);
      }else{
         g.setColor(new Color(0,0,0));
         g.fillRect(0,0,getWidth(),getHeight());
         g.setColor(Color.RED);
         g.setFont(new Font("Courier",Font.BOLD,50));
         g.drawString("Game Over",HEIGHT*SCALE/2,WIDTH*SCALE/4+80);
      }
    }
    
    if(playing)g.translate(cam.getX(),cam.getY());
    if(!showDeathScreen&&playing) handler.render(g);
   // else if(!playing)launcher.render(g);
   
    g.dispose();
    bs.show();
  }
  
  public void tick(){
  	//System.out.println("USERNAME: "+username);
    if(playing)handler.tick();
    for(Entity e:handler.entity){
      if(e.getId()==Id.player )
      //  if(!e.goingDownPipe)
      	if(e instanceof Player){
      		//System.out.println("Player: "+((Player)e).name);
      		if(((Player)(e)).name.compareTo(username)==0)
      			cam.tick(e);
      	
      	}
    }
    if(showDeathScreen&&playing)deathScreenTime++;
    if(deathScreenTime>=40){
      System.out.println("TICK");
      if(!gameOver){
      showDeathScreen = false;
      deathScreenTime = 0;
      if(!multi){
      handler.clearLevel();
      handler.createLevel(image[level]);
      }
      //maintheme.play();
      }else if(gameOver){ 
      	if(!multi){
        Game.handler.clearLevel();
        Game.handler.createLevel(Game.image[0]);
      	}
        Game.coins=0;
        Game.lives=3;
        System.out.println("GAME OVER");
        showDeathScreen = false;
        deathScreenTime = 0;
   //     launcher.menuType=1;
        playing = false;
        gameOver = false;
      }
    }
  }
  
  public Game(GameGUI guiParent){
    this.guiParent=guiParent;
    Dimension size = new Dimension(WIDTH*SCALE,HEIGHT*SCALE);
    //setPrefferedSize(size);
    setMaximumSize(size);
    setMinimumSize(size);
    
  }
  
  public static int getFrameWidth(){
    return WIDTH*SCALE;
  }
  
  public static int getFrameHeight(){
    return HEIGHT*SCALE;
  }
  
  public static Rectangle getVisibleArea() {
  	

  	
    for(int i=0;i<handler.entity.size();i++){
      Entity e = handler.entity.get(i);
      if(e.getId()==Id.player){


      	
      	if(((Player)e).name.compareTo(username)==0){
      		
      		
      		return new Rectangle((e.getX()-(getFrameWidth()/2)),(e.getY()-(getFrameHeight()/2)),getFrameWidth()+10,getFrameHeight()+10);
      	}
      }
    }
    return null;
  }
  
  public static void switchLevel(){
    level++;
    if(level>=image.length)level=0;
      handler.clearLevel();
      handler.createLevel(image[level]);
    //Game.maintheme.stop();
    Game.complete.play();
  }
  
/*  public static void main(String[] args){
    Game game = new Game();
    JFrame frame = new JFrame(TITLE);
    frame.add(game);
    frame.pack();
    frame.setSize(WIDTH*SCALE,HEIGHT*SCALE);
    frame.setResizable(false);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    game.start();
  }*/
  

  
}
