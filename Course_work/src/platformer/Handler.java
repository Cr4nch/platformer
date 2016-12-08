package platformer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import platformer.Entity.Coin;
import platformer.Entity.Entity;
import platformer.Entity.Goomba;
import platformer.Entity.Koopas;
import platformer.Entity.Player;
import platformer.Entity.PowerStar;
import platformer.Entity.TowerBoss;
import platformer.Tile.Flag;
import platformer.Tile.Magma;
import platformer.Tile.PowerUpBlock;
import platformer.Tile.Tile;
import platformer.Tile.Wall;
import platformer.Tile.Pipe;
import platformer.input.KeyInput;

public class Handler{
  public LinkedList<Entity> entity = new LinkedList<Entity>();
  public LinkedList<Tile> tile = new LinkedList<Tile>();
  public int count=0;
  public int sended=0;
  
  public Handler(){
   // createLevel();
  }
  
  public void render(Graphics g){
    Rectangle area = Game.getVisibleArea();
    if(area==null)return;
    for(int i=0;i<Game.handler.entity.size();i++){
      Entity en = Game.handler.entity.get(i);
    //  if(en.getId()==Id.player)((Player)en).name=Game.username;
      if(en.getBounds().intersects(area)&&en.getId()!=Id.particle)en.render(g);
    }
    for(int i=0;i<Game.handler.tile.size();i++){
      Tile ti = Game.handler.tile.get(i);
      if(ti.getBounds().intersects(area))ti.render(g);
    }
    for(Entity en:entity)
      if(en.getBounds().intersects(area)&&en.getId()==Id.particle)en.render(g);
    
    g.drawImage(Game.coin[0].getBufferedImage(),Game.getVisibleArea().x+20,Game.getVisibleArea().y+20,50,50,null);
    g.setColor(Color.WHITE);
    g.setFont(new Font("Courier",Font.BOLD,50));
    g.drawString("x"+Game.coins,Game.getVisibleArea().x+50,Game.getVisibleArea().y+40);
  }
  
  public void tick(){
    Rectangle area = Game.getVisibleArea();
    if(area==null)return;
    for(int i=0;i<entity.size();i++) { 
      //if(area!=null&&entity.get(i).getBounds().intersects(area))
        entity.get(i).tick(); 
    } 
    for(int i=0;i<tile.size();i++) { 
      if(area!=null&&tile.get(i).getBounds().intersects(area))tile.get(i).tick(); 
    }
  }
  
  public void addEntity(Entity en){
    en.setPosition(entity.size());
    entity.add(en);
    count++;
  }
  
  public void  removeEntity(Entity en){
    entity.remove(en);
    count--;
  }
  
  public void addTile(Tile ti){
    ti.setPosition(tile.size());
    tile.add(ti);
    count++;
    
  }
  
  public void removeTile(Tile ti){
    tile.remove(ti);
    count--;
  }
  
  public boolean searchPlayer(String s){
   for(Entity e:entity){
  	 if(e instanceof Player)
  		 if(((Player)e).name.compareTo(s)==0)
  			 return true;
   }
   return false;
  }
  
  public void createLevel(BufferedImage level){
    int width = level.getWidth();
    int height = level.getHeight();
    
    for(int y=0;y<height;y++){
      for(int x=0;x< width;x++){
        int pixel = level.getRGB(x,y);
        int red =  (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue  = (pixel) & 0xff;
        
        if(red==0&&green==0&&blue==255){
        	Game.spawnX=x*64;
        	Game.spawnY=y*64;
        }
        
        if(red==0&&green==0&&blue==0)addTile(new Wall(x*64,y*64,64,64,true,Id.wall,this));
        if(red==255&&green==0&&blue==0)addTile(new Magma(x*64,y*64,64,64,true,Id.magma,this));
        if(red==0&&green==255&&blue==0)addTile(new Flag(x*64,y*64,64,64*5,true,Id.flag,this));
        if(red==255&&green==250&&blue==0)addEntity(new Coin(x*64,y*64,64,64,true,Id.coin,this));
        if(red==255&&green==200&&blue==0)addEntity(new PowerStar(x*64,y*64,64,64,true,Id.star,this));
        if(red==255&&green==255&&blue==0)addTile(new PowerUpBlock(x*64,y*64,64,64,true,Id.powerUp,this,Game.liveMushroom,(byte)1));
        if(red==255&&green==255&&blue==1)addTile(new PowerUpBlock(x*64,y*64,64,64,true,Id.powerUp,this,Game.mushroom,(byte)0));
        if(red==0&&(green>123&&green<129)&&blue==0)addTile(new Pipe(x*64,y*64,64,65*15,true,Id.pipe,this,128-green,false));
        
        
        Player player = new Player(x*64,y*64,64,64,true,Id.player,this);
        player.name=Game.username;
        if(red==0&&green==0&&blue==255)addEntity(player);
        
        //if(red==255&&green==0&&blue==0)addEntity(new Mushroom(x*64,y*64,64,64,true,Id.mushroom,this));
        if(red==255&&green==119&&blue==1)addEntity(new Goomba(x*64,y*64,64,64,true,Id.goomba,this));
        if(red==255&&green==119&&blue==0)addEntity(new Koopas(x*64,y*64,64,64,true,Id.koopas,this));
        if(red==255&&green==0&&blue==255)addEntity(new TowerBoss(x*64,y*64,64,64,true,Id.towerboss,this,100));
      }
    }
   /* for(int i=0;i<Game.WIDTH*Game.SCALE/64+1;i++){
      addTile(new Wall(i*64,Game.HEIGHT*Game.SCALE-64,64,64,true,Id.wall,this));
      if(i!=0&&i!=1&&i!=16&i!=17)addTile(new Wall(i*64,100,64,64,true,Id.wall,this));
    }*/
  }
  
  public boolean createLevel(byte[] data){
  //	clearLevel();
  	System.out.println("Start creating level...");
    String sign= new String();
    
    try{
    BufferedWriter bw =new BufferedWriter(new FileWriter(new File("/home/sergey/Desktop/received.txt")));
    
    for(int i=0;i<data.length;i++)bw.write((int)data[i]+" ");
    
    bw.close();
    
    }catch(IOException e){
    	e.printStackTrace();
    }
    
    
    
    try{
      sign = new String(data,0,4,"UTF-8");
    }catch(UnsupportedEncodingException e){
      System.out.println("[!] Invalid signature. Cann't read map!");
    }
    if(sign.compareTo("map ")!=0){
      System.out.println("[!] Invalid signature. Cann't read map!");
      return false;
    }
    try{
      int pos=5;
      

      
      
      while(data[pos]!=127){
        if(data[pos]!=42){
        	System.out.println("Canary byte!!! "+pos+" "+data[pos]);
        }
        pos++;
        byte id=data[pos];
        if(id==127)break;
        pos++;
        byte[] bytes = new byte[4];
        System.arraycopy(data,pos,bytes,0,4);
        ByteBuffer wrapped = ByteBuffer.wrap(bytes); 
        int x=wrapped.getInt();
        pos+=4;
        System.arraycopy(data,pos,bytes,0,4);
        wrapped = ByteBuffer.wrap(bytes); 
        int y=wrapped.getInt();
        pos+=3;
        System.out.println("ID "+id);
        switch(id){
          case 0:{
          	System.out.println(x+" "+y+" WALL");
            addTile(new Wall(x,y,64,64,true,Id.wall,this));
            break;
          }
          case 1:{
            addTile(new Magma(x,y,64,64,true,Id.magma,this));
            break;
          }
          case 2:{
            addTile(new Flag(x,y,64,64*5,true,Id.flag,this));
            break;
          }
          case 3:{
            byte state=data[++pos];
            byte type=data[++pos];
            PowerUpBlock block=new PowerUpBlock(x,y,64,64,true,Id.powerUp,this,Game.liveMushroom,type);
            block.setState(state);
            addTile(block);      
            break;
          }
          case 4:{
            byte type=data[++pos];
            addTile(new Pipe(x,y,64,65*15,true,Id.pipe,this,type,false));
            break;
          }
          case 5:{
            addEntity(new Coin(x,y,64,64,true,Id.coin,this));
            break;
          }
          case 6:{
            byte velX=data[++pos];
            byte velY=data[++pos];
            PowerStar star=new PowerStar(x,y,64,64,true,Id.star,this);
            star.setVelX(velX);
            star.setVelY(velY);
            addEntity(star);
            break;
          }
          case 7:{
          	System.out.println(x+" "+y+" USER");
            byte velX=data[++pos];
            byte velY=data[++pos];
            byte state=data[++pos];
            byte facing=data[++pos];
            int posEnd=pos;
            while(data[posEnd]!=42)posEnd++;
            String name=new String(data,pos+1,posEnd-pos-1,"UTF-8");
            pos=posEnd-1;
            Player player= new Player(x,y,64,64,true,Id.player,this);
            player.name=name;
            player.setVelX(velX);
            player.setVelY(velY);
            player.setState(state);
            player.setFacing(facing);
            //player.name=name;
            System.out.println(name);
            addEntity(player);
            break;
          }
          case 8:{
            byte velX=data[++pos];
            byte velY=data[++pos];
            byte facing=data[++pos];
            Goomba goomba =new Goomba(x,y,64,64,true,Id.goomba,this);
            goomba.setVelX(velX);
            goomba.setVelY(velY);
            goomba.setFacing(facing);
            addEntity(goomba);
            break;
          }
          case 9:{
            byte velX=data[++pos];
            byte velY=data[++pos];
            byte state=data[++pos];
            byte facing=data[++pos];
            byte shellCount=data[++pos];
            Koopas koopas=new Koopas(x,y,64,64,true,Id.koopas,this);
            koopas.setVelX(velX);
            koopas.setVelY(velY);
            koopas.setState(state);
            koopas.setShellCount(shellCount);
            addEntity(koopas);
            break;
          }
          case 10:{
          	
            byte velX=data[++pos];
            byte velY=data[++pos];
            byte state=data[++pos];
            byte facing=data[++pos];
            byte hp=data[++pos];
            TowerBoss boss = new TowerBoss(x,y,64,64,true,Id.towerboss,this,100);
            boss.setVelX(velX);
            boss.setVelY(velY);
            boss.setState(state);
            boss.setHp(hp);
            addEntity(boss);
            break;
          }
          default:{
            break;
          }
        }
        pos++;
      }
      
      pos++;
      byte[] bytes = new byte[4];
      System.arraycopy(data,pos,bytes,0,4);
      ByteBuffer wrapped = ByteBuffer.wrap(bytes); 
      Game.spawnX=wrapped.getInt();
      pos+=4;
      System.arraycopy(data,pos,bytes,0,4);
      wrapped = ByteBuffer.wrap(bytes); 
      Game.spawnY=wrapped.getInt();
      
     }catch(Exception e){
       System.out.println("[!] Data corrupted!");
    }
    

    
    System.out.println("Level created!!!");
    System.out.println("Tile: "+tile.size());
    System.out.println("Entity: "+entity.size());
    return true;
  }
  
  
  public byte[] mapToByte(){
  	
  	
    byte[] map =new byte[2048];
    byte[] sign = new byte[0];
    try{
    sign = new String("map ").getBytes("UTF-8");
    }catch(UnsupportedEncodingException e){e.printStackTrace();};
    for(int i=0;i<sign.length;i++)map[i]=sign[i];
    int pos=5;
    map[pos]=42;
    
    
    for(int i=sended;i<tile.size();i++){
    	if(pos>=2000)break;
      Tile t = tile.get(i);
        if(t.id==Id.wall){
          map[++pos]=0;
          byte[] bytes = ByteBuffer.allocate(4).putInt(t.getX()).array();
          for(int k=0;k<bytes.length;k++)map[++pos]=bytes[k];

          bytes = ByteBuffer.allocate(4).putInt(t.getY()).array();
          for(int k=0;k<bytes.length;k++)map[++pos]=bytes[k];
        }
        if(t.id==Id.magma){
          map[++pos]=1;

          byte[] bytes = ByteBuffer.allocate(4).putInt(t.getX()).array();
          for(int k=0;k<bytes.length;k++)map[++pos]=bytes[k];

          bytes = ByteBuffer.allocate(4).putInt(t.getY()).array();
          for(int k=0;k<bytes.length;k++)map[++pos]=bytes[k];
        }
        if(t.id==Id.flag){
          map[++pos]=2;

          byte[] bytes = ByteBuffer.allocate(4).putInt(t.getX()).array();
          for(int k=0;k<bytes.length;k++)map[++pos]=bytes[k];

          bytes = ByteBuffer.allocate(4).putInt(t.getY()).array();
          for(int k=0;k<bytes.length;k++)map[++pos]=bytes[k];
        }
        if(t.id==Id.powerUp){
          map[++pos]=3;

          byte[] bytes = ByteBuffer.allocate(4).putInt(t.getX()).array();
          for(int k=0;k<bytes.length;k++)map[++pos]=bytes[k];

          bytes = ByteBuffer.allocate(4).putInt(t.getY()).array();
          for(int k=0;k<bytes.length;k++)map[++pos]=bytes[k];
          
          if(t instanceof PowerUpBlock){
          map[++pos]=((PowerUpBlock)t).getState();
          map[++pos]=((PowerUpBlock)t).getType();
          }
        }
        if(t.id==Id.pipe){
          map[++pos]=4;

 
          byte[] bytes = ByteBuffer.allocate(4).putInt(t.getX()).array();
          for(int k=0;k<bytes.length;k++)map[++pos]=bytes[k];

          bytes = ByteBuffer.allocate(4).putInt(t.getY()).array();
          for(int k=0;k<bytes.length;k++)map[++pos]=bytes[k];
          
          if(t instanceof Pipe){
          map[++pos]=((Pipe)t).getType();
          }
        }
        map[++pos]=42;
        sended++;
      }
      
    
    for(int i=sended-tile.size();i<entity.size();i++){
    	if(pos>=2000)break;
      Entity t = entity.get(i);
        if(t.id==Id.coin){
          map[++pos]=5;
          byte[] bytes = ByteBuffer.allocate(4).putInt(t.getX()).array();
          for(int k=0;k<bytes.length;k++)map[++pos]=bytes[k];

          bytes = ByteBuffer.allocate(4).putInt(t.getY()).array();
          for(int k=0;k<bytes.length;k++)map[++pos]=bytes[k];
        }
        if(t.id==Id.star){
          map[++pos]=6;
          byte[] bytes = ByteBuffer.allocate(4).putInt(t.getX()).array();
          for(int k=0;k<bytes.length;k++)map[++pos]=bytes[k];

          bytes = ByteBuffer.allocate(4).putInt(t.getY()).array();
          for(int k=0;k<bytes.length;k++)map[++pos]=bytes[k];
          map[++pos]=(byte)t.getVelX();
          map[++pos]=(byte)t.getVelY();
        }
        if(t.id==Id.player){
          map[++pos]=7;
          byte[] bytes = ByteBuffer.allocate(4).putInt(t.getX()).array();
          for(int k=0;k<bytes.length;k++)map[++pos]=bytes[k];

          bytes = ByteBuffer.allocate(4).putInt(t.getY()).array();
          for(int k=0;k<bytes.length;k++)map[++pos]=bytes[k];
          map[++pos]=(byte)t.getVelX();
          map[++pos]=(byte)t.getVelY();
          if(t instanceof Player){
          map[++pos]=((Player)t).getState();
          map[++pos]=((Player)t).getFacing();
          byte[] name = new byte[0];
          try{
          name=((Player)t).name.getBytes("UTF-8");
          }catch(UnsupportedEncodingException e){e.printStackTrace();}
          for(int j=0;j<name.length;j++)
            map[++pos]=name[j];
          }
        }
        if(t.id==Id.goomba){
          map[++pos]=8;
          byte[] bytes = ByteBuffer.allocate(4).putInt(t.getX()).array();
          for(int k=0;k<bytes.length;k++)map[++pos]=bytes[k];

          bytes = ByteBuffer.allocate(4).putInt(t.getY()).array();
          for(int k=0;k<bytes.length;k++)map[++pos]=bytes[k];
          map[++pos]=(byte)t.getVelX();
          map[++pos]=(byte)t.getVelY();
          map[++pos]=t.getFacing();
        }
        if(t.id==Id.koopas){
          map[++pos]=9;
          byte[] bytes = ByteBuffer.allocate(4).putInt(t.getX()).array();
          for(int k=0;k<bytes.length;k++)map[++pos]=bytes[k];

          bytes = ByteBuffer.allocate(4).putInt(t.getY()).array();
          for(int k=0;k<bytes.length;k++)map[++pos]=bytes[k];
          map[++pos]=(byte)t.getVelX();
          map[++pos]=(byte)t.getVelY();
          
          
          if(t instanceof Koopas){
          map[++pos]=((Koopas)t).getState();
          map[++pos]=((Koopas)t).getFacing();
          map[++pos]=((Koopas)t).getShellCount();
          }
        }
        if(t.id==Id.towerboss){
          map[++pos]=10;
          byte[] bytes = ByteBuffer.allocate(4).putInt(t.getX()).array();
          for(int k=0;k<bytes.length;k++)map[++pos]=bytes[k];

          bytes = ByteBuffer.allocate(4).putInt(t.getY()).array();
          for(int k=0;k<bytes.length;k++)map[++pos]=bytes[k];
          map[++pos]=(byte)t.getVelX();
          map[++pos]=(byte)t.getVelY();
          
          if(t instanceof TowerBoss){
          map[++pos]=((TowerBoss)t).getState();
          map[++pos]=((TowerBoss)t).getFacing();
          map[++pos]=((TowerBoss)t).getHp();
          }
        }
        map[++pos]=42;
        sended++;
      }
    map[++pos]=127;
    
    byte[] bytes = ByteBuffer.allocate(4).putInt(Game.spawnX).array();
    for(int k=0;k<bytes.length;k++)map[++pos]=bytes[k];
    bytes = ByteBuffer.allocate(4).putInt(Game.spawnY).array();
    for(int k=0;k<bytes.length;k++)map[++pos]=bytes[k];
    
    System.out.println("map size:"+pos);
    
    try{
    BufferedWriter bw =new BufferedWriter(new FileWriter(new File("/home/sergey/Desktop/sended.txt")));
    
    for(int i=0;i<map.length;i++)bw.write((int)map[i]+" ");
    
    bw.close();
    
    }catch(IOException e){
    	e.printStackTrace();
    }
    
    
    return map;
  }
  
  public void respawnPlayer(byte[] data){
  	try{
  	//byte[] data = packet.getData();
  	Player player;
  	if(new String(data,0,4).compareTo("resp")!=0){
  		System.out.println("Error: invalid respawn signature!");
  		return;
  	}
  	int pos=4;
    int posEnd=pos;
    while(data[posEnd]!=42)posEnd++;
    String name=new String(data,pos,posEnd-pos,"UTF-8");
    pos=posEnd; 
  	if(data[pos]!=42){
  		System.out.println("Error: invalid canary byte!");
  		return;  		
  	}
  	pos++;
    System.out.println("Update player "+name);
    for(Entity e:entity){
   	 if(e instanceof Player)
   		 if(((Player)e).name.compareTo(name)==0){
   			 player=((Player)e); 
         byte[] bytes = new byte[4];
         System.arraycopy(data,pos,bytes,0,4);
         ByteBuffer wrapped = ByteBuffer.wrap(bytes); 
         pos+=4;
         int x=wrapped.getInt();
         
         System.arraycopy(data,pos,bytes,0,4);
         wrapped = ByteBuffer.wrap(bytes); 
         pos+=4;
         int y=wrapped.getInt();
         ((Player)e).setX(x);
         ((Player)e).setY(y);
         ((Player)e).setVelX(0);
         ((Player)e).setVelY(0);
    }
    }
  	}catch(UnsupportedEncodingException e){
  		e.printStackTrace();
  	}
  	System.out.println("USER RESPAWNED");  	
  }
  
  public void updatePlayer(byte[] data){
  	try{
  	//byte[] data = packet.getData();
  	Player player;
  	if(new String(data,0,4).compareTo("upd ")!=0){
  		System.out.println("Error: invalid update signature!");
  		return;
  	}
  	int pos=4;
    int posEnd=pos;
    while(data[posEnd]!=42)posEnd++;
    String name=new String(data,pos,posEnd-pos,"UTF-8");
    pos=posEnd; 
  	if(data[pos]!=42){
  		System.out.println("Error: invalid canary byte!");
  		return;  		
  	}
  	pos++;
    System.out.println("Update player "+name);
    for(Entity e:entity){
   	 if(e instanceof Player)
   		 if(((Player)e).name.compareTo(name)==0){
   			 player=((Player)e); 
         byte[] bytes = new byte[4];
         System.arraycopy(data,pos,bytes,0,4);
         ByteBuffer wrapped = ByteBuffer.wrap(bytes); 
         pos+=4;
         int key=wrapped.getInt();
         byte rOrp=data[pos];
         System.out.println("KEY: "+key);
         System.out.println("rOrp: "+rOrp);
         switch(key){
         case KeyEvent.VK_W:{
        	 if(rOrp==0)KeyInput.wPressed(player);
        	 else if(rOrp==1)KeyInput.wReleased(player);
        	 break;
         }
         case KeyEvent.VK_S:{
        	 if(rOrp==0)KeyInput.sPressed(player);
        	 else if(rOrp==1)KeyInput.sReleased(player);
        	 break;
         }
         case KeyEvent.VK_A:{
        	 if(rOrp==0)KeyInput.aPressed(player);
        	 else if(rOrp==1)KeyInput.aReleased(player);
        	 break;
         }
         case KeyEvent.VK_D:{
        	 if(rOrp==0)KeyInput.dPressed(player);
        	 else if(rOrp==1)KeyInput.dReleased(player);
        	 break;
         }
         case KeyEvent.VK_SPACE:{
        	 if(rOrp==0)KeyInput.spacePressed(player);
        	 else if(rOrp==1)KeyInput.spaceReleased(player);
        	 break;
         }
   		 }
    }
    }
  	}catch(UnsupportedEncodingException e){
  		e.printStackTrace();
  	}
  	System.out.println("USER UPDATED");
  }
  
  public void clearLevel(){
    entity.clear();
    tile.clear();
  }
  
}
