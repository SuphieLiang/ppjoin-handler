package it.tsoru.ppjoinhandler;

/**
 * @author Tommaso Soru <tsoru@informatik.uni-leipzig.de>
 *
 */
public class Pair<S1, S2> {
	
	private S1 s1;
	private S2 s2;

	public Pair(S1 s1, S2 s2) {
		this.s1 = s1;
		this.s2 = s2;
	}

	public S1 getKey() {
		return s1;
	}

	public void setKey(S1 s1) {
		this.s1 = s1;
	}

	public S2 getValue() {
		return s2;
	}

	public void setValue(S2 s2) {
		this.s2 = s2;
	}
	
	
	
}
