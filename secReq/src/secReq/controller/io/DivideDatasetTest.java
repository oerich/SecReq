package secReq.controller.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.junit.Test;

public class DivideDatasetTest {

	@Test
	public void test() throws IOException {
		DivideDataset dd = new DivideDataset();

		File file = new File("testfiles/homeNetwork-complete.csv");
		dd.setDataSet(file);

		// make sure the computation works and the testfile is correct:
		assertEquals(file, dd.getDataSet());
		assertEquals(112, length(file));
		assertEquals(42, secReqs(file));
		List<String> ids = new Vector<String>(112);
		for (String s:ids(file)){
			assertFalse("Duplicate ID: " + s, ids.contains(s));
			ids.add(s);
		}
		ids.clear();

		File fileA = new File("testfiles/homeNetwork-A.csv");
		File fileB = new File("testfiles/homeNetwork-B.csv");
		File fileC = new File("testfiles/homeNetwork-C.csv");

		dd.divideInto(new File[] { fileA, fileB, fileC });
		
		assertEquals(38, length(fileA));
		assertEquals(14, secReqs(fileA));
		assertEquals(37, length(fileB));
		assertEquals(14, secReqs(fileB));
		assertEquals(37, length(fileC));
		assertEquals(14, secReqs(fileC));
		
		for (String s:ids(fileA)){
			assertFalse("Duplicate ID in A: " + s, ids.contains(s));
			ids.add(s);
		}		
		for (String s:ids(fileB)){
			assertFalse("Duplicate ID in B: " + s, ids.contains(s));
			ids.add(s);
		}		
		for (String s:ids(fileC)){
			assertFalse("Duplicate ID in C: " + s, ids.contains(s));
			ids.add(s);
		}		
	}

	private int length(File file) throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(file));
		String l = r.readLine();
		int ret = 0;
		while (l != null) {
			ret++;
			l = r.readLine();
		}
		r.close();
		return ret;
	}

	private int secReqs(File file) throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(file));
		String l = r.readLine();
		int ret = 0;
		while (l != null) {
			if ("sec".equals(l.split(";")[2]))
				ret++;
			l = r.readLine();
		}
		r.close();
		return ret;
	}
	
	private List<String> ids(File file) throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(file));
		String l = r.readLine();
		List<String> ret = new LinkedList<String>();
		while (l != null) {
			ret.add(l.split(";")[0]);
			l = r.readLine();
		}
		r.close();
		return ret;		
	}
}
