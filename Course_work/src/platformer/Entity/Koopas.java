package platformer.Entity;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import platformer.Handler;
import platformer.Id;
import platformer.Tile.Tile;
import platformer.states.KoopasState;

public class Koopas extends Entity{
  
  public  Random rand;
  
  private int shellCount;
  public KoopasState koopasState;
  
  public Koopas(int x,int y,int width,int height,boolean soild,Id id,Handler handler){
    super(x,y,width,height,soild,id,handler);
    
    rand=new Random();
    
 //   int dir=rand.nextInt(2);
      setVelX(1);

     
     koopasState = KoopasState.WALKING;
     
  }
  
  @Override
  public void render(Graphics g){
    if(koopasState==KoopasState.WALKING||koopasState==KoopasState.SPINNING)g.setColor(Color.GREEN);
    else g.setColor(Color.WHITE);
    g.fillRect(x,y,width,height);
  }
  
  @Override
  public void tick(){
    x+=velX;
    y+=velY;
    
    if(koopasState==KoopasState.SHELL){
      
      setVelX(0);
      shellCount++;
      if(shellCount>=300){
        shellCount=0;
        koopasState=KoopasState.WALKING;
      } 

    }
    
    if(koopasState==KoopasState.WALKING||koopasState==KoopasState.SPINNING){
      if(velX==0){
        int dir=rand.nextInt(2);
        if(dir==1){
          setVelX(2);
        }else {
          setVelX(-2);
        }
      }
      shellCount=0;
    
    }
    
     for(Tile t:handler.tile){
      if(!t.soild)break;
      if(t.getId()==Id.wall){
        if(getBoundsBottom().intersects(t.getBounds())){
          setVelY(0);
          if(falling)falling=false;
        }else{
          if(!falling){
            gravity=0.0;
            falling=true;
          }
        }
        if(getBoundsLeft().intersects(t.getBounds())){
          if(koopasState==KoopasState.SPINNING)setVelX(10);
          else setVelX(5);
          facing=1;
        }
        if(getBoundsRight().intersects(t.getBounds())){
          if(koopasState==KoopasState.SPINNING)setVelX(-10);
          else setVelX(-5);
          facing=0;
        }
      }
    }
    if(falling){
      gravity+=0.1;
      setVelY((int)gravity);
    }

  }
  
  public void setState(byte state){
    if(state==0)koopasState=KoopasState.WALKING;
    else if(state==1)koopasState=KoopasState.SHELL;
    else if(state==2)koopasState=KoopasState.SPINNING;
  }
  
  public byte getState(){
    if(koopasState==KoopasState.WALKING)return 0;
    if(koopasState==KoopasState.SHELL)return 1;
    if(koopasState==KoopasState.SPINNING)return 2;
    return -1;
  }
  
  public void setShellCount(int count){
    this.shellCount=count;
  }
  
  public byte getShellCount(){
    return (byte)shellCount;
  }
    
}
