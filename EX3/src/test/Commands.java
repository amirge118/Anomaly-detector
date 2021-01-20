package test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Commands {

	public interface DefaultIO{
		public String readText() throws IOException;
		public void write(String text)throws IOException;
		public float readVal() throws IOException;
		public void write(float val)throws IOException;
		public void getcsv (PrintWriter put) throws IOException;
		public void upcsv ( BufferedReader in) throws IOException;

	}
	public class StandartIO implements DefaultIO{

		public String readText() throws IOException{
			Scanner input = new Scanner(System.in);
			String text= input.nextLine();
			input.close();
			return text;
		}
		public void write(String text) throws IOException{
			System.out.println(text);
		}
		public float readVal() throws IOException{
			Scanner input = new Scanner(System.in);
			float val= input.nextFloat();
			input.close();
			return val;
		}
		public void write(float val) throws IOException{
			System.out.println(val);
		}
		public void getcsv(PrintWriter out) throws IOException {
			String line = dio.readText();
			if (line==""){
				line=dio.readText();
			}
			while(line.compareTo("done")!=0){
				out.write(line+"\n");
				line = dio.readText();
			}
		}

		@Override
		public void upcsv(BufferedReader in) throws IOException {

		}
	}

	DefaultIO dio;
	public Commands(DefaultIO dio) { this.dio=dio;	}


	private class SharedState{
		float threshold = (float) 0.9;
		TimeSeries traincsv;
		TimeSeries testcsv;
		SimpleAnomalyDetector test = new SimpleAnomalyDetector();
		List<AnomalyReport> normalyrep;
		StandartIO stand = new StandartIO();
		
		public void setThreshold(float threshold) {
			this.threshold = threshold;
		}
		public float getThreshold() {
			return threshold;
		}

	}
	private  SharedState sharedState=new SharedState();

	public abstract class Command{
		protected String description;
		public Command(String description) {
			this.description=description;
		}
		public abstract void execute() throws IOException;
		public abstract void printdes() throws IOException;
		public abstract String getdes();
	}

	public class UploadComman extends Command{
		public UploadComman() {
			super("upload a time series csv file");
		}
		@Override
		public void printdes() throws IOException { sharedState.stand.write(description); }
		@Override
		public  String getdes(){ return description; }
		@Override
		public void execute() throws IOException {
			//server side
			dio.write("Please upload your local train CSV file.\n");
			PrintWriter out =new PrintWriter(new FileWriter("anomalyTrain.csv"));
			sharedState.stand.getcsv(out);
			out.close();
			sharedState.traincsv=new TimeSeries("anomalyTrain.csv");

			dio.write("Upload complete.\n");

			dio.write("Please upload your local test CSV file.\n");
			PrintWriter out1 =new PrintWriter( new FileWriter("anomalyTest.csv"));
			sharedState.stand.getcsv(out1);
			out1.close();
			sharedState.testcsv=new TimeSeries("anomalyTest.csv");
			dio.write("Upload complete.\n");

			//client side
//			Scanner path1 = new Scanner(System.in);
//			String path = path1.nextLine();
//			dio.uplodfile(path);
//			dio.write("done");
		}		
	}
	public class Algoset extends Command{

		public Algoset() {
			super("algorithm settings");
		}
		@Override
		public void printdes() throws IOException {
			dio.write(description);
		}
		@Override
		public  String getdes(){
			return description;
		}
		@Override
		public void execute() throws IOException{

			float thres =sharedState.getThreshold();
			boolean goodinput=true;
			dio.write("The current correlation threshold is "+thres+"\n");
			dio.write("Type a new threshold\n");
			while (goodinput) {
				float newval = dio.readVal();
				if (newval > 0 && newval < 1){
					thres = newval;
					goodinput=false;
				}
				else dio.write("please choose a value betweem 0 and 1\n");
				sharedState.setThreshold(thres);
			}
		}
	}
	public class Detectano extends Command{
		public Detectano() {
			super("detect anomalies");
		}

		@Override
		public void printdes() throws IOException {
			dio.write(description);
		}
		@Override
		public  String getdes(){
			return description;
		}
		@Override
		public void execute() throws IOException{
			sharedState.test.learnNormal(sharedState.traincsv);
			sharedState.normalyrep=sharedState.test.detect(sharedState.testcsv);
			dio.write("anomaly detection complete.\n");
		}
	}
	public class Disres extends Command{

		public Disres() {
			super("display results");
		}

		@Override
		public void printdes() throws IOException {
			dio.write(description);
		}
		@Override
		public  String getdes(){
			return description;
		}
		@Override
		public void execute() throws IOException {
			for (AnomalyReport x:sharedState.normalyrep ) {
				dio.write(x.timeStep+"	"+x.description+"\n");
			}
			dio.write("Done\n");
		}
	}
	public class UplAn extends Command{

		public UplAn() {
			super("upload anomalies and analyze results");
		}

		@Override
		public void printdes() throws IOException {
			dio.write(description+"\n");
		}
		@Override
		public  String getdes(){
			return description;
		}
		@Override
		public void execute() throws IOException {
			int count=0;
			int lentgh=1;
			dio.write("Please upload your local anomalies file\n");
			List<Integer> check = new ArrayList<Integer>();
			for (int i=0;i<sharedState.normalyrep.size()-1;i++) {
				check.add((int) sharedState.normalyrep.get(i).timeStep);
				count++;
				for (int j = i + 1; j < sharedState.normalyrep.size(); j++){
					if (sharedState.normalyrep.get(i).timeStep + 1 == sharedState.normalyrep.get(j).timeStep && sharedState.normalyrep.get(i).description.compareTo(sharedState.normalyrep.get(j).description)==0) {i=j;}
					else {break;}
				}
				check.add((int) sharedState.normalyrep.get(i).timeStep);
			}
			String name= "decfile.txt";
			PrintWriter out =new PrintWriter( new FileWriter(name));
			sharedState.stand.getcsv(out);
			out.close();
			dio.write("upload complete\n");
			int start;
			int end;
			int numcount=0;
			int countP=0;
			List<Integer> filechecklist = new ArrayList<Integer>();
			BufferedReader reader = new BufferedReader(new FileReader(name));
			String line= null;
			while ((line = reader.readLine()) != null) {
				if (line.compareTo("")==0){
					continue;
				}
				String[] linesplite = line.split(",");
				start = Integer.parseInt(linesplite[0]);
				end = Integer.parseInt(linesplite[1]);
				filechecklist.add(start);
				filechecklist.add(end);
				countP++;
				numcount +=end-start+1;
			}
			reader.close();
			int countn= sharedState.traincsv.tabledata.size();
			int countN= countn-numcount;
			int count_TP=0;
			int count_FP=0;
			boolean check_f_t=true;
			for (int i=0;i<check.size();i+=2){
				for (int j=0;j<filechecklist.size();j+=2){
					if (check.get(i)>=filechecklist.get(j)){
						if (check.get(i)<=filechecklist.get(j+1)){
							count_TP++;
							check_f_t=false;
							break;
						}
					}
					if (check.get(i)<filechecklist.get(j)){
						if (check.get(i+1)<=filechecklist.get(j+1)-1){
							count_TP++;
							check_f_t=false;
							break;
						}
					}

				}
				if (check_f_t){
					count_FP++;
				}
				check_f_t=true;
			}
			double TP_rate=(double)count_TP/(double)countP;
			double FA_rate=(double)count_FP/(double)countN;
			TP_rate=(double)((int)(TP_rate*1000))/1000;
			FA_rate=(double)((int)(FA_rate*1000))/1000;
			dio.write("True Positive Rate: "+TP_rate+"\n");
			dio.write("False Positive Rate: "+FA_rate+"\n");


		}
	}
	public class exit extends Command{

		public exit() {
			super("exit");
		}
		@Override
		public void printdes() throws IOException {
			dio.write(description+"\n");
		}
		@Override
		public  String getdes(){
			return description;
		}
		@Override
		public void execute() throws IOException {

		}
	}
		
}
