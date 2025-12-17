// Nayana Surisetti
// Period 6
// Fraction Calculator Project

import java.util.*;

// Calculates and prints result of two numbers (fractions, mixed fractions, integers) based on the operator that the user enters.
public class FracCalc {

   // It is best if we have only one console object for input
   public static Scanner console = new Scanner(System.in);
   
   // This main method will loop through user input and then call the
   // correct method to execute the user's request for help, test, or
   // the mathematical operation on fractions. or, quit.
   // DO NOT CHANGE THIS METHOD!!
   public static void main(String[] args) {
   
      // initialize to false so that we start our loop
      boolean done = false;
      
      // When the user types in "quit", we are done.
      while (!done) {
         // prompt the user for input
         String input = getInput();
         
         // special case the "quit" command
         if (input.equalsIgnoreCase("quit")) {
            done = true;
         } else if (!UnitTestRunner.processCommand(input, FracCalc::processCommand)) {
        	   // We allowed the UnitTestRunner to handle the command first.
            // If the UnitTestRunner didn't handled the command, process normally.
            String result = processCommand(input);
            
            // print the result of processing the command
            System.out.println(result);
         }
      }
      
      System.out.println("Goodbye!");
      console.close();
   }

   // Prompt the user with a simple, "Enter: " and get the line of input.
   // Return the full line that the user typed in.
   public static String getInput() {
      // TODO: Implement this method
      System.out.println("Enter: ");
      String input = console.nextLine();
      return input;

   }
   
   // processCommand will process every user command except for "quit".
   // It will return the String that should be printed to the console.
   // This method won't print anything.
   // DO NOT CHANGE THIS METHOD!!!
   public static String processCommand(String input) {

      if (input.equalsIgnoreCase("help")) {
         return provideHelp();
      }
      
      // if the command is not "help", it should be an expression.
      // Of course, this is only if the user is being nice.
      return processExpression(input);
   }
   
   // Lots work for this project is handled in here.
   // Of course, this method will call LOTS of helper methods
   // so that this method can be shorter.
   // This will calculate the expression and RETURN the answer.
   // This will NOT print anything!
   // Input: an expression to be evaluated
   //    Examples: 
   //        1/2 + 1/2
   //        2_1/4 - 0_1/8
   //        1_1/8 * 2
   // Return: the fully reduced mathematical result of the expression
   //    Value is returned as a String. Results using above examples:
   //        1
   //        2 1/8
   //        2 1/4
   public static String processExpression(String input) {
      Scanner parser = new Scanner(input);
      // gets original numbers and operator
      String num1 = parser.next();
      String operator = parser.next();
      String num2 = parser.next();
      // extracts numerator, denominator, and whole
      int n1 = Integer.valueOf(numerator(num1));
      int d1 = Integer.valueOf(denominator(num1));
      int whole1 = Integer.valueOf(whole(num1));
      int n2 = Integer.valueOf(numerator(num2));
      int d2 = Integer.valueOf(denominator(num2));
      int whole2 = Integer.valueOf(whole(num2));
      boolean negative = false;
      int finalN = 0;
      int finalD = 0;
      // normalization
      if(n1 < 0 && d1 < 0){
         n1 *= -1;
         d1 *= -1;
      }
      // checks for mixed fractions
      if(whole1 != 0){
         n1 = mixedNum(whole1, n1, d1, negative);
      }
      if(whole2 != 0){
         n2 = mixedNum(whole2, n2, d2, negative);
      }
      // Checks operator and does math accordingly
      // Multiples straight for multiplication
      if(operator.equals("*")){
         finalN = n1 * n2;
         finalD = d1 * d2;
      // multiplies by reciprocal for division
      } else if(operator.equals("/")){
         finalN = n1 * d2;
         finalD = n2 * d1;
      // makes numbers the same denominator using lcm, adds numerators
      } else if (operator.equals("+")){
         n1 = n1 * (lcm(d1, d2) / d1);
         n2 = n2 * (lcm(d1, d2) / d2);
         finalN = n1 + n2;
         finalD = lcm(d1,d2);
      // makes numbers the same denominator using lcm, subtracts numerators
      } else {
         n1 = n1 * (lcm(d1, d2) / d1);
         n2 = n2 * (lcm(d1, d2) / d2);
         finalN = n1 - n2;
         finalD = lcm(d1,d2);
      }
      // final answer
      return simplify(finalN,finalD);
   }

   // Extracts the numerator from the user input
   // Checks for mixed number and gets token accordingly
   // Parameters:
   //    String num - the complete number that the user inputs
   // Return:
   //    The String value of the numerator
   public static String numerator(String num){
      // Checks for mixed number
      if(num.indexOf("_") != -1){
         return (num.substring((num.indexOf("_") + 1), num.indexOf("/")));
      // Checks for fractions
      } else if (num.indexOf("/") != -1){
         return (num.substring(0,num.indexOf("/")));
      } else {
         return "0";
      }
   }

   // Extracts the denominator from the user input
   // Checks for mixed number and gets token accordingly
   // Parameters:
   //    String num - the complete number that the user inputs
   // Return:
   //    The String value of the denominator
   public static String denominator(String num){
      // Checks for mixed numbers
      if(num.indexOf("_") != -1){
         return (num.substring(num.indexOf("/")+1));
      // Checks for fraction
      } else if (num.indexOf("/") != -1){
         return (num.substring(num.indexOf("/")+1));
      } else {
         return "1";
      }
   }

   // Extracts whole number from the user input
   // Checks for mixed number and gets token accordingly
   // Parameters:
   //    String whole - the complete number that the user inputs
   // Return:
   //    The String value of the whole number
   public static String whole (String num){
      // Checks for mixed number
      if(num.indexOf("_") != -1){
         return (num.substring(0,num.indexOf("_")));
      // Checks for fraction
      } else if (num.indexOf("/") != -1){
         return "0";
      } else {
         return num;
      }
   }

   // Converts mixed number to improper fraction using the formula
   // Parameters:
   //    int whole - the whole number, int num - the numerator, int den - the denominator
   //    boolean negative - checks if the number is negative
   // Return:
   //    The improper fraction
   public static int mixedNum(int whole, int num, int den, boolean negative){
      // checks if number is negative
      if(whole < 0 || num < 0 || den < 0){
         negative = true;
      }
      // Turns all numbers positive to make improper fraction
      whole = Math.abs(whole);
      num = Math.abs(num);
      den = Math.abs(den);

      // Makes the number negative if it is originally negative
      if(negative){
         return (-1 * (den * whole + num));
      } else {
         return (den * whole + num);
      }
   }

   // Gets the Greatest Common Factor of two numbers
   // Parameters -
   // int x - number 1, int y - number 2
   // Return - 
   //    the gcf
   public static int gcf(int x, int y){
      int remainder = 0;
      while(y != 0){
         remainder = x % y;
         x = y;
         y = remainder;
      }
      return x;
   }

   // Simplifies numbers (fractions, mixed numbers, normalization)
   // Parameters -
   //    int num - the numerator, int den - the denominator
   // Return -
   //    returns the simplified answer
   public static String simplify(int num, int den){
      // Simplifies fractions
      int gcf = Math.abs(gcf(num,den));
      num /= gcf;
      den /= gcf;
      // normalization
      if(num < 0 && den < 0){
         num *= -1;
         den *= -1;
      }
      // Checkes for integer
      if(den == 1){
         return num + "";
      } else {
         // Checks for mixed number
         if(Math.abs(num) > Math.abs(den)){
            return ((num/den) + " " + (Math.abs(num%den)) + "/" + den);
         }
         // returns fraction
         return(num + "/" + den);
      }
   }

   // Finds the least common multiple of two numbers
   // Parameters -
   //    int x - number 1, int y - number 2
   // Return -
   //    the lcm
   public static int lcm(int x, int y){
      // Uses the gcf and two numbers to find lcm
      return ((x * y) / gcf(x,y));
   }

   // Returns a string that is helpful to the user about how
   // to use the program. These are instructions to the user.
   public static String provideHelp() {
      // TODO: Update this help text!
     
      String help = "";
      help += "Use the documents on Schoology and helper methods!";
      
      return help;
   }
}