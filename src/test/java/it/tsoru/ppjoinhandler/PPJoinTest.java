package it.tsoru.ppjoinhandler;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javafx.util.Pair;

import org.junit.Test;

/**
 * @author Tommaso Soru <tsoru@informatik.uni-leipzig.de>
 *
 */
public class PPJoinTest {

	private static final String TEST_FILE = "src/test/resources/ppjoin-test.txt";

	private static final double THRESHOLD = 0.7;

	@Test
	public void extractPairsTest() throws IOException {

		List<String> list = Arrays.asList(
				"\"basketball team\"@en <=> \"baseball team\"@en",
				"\"automobile engine\"@en <=> \"automobile\"@en",
				"\"basketball league\"@en <=> \"baseball league\"@en",
				"\"basketball player\"@en <=> \"baseball player\"@en");

		PPJoinHandler h = new PPJoinHandler(THRESHOLD);

		Scanner in = new Scanner(new File(TEST_FILE));
		while (in.hasNextLine()) {
			h.addEntry(in.nextLine());
		}
		in.close();

		List<Pair<String, String>> result = h.run();

		for (Pair<String, String> entry : result) {
			String string = entry.getKey() + " <=> "
					+ entry.getValue();
			System.out.println(string);
			assertTrue(list.contains(string));
		}

	}
}


