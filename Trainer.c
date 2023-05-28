#include "stdio.h"
#include "ctype.h"
#include "stdlib.h"
#include "string.h"

#define MAX_LINE_LENGTH 250

typedef struct Line
{
   char* text;
   int index;
   struct Line* next;
} Line;

// removes leading and trailing whitespace characters
void trim(char *str) {
   // remove leading whitespaces
   char *start = str;
   while (*start && isspace(*start)) {
      start++;
   }
   // remove trailing whitespaces
   char *end = str + strlen(str) - 1;
   while (end > start && isspace(*end)) {
      end--;
   }
   *(end + 1) = '\0';
   // shift trimmed to beginning
   if (str != start) {
      memmove(str, start, end - start + 2);
   }
}

// returns lines as a chain of Line
Line* getLines(int *i, FILE *file)
{
   // allocate lines
   Line* lines = (struct Line*)malloc(sizeof(Line));
   Line* temp = lines;
   char line[MAX_LINE_LENGTH];
   // create linked list of lines
   while (fgets(line, sizeof(line), file) != NULL) {
      // allocate line text
      int lineLength = strlen(line);
      temp->text = (char*)malloc(sizeof(char) * lineLength + 1);
      strcpy(temp->text, line);
      temp->index = (*i)++;
      // allocate next line
      temp->next = (Line*)malloc(sizeof(Line));
      temp = temp->next;
   }
   return lines;
}

// prompt user if they want to continue
int promptContinue(Line* temp, int lastIndex)
{
   printf("\nWant to start where you left off? (y)\n\n");
   Line* temp2 = temp;
   // set temp2 to the Line with the text they missed
   while (temp2->index < lastIndex) temp2 = temp2->next;
   // print rest of solution
   while (temp2 && temp2->next)
   {
      printf("%s", temp2->text);
      temp2 = temp2->next;
   }
   // take user in
   char input[MAX_LINE_LENGTH];
   fgets(input, sizeof(input), stdin);
   int len = strlen(input);
   if (len > 0 && input[len - 1] == '\n') {
      input[len - 1] = '\0';
   }
   // user want to continue
   if (strcmp("y", input) == 0)
   {
      // print only the lines they got right
      system("clear");
      while (temp->index < lastIndex && temp && temp->next)
      {
         printf("%s", temp->text);
         temp = temp->next;
      }

      return 0;
   }
   else
   {
      return 1;
   }
}

void startTrainer(FILE *file)
{
   int i = 0;
   int points = 0;
   Line* lines = getLines(&i, file);
   Line* temp = lines;
   system("clear");
   char input[MAX_LINE_LENGTH];
   while (temp && temp->next)
   {
      // skip lines that are blank
      if (strcmp("\n", temp->text) == 0)
      {
         printf("%s", temp->text);
         temp = temp->next;
         points++;
         continue;
      }
      char ans[MAX_LINE_LENGTH];
      strcpy(ans, temp->text);
      trim(ans);
      // get user input
      fgets(input, sizeof(input), stdin);
      int len = strlen(input);
      if (len > 0 && input[len - 1] == '\n') {
         input[len - 1] = '\0';
      }
      // user is incorrect 
      if (strcmp(input, ans) != 0)
      {
         int lastIndex = temp->index;
         printf("Current Score: %d / %d %d%%\t", points, i, (int)(points / (double)i * 100));
         // prompt user
         if (promptContinue(lines, lastIndex))
         {
            break;
         }

         continue;
      }
      // user is correct
      else
      {
         // print the lines formatted and award point
         system("clear");
         Line* temp2 = lines;
         while (temp2->index <= temp->index && temp2 && temp2->next)
         {
            printf("%s", temp2->text);
            temp2 = temp2->next;
         }
         points++;
      }
      temp = temp->next;
   }
   printf("\n100%% :))\n");
}

int main(int argc, char** argv)
{
   if (argc == 2)
   {
      FILE *file;
      file = fopen(argv[1], "r");
      if (file == NULL)
      {
         printf("File name not provided as a command line argument!\n");
         return 1;
      }
      startTrainer(file);
   }
   else
   {
      printf("File name not provided as a command line argument!\n");
   }
   return 0;
}
