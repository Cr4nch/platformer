package platformer.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import platformer.Game;
import platformer.Handler;
import platformer.Entity.Player;

public class Client extends Thread{
  private InetAddress address;
  private int port;
  private DatagramSocket socket;
  private Handler handler;
  
  public Client(String address,int port,Handler handler){
    this.handler=handler;
    try{
      this.socket= new DatagramSocket();
      this.address=InetAddress.getByName(address);
      this.port=port;
    }catch(SocketException e){
      System.out.println("[!] Error:cann't open socket");
    }catch(UnknownHostException e){
      System.out.println("[!] Error:invalid host name");
    }
  }
  
  public void run(){
    System.out.println("[*] Client Started!");
    
    try{
      String request = "JOIN "+Game.username;
      byte[] buffer = request.getBytes("UTF-8");
      DatagramPacket packet = new DatagramPacket(buffer,buffer.length,address,port);
      socket.send(packet);
      buffer= new byte[7];
      packet=new DatagramPacket(buffer,buffer.length);
      socket.receive(packet);
      port = packet.getPort();
      String ans = new String(packet.getData());
      if(ans.compareTo("WELCOME")!=0){
        
        System.out.println("[!] Server give invalid ansver!");
        return;
      }
    }catch(SocketTimeoutException e){
      System.out.println("[!] Server don't ansver!");
    }catch(IOException e){
      System.out.println("[!] IO error!");
    }
    
    try{
    	if(Game.localServer==false)
      requestMap();
    }catch(UnsupportedEncodingException e){
    	e.printStackTrace();
    }catch(IOException e){
    	e.printStackTrace();
    }
    
    while(true){
    	try{
    	byte[] buffer = new byte[1024];
    	DatagramPacket packet = new DatagramPacket(buffer,buffer.length);
    	socket.receive(packet);
    	String sign = new String(packet.getData(),0,4);
    	if(sign.compareTo("upd ")==0){
    		Game.handler.updatePlayer(packet);
    	}else if(sign.compareTo("kill ")==0)
    		break;
    	}catch(IOException e){
    		e.printStackTrace();
    	}
    }
    
    
    System.out.println("[*] Client Stoped!");
  }
  
  public boolean sendUpdate(Player player){
  	System.out.println("SEND UPDATE");
  	try{
  		String msg="upd player "+player.name;
  		byte[] msgb = msg.getBytes("UTF-8");
  		byte[] data = new byte[1024];
  		int pos=msgb.length;
  		for(int i=0;i<msgb.length;i++){
  			data[i]=msgb[i];
  		}
  		data[pos]=42;
      byte[] bytes = ByteBuffer.allocate(4).putInt(player.getX()).array();
      for(int k=0;k<bytes.length;k++)data[++pos]=bytes[k];

      bytes = ByteBuffer.allocate(4).putInt(player.getY()).array();
      for(int k=0;k<bytes.length;k++)data[++pos]=bytes[k];
      data[++pos]=(byte)player.getVelX();
      data[++pos]=(byte)player.getVelY();
      if(player instanceof Player){
      data[++pos]=((Player)player).getState();
      data[++pos]=((Player)player).getFacing();  
      
      DatagramPacket packet = new DatagramPacket(data,data.length,address,port);
      socket.send(packet);
      }
      data[++pos]=127;
  	
  	}catch(UnsupportedEncodingException e){
  		return false;
  	}catch(IOException e){
  		e.printStackTrace();
  	}
  	return true;
  }
  
  public boolean disconnect(){
  	return true;
  }
  
  public boolean respawn(){
  	return true;
  }
  
  public void requestMap()
    throws UnsupportedEncodingException,
    IOException
  {
  	Game.playing=false;
    Game.handler.clearLevel();
//    System.out.println("Level cleared")
    byte[] buffer = new String("map ").getBytes("UTF-8");
    DatagramPacket packet = new DatagramPacket(buffer,buffer.length,address,port);
    socket.send(packet); 
    
    while(true){
    buffer= new byte[2048];
    packet = new DatagramPacket(buffer,buffer.length);
    socket.receive(packet);
    if(new String(packet.getData(),0,4).compareTo("end ")==0)break;
    else if(new String(packet.getData(),0,4).compareTo("map ")==0)
    	Game.handler.createLevel(packet.getData());
    };
    //Game.handler.clearLevel();
    //System.out.println("HENDLER ANS: "+ans);
    Game.playing=true;
  }
  
  

  public boolean ping(){
    return true;
  }
  
  public void pong(){
  }
  
}
