
import java.nio.file.Paths;
import java.util.LinkedList;
import java.io.IOException;
import java.nio.file.Files;

public class Main {
	 
  //NEEDs updating with actual awk file

   public static void main(String[] args) {
    String fileContent;// unused for now
    Lexer lexer;
    Token tokens; // unsued for now
    String holy= "I cant f#2!(@8 believe this"; // tester string
    try {
	fileContent = new String (Files.readAllBytes(Paths.get("test.awk"))); // calls method to read everything in file (used)
      lexer = new Lexer(holy); // new lexer object with file bites   
     
      lexer.lex(); // calls lex method to deal with new data
      

      for (Token token : lexer.getTokenList()) {
        String Display = token.ToString();
        System.out.println(Display); // display token information 
      }
      
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Oops Somethng went wrong 1"); // error message
     } catch(IOException e) {
      System.out.println("Oops Something went wrong 2"); // error message
    }
  }
}