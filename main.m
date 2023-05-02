#import <Foundation/Foundation.h>
#import <CoreFoundation/CoreFoundation.h>

int main(int argc, const char * argv[]) {
   @autoreleasepool {

      system("clear");

      if (argc == 2) {

         NSString *filePath = [NSString stringWithUTF8String:argv[1]];

         NSError *error;

         NSString *fileContents = [NSString stringWithContentsOfFile:filePath encoding:NSUTF8StringEncoding error:&error];

         if (fileContents == nil) {

            NSLog(@"Error reading file: %@", error.localizedDescription);

         } else {

            NSArray *lines = [fileContents componentsSeparatedByString:@"\n"];
            NSUInteger lineCount = [lines count] - 1;

            NSUInteger score = 0;

            NSCharacterSet *whitespaceSet = [NSCharacterSet whitespaceCharacterSet];

            for (NSUInteger i = 0; i < lineCount; i++) {

               NSString *line = [lines objectAtIndex:i];

               NSString *lineAnswer = [[line stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]] stringByTrimmingCharactersInSet:[NSCharacterSet newlineCharacterSet]];

               NSString *trimmedLine = [line stringByTrimmingCharactersInSet:whitespaceSet];

               if ([lineAnswer hasPrefix:@"//"]) {
                  NSLog(@"%@\n", line);
                  score++;
                  continue;
               }

               else if ([lineAnswer length] == 0) {
                  NSLog(@"%@\n", line);
                  score++;
                  continue;
               }

               NSDate *currentDate = [NSDate date];
               NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
               [dateFormatter setDateFormat:@"yyyy-MM-dd HH:mm:ss.SSS"];

               NSString *dateString = [dateFormatter stringFromDate:currentDate];

               char *programName = NULL;

               CFBundleRef mainBundle = CFBundleGetMainBundle();

               if (mainBundle) {
                  CFURLRef executableURL = CFBundleCopyExecutableURL(mainBundle);

                  if (executableURL) {
                     char executablePath[PATH_MAX];

                     if (CFURLGetFileSystemRepresentation(executableURL, true, (UInt8 *)executablePath, PATH_MAX)) {
                        programName = strrchr(executablePath, '/') + 1;
                     }

                     CFRelease(executableURL);

                  }
               }

               if (programName) {
                  printf("%s %s[%d:%d] ", [dateString UTF8String],  programName, getpid(), mach_thread_self());
               } else {
                  printf("%s [program][%d:%d] ", [dateString UTF8String], getpid(), mach_thread_self());
               }

               NSString *inputString;

               char buffer[1000];
               fgets(buffer, 1000, stdin);

               inputString = [NSString stringWithUTF8String:buffer];

               NSString *inputAnswer = [[inputString stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]] stringByTrimmingCharactersInSet:[NSCharacterSet newlineCharacterSet]];

               if ([lineAnswer isEqualToString:inputAnswer]) {

                  score++;
                  system("clear");

                  for (NSUInteger iii = 0; iii < i; iii++) {
                     NSString *lineee = [lines objectAtIndex:iii];
                     NSLog(@"%@", lineee);
                  }

                  NSLog(@"%@", line);

               } else {

                  system("clear");

                  for (NSUInteger ii = 0; ii < lineCount; ii++) {
                     NSString *linee = [lines objectAtIndex:ii];
                     NSLog(@"%@", linee);
                  }

                  NSLog(@"Do you want to start where you left off? (y)");

                  char buffer[100];
                  fgets(buffer, 100, stdin);

                  NSString *inputStringg = [NSString stringWithUTF8String:buffer];
                  NSString *inputAnswerr = [[inputStringg stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]] stringByTrimmingCharactersInSet:[NSCharacterSet newlineCharacterSet]];

                  if ([inputAnswerr isEqualToString:@"y"]) {

                     system("clear");

                     for (NSUInteger iii = 0; iii < i; iii++) {
                        NSString *lineee = [lines objectAtIndex:iii];
                        NSLog(@"%@", lineee);
                     }

                     i--;

                     continue;

                  } else {

                     break;

                  }
               }
            }

            double percentage = (double)score / (double)lineCount * 100.0;

            NSLog(@"FINAL SCORE: %.2f%%", percentage);

         }

      } else {

         NSLog(@"The only command line argument is the file name!");

      }

   }
   return 0;
}
