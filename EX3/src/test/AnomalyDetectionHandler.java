package test;


import test.Commands.DefaultIO;
import test.Server.ClientHandler;


import java.io.*;
import java.util.Scanner;

public class AnomalyDetectionHandler implements ClientHandler{

	public class SocketIO implements DefaultIO {

		Scanner in;
		PrintWriter out;
		public SocketIO(PrintWriter outToclient, BufferedReader inFromclient) {
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
		}
		@Override
		public float readVal() throws IOException {
			return in.nextFloat();
		}

		@Override
		public void write(float val) throws IOException {
			out.print(val);
		}

		@Override
		public void getcsv(PrintWriter put) throws IOException {
			String line = readText();
			if (line==""){
				line=readText();
			}
			while(line.compareTo("done")!=0){
				out.write(line+"\n");
				line = readText();
			}
		}

		@Override
		public void upcsv(FileReader in) throws IOException {
			
			BufferedReader upreader= new BufferedReader(in);
			String line = upreader.readLine();
			while((line=upreader.readLine())!=null){
			if (line.compareTo("")==0){
				line=readText();
			}
			out.write(line+"\n");
			}
			upreader.close();
		
		}
	}

	@Override
	public void handel(OutputStream out, InputStream in) {
		//
		BufferedReader inFromclient =new BufferedReader(new InputStreamReader(in));
		PrintWriter outToclient = new PrintWriter(out);
		String line = inFromclient.readLine();
		// SocketIO socIO =new SocketIO(outToclient,inFromclient);
		// CLI cliTest = new CLI(socIO);
		//cliTest.start();
		


			////write to server num
			//do somtings bcuse of the num
		//String line = socIO.write();


	}





}
/