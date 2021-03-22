import java.net.*;
import java.io.*;
class Printer extends Thread{
	private DataInputStream in;
	public Printer(DataInputStream i){
			this.in = i;
	}
	@Override
	public void run() {
		
		String response = "";

		while (in != null){
			try {
				response = (String)in.readUTF();
				//if (response.isEmpty())
					System.out.println(response);
			}
			catch(IOException e)
			{
				//System.out.println(e);
			}
		}
	}
}
public class Client{
	private String serverName;
	private int port;
	private String userName;
	private Socket socket;
	private OutputStream outToServer;
	private DataOutputStream outPut;
	private InputStream inFromServer;
	private DataInputStream in;
	private BufferedReader inPut;
	
	public Client(String username, String serverName, int port) {
		try {
			this.userName = username;
			this.serverName = serverName;
			this.port = port;
			System.out.println("Connecting to " + this.serverName + " on port " + this.port);
			this.socket = new Socket(this.serverName, this.port);
			
			System.out.println("Just connected to " + this.socket.getRemoteSocketAddress());

			this.outToServer = this.socket.getOutputStream();
			this.outPut = new DataOutputStream(this.outToServer);

			this.inFromServer = this.socket.getInputStream();
			this.in = new DataInputStream(this.inFromServer);
			
			this.inPut = new BufferedReader(new InputStreamReader(System.in));
			outPut.writeUTF(userName);
			Printer p = new Printer(this.in);
			p.start();
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
	}
	public String ReadMessage(){
		String str = "";
		try
		{
			str = this.inPut.readLine(); 
		} catch (IOException e) {
				e.printStackTrace();
		}
		return str;
	}
	public void SendMessage (String message){
		try
		{
			outPut.writeUTF(message);
		} catch (IOException e) {
				e.printStackTrace();
		}
	}
	
	public static void main(String [] args) {
		Client c = new Client(args[0], args[1],Integer.parseInt(args[2]));
		String message = "";
		while (!message.equals("q")){
				message = c.ReadMessage();
				c.SendMessage(message);
		}
	}
}
