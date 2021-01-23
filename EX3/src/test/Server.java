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
	volatile int clienLimit=0;
	public Server() {
		stop=false;
	}
	private void startServer(int port, ClientHandler ch){
		try {
			ServerSocket server = new ServerSocket(port);
			while (!stop) {
				server.setSoTimeout(1000);
				if(clienLimit==10){	continue;	}
				try{
					Socket client = server.accept();
					InputStream in = client.getInputStream();
					OutputStream out = client.getOutputStream();
					clienLimit++;
					ch.handel(out,in);
					in.close();
					out.close();
					client.close();
					clienLimit--;
					stop();
				}catch(IOException E){}
			}
			server.close();
		}catch (SocketException e){}catch (IOException e1){}
	}
	public void start(int port, ClientHandler ch) {
		new Thread( () -> startServer(port,ch)).start();
	}
	public void stop() {
		stop=true;
	}
}
