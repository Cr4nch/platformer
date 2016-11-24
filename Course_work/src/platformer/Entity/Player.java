package platformer.Entity;

import java.awt.Graphics;
import java.util.Random;

import platformer.Game;
import platformer.Handler;
import platformer.Id;
import platformer.Tile.Tile;
import platformer.Tile.Trail;
import platformer.states.BossState;
import platformer.states.KoopasState;
import platformer.states.PlayerState;

public class Player extends Entity{
  private int frame = 0;
  private int frameDelay = 0;
  private PlayerState state;
  private int pixelsTravelled = 0;
  private Random rand = new Random();
  private boolean invincible = false;
  private int invincibilityTime = 0;
  private int particleDelay = 0;
  public String name="John";
  
  
  public Player(int x,int y,int width,int height,boolean soild,Id id,Handler handler){
    super(x,y,width,height,soild,id,handler);
    setVelX(0);
    setVelY(0);
    state = PlayerState.SMALL;
  }
  
  @Override
  public void tick(){
    x+=velX;
    y+=velY;
   // falling=false;
    
    if(invincible){
      if(facing==0){
         handler.addTile(new Trail(x,y,width,height,false,Id.trail,handler,
                                   Game.player[0].getBufferedImage()));
      }else if(facing==1){
         handler.addTile(new Trail(x,y,width,height,false,Id.trail,handler,
                                   Game.player[1].getBufferedImage()));
      }
      particleDelay++;
      invincibilityTime++;
      if(particleDelay>2){
        particleDelay=0;
        handler.addEntity(new Particle(x+rand.nextInt(width),y+rand.nextInt(height),width/4,height/4,false,Id.particle,handler));
      }
      if(invincibilityTime>=600){
        invincible=false;
      }
    }
    
    for(int i=0;i<handler.tile.size();i++){
      Tile t = handler.tile.get(i);
      if(!t.soild||goingDownPipe)continue;
      
      if(getBounds().intersects(t.getBounds())){
          if(t.getId()==Id.flag){
            Game.switchLevel();
          }
          if(t.getId()==Id.magma){
            die();
          }
        }
      
    
        if(getBoundsTop().intersects(t.getBounds())){
          setVelY(0);
          if(jumping){
            jumping=false;
            gravity=0.0;
            falling=true;
          }
          if(t.getId() == Id.powerUp){
            t.activated = true;
          }
        }
        if(getBoundsBottom().intersects(t.getBounds())){
          //if(t.getId()!=
          setVelY(0);
          if(falling)falling=false;
        //  y = t.getY()-t.height;
        }else{
          if(!falling&&!jumping){
            gravity=0.0;
            falling=true;
          }
        }
        if(getBoundsLeft().intersects(t.getBounds())){
          setVelX(0);
          x = t.getX()+t.width;
        }
        if(getBoundsRight().intersects(t.getBounds())){
          setVelX(0);
          x = t.getX()-t.width;
        } 
        
    }
    
    for(int i=0;i<handler.entity.size();i++){
      Entity e = handler.entity.get(i);
      if(e.getId() == Id.mushroom){
        if(getBounds().intersects(e.getBounds())){
          int tpx = getX();
          int tpy = getY();
          if(state==PlayerState.SMALL && ((Mushroom)e).type == 0){
          width*=2;
          height*=2;
          state = PlayerState.BIG;
          }else if (((Mushroom)e).type == 1)
            Game.lives++;
          setX(tpx-width);
          setY(tpy-height);
         
          e.die();
        }}
      if(e.getId()==Id.coin){
        if(getBounds().intersects(e.getBounds())){
            Game.coins++;
            e.die(); 
            continue;
          }
        }
        if(e.getId() == Id.goomba){
          if(getBoundsBottom().intersects(e.getBounds())){
            Game.stomp.play();
            e.die();
          }else
          if(getBounds().intersects(e.getBounds())&&!invincible){
            if(state == PlayerState.BIG){
              state = PlayerState.SMALL;
              width/=2;
              height/=2;
              x+=width;
              y+=height;
            }else if ( state == PlayerState.SMALL)
            die();
          }
        }
        if(e.getId() == Id.plant){
          if(getBoundsBottom().intersects(e.getBounds())){
            e.die();
          }else
          if(getBounds().intersects(e.getBounds())&&!invincible){
            if(state == PlayerState.BIG){
              state = PlayerState.SMALL;
              width/=2;
              height/=2;
              x+=width;
              y+=height;
            }else if ( state == PlayerState.SMALL)
            die();
          }
        }
        if(e.getId() == Id.koopas){
          if(((Koopas)e).koopasState == KoopasState.WALKING){
            if(getBoundsBottom().intersects(e.getBoundsTop())){
            	((Koopas)e).koopasState = KoopasState.SHELL;
              
              jumping=true;
              falling=false;
              gravity=3.5;
            }else if(getBounds().intersects(e.getBounds()))die();
          }else if(((Koopas)e).koopasState == KoopasState.SHELL){
            if(getBoundsBottom().intersects(e.getBoundsTop())){
            	((Koopas)e).koopasState = KoopasState.SPINNING;
              int dir=rand.nextInt(2);
              if(dir==1){
                e.setVelX(6);
              }else {
                e.setVelX(-6);
              }
              
              jumping=true;
              falling=false;
              gravity=3.5;
            }else if(getBoundsLeft().intersects(e.getBoundsRight())){
              e.setVelX(-10);
              ((Koopas)e).koopasState = KoopasState.SPINNING;
            }else if(getBoundsRight().intersects(e.getBoundsLeft())){
              e.setVelX(10);
              ((Koopas)e).koopasState = KoopasState.SPINNING;
            }
            //else if(getBounds().intersects(e.getBounds()))die();
          }else if(((Koopas)e).koopasState == KoopasState.SPINNING){
            if(getBoundsBottom().intersects(e.getBoundsTop())){
            	((Koopas)e).koopasState = KoopasState.SHELL;
              
              jumping=true;
              falling=false;
              gravity=3.5;
            }else if(getBounds().intersects(e.getBounds())&&!invincible)die();            
          }
        }
        if(e.getId() == Id.towerboss){
          if(getBoundsBottom().intersects(e.getBounds())){
            if(((TowerBoss)e).attackable){
            ((TowerBoss)e).hp--;
            e.falling=true;
            e.gravity=3.0;
            ((TowerBoss)e).bossState=BossState.RECOVERING;
            ((TowerBoss)e).attackable =false;
            ((TowerBoss)e).phaseTime = 0;
            jumping=true;
            falling=false;
            gravity=3.5;
            }
          }else
          if(getBounds().intersects(e.getBounds())&&((TowerBoss)e).attackable&&!invincible){
            if(state == PlayerState.BIG){
              state = PlayerState.SMALL;
              width/=2;
              height/=2;
              x+=width;
              y+=height;
            }else if ( state == PlayerState.SMALL)
              die();
          }
        }
        if(e.getId() == Id.star){
          if(getBounds().intersects(e.getBounds())){
            invincible = true;
            e.die();
          }
        }
          
      }
   
    
    if(jumping&&!goingDownPipe){
      gravity-=0.13;
      setVelY((int)-gravity);
      if(gravity<=0.6){
        jumping=false;
        falling=true;
      }
    }
    if(falling&&!goingDownPipe){
      gravity+=0.13;
      setVelY((int)gravity);
    }
    
    if(velX==0 && velY==0){
      facing=2;
      frame=0;
      frameDelay=0;
    }
    
    if(velX!=0){
    frameDelay++;
    if(frameDelay>=10){
      frame++;
      if(frame>=3)
        frame=0;
      frameDelay=0;
    }
    }
    
    if(goingDownPipe){
      boolean intersects=false;
      for(int i=0;i<Game.handler.tile.size();i++){
        Tile t= Game.handler.tile.get(i);
        if(t.getId()==Id.pipe){
          if(getBounds().intersects(t.getBounds())){
            intersects=true;
          switch(t.facing){
            case 0:
              setVelY(-5);
              setVelX(0);
              pixelsTravelled+=Math.abs(velY);
              break;
            case  2:
              setVelY(5);
              setVelX(0);
              pixelsTravelled+=Math.abs(velY);
              break;
          }
          if(pixelsTravelled>t.height+height){
            goingDownPipe=false;
            pixelsTravelled=0;
          }
        }
     }
      }
      if(!intersects){
            goingDownPipe=false;
            pixelsTravelled=0;      
      }
    }
    
  }
  
  @Override
  public void render(Graphics g){
    g.drawString(name,x,y);
    if(facing==2)
      g.drawImage(Game.player[0].getBufferedImage(),x,y,width,height,null);
    else if(facing==0)
      g.drawImage(Game.player[frame+1].getBufferedImage(),x,y,width,height,null);
    else if(facing==1)
      g.drawImage(Game.player[frame+4].getBufferedImage(),x,y,width,height,null);
         
  }
  
  public void setState(byte state){
    if(state==0)this.state=PlayerState.SMALL;
    else if(state==1)this.state=PlayerState.BIG;
    else if(state==2)this.state=PlayerState.DEAD;
  }
  
  public byte getState(){
    if(this.state==PlayerState.SMALL)return 0;
    if(this.state==PlayerState.BIG)return 1;
    if(this.state==PlayerState.DEAD)return 2;
    return -1;
  }
  
  public byte getFacing(){
    return (byte)facing;
  }
  
  
  
}
