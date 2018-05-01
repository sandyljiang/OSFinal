import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;


public class Client {
  public static void main(String[] args) throws Exception {
    String fileName = null;
    
    try {
      fileName = args[0];
    } catch (Exception e) {
      //Enter file containing the words and syllable counts
      System.out.println("Enter the name of the file containing the words and syllable counts: ");
      Scanner scanner1 = new Scanner(System.in);
      String file_name1 = scanner1.nextLine();
      
      File file1 = new File(file_name1);
      Socket socket = new Socket("localhost", 3332);
      ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
      ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
      
      //enter the list of words you do not want
      System.out.println("Enter the name of the file containing the words to exclude: ");
      Scanner scanner2 = new Scanner(System.in);
      String file_name2 = scanner2.nextLine();
      File file2 = new File(file_name1);
      
      oos.writeObject(file1.getName());
      oos.writeObject(file2.getName());
      
      //get haiku
      FileInputStream fis = new FileInputStream(file1);
      byte [] buffer = new byte[Server.BUFFER_SIZE];
      Integer bytesRead = 0;
      
      while ((bytesRead = fis.read(buffer)) > 0) {
        oos.writeObject(bytesRead);
        oos.writeObject(Arrays.copyOf(buffer, buffer.length));
      }
      oos.close();
      ois.close();
      fis.close();
      System.exit(0);    
    } 
  }  
}  