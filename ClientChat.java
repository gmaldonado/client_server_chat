/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

/**
 *
 * @author gmaldonado
 */
public class ClientChat{
    private final static Scanner scanner = new Scanner(System.in);
    private static int port;
    private static int timeout;
    private String nickname;
    private String ip;
    private boolean connected;
    private static String serverIp;
    private static Thread listening;
    
    public ClientChat(int port, int timeout,String nickname,String ip,String serverIp){
        this.port = port;
        this.timeout = timeout;
        this.nickname = nickname;
        this.ip = ip;
        this.connected = false;
        this.serverIp = serverIp;
        this.listening = new Thread(new RunnableThread());
        
    }
    
    private class RunnableThread implements Runnable{
        Thread runner;
        public RunnableThread(){
            runner = new Thread(this);
            runner.start();
        }
        @Override
        public void run(){
                try{
                    DatagramSocket socket = new DatagramSocket(port);
                    while(true){
                        byte[] buffer = new byte[65507];        			
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet); 
                        String message = new String(packet.getData(),0,packet.getLength());
                        if(message.startsWith("in")){
                            String data[] = message.split("-");
                            String userConnected = data[1];
                            if(!userConnected.equals(nickname)){
                                System.out.println(userConnected+" has connected");
                            }
                        }
                        else if(message.startsWith("out")){
                            String data[] = message.split("-");
                            String userDisconnected = data[1];
                            if(!userDisconnected.equals(nickname)){
                                System.out.println(userDisconnected+" has disconnected");
                            }
                        }
                        else{
                            System.out.println(message);
                            sendAck(packet, socket, "1");
                        }
                    }
                }
                catch(Exception e){
                    System.out.println("Error in listener");
                }
            
        }
    }
    

    
    public void startClient(){
        if(connect()==1){
            System.out.println("Welcome "+nickname);
            while(true){
                try{
                    String line = scanner.nextLine();
                    if(line.equals("disconnect")){
                        sendMessage("disconnect-"+nickname);
                        System.out.println("Bye..");
                        System.exit(0);
                    }
                    else if(line.startsWith("msg")){
                        sendMessage(line+"-"+nickname);
                    }
                    else if(line.equals("users")){
                        String connectedUsers = getConnectedUsers();
                        if(connectedUsers!=null){
                            if(connectedUsers.length()==0){
                                System.out.println("There's no connected users");
                            }
                            else{
                                String data[] = connectedUsers.split("-");
                                if(data[0].equals(nickname) && data.length==1){
                                    System.out.println("You are the only one connected :(");
                                }
                                else{
                                  for(int i=0;i<data.length;i++){
                                      if(!data[i].equals(nickname)){
                                          System.out.println(data[i]);
                                      }
                                  }  
                                }
                                
                            }
                        }
                        else{
                            System.out.println("Error getting connected users");
                        }
                            
                       
                    }


                }
                catch(Exception e){

                }
            
            } 
        }
        else if(connect()==0){
            System.err.println("Another person is using that nickname");
            System.exit(0);
        }
        else{
            System.err.println("Problem connecting, please try again later");
            System.exit(0);
        }

    }

    public String getConnectedUsers(){
        String message="users";
        try {     
            byte[] buffer = message.getBytes();
            DatagramSocket socket = new DatagramSocket();
            DatagramPacket out = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(serverIp), port);
            socket.send(out);
            
            socket.setSoTimeout(timeout);
            buffer = new byte[256];
            out = new DatagramPacket(buffer, buffer.length);
            socket.receive(out);
            
            String ack = new String(out.getData(),0,out.getLength());
            return ack;
            
        } 
        catch(Exception ex){
            return null;
        }
        
    }
    
    public int connect(){
        String message = "connect-"+nickname+"-"+ip;
        try {     
            byte[] buffer = message.getBytes();
            DatagramSocket socket = new DatagramSocket();
            DatagramPacket out = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(serverIp), port);
            socket.send(out);
            
            socket.setSoTimeout(timeout);
            buffer = new byte[256];
            out = new DatagramPacket(buffer, buffer.length);
            socket.receive(out);
            
            String ack = new String(out.getData(),0,out.getLength());
            return Integer.parseInt(ack);
        } 
        catch(Exception ex){
            return -1;
        }
        
    }
    
    public static void sendMessage(String message){
        try {     
            byte[] buffer = message.getBytes();
            DatagramSocket socket = new DatagramSocket();
            DatagramPacket out = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(serverIp), port);
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
        ClientChat client = new ClientChat(10000,5000,"gonzalo","172.17.49.158","172.17.49.158");
        client.startClient();
    }
}
