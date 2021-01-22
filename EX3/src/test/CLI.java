package test;

import java.io.IOException;
import java.util.ArrayList;

import org.w3c.dom.css.CSSImportRule;
import test.Commands.Command;
import test.Commands.DefaultIO;

public class CLI {

	ArrayList<Command> commands;
	DefaultIO dio;
	Commands c;
	boolean exitO =true;


	public CLI(DefaultIO dio) {
		this.dio=dio;
		c=new Commands(dio); 
		commands=new ArrayList<>();
		//add all commands
		commands.add(c.new UploadComman());
		commands.add(c.new Algoset());
		commands.add(c.new Detectano());
		commands.add(c.new Disres());
		commands.add(c.new UplAn());
		commands.add(c.new exit());
		// implement
	}
	public void Printmenu(){
		try{
		dio.write("Welcome to the Anomaly Detection Server.\n");
		dio.write("Please choose an option:\n");
		int numcom = 1;
		for (Command x : commands) {
			dio.write(numcom + ". " + x.getdes() + "\n");
			numcom++;
		}
	}catch(IOException E){return;}	
	}
	public void start(){
		try {
			while (exitO) {


				Printmenu();
				int num_to_ex =(int) dio.readVal();
				if (num_to_ex > commands.size() || num_to_ex < 1) {
					dio.write("try another number\n");
					continue;
				}

				commands.get( num_to_ex - 1).execute();
				if (num_to_ex == 6) {
					exitO = false;
				}
			}
		} catch (IOException e){
			return;
		}
	}


}
