import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Learner {


   private static String fileName;
   private static HashMap<Integer, String> trueLines = new HashMap<Integer, String>();

   public static void start(String fileName) throws FileNotFoundException, IOException, InterruptedException
   {
      Learner.fileName = fileName;
      Learner.loadTrueLines();
      Learner.testUser();
   }

   private static void loadTrueLines() throws FileNotFoundException 
   {
      File file = new File(fileName);
      Scanner fileScanner = new Scanner(file);
      for (int i = 0; fileScanner.hasNextLine(); i++)
      {
         trueLines.put(i, fileScanner.nextLine());
      }
      fileScanner.close();
   }

   public static void clearConsole() throws IOException, InterruptedException {
      if (System.getProperty("os.name").contains("Windows")) {
         new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
      } else {
         new ProcessBuilder("bash", "-c", "clear").inheritIO().start().waitFor();
      }
   }

   private static void printStr(String str, int width)
   {
      int spaces = width - str.length();
      System.out.print(str);
      for (int i = 0; i < spaces; i++) System.out.print(' ');
   }


   private static void testUser() throws FileNotFoundException, IOException, InterruptedException 
   {
      clearConsole();
      Scanner userIn = new Scanner(System.in);
      String trueLine;
      String userLine;
      int maxWidth = 0;
      int trueScore = 0;
      for (int i = 0; i < Learner.trueLines.size(); i++)
      {
         trueLine = Learner.trueLines.get(i);
         maxWidth = Math.max(maxWidth, trueLine.length());
      }
      maxWidth += 50;
      for (int i = 0; i < Learner.trueLines.size(); i++)
      {
         trueLine = Learner.trueLines.get(i);
         userLine = userIn.nextLine();
         if (trueLine.trim().equals(userLine.trim()))
         {
            clearConsole();
            for (int j = 0; j <= i; j++)
            {
               printStr(trueLines.get(j), maxWidth);
               System.out.println(" // :)");
            }
            trueScore += 1;
         }
         else
         {
            clearConsole();
            for (int j = 0; j < i; j++)
            {
               printStr(trueLines.get(j), maxWidth);
               System.out.println(" // :)");
            } 
            printStr("", maxWidth);
            printStr("", maxWidth);
            printStr("", maxWidth);
            for (int j = i; j < Learner.trueLines.size(); j++)
            {
               printStr(trueLines.get(j), maxWidth);
               System.out.println(" // :(");  
            }
            int score = (int)((double) trueScore / trueLines.size() * 100);
            System.out.println("SCORE: " + score + "%");
            System.out.println("Want to start where you left off? (y)");
            userLine = userIn.nextLine();
            if (userLine.trim().equals("y"))
            {
                clearConsole();
                for (int j = 0; j < i; j++)
                {
                printStr(trueLines.get(j), maxWidth);
                System.out.println(" // :)");
                }  
                i--;
            }
            else
            {
                break;
            }
         }
      }
      userIn.close();  
      
   }


   public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException
   {
      if (args.length > 0) 
      {
         Learner.start(args[0]);
      } 
      else 
      {
         System.out.println("File name not provided!");
      }

   }
}
