# jPoker
Five-card stud poker game. Java port of command-line C program. Currently a Java command-line program, GUI under production.

I usually run this from within the NetBeans IDE, but I can run it from a command line this way:

$ cd ~/Poker/src

$ javac -cp . poker/*.java

$ java -cp . poker/poker


- Correctly sorts cards and ranks hands, but there's still a particular combination of cards that occasionally produces a null pointer exception.
