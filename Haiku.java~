import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public Class Haiku{
	

	Random rand = new Random();
	int  syllablePointer = 17; //rand.nextInt(17) + 1; //17 is the maximum and the 1 is our minimum 
	int targetCount = 17;
	HashSet<String> finishedHaiku = new HashSet<String>();
	HashSet<String> excludedWords = new HashSet<String>();
	Map<Integer, List<String>> wordsList = new LinkedHashMap< Integer, List<String>>();


	//processing function
	public String sanitizeInput(String word){
		//remove *,.-() etc
		String cleanWord = word.toLowercase();
		return cleanWord;
	}

	//print finish haiku
	public String printHaiku(HashSet<String> finishedHaiku){
		StringBuffer sb = new StringBuffer();
		for(word in finishedHaiku){
			sb.append(word + " ");
		}
		return sb.toString();
	}

	public String getWord(Map<Integer, List<String>> wordsList, syllableCount){
		if (wordsList.containsKey(syllableCount)) {
			Random rand = new Random();
			int wordPtr = rand.nextInt(arr.length) + 1; //length of linky is the maximum and the 1 is our minimum 
			String s = wordsList.get(wordPtr);// change this to get
		}
		return s;
	}

	public void add(Integer key, String newValue) {
		//check that word in source is not in excluded list
		if(!excludedWords.contains(newValue)){
			List<String> currentValue = wordsList.get(key);
			if (currentValue == null) {
				currentValue = new ArrayList<String>();
				wordsList.put(key, currentValue);
			}
			currentValue.add(newValue);
		}
	}

	//add to hashSet 
	public void fillHashSet(String file){
		for(word in excludedWordsFile){
			excludedWords.add(word);	
		}
	}
	/*see if word for count exists
	increment if smaller than target
	decrement if not or if no word exists
	finish when targetCount hit */
	public static void main(String args[]){
		//source file input
		for(word in file){ //this is pseudocode
			sanitizeInput(word);
			wordsList.put(syllableCount, word);
		}
		while (targetCount - syllablePointer > 0){
			String word = getWord(wordsList, syllablePointer);
			if(word.length() > 0){ //change this so word exists
				finishedHaiku.add(word);
				syllablePointer--;
				targetCount - syllablePointer;	
			}	
		}
		String haiku = printHaiku(finishedHaiku);
	//add haiku to buffer
	}
}
