package platformer.Entity;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import platformer.Game;
import platformer.Handler;
import platformer.Id;
import platformer.Tile.Tile;
import platformer.states.BossState;

public class TowerBoss extends Entity{
  
  public int jumpTime = 0;
  
  public boolean addJumpTime = false;
  private Random random;
  private int frame;
  private int frameDelay=0;
  public BossState bossState;
  public int hp;
  public int phaseTime;
 
  public boolean attackable = false;
    
  public TowerBoss(int x,int y,int width,int height,boolean soild,Id id,Handler handler,int hp){
    super(x,y,width,height,soild,id,handler);
    this.hp = hp;
    bossState = BossState.IDLE;
    frameDelay=0;
    random = new Random();
  }
  
  @Override
  public void render(Graphics g){
  /*  if(bossState==BossState.IDLE||bossState==BossState.SPINNING)g.setColor(Color.YELLOW);
    else if(bossState==BossState.RECOVERING)g.setColor(Color.RED);
    else g.setColor(Color.ORANGE);
    
    g.fillRect(x,y,width,height);*/
  	
  	if(velX<0)facing=0;
  	else if(velX>0)facing=1;
  	else facing=2;

    if(facing==0)
      g.drawImage(Game.towerBoss[frame].getBufferedImage(),x,y,width,height,null);
    else if(facing==1)
      g.drawImage(Game.towerBoss[frame+2].getBufferedImage(),x,y,width,height,null);
    else g.drawImage(Game.towerBoss[4].getBufferedImage(),x,y,width,height,null);
      
  }
  
  @Override
  public void tick(){
    x+=velX;
    y+=velY;
  //  System.out.println(velX);
    
    if(velX!=0){
    frameDelay++;
    if(frameDelay>=10){
      frame++;
      if(frame>=2)
        frame=0;
      frameDelay=0;
    }
    }
    
    if(hp<=0)die();
    
    phaseTime++;
    
    if(phaseTime>=180&&bossState==BossState.IDLE)chooseState();
    if(phaseTime>=600&&bossState!=BossState.SPINNING)chooseState();
    
        
    if(bossState==BossState.RECOVERING&&phaseTime>180){
      bossState = BossState.SPINNING;
      phaseTime = 0;
    }
    
    if(bossState==BossState.SPINNING&&phaseTime>=360){
      phaseTime = 0;
      bossState = BossState.IDLE;
    }
    
    if(bossState==BossState.IDLE||bossState==BossState.RECOVERING){
      setVelX(0);
      setVelY(0);
    }
    
    if(bossState==BossState.JUMPING||bossState==BossState.RUNNING)attackable=true;
    else attackable = false;
    
    if(bossState!=BossState.JUMPING){
      addJumpTime=false;
      jumpTime=0;
    }
    
    if(addJumpTime){
      jumpTime++;
      if(jumpTime>=30){
        addJumpTime=false;
        jumpTime=0;
      }
    
      if(!jumping&&!falling){
        jumping=true;
        gravity=8.0;
      }
    }
    
    for(int i=0;i<handler.tile.size();i++){
      Tile t = handler.tile.get(i);
      if(!t.soild)continue; 
      if(getBoundsTop().intersects(t.getBounds())){
          setVelY(0);
          if(jumping){
            jumping=false;
            gravity=0.0;
            falling=true;
          }
        }
      if(getBoundsBottom().intersects(t.getBounds())){
          setVelY(0);
          if(falling){
            falling=false;
            addJumpTime=true;
          }
        }  
      if(getBoundsLeft().intersects(t.getBounds())){
          setVelX(0);
          if(bossState == BossState.RUNNING)setVelX(4);
          x = t.getX()+t.width;
          
      }
        if(getBoundsRight().intersects(t.getBounds())){
          setVelX(0);
          if(bossState == BossState.RUNNING)setVelX(-4);
          x = t.getX()-t.width;
        }  
    }
    
    for(int i=0;i<handler.entity.size();i++){
      Entity e = handler.entity.get(i);
      if(e.getId()==Id.player){
        if(bossState==BossState.JUMPING){
          if(e.getX()<getX())setVelX(-3);
          else setVelX(3);
        }else if(bossState==BossState.RUNNING){
          
        }else if(bossState == BossState.SPINNING){
          if(e.getX()<getX())setVelX(-3);
          else setVelX(3);
        }
      }
    }
    
    if(jumping){
      gravity-=0.15;
      setVelY((int)-gravity);
      if(gravity<=0.6){
        jumping=false;
        falling=true;
      }
    }
    if(falling){
      gravity+=0.15;
      setVelY((int)gravity);
    }
      
  }
  
  public void chooseState(){
    int nextPhase = random.nextInt(2);
    if(nextPhase == 0){
      bossState = BossState.RUNNING;
      int dir = random.nextInt(2);
      if(dir==0)setVelX(-4);
      else setVelX(4);
    }else {
      bossState = BossState.JUMPING;
      jumping = true;
      gravity = 8.0;
    }
    
    phaseTime=0;
    
  }
  
  public void setState(byte state){
    if(state==0)bossState=BossState.IDLE;
    else if(state==1)bossState=BossState.SPINNING;
    else if(state==2)bossState=BossState.JUMPING;
    else if(state==3)bossState=BossState.RECOVERING;
    else if(state==4)bossState=BossState.RUNNING;
  }
  
  public byte getState(){
    if(bossState==BossState.IDLE)return 0;
    if(bossState==BossState.SPINNING)return 1;
    if(bossState==BossState.JUMPING)return 2;
    if(bossState==BossState.RECOVERING)return 3;
    if(bossState==BossState.RUNNING)return 4;
    return -1;
  }
  
  public void setHp(byte hp){
    this.hp=hp;
  }
  
  public byte getHp(){
    return (byte)hp;
  }
  
}
