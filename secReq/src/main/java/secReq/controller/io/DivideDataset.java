package secReq.controller.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DivideDataset {

	private File file;

	public void setDataSet(File file) {
		this.file = file;
	}

	public File getDataSet() {
		return this.file;
	}

	public void divideInto(File[] files) throws IOException {
		// init:
		BufferedWriter[] writers = new BufferedWriter[files.length];
		for (int i = 0; i < files.length; i++) {
			files[i].delete();
			writers[i] = new BufferedWriter(new FileWriter(files[i]));
		}
		int secIndex = 0;
		int nonIndex = 0;

		// devide the data set into several other files:
		BufferedReader r = new BufferedReader(new FileReader(file));
		String l = r.readLine();
		while (l != null) {
			String[] values = l.split(";");
			if ("sec".equals(values[2])) {
				writers[secIndex].write(l);
				writers[secIndex].newLine();
				secIndex = nextInt(secIndex, files.length);
			} else {
				writers[nonIndex].write(l);
				writers[nonIndex].newLine();
				nonIndex = nextInt(nonIndex, files.length);
			}
			l = r.readLine();
		}
		
		// clean up:
		r.close();
		for (BufferedWriter w : writers) {
			w.flush();
			w.close();
		}
	}

	private int nextInt(int current, int max) {
		current++;
		if (current == max)
			current = 0;
		return current;
	}

}
