package platformer.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class BroadcastNotifer extends Thread {
	private String name;
	private Server server;
	private DatagramPacket packet;
	
	
	BroadcastNotifer(Server server,DatagramPacket packet,String name){
		this.name=name;
		this.server=server;
		this.packet=packet;
	}
	
	public void run(){
		System.out.println("BroadcastNotifer started!");
		HashMap<String,ClientResponder> clients=new HashMap<String,ClientResponder>();
		clients=server.getClients();
		for(Map.Entry e:clients.entrySet()){
			String nameClient=(String) e.getKey();
			ClientResponder client=(ClientResponder) e.getValue();
			if(name.compareTo(nameClient)!=0){
				try{
				System.out.print("Send update to "+client.name);
				client.sendUpdate(packet);
				}catch(IOException event){
					event.printStackTrace();
				}
			}
		}
	}
}
