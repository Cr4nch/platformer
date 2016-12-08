package platformer.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

import platformer.Handler;

public class Server extends Thread{
  private ServerSocket serverSocket;
  private Handler handler;
  private HashMap<String,ClientResponder> clients;
  public static int clientCount=0;
  
  
  
  
  public Server(Handler handler,int port){
    this.handler=handler;
    clients = new HashMap<String,ClientResponder>();
    try{
      serverSocket = new ServerSocket(port);
    }catch(SocketException e){
      System.out.println("[!] Error:Can't start server");
    }catch(IOException e){
    	System.out.println("[!] Error:Can't start server");
    }
  }
  
  
  public void run(){
    System.out.println("[*] Server Started!");
    
    byte[] buffer = new byte[1024];
    try{
      while(true){
      Socket socket = serverSocket.accept();
      //DatagramPacket packet = new DatagramPacket(buffer,buffer.length);
      InputStream in = socket.getInputStream();
      OutputStream out = socket.getOutputStream();
      int size=in.read(buffer);
      if(size<=0)continue;
      String cmd = new String(buffer,0,size,"UTF-8");
      String[] parts = cmd.split(" ");
      if(parts.length==2 && parts[0].compareTo("JOIN")==0){
       // DatagramSocket socket = new DatagramSocket();
       // System.out.println("[*] Joined user "+parts[1]+" from "+packet.getAddress().toString()
       //                      +":"+packet.getPort());
        ClientResponder client = new ClientResponder(socket,parts[1],handler,this);
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
