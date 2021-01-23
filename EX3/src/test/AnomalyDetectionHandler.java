package test;


import test.Commands.DefaultIO;
import test.Server.ClientHandler;


import java.io.*;
import java.net.Socket;
import java.util.Dictionary;
import java.util.Scanner;

public class AnomalyDetectionHandler implements ClientHandler{

	public class SocketIO implements DefaultIO {

		Scanner in;
		PrintWriter out;
		public SocketIO(OutputStream outToclient, InputStream inFromclient) {
			in = new Scanner(inFromclient);
			out = new PrintWriter(outToclient);
		}
		@Override
		public String readText() throws IOException {
			return in.nextLine();
		}
		@Override
		public void write(String text) throws IOException {
			out.print(text);
			out.flush();
		}
		@Override
		public float readVal() throws IOException {
			return in.nextFloat();
		}
		@Override
		public void write(float val) throws IOException {
			out.print(val);
			out.flush();
		}
	}
	@Override
	public void handel(OutputStream out, InputStream in) throws IOException {

		PrintWriter outtosceen= new PrintWriter(out);
		SocketIO socket = new SocketIO(out,in);
		CLI cli = new CLI(socket);
		cli.start();
		cli.dio.write("bye");
	
	}





}
