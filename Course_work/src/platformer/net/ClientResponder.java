package platformer.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

import platformer.Game;
import platformer.Handler;
import platformer.Id;
import platformer.Entity.Player;

public class ClientResponder extends Thread{
  private DatagramSocket socket;
  private InetAddress address;
  private int port;
  private Handler handler;
  private String name;
  private Player user;
  private Server parentServer;
  
  ClientResponder(DatagramSocket socket,DatagramPacket packet,Handler handler,Server parentServer){
    this.socket=socket;
    this.address=packet.getAddress();
    this.port=packet.getPort();
    this.handler=handler;
    this.parentServer=parentServer;
    String  data = new String();
    try{
      data = new String(packet.getData(),"UTF-8");
    }catch(UnsupportedEncodingException e){
      e.printStackTrace();
    }
    String[] parts = data.split(" ");
    if(parts.length<2){
      System.out.println("[!] Name not found! ( You shouldn't see this error)");
      this.name=address.toString()+":"+port;
    }else{
    	int pos=0;
    	while(parts[1].charAt(pos)!=0)pos++;
    	this.name=parts[1].substring(0,pos);
    }
    
  }
  
  public void run(){
    System.out.println("[*] client responder for "+this.name+" started!");
    
    try{
      String message = "WELCOME";
      byte[] data = message.getBytes("UTF-8");
      DatagramPacket packet = new DatagramPacket(data,data.length,address,port);
      socket.send(packet);
      if(!Game.handler.searchPlayer(name)){
      user =new Player(Game.spawnX ,Game.spawnY ,64,64,true,Id.player,Game.handler);
      user.name=this.name;
      Game.handler.addEntity(user);
      }
    }catch(IOException e){
      System.out.println("[!] IOException at client reasponder for "+this.name);
    }

    
    while(true){
      try{
        byte[] data = new byte[1024];
        DatagramPacket packet = new DatagramPacket(data,data.length,address,port);
        

          socket.receive(packet);
          String cmd = new String(packet.getData(),0,4,"UTF-8");
          
          if(cmd.compareTo("ping")==0){
            pong(packet);
          }else if(cmd.compareTo("map ")==0){
            sendMap();
          }else if(cmd.compareTo("kill")==0){
          	new BroadcastNotifer(parentServer,packet,name);
            user.die();
            return;
          }else if(cmd.compareTo("upd ")==0){
          	System.out.println("RECEIVED UPDATE");
          	new BroadcastNotifer(parentServer,packet,name);
          	handler.updatePlayer(packet);
          }else{
          	
          }
          
          
      }catch(SocketTimeoutException e){
          if(!ping())break;
      }catch(IOException e){
      System.out.println("[!] IOException at client reasponder for "+this.name);
      }

  }
    
    socket.close();
    
    System.out.println("[*] client responder for "+this.name+" stoped!");
  }
  
  public void sendMap()
    throws IOException 
  {
  	//if(Game.localServer)return;
  	while(Game.handler.sended<Game.handler.count){
  		byte[] buffer = Game.handler.mapToByte();
  		DatagramPacket packet = new DatagramPacket(buffer,buffer.length,address,port);
  		socket.send(packet);
  	}
  	byte[] buffer = new String("end ").getBytes("UTF-8");
		DatagramPacket packet = new DatagramPacket(buffer,buffer.length,address,port);
		socket.send(packet);  	
  	Game.handler.sended=0;
  }
  
  
  public boolean ping(){
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

	public void sendUpdate(DatagramPacket packet) 
			throws IOException 
	{
		byte[] data = packet.getData();
		DatagramPacket p = new DatagramPacket(data,data.length,address,port);
		socket.send(p);
	}
  
  
}
