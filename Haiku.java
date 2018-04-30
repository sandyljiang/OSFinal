import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.*;

public class Haiku{
  
  
  Random rand = new Random();
  private static int  syllablePointer = 17; //rand.nextInt(17) + 1; //17 is the maximum and the 1 is our minimum 
  private static int targetCount = 17;
  private static HashSet<String> finishedHaiku = new HashSet<String>();
  private static HashSet<String> excludedWords = new HashSet<String>();
  private static Map<Integer, List<String>> wordsList = new LinkedHashMap< Integer, List<String>>();
  
  
  //processing function
  public static String sanitizeInput(String word){
    //remove *,.-() etc
    String cleanWord = word.toLowerCase();
    return cleanWord;
  }
  
  //print finish haiku
  public static String printHaiku(HashSet<String> finishedHaiku){
    StringBuffer sb = new StringBuffer();
    for(String word: finishedHaiku){
      sb.append(word + " ");
    }
    return sb.toString();
  }
  
  public static String getWord(Map<Integer, List<String>> wordsList, int syllableCount){
    String s = "";
    if (wordsList.containsKey(syllableCount)) {
      List<String> arrWords = wordsList.get(syllableCount); //list of words with that syllable count
      Random rand = new Random();
      int wordPtr = rand.nextInt(arrWords.size()) + 1; //random number 
      s = wordsList.get(wordPtr).toString();
    }
    return s;
}

public static void add(Integer key, String newValue) {
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

// read in words and add to hashSet 
public static void fillHashSet(String excludedWordsFile){
  for(int i = 0; i < excludedWordsFile.length(); i++){
    String word = xxx;
    excludedWords.add(word); 
  }
}
/*see if word for count exists
 increment if smaller than target
 decrement if not or if no word exists
 finish when targetCount hit */
public static void main(String args[]){
  //source file input
  int syllableCount;
  String file = args[0];  //this is pseudocode
  for(String word: file){
    sanitizeInput(word);
    syllableCount = 1; //make it the second entry 
    wordsList.put(syllableCount, word);
  }
  while (targetCount - syllablePointer > 0){
    String word = getWord(wordsList, syllablePointer);
    if(word.length() > 0){ //change this so word exists
      finishedHaiku.add(word);
      syllablePointer--;
      targetCount = targetCount - syllablePointer; 
    } 
  }
  String haiku = printHaiku(finishedHaiku);
  //add haiku to buffer
}
}
