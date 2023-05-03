import sys
import os


class Trainer:

    def __init__(self) -> None:
        self.trueLines = []
        self.maxLineLen = -1

    # starts training user
    def start(self, fileName) -> None:
        self.__loadTrueLines(fileName=fileName)
        self.__setMaxLineLength()
        self.__trainuser()

    # load lines
    def __loadTrueLines(self, fileName) -> None:
        try:
            with open(fileName, 'r') as f:
                self.trueLines = [line for line in f.readlines()]
        except Exception:
            print("System failed to load file contents!")

    # find longest line length
    def __setMaxLineLength(self) -> None:
        for trueLine in self.trueLines:
            self.maxLineLen = max(self.maxLineLen, len(trueLine))

    # display line with status
    def __displayLine(self, line, correct) -> None:
        print(line.rstrip("\n"), end=" ")
        # calc num of spaces for perfect alignment
        spacesCount = self.maxLineLen - len(line)
        for i in range(0, spacesCount):
            print(end=" ")
        if correct:
            print("\t// :)")
        else:
            print("\t// :(")

    # start prompting user
    def __trainuser(self) -> None:
        os.system("clear")
        # lines to compare
        trueLine = ''
        userLine = ''
        # for calculating score
        userPoints = 0
        truePoints = len(self.trueLines)
        # prompt each line
        i = 0
        while i < len(self.trueLines):

            trueLine = self.trueLines[i]
            # skip single line comments
            if "//" in trueLine:
                userPoints += 1
                continue
            # skip blank lines
            elif len(trueLine.strip()) == 0:
                userPoints += 1
                continue

            userLine = input()
            # user input equals true line
            if trueLine.strip() == userLine.strip():
                os.system("clear")
                j = 0
                # display lines correctly matched
                while j <= i:
                    self.__displayLine(self.trueLines[j], True)
                    j += 1
                userPoints += 1
                # not
            else:
                os.system("clear")
                j = 0
                # display lines correctly matched
                while j < i:
                    self.__displayLine(self.trueLines[j], True)
                    j += 1
                j = i
                # display remaining lines to learn
                while j < len(self.trueLines):
                    self.__displayLine(self.trueLines[j], False)
                    j += 1

                # calculate and print score
                score = userPoints / truePoints * 100
                print("CURRENT SCORE: ", round(score, 2), "%")
                # prompt user to resume
                print("Want to start where you left off? (y)")
                userLine = input()
                # user wantss to resume
                if userLine.strip() is 'y':
                    os.system("clear")
                    j = 0
                    while j < i:
                        self.__displayLine(self.trueLines[j], True)
                        j += 1
                    i -= 1
                # user gave up :(
                else:
                    break

            i += 1
        # user made it :)
        if i == len(self.trueLines):
            print("100% :)")


if __name__ == "__main__":
    args = sys.argv
    # user passed in at least one command line argument
    if len(args) > 1:
        t = Trainer()
        t.start(args[1])
    # not
    else:
        print("File name not provided!")
