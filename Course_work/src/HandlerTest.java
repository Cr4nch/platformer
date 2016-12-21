import static org.junit.Assert.*;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import org.junit.Test;

import platformer.Game;
import platformer.Handler;
import platformer.Id;
import platformer.Entity.*;
import platformer.Tile.*;


public class HandlerTest { 
	
	private void addEntitys(Handler handler){
		int x=1,y=1;
		
		handler.addEntity(new Player(x*64,y*64,64,64,true,Id.player,handler));
		handler.addEntity(new Mushroom(x*64,y*64,64,64,true,Id.mushroom,handler,1));
		handler.addEntity(new Goomba(x*64,y*64,64,64,true,Id.goomba,handler));
		handler.addEntity(new Koopas(x*64,y*64,64,64,true,Id.koopas,handler));
		handler.addEntity(new TowerBoss(x*64,y*64,64,64,true,Id.towerboss,handler,100));
		handler.addEntity(new Coin(x*64,y*64,64,64,true,Id.coin,handler));
		handler.addEntity(new PowerStar(x*64,y*64,64,64,true,Id.star,handler));
	}
	
	private void addTiles(Handler handler){
		int x=1,y=1;
		
		handler.addTile(new Wall(x*64,y*64,64,64,true,Id.wall,handler));
		handler.addTile(new Magma(x*64,y*64,64,64,true,Id.magma,handler));
		handler.addTile(new Flag(x*64,y*64,64,64*5,true,Id.flag,handler));
		handler.addTile(new PowerUpBlock(x*64,y*64,64,64,true,Id.powerUp,handler,Game.liveMushroom,(byte)1));
		handler.addTile(new Pipe(x*64,y*64,64,65*15,true,Id.pipe,handler,123,false));
	}

	@Test
	public void testAddEntity() {
		Handler handler = new Handler();
		addEntitys(handler);
		LinkedList<Entity> entity = handler.entity;
		assertEquals("invalid count entitys: "+entity.size(),7,entity.size());
		
		assertEquals("invalid player signature.",Id.player,entity.get(0).id);
		assertEquals("invalid mushroom signature.",Id.mushroom,entity.get(1).id);
		assertEquals("invalid goomba signature.",Id.goomba,entity.get(2).id);
		assertEquals("invalid koopas signature.",Id.koopas,entity.get(3).id);
		assertEquals("invalid towerboss signature.",Id.towerboss,entity.get(4).id);
		assertEquals("invalid coin signature.",Id.coin,entity.get(5).id);
		assertEquals("invalid star signature.",Id.star,entity.get(6).id);
		//fail("Not yet implemented");
	}

	@Test
	public void testRemoveEntity() {
		Handler handler = new Handler();
		addEntitys(handler);
		assertEquals("invalid count entitys: "+handler.count,7,handler.count);
		
		assertEquals("invalid first element signature.",Id.player,handler.entity.get(0).id);
		handler.removeEntity(handler.entity.get(0));
		assertEquals("invalid count entitys: "+handler.count,6,handler.count);
		
		assertEquals("invalid first element signature.",Id.mushroom,handler.entity.get(0).id);
		handler.removeEntity(handler.entity.get(0));
		assertEquals("invalid count entitys: "+handler.count,5,handler.count);
		
		assertEquals("invalid first element signature.",Id.goomba,handler.entity.get(0).id);
		handler.removeEntity(handler.entity.get(0));
		assertEquals("invalid count entitys: "+handler.count,4,handler.count);
		
		assertEquals("invalid first element signature.",Id.koopas,handler.entity.get(0).id);
		handler.removeEntity(handler.entity.get(0));
		assertEquals("invalid count entitys: "+handler.count,3,handler.count);
		
		assertEquals("invalid first element signature.",Id.towerboss,handler.entity.get(0).id);
		handler.removeEntity(handler.entity.get(0));
		assertEquals("invalid count entitys: "+handler.count,2,handler.count);	
		
		assertEquals("invalid first element signature.",Id.coin,handler.entity.get(0).id);
		handler.removeEntity(handler.entity.get(0));
		assertEquals("invalid count entitys: "+handler.count,1,handler.count);	
		
		assertEquals("invalid first element signature.",Id.star,handler.entity.get(0).id);
		handler.removeEntity(handler.entity.get(0));
		assertEquals("invalid count entitys: "+handler.count,0,handler.count);	
	}

	@Test
	public void testAddTile() {
		Handler handler = new Handler();
		addTiles(handler);
		LinkedList<Tile> tile = handler.tile;
		assertEquals("invalid count entitys: "+tile.size(),5,tile.size());
		
		assertEquals("invalid player signature.",Id.wall,tile.get(0).id);
		assertEquals("invalid mushroom signature.",Id.magma,tile.get(1).id);
		assertEquals("invalid goomba signature.",Id.flag,tile.get(2).id);
		assertEquals("invalid koopas signature.",Id.powerUp,tile.get(3).id);
		assertEquals("invalid towerboss signature.",Id.pipe,tile.get(4).id);
	}

	@Test
	public void testRemoveTile() {
		Handler handler = new Handler();
		addTiles(handler);
		assertEquals("invalid count entitys: "+handler.count,5,handler.count);
		
		assertEquals("invalid first element signature.",Id.wall,handler.tile.get(0).id);
		handler.removeTile(handler.tile.get(0));
		assertEquals("invalid count entitys: "+handler.count,4,handler.count);
		
		assertEquals("invalid first element signature.",Id.magma,handler.tile.get(0).id);
		handler.removeTile(handler.tile.get(0));
		assertEquals("invalid count entitys: "+handler.count,3,handler.count);
		
		assertEquals("invalid first element signature.",Id.flag,handler.tile.get(0).id);
		handler.removeTile(handler.tile.get(0));
		assertEquals("invalid count entitys: "+handler.count,2,handler.count);
		
		assertEquals("invalid first element signature.",Id.powerUp,handler.tile.get(0).id);
		handler.removeTile(handler.tile.get(0));
		assertEquals("invalid count entitys: "+handler.count,1,handler.count);
		
		assertEquals("invalid first element signature.",Id.pipe,handler.tile.get(0).id);
		handler.removeTile(handler.tile.get(0));
		assertEquals("invalid count entitys: "+handler.count,0,handler.count);	
		
	}

	@Test
	public void testSearchPlayer() {
		Handler handler = new Handler();
		Player player = new Player(64,64,64,64,true,Id.player,handler);
		handler.addEntity(player);
		player.name="TestName";
		assertTrue(handler.searchPlayer("TestName"));
		assertFalse(handler.searchPlayer("MustBeFalse"));
	}

	@Test
	public void testCreateLevelBufferedImage() {
		Handler handler = new Handler();
		try {		
			BufferedImage img = ImageIO.read(getClass().getResource("/platformer/res/level1.png"));
			handler.createLevel(img);
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertEquals("invalid count : "+handler.count,101,handler.count);
		assertEquals("invalid count entitys: "+handler.entity.size(),51,handler.entity.size());
		assertEquals("invalid count tiles: "+handler.tile.size(),50,handler.tile.size());		
		
		for(int i=0;i<10;i++){
			assertEquals("invalid element signature.",Id.wall,handler.tile.get(i).id);		
		}
		for(int i=10;i<19;i++){
			assertEquals("invalid element signature.",Id.magma,handler.tile.get(i).id);		
		}
		for(int i=20;i<29;i++){
			assertEquals("invalid element signature.",Id.flag,handler.tile.get(i).id);		
		}
		for(int i=30;i<39;i++){
			assertEquals("invalid element signature.",Id.powerUp,handler.tile.get(i).id);		
		}
		for(int i=40;i<49;i++){
			assertEquals("invalid element signature.",Id.pipe,handler.tile.get(i).id);		
		}
		for(int i=0;i<1;i++){
			assertEquals("invalid element signature.",Id.player,handler.entity.get(i).id);		
		}
		for(int i=1;i<10;i++){
			assertEquals("invalid element signature.",Id.koopas,handler.entity.get(i).id);		
		}
		for(int i=11;i<20;i++){
			assertEquals("invalid element signature.",Id.goomba,handler.entity.get(i).id);		
		}
		for(int i=21;i<30;i++){
			assertEquals("invalid element signature.",Id.towerboss,handler.entity.get(i).id);		
		}
		for(int i=31;i<40;i++){
			assertEquals("invalid element signature.",Id.coin,handler.entity.get(i).id);		
		}
		for(int i=41;i<50;i++){
			assertEquals("invalid element signature.",Id.star,handler.entity.get(i).id);		
		}
		
	}

	/*@Test
	public void testCreateLevelByteArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testMapToByte() {
		fail("Not yet implemented");
	}*/

	@Test
	public void testRespawnPlayer() {
		Handler handler = new Handler();
		Player player = new Player(64,64,64,64,true,Id.player,handler);
		handler.addEntity(player);
		player.name="TestName";
		String msg="resp"+"TestName";
		byte[] msgb=null;
		try {
			msgb = msg.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] data = new byte[1024];
		int pos=msgb.length;
		for(int i=0;i<msgb.length;i++){
			data[i]=msgb[i];
		}
		data[pos]=42;
    byte[] bytes = ByteBuffer.allocate(4).putInt(0).array();
    for(int k=0;k<bytes.length;k++)data[++pos]=bytes[k];

    bytes = ByteBuffer.allocate(4).putInt(0).array();
    for(int k=0;k<bytes.length;k++)data[++pos]=bytes[k]; 
    handler.respawnPlayer(data);
    assertEquals("invalid X coordinate",0,handler.entity.get(0).getX());
    assertEquals("invalid Y coordinate",0,handler.entity.get(0).getY());
	}

	@Test
	public void testUpdatePlayer() {
		Handler handler = new Handler();
		Player player = new Player(64,64,64,64,true,Id.player,handler);
		handler.addEntity(player);
		player.name="TestName";
		String msg="upd "+"TestName";
		byte[] msgb=null;
		try {
			msgb = msg.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] data = new byte[1024];
		int pos=msgb.length;
		for(int i=0;i<msgb.length;i++){
			data[i]=msgb[i];
		}
		data[pos]=42;

    byte[] bytes = ByteBuffer.allocate(4).putInt(KeyEvent.VK_D).array();
    for(int k=0;k<bytes.length;k++)data[++pos]=bytes[k];
    data[++pos]=0;
    data[++pos]=127;
    handler.updatePlayer(data);
    assertEquals("Invalid velX",player.getVelX(),5);
	}

	@Test
	public void testAddPlayer() {
		Handler handler = new Handler();
		String msg="add "+"NAME";
		byte[] msgb=null;
		try {
			msgb = msg.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] data = new byte[1024];
		int pos=msgb.length;
		for(int i=0;i<msgb.length;i++){
			data[i]=msgb[i];
		}
		data[pos]=42;
		handler.addPlayer(data);
		assertEquals("invalid count : "+handler.count,1,handler.count);
		assertEquals("invalid count entitys: "+handler.entity.size(),1,handler.entity.size());
		assertEquals("invalid count tiles: "+handler.tile.size(),0,handler.tile.size());	
		assertEquals("invalid element signature.",Id.player,handler.entity.get(0).id);
	}

	@Test
	public void testClearLevel() {
		Handler handler = new Handler();
		addEntitys(handler);
		addTiles(handler);
		assertEquals("invalid count : "+handler.count,12,handler.count);
		assertEquals("invalid count entitys: "+handler.entity.size(),7,handler.entity.size());
		assertEquals("invalid count tiles: "+handler.tile.size(),5,handler.tile.size());
		handler.clearLevel();
		assertEquals("invalid count : "+handler.count,0,handler.count);
		assertEquals("invalid count entitys: "+handler.entity.size(),0,handler.entity.size());
		assertEquals("invalid count tiles: "+handler.tile.size(),0,handler.tile.size());		
	}

}
