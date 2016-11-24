package platformer.net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;

import platformer.Handler;

public class Server extends Thread{
  private DatagramSocket serverSocket;
  private Handler handler;
  private HashMap<String,ClientResponder> clients;
  public static int clientCount=0;
  
  
  
  
  public Server(Handler handler,int port){
    this.handler=handler;
    clients = new HashMap<String,ClientResponder>();
    try{
      serverSocket = new DatagramSocket(port);
    }catch(SocketException e){
      System.out.println("[!] Error:Can't start server");
    }
  }
  
  
  public void run(){
    System.out.println("[*] Server Started!");
    
    byte[] buffer = new byte[1024];
    try{
      while(true){
      DatagramPacket packet = new DatagramPacket(buffer,buffer.length);
      serverSocket.receive(packet);
      String cmd = new String(packet.getData(),"UTF-8");
      String[] parts = cmd.split(" ");
      if(parts.length==2 && parts[0].compareTo("JOIN")==0){
        DatagramSocket socket = new DatagramSocket();
        System.out.println("[*] Joined user "+parts[1]+" from "+packet.getAddress().toString()
                             +":"+packet.getPort());
        ClientResponder client = new ClientResponder(socket,packet,handler,this);
        client.start();
        clients.put(parts[1],client);
        clientCount++;
      }
      }
      
    }catch(Exception e){
      System.out.println("[!] Unknown runtime error!");
    }
    
    System.out.println("[*] Server Stoped!");
  }
  
  
  public HashMap<String,ClientResponder> getClients(){
  	return this.clients;
  }
  
}
