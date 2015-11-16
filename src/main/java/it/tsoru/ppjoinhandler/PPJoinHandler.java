package it.tsoru.ppjoinhandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javafx.util.Pair;
import jp.ndca.similarity.join.PPJoin;
import jp.ndca.similarity.join.StringItem;
import jp.ndca.similarity.join.Tokenizer;

/**
 * @author Tommaso Soru <tsoru@informatik.uni-leipzig.de>
 *
 */
public class PPJoinHandler {

	private HashMap<Integer, String> dataset = new HashMap<>();
	private PPJoin ppjoin = new PPJoin();
	private Tokenizer tok = ppjoin.getTokenizer();
	private List<StringItem> stringItems = new ArrayList<StringItem>();

	private double threshold;

	public PPJoinHandler(double threshold) {
		super();
		this.threshold = threshold;
	}

	public void addEntry(String entry) {

		if (!dataset.containsKey(entry)) {

			Integer id = dataset.size();
			dataset.put(id, entry);
			String[] tokens = tok.tokenize(entry, false);
			Arrays.sort(tokens);
			stringItems.add(new StringItem(tokens, id));

		}

	}

	public List<Pair<String, String>> run() {

		StringItem[] strDatum = stringItems.toArray(new StringItem[stringItems
				.size()]);
		Arrays.sort(strDatum);

		ppjoin.setUseSortAtExtractPairs(false);

		List<Entry<StringItem, StringItem>> result = ppjoin.extractPairs(
				strDatum, threshold);

		ArrayList<Pair<String, String>> res = new ArrayList<>();
		for (Entry<StringItem, StringItem> entry : result) {
			Pair<String, String> pair = new Pair<String, String>(dataset
					.get(entry.getKey().getId()), dataset.get(entry.getValue()
					.getId()));
			res.add(pair);
		}

		return res;

	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

}