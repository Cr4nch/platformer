package platformer.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class BroadcastNotifer extends Thread {
	private String name;
	private Server server;
	private byte[]  packet;
	private boolean forall;
	
	
	BroadcastNotifer(Server server,byte[] packet,String name,boolean forall){
		this.name=name;
		this.server=server;
		this.packet=packet;
		this.forall=forall;
	}
	
	public void run(){
		System.out.println("BroadcastNotifer started!");
		HashMap<String,ClientResponder> clients=new HashMap<String,ClientResponder>();
		clients=server.getClients();
		for(Map.Entry e:clients.entrySet()){
			String nameClient=(String) e.getKey();
			ClientResponder client=(ClientResponder) e.getValue();
			if(((name.compareTo(nameClient)==0)&&(forall==false))==false)
			{
				try{
				System.out.println("Send update to "+client.name);
				client.sendUpdate(packet);
				}catch(IOException event){
					event.printStackTrace();
				}
			}
		}
	}
}
