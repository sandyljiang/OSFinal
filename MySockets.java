import java.net.*;
import java.io.*;
import java.io.File;
import java.util.Arrays;
import java.util.Scanner;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.*;
import java.io.*;

class MySockets {
  public static void main (String args[]) {
    new Server().start();
    new Client().start();
  }
}

class Server extends Thread {
  Socket socket = null ;
  ObjectInputStream ois = null;
  ObjectOutputStream oos = null;
  public static Random rand = new Random();
  private static int  syllablePointer = 17; //rand.nextInt(17) + 1; //17 is the maximum and the 1 is our minimum 
  private static int targetCount = 17;
  private static HashSet<String> finishedHaiku = new HashSet<String>();
  private static HashSet<String> excludedWords = new HashSet<String>();
  private static Map<Integer, List<String>> wordsList = new LinkedHashMap< Integer, List<String>>();
  private static File sylfile;
  private static byte[] haiku = new byte[7000];
  
  public void run() {
    try {
      ServerSocket server = new ServerSocket(4444);
      while(true) {
        socket = server.accept();
        ois = new ObjectInputStream(socket.getInputStream());
        File file = (File) ois.readObject();
        File file2 = (File) ois.readObject();
        System.out.println("Server Received: " + file + " , " + file2);
        oos = new ObjectOutputStream(socket.getOutputStream());
        
        //generate haiku here
        oos = new ObjectOutputStream(socket.getOutputStream());
        haiku = Server.generate(wordsList, excludedWords);
        oos.writeObject("Server sent :" + haiku);
        ois.close();
        oos.close();
        socket.close();
      }
    } catch (Exception e) {
    }
  }
  
  public static byte[] generate(Map<Integer, List<String>> wordsList, HashSet<String> excludedWords){
    int syllableCount;
    String line;
    try{
      BufferedReader reader = new BufferedReader(new FileReader(sylfile));
      
      while ((line = reader.readLine()) != null)
      {
        String[] parts = line.split(",", 2);
        if (parts.length >= 2)
        {
          String key = parts[0];
          String value = parts[1];
          sanitizeInput(value);
          Integer k = new Integer(key);
          if(!excludedWords.contains(value)){
            List<String> currentValue = wordsList.get(k);
            if (currentValue == null) {
              currentValue = new ArrayList<String>();
              wordsList.put(k, currentValue);
            }
            currentValue.add(value);
          }
        } 
        else {
          System.out.println("ignoring line: " + line);
        }
      }
      
      reader.close();
      
    }
    catch (FileNotFoundException ex)  
    {
      System.out.println("File doesn't exist");
    }
    catch (IOException e) {
      System.out.println("I/O doesn't exist");
    }
    
    while (targetCount - syllablePointer > 0){
      String word = getWord(wordsList, syllablePointer);
      if(word.length() > 0){
        finishedHaiku.add(word);
        syllablePointer--;
        targetCount = targetCount - syllablePointer; 
      }
      else {
        word = getWord(wordsList, syllablePointer + 1);
      }
    }
    String haiku2 = printHaiku(finishedHaiku);
    haiku = haiku2.getBytes();
    //add haiku to buffer
    return haiku;
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
  
// read in words and add to hashSet 
  public static void fillHashSet(String input){
    String[] words = input.split(",");
    for(String word: words){
      excludedWords.add(word); 
    }
  }
  
  
  
  
  
  
  
  
  
}

class Client extends Thread {
  InetAddress host = null;
  Socket socket = null;
  ObjectOutputStream oos = null;
  ObjectInputStream ois = null;
  String fileName = null;
  
  public void run() {
    try {
      int x = 0;
      // first question
      host = InetAddress.getLocalHost();
      socket = new Socket(host.getHostName(), 4444);
      oos = new ObjectOutputStream(socket.getOutputStream());
      //  this is where we write to server the message
      System.out.println("Enter the name of the file containing the words and syllable counts: ");
      Scanner scanner1 = new Scanner(System.in);
      String file_name1 = scanner1.nextLine();
      File file1 = new File(file_name1);
      oos.writeObject(file1);
      System.out.println("Enter the name of the excluding words list");
      Scanner scanner2 = new Scanner(System.in);
      String file_name2 = scanner2.nextLine();
      File file2 = new File(file_name2);
      oos.writeObject(file2);
      
      ois = new ObjectInputStream(socket.getInputStream());
      
      // this is where we get our input
      String message = (String) ois.readObject();
      System.out.println("Client Received: " + message);
      ois.close();
      oos.close();
      socket.close();
      
      // the haiku
      host = InetAddress.getLocalHost();
      socket = new Socket(host.getHostName(), 4444);
      oos = new ObjectOutputStream(socket.getOutputStream());
      //  this is where we write to server the message
      oos.writeObject("Client Message " + x);
      ois = new ObjectInputStream(socket.getInputStream());
      
      // this is where we get our input
      message = (String) ois.readObject();
      System.out.println("Client Received: " + message);
      
      FileInputStream fis = new FileInputStream(file1);
      byte [] buffer = new byte[7000];
      Integer bytesRead = 0;
      
      while ((bytesRead = fis.read(buffer)) > 0) {
        oos.writeObject(bytesRead);
        oos.writeObject(Arrays.copyOf(buffer, buffer.length));
      }
      ois.close();
      oos.close();
      socket.close();
      
    } catch (Exception e) {
      
    }
  }
}