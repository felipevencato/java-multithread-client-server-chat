import java.net.*;
import java.util.*;
import java.io.*;

class ClientConection extends Thread{
	private Server server;
	private Socket socket;
	private DataOutputStream outPut;
	private DataInputStream inPut;
	private String userName;

	public ClientConection (Server s){
		try{
			
			this.server = s;
			this.socket = this.server.getSeverSocket().accept();
			this.outPut = new DataOutputStream(this.socket.getOutputStream());
			this.inPut = new DataInputStream(this.socket.getInputStream());
			this.userName = this.inPut.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Just connected to " + this.socket.getRemoteSocketAddress() + " as " + this.userName);
	}

	public void sendMessage(String message){
		try {
			this.outPut.writeUTF(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public String getUserName(){
		return this.userName;
	}
	public String readMessage(){
		String str = "";
		try {
			str = java.time.LocalTime.now() + "-" + "[" + this.getUserName() + "]" + this.inPut.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}

	@Override
	public void run() {
			System.out.println("Thread Iniciada");
			//nescessário para receber várias mensagens
			while(true) {
				this.server.sendMessages(this.readMessage());
			}
	}
}

public class Server{
	private ServerSocket serverSocket = null;
	private ArrayList <ClientConection> conections = null;

	public Server(int port) throws IOException {
		System.out.println("Iniciando servidor na porta " + port);
		this.serverSocket = new ServerSocket(port);
		this.conections = new ArrayList<ClientConection>();
		System.out.println("Iniciado");
	}

	public ServerSocket getSeverSocket(){
		return this.serverSocket;
	}

	public void AddConection(ClientConection c){
		System.out.println("Waiting for client on port " + this.serverSocket.getLocalPort() + "...");
		this.conections.add(c);
		c.start();
	}

	public void sendMessages(String s){
		for (ClientConection clientConection : conections) {
			clientConection.sendMessage(s);
		}
	}
	
	public static void main(String [] args) {
		int port = Integer.parseInt(args[0]);
		
		try {
			Server s = new Server(port);
			while (true){
				ClientConection c = new ClientConection(s);
				s.AddConection(c);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
