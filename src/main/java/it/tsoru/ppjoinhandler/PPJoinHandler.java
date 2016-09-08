package it.tsoru.ppjoinhandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

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
	private boolean multithread;

	public PPJoinHandler(double threshold) {
		super();
		this.threshold = threshold;
		this.multithread = false;
	}

	public void addEntry(String entry) {

		if (!dataset.containsValue(entry)) {

			Integer id = dataset.size();
			dataset.put(id, entry);
			String[] tokens = tok.tokenize(entry, false);
			Arrays.sort(tokens);
			stringItems.add(new StringItem(tokens, id));

		}

	}

	public Integer addEntry(String[] tokens) {

		String value = Arrays.toString(tokens);

		// if (!dataset.containsValue(value)) {
		Integer id = dataset.size();
		dataset.put(id, value);
		Arrays.sort(tokens);
		stringItems.add(new StringItem(tokens, id));
		// }

		return id;

	}

	public List<Pair<String, String>> run() {

		StringItem[] strDatum = stringItems.toArray(new StringItem[stringItems
				.size()]);
		Arrays.sort(strDatum);

		ppjoin.setUseSortAtExtractPairs(false);
		ppjoin.setUsePlus(true);

		List<Entry<StringItem, StringItem>> result = ppjoin.extractPairs(
				strDatum, threshold);

		ArrayList<Pair<String, String>> res = new ArrayList<>();
		for (Entry<StringItem, StringItem> entry : result) {
			Pair<String, String> pair = new Pair<String, String>(
					dataset.get(entry.getKey().getId()), dataset.get(entry
							.getValue().getId()));
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
	
	public void setMultithread(boolean mt) {
		this.multithread  = mt;
	}

	public List<Pair<Integer, Integer>> runGetIds() {
		
		StringItem[] strDatum = stringItems.toArray(new StringItem[stringItems
				.size()]);
		Arrays.sort(strDatum);

		ppjoin.setUseSortAtExtractPairs(false);
		ppjoin.setMultithread(multithread);
		
		List<Entry<StringItem, StringItem>> result = ppjoin.extractPairs(
				strDatum, threshold);

		ArrayList<Pair<Integer, Integer>> res = new ArrayList<>();
		for (Entry<StringItem, StringItem> entry : result) {
			Pair<Integer, Integer> pair = new Pair<Integer, Integer>(
					entry.getKey().getId(), entry
							.getValue().getId());
			res.add(pair);
		}

		return res;
	}

}