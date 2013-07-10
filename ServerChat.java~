


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map.Entry;

public class ServerChat extends Thread{
    
    private static int port;
    private int timeout;
    private String name; 
    private String ip;
    private HashMap<String,String> clients; //nickname,ip

    public ServerChat(int port, int timeout,String ip){
        this.port = port;
        this.timeout = timeout;
        this.name = "Server";
        this.ip = ip;
        this.clients = new HashMap<String, String>();
        start();
    }
    
    public void run(){
        System.out.println("Server running");
        try{
            DatagramSocket socket = new DatagramSocket(port);
            while(true){
                byte[] buffer = new byte[65507];        			
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet); 
                String message = new String(packet.getData(),0,packet.getLength());
                System.out.println(message);
                if(message.startsWith("connect")){
                    String data[] = message.split("-");
                    String nickname = data[1];
                    String userIp = data[2];
                    if(clients.get(nickname)==null){
                        clients.put(nickname,userIp);
                        sendAck(packet, socket,"1");
                        
                        for(Entry<String, String> entry : clients.entrySet()) {
                            if(!entry.getKey().equals(nickname)){
                               sendMessage(entry.getValue(), "in-"+nickname); 
                            }
                            
                        }
                    }
                    else{
                        sendAck(packet, socket,"0");
                    }
                  
                }
                else if(message.startsWith("disconnect")){
                    String data[] = message.split("-");
                    String nickname = data[1];
                    clients.remove(nickname);
                    for(Entry<String, String> entry : clients.entrySet()) {
                        sendMessage(entry.getValue(), "out-"+nickname); 
                    }
                }
                
                else if(message.startsWith("msg")){
                    String data[] = message.split("-");
                    String nickname= data[1];
                    String realMessage = data[2];
                    String sender = data[3];
                    if(clients.get(nickname)==null){
                        System.out.println("fail");
                        sendMessage(clients.get(sender), "Not user connected with that username");
                    }
                    else{
                        //sendMessage(clients.get(nickname),sender+" says: "+realMessage);
                        try {     
                            String toSend = sender+" says: "+realMessage;
                            byte[] buf = toSend.getBytes();
                            DatagramSocket sock = new DatagramSocket();
                            DatagramPacket out = new DatagramPacket(buf, buf.length, InetAddress.getByName(clients.get(nickname)), port);
                            sock.send(out);
                            
                            //waiting for an ACK of message
                            sock.setSoTimeout(timeout);
                            byte[] bufferACK = new byte[256];
                            out = new DatagramPacket(bufferACK, bufferACK.length);
                            sock.receive(out);
                            
                            sendMessage(clients.get(sender),"message received by "+nickname);
                            
                        } 
                        catch(Exception ex){
                            sendMessage(clients.get(sender),nickname+" is not connected anymore, message not sent :(");
                            clients.remove(nickname);
                        }
                    }
                }
                else if(message.equals("users")){
                    String connectedUsers = "";
                    for(Entry<String, String> entry : clients.entrySet()) {
                       connectedUsers += entry.getKey()+"-";
                    }
                    sendAck(packet, socket, connectedUsers);
                }
                
            }
            
        }
        catch (SocketException se) {
              System.out.println("chat error " + se); 

        }
        catch (IOException se) {
              System.out.println("chat error " + se);
        }
    }
    
    public static void sendMessage(String userIp,String message){
        try {     
            byte[] buffer = message.getBytes();
            DatagramSocket socket = new DatagramSocket();
            DatagramPacket out = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(userIp), port);
            socket.send(out);
        } 
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        
    }
    
    public void sendAck(DatagramPacket packet, DatagramSocket socket,String message){
        try {
            byte[] buffer = message.getBytes();
            InetAddress address = packet.getAddress();
            packet = new DatagramPacket(buffer,buffer.length,address,packet.getPort());
            socket.send(packet);
        } 
        catch (IOException ex) {
            System.out.println("chat error"+ex);
        }
    }

    public static void main(String[] args) {
        ServerChat server = new ServerChat(10000,5000,"192.168.2.4");
        
    }
}
