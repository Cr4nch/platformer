package platformer.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import platformer.Game;
import platformer.Handler;

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
    
    
    
    
    System.out.println("[*] Client Stoped!");
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
