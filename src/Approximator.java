public class Approximator {
	
	public static void main(String[] args) {
		DataReader drTrain = new DataReader("res//" + "train" + Data.taskNo + ".txt");
		drTrain.readData("train");
		
		DataReader drVal = new DataReader("res//" + "validation" + Data.taskNo + ".txt");
		drVal.readData("validation");		
		
		int order = 10;
		double[] lambdas = {0.000001, 0.00001, 0.0001, 0.001, 0.01, 0.1, 1, 10, 100, 1000, 10000};
		
		Plotter plotter = new Plotter("DATA: " + Data.taskNo + " | Regression for training points", true, true);
		Plotter plotterQ = new Plotter("DATA: " + Data.taskNo + " | Error");
		
		double[][] points = new double[lambdas.length][order];
		for(int i=0; i<lambdas.length; i++) {
			System.out.println("Calculating order " + i + "...");
			
			Regression reg = new Regression(drTrain.getPoints(), drTrain.getCPoints(),
					drVal.getPoints(), drVal.getCPoints(), order, lambdas[i]);
			reg.doRegression();
			
			plotter.addFunctionToCommand(reg.getp(), lambdas[i]);
			
			reg.saveAvgQToFile(i);
			reg.saveAvgQValToFile(i);	
			
			points[i] = reg.getp();
		}
		
		plotter.gnuplot(plotter.command);
		
		plotterQ.addQToCommand();
		plotterQ.gnuplot(plotterQ.qcommand);
		
		Plotter plotterVal = new Plotter("DATA: " + Data.taskNo + " | Regression for validation points for lambda: " + lambdas[(int)Data.minAvgQVal[0]], true, true);
		
		/*Regression reg = new Regression(drVal.getPoints(), drVal.getCPoints(),
				drVal.getPoints(), drVal.getCPoints(), order, lambdas[(int)Data.minAvgQVal[0]]);
		reg.doRegression();*/
		plotterVal.addFunctionToCommand(points[(int)Data.minAvgQVal[0]], lambdas[(int)Data.minAvgQVal[0]]);
		plotterVal.gnuplot(plotterVal.command);
	}

}
