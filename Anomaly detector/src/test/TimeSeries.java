package test;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class TimeSeries  {
	String[] elements ;
	LinkedList<float[]> tabledata= new LinkedList<>();
	Boolean haveelements = false;

	float[] getrow(int j){
		int i=0;
		for (float[] x:tabledata) {
			if (j==i) {
				return x;
			}
			i++;

		}
		float[] b=new float[2];
		return b;
	}

	float[] getcol(int j){
		float[] check22= new float[tabledata.size()];
		int i=0;
		for(float[] x:tabledata){
			check22[i++]=x[j];}
		return check22;
	}

	void AddRow(float[] newrow) {
		float[] addrow =  new float[newrow.length];
		for (int i=0;i< newrow.length;i++) {
			addrow[i]=newrow[i];
		}

		//tocheck if is by value or reperens
		tabledata.add(addrow);
	}

	LinkedList<float[]> getdata() {
		return tabledata;
	}

	float getvalbytime(String elment, float time) {
		int index = 0;
		for (int i = 1; i < elements.length; i++) {
			if (elements[i] == elment) {
				index = i;
			}
		}
		for (float[] x : tabledata) {
			if (x[0] == time) {
				return x[index];
			}
		}
		return 0;
	}

	public TimeSeries(String csvFileName) {
		try {
			Boolean checkelement = true;
			ArrayList<String> setrowS = new ArrayList<>();
			ArrayList<Float> setrowF = new ArrayList<Float>();
			BufferedReader reader = new BufferedReader(new FileReader(csvFileName));
			String line= null;

			while ((line = reader.readLine()) != null) {
				Scanner myscan = new Scanner(line);
				if (line.compareTo("")==0){
					if(haveelements){break;}
					else{continue;}
				}
				String[] linesplite = line.split(",");
				if (!haveelements){
					SetElements(linesplite);
					haveelements=true;
				}else{
					float[] beckrowf = new float[linesplite.length];
					for (int i=0;i<linesplite.length;i++) {
						beckrowf[i]=Float.parseFloat(linesplite[i]);
					}
					AddRow(beckrowf);
				}
				myscan.close();
			}
			reader.close();

		} catch (IOException e) {
			return;
		}


	}

	void SetElements(String[] srselementes) {
		int sizeelmentes = srselementes.length;
		elements = new String[sizeelmentes];
		for (int i = 0; i < sizeelmentes; i++) {
			elements[i] = srselementes[i];
		}
		haveelements=true;
	}

	String[] Getelements() {
		return elements; }

	String getelementbyid(int i){
		return elements[i];
	}

	float[] getelementrowbyname(String element){
		for (int i=0;i<elements.length;i++) {
			if (elements[i].compareTo(element)==0){
				return getcol(i);
			}

		}
		return getcol(0);
	}


}