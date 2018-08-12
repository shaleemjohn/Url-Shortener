package application;

import java.util.ArrayList;
import java.util.List;

// for functionality
public class Model {
		
		public String shortenURL(int id) {
			
			String str = "0abAB1cdeCDE2fghFGH3ijkIJK4lmnLMN5opqOPQ6rstRST7uvwUVW8xyzXYZ9";
			
			List<Integer> list = new ArrayList<Integer>();
			
			while(id!=0) {
				list.add(id % 62);
				id /= 62;
			}
			
			String shorturl = "";
			for (int i = list.size() - 1; i >= 0; i--) {
				shorturl += str.charAt(list.get(i));
			}
			
			return shorturl;
			
	}
}