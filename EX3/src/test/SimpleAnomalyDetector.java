package test;
import java.util.LinkedList;
import java.util.List;

public class SimpleAnomalyDetector implements TimeSeriesAnomalyDetector {
	List<CorrelatedFeatures> normallist= new LinkedList<>();;

	float threshold = (float) 0.9;
	public void setThreshold(float threshold) {
		this.threshold = threshold;
	}
	public float getThreshold() {
		return threshold;
	}
	@Override
	public void learnNormal(TimeSeries ts) {

		LinkedList<String> checkifnot=new LinkedList<>();
		int sizeofrow = ts.getdata().size();
		float[] check1= new float[sizeofrow];
		float[] check2= new float[sizeofrow];
		float pears=0,checkpeas=0;
		int indexcon=0,p=0;
		float dis;

		for (int i=0;i<ts.elements.length-1;i++){
			pears=0;
			check1=ts.getcol(i);
			for (int j=i+1 ;j<ts.elements.length;j++){
				check2=ts.getcol(j);
				checkpeas=Math.abs(StatLib.pearson(check1,check2));
				if (checkpeas>pears){
					pears=checkpeas;
					indexcon=j;

				}
			}//the index of the connection is j
			float maxdis=0;
			if (pears>threshold){
				check2=ts.getcol(indexcon);

				Point[] pointforline=new Point[sizeofrow];
				for (int x=0;x<sizeofrow;x++ ) {
					pointforline[x]= new Point(check1[x],check2[x]);
				}
				Line newline=StatLib.linear_reg(pointforline);


				for (int k=0;k<sizeofrow;k++){

					dis=StatLib.dev(pointforline[k],newline);
					if (dis>maxdis){
						maxdis=dis;
					}
				}
				maxdis=maxdis*(float)1.1;
				//System.out.println(ts.Getelements()[i]+"  "+ts.Getelements()[indexcon]+" "+pears+" "+maxdis);
				normallist.add(new CorrelatedFeatures(ts.Getelements()[i],ts.Getelements()[indexcon],pears,newline,maxdis));

			}

		}



	}

	@Override
	public List<AnomalyReport> detect(TimeSeries ts) {
		List<AnomalyReport> normalrep=new LinkedList<>();
		float dis;
		for (CorrelatedFeatures con:normallist ){
			float[] firstcol= ts.getelementrowbyname(con.feature1);
			float[] seconcol = ts.getelementrowbyname(con.feature2);
			dis=0;
			for (int i =0; i<ts.getdata().size();i++){

				dis=StatLib.dev(new Point(firstcol[i],seconcol[i]),con.lin_reg);

				if (dis > con.threshold) {
					String rep =con.feature1+"-"+con.feature2;
					//System.out.println(rep+ dis+" "+ ts.getrow(i)[0]+" " +i);
					normalrep.add(new AnomalyReport(rep,(long)(i+1)));
				}
			}

		}
		return normalrep;

	}

	public List<CorrelatedFeatures> getNormalModel(){
		return normallist;
	}
}
