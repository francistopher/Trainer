import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Trainer {

   private static ArrayList<String> trueLines = new ArrayList<String>();
   private static int maxLineLength = -1;

   // prepares and starts training the user
   public static void start(String fileName) {
      loadTrueLines(fileName);
      setMaxLineLength();
      trainUser();
   }

   // get max line length
   private static void setMaxLineLength() {
      for (String trueLine : trueLines) {
         maxLineLength = Math.max(maxLineLength, trueLine.length());
      }
   }

   // loads each line from file in its corresponding number line in a hashmap
   private static void loadTrueLines(String fileName) {
      // create file given file name
      File file = new File(fileName);
      try {
         // scan file
         Scanner fileScanner = new Scanner(file);
         // scan each line
         while (fileScanner.hasNextLine()) {
            // load line into hashmap with it's corresponding number line
            trueLines.add(fileScanner.nextLine());
         }
         fileScanner.close();
      } catch (FileNotFoundException exception) {
         System.out.println("System failed to load file contents!");
      }

   }

   // clears terminal
   public static void clearTerminal() {
      try {
         new ProcessBuilder("bash", "-c", "clear").inheritIO().start().waitFor();
      } catch (IOException | InterruptedException e) {
         System.out.println("System failed to clear the terminal!");
      }
   }

   // displays line given with a // :) or a // :( at the end
   private static void displayLine(String line, boolean correct) {
      System.out.print(line);
      // calculate number spaces for perfect alignment
      int spacesCount = maxLineLength - line.length();
      for (int i = 0; i < spacesCount; i++, System.out.print(" "))
         ;
      if (correct) {
         System.out.println("\t// :)");
      } else {

         System.out.println("\t// :(");
      }
   }

   // prompts user for each line of the file
   private static void trainUser() {
      clearTerminal();
      // scan system input
      Scanner in = new Scanner(System.in);
      // lines to compare
      String trueLine;
      String userLine;
      // use to calculate score
      int userPoints = 0;
      int truePoints = trueLines.size();
      // prompt each line
      int i = 0;
      for (; i < Trainer.trueLines.size(); i++) {
         trueLine = Trainer.trueLines.get(i);
         // skip single line comments
         if (trueLine.contains("//")) {
            userPoints += 1;
            continue;
         }
         // skip blank lines
         else if (trueLine.trim().length() == 0) {
            userPoints += 1;
            continue;
         }
         userLine = in.nextLine();
         // user input equals the true line
         if (trueLine.trim().equals(userLine.trim())) {
            // displays lines correctly matched
            clearTerminal();
            for (int j = 0; j <= i; j++) {
               displayLine(trueLines.get(j), true);

            }
            userPoints += 1;
         }
         // not
         else {
            // displays lines correctly matched
            clearTerminal();
            for (int j = 0; j < i; j++) {
               displayLine(trueLines.get(j), true);
            }
            // displays remaining lines to learn
            for (int j = i; j < Trainer.trueLines.size(); j++) {
               displayLine(trueLines.get(j), false);
            }
            // calculate and print score
            int score = (int) ((double) userPoints / truePoints * 100);
            System.out.println("CURRENT SCORE: " + score + "%");
            // prompts user to resume
            System.out.println("Want to start where you left off? (y)");
            userLine = in.nextLine();
            // user wants to resume
            if (userLine.trim().equals("y")) {
               // displays lines correctly matched
               clearTerminal();
               for (int j = 0; j < i; j++) {
                  displayLine(trueLines.get(j), true);
               }
               // dial back to past state
               i--;
            } else {
               // user gave up :(
               break;
            }
         }

      }
      // if user made it to the end
      if (i == trueLines.size()) {
         System.out.println("100% :)");
      }
      in.close();
   }

   public static void main(String[] args) {
      // user passed in at least one command line argument
      if (args.length > 0) {
         Trainer.start(args[0]);
      }
      // not
      else {
         System.out.println("File name not provided!");
      }

   }
}
