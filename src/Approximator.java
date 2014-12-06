public class Approximator {
	
	public static void main(String[] args) {
		DataReader drTrain = new DataReader("res//" + "train" + Data.taskNo + ".txt");
		drTrain.readData("train");
		
		DataReader drVal = new DataReader("res//" + "validation" + Data.taskNo + ".txt");
		drVal.readData("validation");		
		
		int order = 10;
		
		Plotter plotter = new Plotter("DATA: " + Data.taskNo + " | Regression for training points", true, true);
		Plotter plotterQ = new Plotter("DATA: " + Data.taskNo + " | Error");
		
		
		for(int i=1; i<=order; i++) {
			System.out.println("Calculating order " + i + "...");
			
			Regression reg = new Regression(drTrain.getPoints(), drTrain.getCPoints(),
					drVal.getPoints(), drVal.getCPoints(), i);
			reg.doRegression();
			
			plotter.addFunctionToCommand(reg.getp());
			
			reg.saveAvgQToFile(i);
			reg.saveAvgQValToFile(i);
			
		}
		
		plotter.gnuplot(plotter.command);
		
		plotterQ.addQToCommand();
		plotterQ.gnuplot(plotterQ.qcommand);
		
		Plotter plotterVal = new Plotter("DATA: " + Data.taskNo + " | Regression for validation points for polynomial of order: " + (int)Data.minAvgQVal[0], true, true);
		
		Regression reg = new Regression(drVal.getPoints(), drVal.getCPoints(),
				drVal.getPoints(), drVal.getCPoints(), (int)Data.minAvgQVal[0]);
		reg.doRegression();
		plotterVal.addFunctionToCommand(reg.getp());
		plotterVal.gnuplot(plotterVal.command);
	}

}
