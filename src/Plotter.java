import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Plotter {
	
	public String getRescaledValueOfX() {
		return "((" + (Data.xNormalizedEnd - Data.xNormalizedStart) +" * (x -(" + Data.xStart + "))) / (" + (Data.xEnd - Data.xStart) + ") +(" + Data.xNormalizedStart + "))";
	}
	
	String command = "plot '" + Data.workspacePath + "/res/in" + Data.taskNo + ".dat' using 1:2 title 'Points'";
	
	String qcommand = "";
	
	public void addFunctionToCommand(double[] p) {
		String function = "";
		for(int i=p.length-1; i>0; i--) {
			function += p[i] + "*" + getRescaledValueOfX() + "**" + i + " + ";
		}
		function += p[0];
		
		command += ",  " + function + " title 'x^" + (p.length-1) + "'";
	}
	
	public void addQToCommand(int order) {
		qcommand = "plot ";
		for(int i=1; i<=order; i++) {
			qcommand += "'" + Data.workspacePath + "/res/q" + Data.taskNo + "_" + i + ".dat' using 1:2 w lines title 'Q " + i + "'";
			if(i!=order)	qcommand+= ",  ";
		}
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
