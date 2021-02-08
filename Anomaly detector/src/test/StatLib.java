package test;
public class StatLib {
	// simple average
	public static float avg(float[] x){
		float y=0;
		for(int i=0;i<x.length;i++){
			y+=x[i];
		}
		if (x.length!=0) {
			y = y / x.length;
			return y;
		}
		return 0;
	}
	// returns the variance of X and Y
	public static float var(float[] x){
		if(x.length==0)
			return 0;
		float A=0,B=0;
		for (int i=0;i<x.length;i++){
			A+=(x[i]*x[i]);
		}
		A=A/x.length;
		for (int i=0;i<x.length;i++){
			B+=(x[i]);
		}
		B=B/x.length;
		B=B*B;
		return A-B;
	}
	// returns the covariance of X and Y
	public static float cov(float[] x, float[] y){

		float avX=avg(x);
		float avy=avg(y);
		float totav=0;
		for (int i=0;i<x.length||i<y.length;i++){
			totav+=(x[i]-avX)*(y[i]-avy);
		}
		return (totav/x.length);
	}
	// returns the Pearson correlation coefficient of X and Y
	public static float pearson(float[] x, float[] y){
		float covXY=cov(x,y);
		float varx =var(x);
		float vary =var(y);
		return covXY/((float) (Math.sqrt((double)varx))*(float) (Math.sqrt((double)vary)));
	}
	// performs a linear regression and returns the line equation
	public static Line linear_reg(Point[] points){
		float[] arrayX= new float[points.length];
		float[] arrayY= new float[points.length];

		for (int i=0;i<points.length;i++){
			arrayX[i]=points[i].x;
			arrayY[i]=points[i].y;
		}
		float covpo=cov(arrayX,arrayY);
		float varX=var(arrayX);
		float avrX=avg(arrayX);
		float avrY=avg(arrayY);
		float a=covpo/varX;
		float b=avrY-(a*avrX);
		Line newline=new Line(a,b);
		return newline;
	}
	// returns the deviation between point p and the line equation of the points
	public static float dev(Point p,Point[] points){
		return dev(p,linear_reg(points));
	}
	// returns the deviation between point p and the line
	public static float dev(Point p,Line l){
		float lineY=Math.abs(p.y-(l.a*p.x)-l.b);
		return lineY;
	}
}
