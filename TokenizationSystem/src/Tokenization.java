import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;


public class Tokenization
{
	public static void main(String[] args) throws IOException 
	{
		String word;
		String content1;
		String tokenRet;
		HashMap<String, Integer> words = new HashMap<String, Integer>();
		ValueComparator compare =  new ValueComparator(words);
		TreeMap<String,Integer> sorted_words = new TreeMap<String,Integer>(compare);
		int numWords = 0;
		int vocabSize = 0;

		List<String> stopWords = new LinkedList<String>();
		TreeMap<String,Integer> sorted_wordsRemoved = new TreeMap<String,Integer>(compare);
		String stopWordsRemovedRet;


		//Part A Part I implementation of tokenization

		// Set up file reading

		String content = readFile("p2-input-part-B.txt", StandardCharsets.UTF_8);

		//relaces periods with ""
		content1 = content.replace(".", "");


		Scanner wordsIn = new Scanner(content1);
		wordsIn.useDelimiter("[^a-zA-Z0-9]");  // delimiters are nonletters-digits



		while (wordsIn.hasNext())      // while more words to process
		{
			word = wordsIn.next(); 
			word= word.toLowerCase();
			numWords++;


			if (words.containsKey(word))
			{
				// word already in list, just increment frequency
				words.put(word, words.get(word)+1);

			}
			else
			{
				// insert new word into list
				words.put(word, 1);   // set frequency to 1

			}

		}
		//removes blank space as word
		words.remove("");



		//puts map of words into sorted treemap
		sorted_words.putAll(words);
		tokenRet="";
		for (Entry<String, Integer> entry : sorted_words.entrySet()) {
			tokenRet+=entry.getKey()+" : "+entry.getValue();
			tokenRet+="\r\n";
		}
		//writes tokenizer term array to a file
		File report = new File("tokenizerTermArray.txt");
		FileWriter writer = new FileWriter(report,true);
		writer.write(tokenRet);
		writer.flush();


		// Part A Part II

		// deep clone of words hashMap
		HashMap<String, Integer> stopWordsRemoved = new HashMap<String,Integer>();

		for(Entry<String, Integer> entry : words.entrySet()){
			stopWordsRemoved.put(entry.getKey(), entry.getValue());
		}


		//reads in stop words txt file
		String stops = readFile("stopwords.txt", StandardCharsets.UTF_8);
		Scanner stopwordsIn = new Scanner(stops);
		wordsIn.useDelimiter("[^a-zA-Z0-9]");  // delimiters are nonletters-digits

		//puts the stop words into a list
		while (stopwordsIn.hasNext())      // while more words to process
		{
			word = stopwordsIn.next(); 
			word= word.toLowerCase();
			stopWords.add(word);
		}

		//removes stop words from the hashmap stopWordsRemoved

		for(String remove : stopWords){
			if(stopWordsRemoved.containsKey(remove)){
				stopWordsRemoved.remove(remove);
			}
		}


		//puts map of stopWordsRemoved into sorted treemap
		sorted_wordsRemoved.putAll(stopWordsRemoved);
		stopWordsRemovedRet="";
		for (Entry<String, Integer> entry : sorted_wordsRemoved.entrySet()) {
			stopWordsRemovedRet+=entry.getKey()+" : "+entry.getValue();
			stopWordsRemovedRet+="\r\n";
		}
		//writes stopWordsRemoved term array to a file
		File report2 = new File("stopWordsRemovedArray.txt");
		FileWriter writer2 = new FileWriter(report2,true);
		writer2.write(stopWordsRemovedRet);
		writer2.flush();

		//TODO Part A Part III

		HashMap<String, Integer> stemmed = new HashMap<String, Integer>();

		for(Entry<String, Integer> entry : stopWordsRemoved.entrySet()){
			stemmed.put(stem(entry.getKey()), entry.getValue());
		}
 
		HashMap<String, Integer> st = new HashMap<String, Integer>();
		st=sortByValues(stemmed);
		Stack<String> sta = new Stack();
		for (Entry<String, Integer> entry : st.entrySet()) {
			sta.add(entry.getKey()+" : "+entry.getValue());
		}
		
		
		  String stemmedWordsRet = "";
        for(int i = 1; i < sta.size(); i++){
        	  stemmedWordsRet+=sta.pop();
        	  stemmedWordsRet+="\r\n";
        	  if(i==200){break;}
        }
	  
   System.out.println(numWords);
   System.out.println(sta.size());

		//writes stemmedWords term array to a file
		File report3 = new File("stemmedWordsArray.txt");
		FileWriter writer3 = new FileWriter(report3,true);
		writer3.write(stemmedWordsRet);
		writer3.flush();


	} //end of main

	//method for reading file and returning it in string form
	static String readFile(String path, Charset encoding) 
			throws IOException 
			{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
			}

	//Porter Stemming Methods
	//Step 1a
	//- Replace sses by ss (e.g., stresses —> stress).

	static String replaceSSES(String in){
		String ret = in.replace("sses","ss");
		return ret;
	}

	/*- Delete s if the preceding word part contains a vowel not immediately before
	the s (e.g., gaps —> gap but gas —> gas).*/

	static boolean isVowel(char c)
	{
		if(c=='a' || c=='e' || c=='i' || c=='o' ||  c=='u')
		{    
			return true;
		}    
		else
		{
			return false;
		}    
	}

	static String deleteS(String in){
		String st = in;
		char s[]= st.toCharArray();
		boolean vowel = false;
		for(int i = s.length-1; i >= 0 ; i--){
			if(s[i]=='s'){
				if(i>0 && !isVowel(s[i-1])){
					s[i]='\0';
				}	
			}
		}
		String ret  = new String(s);
		return ret;
	}

	/*- Replace ied or ies by i if preceded by more than one letter, otherwise by ie
	(e.g., ties —»tie, cries —» cri).*/
	static String replaceIes(String in){
		int index = 0;
		String ret = in;
		if(in.contains("ied")){
			index  = ret.indexOf("ied");
			if(index > 1){ret = in.replace("ied", "i");}
			if(index == 1){ret = in.replace("ied", "ie");}
		}
		if(ret.contains("ies")){
			index  = ret.indexOf("ies");
			if(index > 1){ret = in.replace("ies", "i");}
			if(index == 1){ret= in.replace("ies", "ie");}
		}
		return ret;
	}

	static String step1aRules(String in){
		String a = replaceSSES(in);
		String b = deleteS(a);
		String ret = replaceIes(b);
		return ret;
	}

	//Step 1b
	/*- Replace eed, eedly by ee if it is in the part of the word after the first nonvowel
      following a vowel (e.g., agreed —> agree, feed —> feed).*/

	static String replaceEed(String in){
		int index = 0;
		String ret = in;
		if(in.contains("eed")){
			index  = ret.indexOf("eed");
			String tmp1 = ret.substring(0, index);
			if(tmp1.contains("a")||tmp1.contains("e")||tmp1.contains("i")||tmp1.contains("o")||tmp1.contains("u")){
				char s[]=tmp1.toCharArray();
				if(!isVowel(s[index-1])){
					ret = in.replace("eed", "ee");
				}
			}
		}
		if(ret.contains("eedly")){
			index  = ret.indexOf("eedly");
			String tmp1 = ret.substring(0, index);
			if(tmp1.contains("a")||tmp1.contains("e")||tmp1.contains("i")||tmp1.contains("o")||tmp1.contains("u")){
				char s[]=tmp1.toCharArray();
				if(!isVowel(s[index-1])){
					ret = in.replace("eedly", "ee");
				}
			}
		}
		return ret;
	}

	/* - Delete ed, edly, ing, ingly if the preceding word part contains a vowel, and
      then if the word ends in at, bl, or iz add e (e.g., fished —> fish, pirating —>
      pirate), or if the word ends with a double letter that is not ll, ss, or zz, remove
      the last letter (e.g., falling^ fall, dripping —> drip), or if the word is short, add
      e (e.g., hoping —» hope).*/

	static String deleteSuffix(String in){
		String ret = in;
		int index = 0;

		if(ret.contains("ed")){

			index  = ret.indexOf("ed");
			String tmp1 = ret.substring(0, index);
			if(tmp1.contains("a")||tmp1.contains("e")||tmp1.contains("i")||tmp1.contains("o")||tmp1.contains("u")){
				char s[]=tmp1.toCharArray();
				if(!isVowel(s[index-1])){
					ret = in.replace("ed", "");
					if(endsWith(ret)){
						ret+="e";
					}
					if(endsWith2(ret)){
						ret = ret.substring(0,ret.length()-1);
					}
					if( ret.length()<=3){
						ret+="e";
					}
				}
			}
		}

		if(ret.contains("edly")){

			index  = ret.indexOf("edly");
			String tmp1 = ret.substring(0, index);
			if(tmp1.contains("a")||tmp1.contains("e")||tmp1.contains("i")||tmp1.contains("o")||tmp1.contains("u")){
				char s[]=tmp1.toCharArray();
				if(!isVowel(s[index-1])){
					ret = in.replace("edly", "");
					if(endsWith(ret)){
						ret+="e";
					}
					if(endsWith2(ret)){
						ret = ret.substring(0,ret.length()-1);
					}
					if( ret.length()<=3){
						ret+="e";
					}
				}
			}
		}

		if(ret.contains("ing")){

			index  = ret.indexOf("ing");
			String tmp1 = ret.substring(0, index);
			if(tmp1.contains("a")||tmp1.contains("e")||tmp1.contains("i")||tmp1.contains("o")||tmp1.contains("u")){
				char s[]=tmp1.toCharArray();
				if(!isVowel(s[index-1])){
					ret = in.replace("ing", "");
					if(endsWith(ret)){
						ret+="e";
					}

					if(endsWith2(ret)){
						ret = ret.substring(0,ret.length()-1);
					}
					if( ret.length()<=3){
						ret+="e";
					}
				}
			}
		}

		if(ret.contains("ingly")){

			index  = ret.indexOf("ingly");
			String tmp1 = ret.substring(0, index);
			if(tmp1.contains("a")||tmp1.contains("e")||tmp1.contains("i")||tmp1.contains("o")||tmp1.contains("u")){
				char s[]=tmp1.toCharArray();
				if(!isVowel(s[index-1])){
					ret = in.replace("ingly", "");
					if(endsWith(ret)){
						ret+="e";
					}
					if(endsWith2(ret)){
						ret = ret.substring(0,ret.length()-1);
					}
					if( ret.length()<=3){
						ret+="e";
					}
				}
			}
		}

		return ret;
	}

	static boolean endsWith(String in){
		char s[]= in.toCharArray();
		int index = s.length-1;
		String c = Character.toString(s[index-1]);
		String d = Character.toString(s[index]);
		String e = c+d;
		if(e.contains("at")||e.contains("bl")||e.contains("iz")){
			return true;
		}
		return false;
	}

	static boolean endsWith2(String in){
		char s[]= in.toCharArray();
		int index = s.length-1;
		String c = Character.toString(s[index-1]);
		String d = Character.toString(s[index]);
		String e = c+d;

		if(e.contains("ll")||e.contains("ss")||e.contains("zz")){
			return false;
		}
		return (s[index-1]==s[index]);

	}

	static String step1bRules(String in){
		String a = replaceEed(in);
		String ret = deleteSuffix(a);
		return ret;
	}

	static String stem(String in){
		String a  = step1aRules(in);
		String ret = step1bRules(a);
		return ret;
	}

	
	//TODO
	private static HashMap sortByValues(HashMap map) { 
	       List list = new LinkedList(map.entrySet());
	       
	       Collections.sort(list, new Comparator() {
	            public int compare(Object o1, Object o2) {
	               return ((Comparable) ((Map.Entry) (o1)).getValue())
	                  .compareTo(((Map.Entry) (o2)).getValue());
	            }
	       });

	      
	       HashMap sortedHashMap = new LinkedHashMap();
	       for (Iterator it = list.iterator(); it.hasNext();) {
	              Map.Entry entry = (Map.Entry) it.next();
	              sortedHashMap.put(entry.getKey(), entry.getValue());
	       } 
	       return sortedHashMap;
	  }
	//TODO
	
	
	
} //end Tokenization class



//comparator class for sorting
class ValueComparator implements Comparator<String> {

	Map<String, Integer> base;
	public ValueComparator(HashMap<String, Integer> words) {
		this.base = words;
	}

	// Note: this comparator imposes orderings that are inconsistent with equals.    
	public int compare(String a, String b) {

		if (base.get(a) >= base.get(b)) {
			return -1;
		} else {
			return 1;
		} // returning 0 would merge keys
	}
}//end comaprator class