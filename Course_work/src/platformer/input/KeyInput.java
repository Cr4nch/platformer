package platformer.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import platformer.Game;
import platformer.Id;
import platformer.Entity.Entity;
import platformer.Entity.FireBall;
import platformer.Entity.Player;
import platformer.Tile.Tile;

public class KeyInput implements KeyListener{
  
  private static boolean fire = true;
  
  
  public static void wPressed(Player p){
    for(int j =0; j<Game.handler.tile.size();j++){
      Tile t = Game.handler.tile.get(j);
      if(t.getId()==Id.pipe){
        if(p.getBoundsTop().intersects(t.getBounds())){
          if(!p.goingDownPipe){
          //  en.setVelX(0);
            p.goingDownPipe=true;
          }
        }
      }
    }
     if(!p.jumping) {
       p.jumping=true;
      p.gravity=9.0;
      Game.jump.play();
     }  	
  }
  
  public static void sPressed(Player p){
    for(int j =0; j<Game.handler.tile.size();j++){
      Tile t = Game.handler.tile.get(j);
      if(t.getId()==Id.pipe){
        if(p.getBoundsBottom().intersects(t.getBounds())){
          if(!p.goingDownPipe){
          //  en.setVelX(0);
            p.goingDownPipe=true;
          }
        }
      }
    }
  }
  
  public static void aPressed(Player p){
    p.facing=0;
    p.setVelX(-5);
  }
  
  public static void dPressed(Player p){
    p.facing=1;
    p.setVelX(5);
  }
  
  public static void spacePressed(Player p){
    
    if(p.facing==0&&fire)
      Game.handler.addEntity(new FireBall(p.getX()-24,p.getY()+12,24,24,true,Id.fireball,p.handler,p.facing));
    if(p.facing==1&&fire)
      Game.handler.addEntity(new FireBall(p.getX()+p.getWidth(),p.getY()+12,24,24,true,Id.fireball,p.handler,p.facing));
    fire = false;
  }
  
  @Override 
  public void keyPressed(KeyEvent event){
    int key = event.getKeyCode();
    

    if(!Game.playing)return;
    
    
    for(int i=0;i<Game.handler.entity.size();i++){
      Entity en = Game.handler.entity.get(i);
      if(en.id == Id.player){
      	
        	if(en instanceof Player){
        		if(((Player)(en)).name.compareTo(Game.username)!=0)
        			continue;
        	}
      	
        if(en.goingDownPipe)return;
    switch(key){
      case KeyEvent.VK_W:
      	wPressed((Player)en);
      	if(Game.multi){
      		Game.client.sendUpdate(key,(byte)0);
      	}
        //en.setVelY(-5);
        break;
     case KeyEvent.VK_S:
    	 sPressed((Player)en);
     	if(Game.multi){
    		Game.client.sendUpdate(key,(byte)0);
    	}
        break;
      case KeyEvent.VK_A:
      	aPressed((Player)en);
      	if(Game.multi){
      		Game.client.sendUpdate(key,(byte)0);
      	}
        break;
      case KeyEvent.VK_D:
      	dPressed((Player)en);
      	if(Game.multi){
      		Game.client.sendUpdate(key,(byte)0);
      	}
        break;
      case KeyEvent.VK_Q:
        en.die();
      	if(Game.multi){
      		Game.client.sendUpdate(key,(byte)0);
      	}
        break;
      case KeyEvent.VK_SPACE:
      	spacePressed((Player)en);
      	if(Game.multi){
      		Game.client.sendUpdate(key,(byte)0);
      	}
        break;
      case KeyEvent.VK_ESCAPE:
        if(Game.playing){
          Game.playing=false;
          Game.guiParent.changeLayout("PAUSE");
        }
      	if(Game.multi){
      		Game.client.sendUpdate(key,(byte)0);
      	}
        break;
    }

      }
    }
  }
  

 
  public static void wReleased(Player p){
    p.setVelY(0); 	
  }
  
  public static void sReleased(Player p){

  }
  
  public static void aReleased(Player p){
    p.setVelX(0);
  }
  
  public static void dReleased(Player p){
    p.setVelX(0);
  }
  
  public static void spaceReleased(Player p){
    fire = true;
  }
  

	@Override
  public void keyReleased(KeyEvent event){
  	
  	
    int key = event.getKeyCode(); 
    for(Entity en:Game.handler.entity){
      if(en.id == Id.player){
      	if(en instanceof Player){
      		if(((Player)(en)).name.compareTo(Game.username)!=0)
      			continue;
      	}
      	
      	
      	
    switch(key){
      case KeyEvent.VK_W:
      	wReleased((Player)en);
        if(Game.multi){
        	Game.client.sendUpdate(key,(byte)1);
        }
        break;
   /*   case KeyEvent.VK_S:
        en.setVelY(0);
        break;*/
      case KeyEvent.VK_A:
      	aReleased((Player)en);
        if(Game.multi){
        	Game.client.sendUpdate(key,(byte)1);
        }
        break;
      case KeyEvent.VK_D:
      	dReleased((Player)en);
        if(Game.multi){
        	Game.client.sendUpdate(key,(byte)1);
        }
        break;
      case KeyEvent.VK_SPACE:
      	spaceReleased((Player)en);
        if(Game.multi){
        	Game.client.sendUpdate(key,(byte)1);
        }
        break;
    
    }
      }
   }
  }
  
  @Override
  public void keyTyped(KeyEvent event){
    
  }
  
}
