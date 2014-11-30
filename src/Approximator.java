public class Approximator {
	
	public static void main(String[] args) {
		DataReader dr = new DataReader("res//in" + Data.taskNo + ".txt");
		dr.readData();
		
		int order = 10;
		
		Plotter plotter = new Plotter();
		Plotter plotterQ = new Plotter();
		
		for(int i=1; i<=order; i++) {
			System.out.println("Calculating order " + i + "...");
			
			Regression reg = new Regression(dr.getPoints(), dr.getCPoints(), i);
			reg.doRegression();
			

			plotter.addFunctionToCommand(reg.getp());
			
			reg.saveQToFile(i);
			plotterQ.addQToCommand(i);
		}
		
		plotter.gnuplot(plotter.command);
		plotterQ.gnuplot(plotterQ.qcommand);
	}

}
