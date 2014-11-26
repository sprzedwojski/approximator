import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


public class Regression {

	private double[] p;
	private double[][] points;
	private double[] cpoints;
	private List<Double> Q;
	private int order;
	private double[] Qder;
	
	public Regression(double[][] points, double[] cpoints, int order) {
		Q = new ArrayList<Double>();
		this.points = points;
		this.cpoints = cpoints;
		this.order = order;
		p = new double[order+1];
		Qder = new double[order+1];
		
		initp();
	}
	
	private void initp() {
		for(int i=0; i<p.length; i++) {
			p[i] = Math.random();
		}
	}
	
	/**
	 * Method that triggers the regression calculation.
	 */
	public void doRegression() {

		for(int i=0; i<1000; i++) {
			Q.add(calculateQ());
			
			recalculateParameters();
		} 
		
	}
	
	/**
	 * Method calculating the value of Q.
	 * @return Value of Q
	 */
	private double calculateQ() {
		double sum = 0.0;
		
		for(int i=0; i<points.length; i++) {
			
			double temp = 0.0;
			
			for(int j=0; j<=order; j++) {
				temp += Math.pow(cpoints[i], j) * p[j];
			}
			temp -= points[i][1];
			
			sum += Math.pow(temp, 2);
			
		}
		
		return sum / (2*points.length);
	}
	
	/**
	 * Method calculating the value of the derivative of Q with respect to the given parameter.
	 * @param Index of the parameter.
	 * @return Value of the derivative.
	 */
	private double calculateQder(int which) {
		double sum = 0.0;
		
		for(int i=0; i<points.length; i++) {
			
			sum += (p[0] - points[i][1]) * Math.pow(cpoints[i], which);
			
			for(int j=1; j<=order; j++) {
				sum += Math.pow(cpoints[i], j) * p[j] * Math.pow(cpoints[i], which);
			}
			
		}
		
		Qder[which] = sum / cpoints.length;
		
		return Qder[which];
	}

	private void recalculateParameters() {
		for(int i=0; i < p.length; i++) {
			p[i] -= Data.alpha * calculateQder(i);
		}
	}
	
	public double[] getp() {
		return p;
	}
	
	public double[][] getQ() {
		double[][] qpoints = new double[Q.size()][2];
		
		for(int i=0; i<qpoints.length; i++) {
			qpoints[i][0] = i;
			qpoints[i][1] = Q.get(i);
		}
		
		return qpoints;
	}
	
	public void saveQToFile(int index) {
		try {
			PrintWriter out = new PrintWriter("res//q" + Data.taskNo + "_" + index + ".dat");
			
			for(int i=0; i<Q.size(); i++) {			
				out.println(i + " " + Q.get(i));
			}
			
			out.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
