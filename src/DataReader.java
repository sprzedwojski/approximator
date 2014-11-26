import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;


public class DataReader {
	
	private File file;
	private double[][] points;
	private int linesCount;

	private double[] cpoints;
 	
	public DataReader(String path) {
		file = new File(path);
		try {
			linesCount = this.countLines(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		points = new double[linesCount-2][2];
		
		cpoints = new double[linesCount-2];
	}
	
	/**
	 * Method reading the data from a given file and saving them to the appropriate variables.
	 */
	public void readData() {
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			PrintWriter out = new PrintWriter("res//in" + Data.taskNo + ".dat");
			String line;
			int lineIndex = 0;
			while ((line = br.readLine()) != null) {
			   
			   if(lineIndex == 0) {
				   Data.xStart = Double.parseDouble(line);
			   } else if(lineIndex == 1) {
				   Data.xEnd = Double.parseDouble(line);
			   } else {
				   String[] split = line.split(";");
				   points[lineIndex-2][0] = Double.parseDouble(split[0]);
				   points[lineIndex-2][1] = Double.parseDouble(split[1]);
				   
				   out.println(points[lineIndex-2][0] + " " + points[lineIndex-2][1]);
			   }
			   
			   lineIndex++;
			}
			br.close();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		castPointsToInterval();
	}
	
	public void castPointsToInterval() {
		
		double min = Data.xStart;
		double max = Data.xEnd;
		
		double a=Data.xNormalizedStart, b=Data.xNormalizedEnd;
		
		for(int i=0; i<points.length; i++) {
			cpoints[i] = ((b-a) * (points[i][0] - min))/(max-min) + a; 
		}
	}
	
	/**
	 * Method counting the number of lines in a given text file.
	 * @param filename
	 * @return Number of lines.
	 * @throws IOException
	 */
	private int countLines(String filename) throws IOException {
	    InputStream is = new BufferedInputStream(new FileInputStream(filename));
	    try {
	        byte[] c = new byte[1024];
	        int count = 0;
	        int readChars = 0;
	        boolean empty = true;
	        while ((readChars = is.read(c)) != -1) {
	            empty = false;
	            for (int i = 0; i < readChars; ++i) {
	                if (c[i] == '\n') {
	                    ++count;
	                }
	            }
	        }
	        return (count == 0 && !empty) ? 1 : count;
	    } finally {
	        is.close();
	    }
	}

	public double[][] getPoints() {
		return points;
	}

	public void setPoints(double[][] points) {
		this.points = points;
	}
	
	public double[] getCPoints() {
		return cpoints;
	}
	
}
