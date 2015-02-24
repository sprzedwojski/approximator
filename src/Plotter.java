import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Plotter {
	String command = "";
	String qcommand = "";
	
	public Plotter(String title, boolean trainingPoints, boolean validationPoints) {
		if(title != null) {
			command += "set title \"" + title + "\"; ";
		}
		command += "plot ";
		if(trainingPoints) {
			command += "'" + Data.workspacePath + "/res/" + "train" + Data.taskNo + ".dat' using 1:2 title 'Training points'";
		}
		if(validationPoints) {
			command += ", '" + Data.workspacePath + "/res/" + "validation" + Data.taskNo + ".dat' using 1:2 title 'Validation points'";
		}
		
	}
	
	public Plotter(String title) {
		if(title != null) {
			qcommand += "set title \"" + title + "\"; ";
		}
	}
	
	public String getRescaledValueOfX() {
		return "((" + (Data.xNormalizedEnd - Data.xNormalizedStart) +" * (x -(" + Data.xStart + "))) / (" + (Data.xEnd - Data.xStart) + ") +(" + Data.xNormalizedStart + "))";
	}
	
	
	public void addFunctionToCommand(double[] p, double lambda) {
		String function = "";
		for(int i=p.length-1; i>0; i--) {
			function += p[i] + "*" + getRescaledValueOfX() + "**" + i + " + ";
		}
		function += p[0];
		
		command += ",  " + function + " title 'lambda " + lambda + "'";
	}
	
	public void addQToCommand() {
//		qcommand += "set xtics ( \"10^-6\" 0, \"10^-5\" 1, \"10^-4\" 2, \"10^-3\" 3,"
//				+ " \"10^-2\" 4, \"10^-1\" 5, \"10^0\" 6, \"10^1\" 7,"
//				+ "\"10^2\" 8, \"10^3\" 9, \"10^4\" 10);";
//		qcommand += "set xtics 5; set format x '%';";		
		
//		qcommand += "set xtics ( \"10^-6\" 0, \"10^-4\" 2,"
//		+ " \"10^-2\" 4, \"10^0\" 6,"
//		+ " \"10^2\" 8, \"10^4\" 10);";
		
		qcommand += "plot ";
		qcommand += "'" + Data.workspacePath + "/res/q" + Data.taskNo + ".dat' using 1:2 w points title 'Q training',"
				+ "'" + Data.workspacePath + "/res/qVal" + Data.taskNo + ".dat' using 1:2 w points title 'Q validation'";
		qcommand+= ",  ";
	}
	
	public void gnuplot(final String com) {
		System.out.println("Command: " + com);
		Runnable task = new Runnable() {
			@Override
			public void run() {
				try {
					String[] call = { "/usr/bin/gnuplot", "--persist", "-e", com };
					Runtime rt = Runtime.getRuntime();
					Process proc = rt.exec(call);
					InputStream stdin = proc.getErrorStream();
					InputStreamReader isr = new InputStreamReader(stdin);
					BufferedReader br = new BufferedReader(isr);
					String line = null;
					while ((line = br.readLine()) != null)
						System.err.println("gnuplot:" + line);
					int exitVal = proc.waitFor();
					if (exitVal != 0)
						System.err.println("gnuplot Process exitValue: "
								+ exitVal);
					proc.getInputStream().close();
					proc.getOutputStream().close();
					proc.getErrorStream().close();
				} catch (Exception e) {
					System.err.println("Fail: " + e);
				}
			}
		};
		
		task.run();
	}
}
