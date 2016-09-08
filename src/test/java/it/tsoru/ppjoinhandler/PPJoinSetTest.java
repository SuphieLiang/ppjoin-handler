package it.tsoru.ppjoinhandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.system.StreamRDF;
import org.junit.Test;

import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.sparql.core.Quad;

/**
 * @author Tommaso Soru <tsoru@informatik.uni-leipzig.de>
 *
 */
public class PPJoinSetTest {

	private static final String TEST_FILE = "src/test/resources/DBpedia-OfficeHolder.nt";
	
	private static final String TARGET_URI = "http://dbpedia.org/resource/Barack_Obama";

	private static final double THRESHOLD = 0.8;

	@Test
	public void extractPairsTest() throws IOException {

		final HashMap<Integer, String> map = new HashMap<>();
		
		final PPJoinHandler h = new PPJoinHandler(THRESHOLD);
		h.setMultithread(false);
		

		final Cache cache = new Cache();
		
		StreamRDF sink = new StreamRDF() {
			
			@Override
			public void triple(Triple triple) {
				
//				if(triple.getObject().isURI())
//					System.out.println(triple);

				if(cache.s.equals("") || triple.getSubject().getURI().equals(cache.s)) {
					
					if(triple.getObject().isURI())
						cache.o.add(triple.getObject().getURI());
					
					cache.s = triple.getSubject().getURI();
					
				} else {
					
					String[] arr = cache.o.toArray(new String[0]);
					Integer id = h.addEntry(arr);
					map.put(id, cache.s);
//					System.out.println("Added "+cache.s+" => "+cache.o);
					cache.o.clear();
					
					if(triple.getObject().isURI())
						cache.o.add(triple.getObject().getURI());
					
					cache.s = triple.getSubject().getURI();
				}
			}
			
			@Override
			public void start() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void quad(Quad quad) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void prefix(String prefix, String iri) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void finish() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void base(String base) {
				// TODO Auto-generated method stub
				
			}
		};
		
		System.out.println("Parsing...");
		RDFDataMgr.parse(sink, "file://" + System.getProperty("user.dir") + "/" + TEST_FILE);
		
		// add last one
		h.addEntry(cache.o.toArray(new String[0]));
		
		for(double THRESHOLD = 0.6; THRESHOLD>0.4; THRESHOLD -= 0.1) {
			
			h.setThreshold(THRESHOLD);
	
			System.out.println("Pairing with threshold="+THRESHOLD+"...");
			long t0 = System.currentTimeMillis();
			List<Pair<Integer, Integer>> result = h.runGetIds();
			double dt = (double) (System.currentTimeMillis() - t0) / 1000.0;
			System.out.println("Done in "+dt+" seconds.");
			
	
			for (Pair<Integer, Integer> entry : result) {
//				String string = map.get(entry.getKey()) + " <=> "
//						+ map.get(entry.getValue());
				
				if(map.get(entry.getKey()).equals(TARGET_URI) || 
						map.get(entry.getValue()).equals(TARGET_URI))
					System.out.println(map.get(entry.getKey()) + " <=> "
							+ map.get(entry.getValue()));
			}
			
		}

	}
}

class Cache {
	String s = "";
	ArrayList<String> o = new ArrayList<>();
}

