package platformer.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;

import platformer.Game;
import platformer.Handler;
import platformer.Id;
import platformer.Entity.Player;

public class ClientResponder extends Thread{
  private Socket socket;
  //private InetAddress address;
 // private int port;
  private Handler handler;
  public String name;
  private Player user;
  private Server parentServer;
  private InputStream in=null;
  private OutputStream out=null;
  
  ClientResponder(Socket socket,String data,Handler handler,Server parentServer){
    this.socket=socket;
   // this.address=packet.getAddress();
   // this.port=packet.getPort();
    this.handler=handler;
    this.parentServer=parentServer;



    	int pos=0;
    	while(data.charAt(pos)!="#".charAt(0))pos++;
    	this.name=data.substring(0,pos);
    
  }
  
  public void run(){
    System.out.println("[*] client responder for "+this.name+" started!");
    
    try {
			in = socket.getInputStream();
			out = socket.getOutputStream();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    
    
    try{
      String message = "WELCOME";
      
      
      byte[] data = message.getBytes("UTF-8");
      //DatagramPacket packet = new DatagramPacket(data,data.length,address,port);
      out.write(data);
      
      Game.semph.acquire();
    //  System.out.println("Responder(init) use semph");
      
      synchronized(Game.handler){
      if(!Game.handler.searchPlayer(name)){
      user =new Player(Game.spawnX ,Game.spawnY ,64,64,true,Id.player,Game.handler);
      user.name=this.name;
      Game.handler.addEntity(user);
      }
      
      
     // System.out.println("Responder(init) release semph");
      Game.semph.release();
      
  		String msg="add "+this.name;
  		byte[] msgb = msg.getBytes("UTF-8");
  		data = new byte[1024];
  		int pos=msgb.length;
  		for(int i=0;i<msgb.length;i++){
  			data[i]=msgb[i];
  		}
  		data[pos]=42;
  		(new BroadcastNotifer(parentServer,data,Game.username,false)).start();
            
      
      
      }
    }catch(IOException e){
      System.out.println("[!] IOException at client reasponder for "+this.name);
    }catch(InterruptedException e){
    	System.out.println("SEMAPHORE CLIENT RESPONDER ERROR");
    }

    
    while(true){
      try{
        byte[] data = new byte[1024];
        //DatagramPacket packet = new DatagramPacket(data,data.length,address,port);
        

          int size=in.read(data);
          String cmd = new String(data,0,4,"UTF-8");
          
          if(size<=0)continue;
          
          Game.semph.acquire();
        //  System.out.println("Responder(main) use semph");
          
          
          synchronized(Game.handler){
          if(cmd.compareTo("map ")==0){
            sendMap();
          }else if(cmd.compareTo("kill")==0){
          	new BroadcastNotifer(parentServer,data,name,false);
            //user.die();
            return;
          }else if(cmd.compareTo("upd ")==0){
          	System.out.println("RECEIVED UPDATE");
          	handler.updatePlayer(data);
          	if(Game.localServer==true)
          		(new BroadcastNotifer(parentServer,data,name,true)).start();
          }else if(cmd.compareTo("resp")==0){
          	System.out.println("RECEIVED RESPAWN");
          	Game.handler.respawnPlayer(data);
          	if(Game.localServer==true)
          	(new BroadcastNotifer(parentServer,data,name,true)).start(); 	
          }else{
          	break;
          }
          }
          
          Game.semph.release();
          
          
      }catch(SocketTimeoutException e){
          //if(!ping())break;
      }catch(IOException e){
      System.out.println("[!] IOException at client reasponder for "+this.name);
      }catch(InterruptedException e){
      	System.out.println("SEMAPHORE CLIENT RESPONDER ERROR");
      }

  }
    
    //socket.close();
    
    System.out.println("[*] client responder for "+this.name+" stoped!");
  }
  
  public void sendMap()
    throws IOException 
  {
  	//if(Game.localServer)return;
  	while(Game.handler.sended<Game.handler.count){
  		byte[] buffer = Game.handler.mapToByte();
  	//	DatagramPacket packet = new DatagramPacket(buffer,buffer.length,address,port);
  		out.write(buffer);
  	}
  	byte[] buffer = new String("end ").getBytes("UTF-8");
		//DatagramPacket packet = new DatagramPacket(buffer,buffer.length,address,port);
		out.write(buffer); 	
  	Game.handler.sended=0;
  }
  
  
/*  public boolean ping(){
    byte[]  data = new byte[0];
    try{
    data = new String("PING").getBytes("UTF-8");
    }catch(UnsupportedEncodingException e){e.printStackTrace();}
    DatagramPacket packet = new DatagramPacket(data,data.length);
    try{
    socket.send(packet);
    }catch(IOException e){
      System.out.println("[!] IOException at client reasponder for "+this.name);
      return false;
    }
    data=new byte[1024];
    packet=new DatagramPacket(data,data.length);
    try{
    socket.receive(packet);
    }catch(SocketTimeoutException e){
      System.out.println("[*] Client "+this.name+" don't ansvered!");
      return false;
    }catch(IOException e){
      System.out.println("[!] IOException at client reasponder for "+this.name);
    }
    String ans = "";
    try{
    ans=new String(packet.getData(),"UTF-8");
    }catch(UnsupportedEncodingException e){
      e.printStackTrace();
    }
    if(ans.compareTo("pong")!=0)return false;
    return true;
  }
  
  public void pong(DatagramPacket packet){
    InetAddress addressDest= packet.getAddress();
    int port =packet.getPort();
    byte[] data=new byte[0];
    try{
    data = new String("pong").getBytes("UTF-8");
    }catch(UnsupportedEncodingException e){
      e.printStackTrace();
    }
    packet = new DatagramPacket(data,data.length,addressDest,port);
    try{
    socket.send(packet);
    }catch(IOException e){
      System.out.println("[!] IOException at client reasponder for "+this.name);
    }
  }
*/
	public void sendUpdate(byte[] data) 
			throws IOException 
	{
		//byte[] data = packet.getData();
		//DatagramPacket p = new DatagramPacket(data,data.length,address,port);
		out.write(data);
	}
  
  
}
