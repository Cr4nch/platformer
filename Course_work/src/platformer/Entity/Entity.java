package platformer.Entity;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

import platformer.Game;
import platformer.Handler;
import platformer.Id;
import platformer.states.BossState;
import platformer.states.KoopasState;

public abstract class Entity{
  protected int x,y;
  protected int width,height;
  protected boolean soild;//delete it
  protected int velX,velY;
  

  //protected int type;
  public int position;//for net
  
  public int facing = 2;
//  public BossState bossState;
//  public KoopasState koopasState;
  
  public boolean jumping = false;
  public boolean falling = true;
  public boolean goingDownPipe = false;
  
  
  public double gravity=0.0;
  
  public Id id;
  public Handler handler;
  
  public Entity(int x,int y,int width,int height,boolean soild,Id id,Handler handler){
    this.x=x;
    this.y=y;
    this.width=width;
    this.height=height;
    this.soild=soild;
    this.handler=handler;
    this.id=id;
  }
  
  public abstract void render(Graphics g);
  public  abstract void tick();
  
  public void die(){
    if(getId()==Id.player){
    	
    if(!Game.multi){
    handler.removeEntity(this);
    Game.loselife.play();
    Game.lives--;
    Game.showDeathScreen =true;
    if(Game.lives<=0){Game.gameOver = true;addRecord();}
    }
    else{
    		if(Game.username.compareTo(((Player)this).name)==0)Game.client.sendRespawn();
    }
    
    
    }else
      handler.removeEntity(this);
  }
  
  public void addRecord(){
    String[] leaders =new String[5];
    int i=-1,j=0;
    String name="mario/leadboard.txt";
    File file = new File(name);
    
    try{
     if(file.exists()){
        BufferedReader br = new BufferedReader(new FileReader(file.getAbsoluteFile()));
        String line=null;
        while((line=br.readLine())!=null){
          i++;
          System.out.println(line);
          
          leaders[i]=line;
          if(i>=4)break;
        }
        br.close();
      }else{
          file.createNewFile();
          
      }
      while(j<=i){
        String[] parts = leaders[j].split(" ");
        int val=Integer.parseInt(parts[1]);
        if(val<Game.coins)break;  
        j++;
      }
      if(j==5)return;
      for(int k=i+1;k>=j;k--){
        if(k<5&&k>0){
          leaders[k]=leaders[k-1];
        }
      }
      
      leaders[j]=Game.username+" "+Game.coins;
      if(i<4)i++;
      
      PrintWriter  out = new PrintWriter(file.getAbsoluteFile());
      for(j=0;j<=i;j++)
        out.print(leaders[j]+"\n");
     
      out.close();
      
    }catch(Exception e){
      e.printStackTrace();
    }
    
  }
  
  public Id getId(){
    return id;
  }
  
  public int getVelX(){
    return velX;
  }
  
  public int getVelY(){
    return velY;
  }
  
  public int getX(){
    return x;
  }
  
  public int getY(){
    return y;
  }
  
  public int getWidth(){
    return width;
  }
  
  public int getHeight(){
    return height;
  }
  
  public boolean getSoild(){
    return soild;
  }
  
  public void setId(Id id){
    this.id=id;
  }
  
  public void setVelX(int velX){
    this.velX=velX;
  }
  
  public void setVelY(int velY){
    this.velY=velY;
  }
  
  public void setX(int x){
    this.x=x;
  }
  
  public void setY(int y){
    this.y=y;
  }
  
  public void setWidth(int width){
    this.width=width;
  }
  
  public void setHeight(int height){
    this.height=height;
  }
  
  public void setSoild(boolean soild){
    this.soild=soild;
  }
  
  public Rectangle getBounds(){
    return new Rectangle(x,y,width,height);
  }
  
  public Rectangle getBoundsTop(){
    return new Rectangle(x+10,y,width-20,5);
  }
  
  public Rectangle getBoundsBottom(){
    return new Rectangle(x+10,y+height-5,width-20,5);
  }
  
  public Rectangle getBoundsLeft(){
    return new Rectangle(x,y+10,5,height-20);
  }
  
  public Rectangle getBoundsRight(){
    return new Rectangle(x+width-5,y+10,5,height-20);
  }
  
  public void setPosition(int position){
    this.position=position;
  }
  
  public void setFacing(int facing){
    this.facing=facing;
  }
  
  public byte getFacing(){
    return (byte)facing;
  }
  
}
