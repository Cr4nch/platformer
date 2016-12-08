package platformer.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import platformer.Game;
import platformer.Handler;
import platformer.Entity.Player;

public class Client extends Thread{
 // private InetAddress address;
 // private int port;
  private Socket socket;
  private Handler handler;
  private InputStream in=null;
  private OutputStream out=null;
  
  public Client(String address,int port,Handler handler){
    this.handler=handler;
    try{
      this.socket= new Socket(address,port);
      in=socket.getInputStream();
      out=socket.getOutputStream();
     // this.address=InetAddress.getByName(address);
     // this.port=port;
    }catch(SocketException e){
      System.out.println("[!] Error:cann't open socket");
    }catch(UnknownHostException e){
      System.out.println("[!] Error:invalid host name");
    }catch(IOException e){
    	e.printStackTrace();
    }
  }
  
  public void run(){
    System.out.println("[*] Client "+Game.username+" Started!");
    
    try{
    	
      String request = "JOIN "+Game.username+'#';
      byte[] buffer = request.getBytes("UTF-8");
     // DatagramPacket packet = new DatagramPacket(buffer,buffer.length,address,port);
      out.write(buffer);
      out.flush();
      buffer= new byte[7];
      //packet=new DatagramPacket(buffer,buffer.length);
      in.read(buffer);
     // port = packet.getPort();
      String ans = new String(buffer);
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
    	System.out.println("WAIT FOR PACKET");
    	byte[] buffer = new byte[1024];
    	//DatagramPacket packet = new DatagramPacket(buffer,buffer.length);
    	in.read(buffer);
    	String sign = new String(buffer,0,4);
    	if(sign.compareTo("upd ")==0){
    		Game.handler.updatePlayer(buffer);
    	}else if(sign.compareTo("kill ")==0)
    		break;
    	else if(sign.compareTo("resp")==0)
    		Game.handler.respawnPlayer(buffer);
    	}catch(IOException e){
    		e.printStackTrace();
    	}
    }
    
    
    System.out.println("[*] Client Stoped!");
  }
  
  public void sendRespawn(){
  	System.out.println("SEND RESPAWN");
  	try{
  		String msg="resp"+Game.username;
  		byte[] msgb = msg.getBytes("UTF-8");
  		byte[] data = new byte[1024];
  		int pos=msgb.length;
  		for(int i=0;i<msgb.length;i++){
  			data[i]=msgb[i];
  		}
  		data[pos]=42;
      byte[] bytes = ByteBuffer.allocate(4).putInt(Game.spawnX).array();
      for(int k=0;k<bytes.length;k++)data[++pos]=bytes[k];

      bytes = ByteBuffer.allocate(4).putInt(Game.spawnY).array();
      for(int k=0;k<bytes.length;k++)data[++pos]=bytes[k];  
      
      out.write(data);
      
  	}catch(UnsupportedEncodingException e){
  		return;
  	}catch(IOException e){
  		e.printStackTrace();
  	}
  }
  
  public boolean sendUpdate(int key,byte rOrp){
  	System.out.println("SEND UPDATE");
  	try{
  		String msg="upd "+Game.username;
  		byte[] msgb = msg.getBytes("UTF-8");
  		byte[] data = new byte[1024];
  		int pos=msgb.length;
  		for(int i=0;i<msgb.length;i++){
  			data[i]=msgb[i];
  		}
  		data[pos]=42;

      byte[] bytes = ByteBuffer.allocate(4).putInt(key).array();
      for(int k=0;k<bytes.length;k++)data[++pos]=bytes[k];
      data[++pos]=rOrp;
      data[++pos]=127;

      
    //  DatagramPacket packet = new DatagramPacket(data,data.length,address,port);
      out.write(data);
      
  	
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
  
  public boolean respawn(byte[] data){
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
    //DatagramPacket packet = new DatagramPacket(buffer,buffer.length,address,port);
    out.write(buffer); 
    
    while(true){
    buffer= new byte[2048];
    //packet = new DatagramPacket(buffer,buffer.length);
    in.read(buffer);
    if(new String(buffer,0,4).compareTo("end ")==0)break;
    else if(new String(buffer,0,4).compareTo("map ")==0)
    	Game.handler.createLevel(buffer);
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
