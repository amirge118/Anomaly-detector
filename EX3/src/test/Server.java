package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server {

	public interface ClientHandler {

		public void handel(OutputStream out, InputStream in) throws IOException;
	}

	volatile boolean stop;
	int clienLimit=10;
	public Server() {
		stop=false;
	}
	
	
	private void startServer(int port, ClientHandler ch){
		// implement here the server...
		try {
			ServerSocket server = new ServerSocket(port);
			while (!stop) {
				server.setSoTimeout(1000);
				Socket client = server.accept();

				InputStream in = client.getInputStream();
				OutputStream out = client.getOutputStream();

				ch.handel(out,in);

				in.close();
				out.close();
				client.close();
				stop();
				System.out.println("system stoped");

			}
		}catch (SocketException e){}catch (IOException e1){}
	}
	
	// runs the server in its own thread
	public void start(int port, ClientHandler ch) {
		System.out.println("start new tread");
		new Thread(()->startServer(port,ch)).start();
	}
	
	public void stop() {
		stop=true;
	}
}
