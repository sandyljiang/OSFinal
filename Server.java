import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.*;
import java.io.*;

public class Server extends Thread {
  public static final int PORT = 3332;
  public static final int BUFFER_SIZE = 100;
  public static Random rand = new Random();
  private static int  syllablePointer = 17; //rand.nextInt(17) + 1; //17 is the maximum and the 1 is our minimum 
  private static int targetCount = 17;
  private static HashSet<String> finishedHaiku = new HashSet<String>();
  private static HashSet<String> excludedWords = new HashSet<String>();
  private static Map<Integer, List<String>> wordsList = new LinkedHashMap< Integer, List<String>>();
  
  @Override
  public void run() {
    try {
      ServerSocket serverSocket = new ServerSocket(PORT);
      
      while (true) {
        Socket s = serverSocket.accept();
        saveFile(s);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  //processing function
  public static String sanitizeInput(String word){
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
  public static void fillHashSet(String input){
      String[] words = input.split(" ");
      for(String word: words){
        excludedWords.add(word); 
      }
  }
  
  private void saveFile(Socket socket) throws Exception {
    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
    FileOutputStream fos = null;
    byte [] buffer = new byte[BUFFER_SIZE];
    
    // 1. Read file name.
    Object o = ois.readObject();
    
    if (o instanceof String) {
      fos = new FileOutputStream(new File("syllables.txt"));
    } else {
      throwException("Something is wrong");
    }
    
    // 2. Read file to the end.
    Integer bytesRead = 0;
    
    do {
      o = ois.readObject();
      
      if (!(o instanceof Integer)) {
        throwException("Something is wrong");
      }
      
      bytesRead = (Integer)o;
      
      o = ois.readObject();
      
      if (!(o instanceof byte[])) {
        throwException("Something is wrong");
      }
      
      buffer = (byte[])o;
      
      // 3. Write data to output file.
      fos.write(buffer, 0, bytesRead);
      
    } while (bytesRead == BUFFER_SIZE);
    
    System.out.println("Thank you for the list!");
    
    fos.close();
    
    ois.close();
    oos.close();
  }
  
  public static void throwException(String message) throws Exception {
    throw new Exception(message);
  }
  
  public static void main(String[] args) {
    new Server().start();
    int syllableCount;
    String[] file = args[0].split(" ");;  //this is pseudocode
    for(String word: file){
      sanitizeInput(word);
      syllableCount = 1; //make it the second entry 
      Integer syll = new Integer(syllableCount);
      wordsList.add(syll, word);
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