package scanner;
import java.io.*;
/**
 * Scanner is a simple scanner for Compilers and 
 * Interpreters (2021-2022) lab exercise 1
 * @author Rohan Rashingkar
 * @version 9/8/21
 * Usage: Tokenizes an input stream into numbers, identifiers, and operators
 */
public class Scanner
{
    private BufferedReader in;
    private char currentChar;
    private boolean eof;
    /**
     * Scanner constructor for construction of a scanner that 
     * uses an InputStream object for input.  
     * Usage: Constructs a scanner if given an input stream
     * FileInputStream inStream = new FileInputStream(new File(<file name>);
     * Scanner lex = new Scanner(inStream);
     * @param inStream the input stream to use
     */
    public Scanner(InputStream inStream)
    {
        in = new BufferedReader(new InputStreamReader(inStream));
        eof = false;
        getNextChar();
    }
    /**
     * Scanner constructor for constructing a scanner that 
     * scans a given input string.  It sets the end-of-file flag an then reads
     * the first character of the input string 
     * into the instance field currentChar.
     * Usage: Scanner lex = new Scanner(input_string);
     * @param inString the string to scan
     */
    public Scanner(String inString)
    {
        in = new BufferedReader(new StringReader(inString));
        eof = false;
        getNextChar();
    }
    /**
     * Method: main
     * Usage: tests the scanner
     * @param args an array of strings
     * @throw IOException an exception from reading or outputting the file
     * @throw ScanErrorException an exception from scanning
     */
    public static void main (String[] args) 
        throws IOException, ScanErrorException
    {
        FileInputStream inStream = new FileInputStream(
            new File("C:\\Users\\scare\\Downloads\\scannerTestAdvanced.txt"));
        Scanner lex = new Scanner(inStream);
        while (lex.hasNext())
        {
            System.out.println(lex.nextToken());
        }
    }
    /**
     * Method: getNextChar
     * Usage: obtains the next character in the input stream
     */
    private void getNextChar()
    {
        try 
        {
            int temp = in.read();
            currentChar = (char) temp;
            if (temp == -1)
            {
                eof = true;
            }
            if (currentChar == '.')
            {
                eof = true;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    /**
     * Method: eat
     * Usage: moves on to the next character in the input stream
     * @param expected the expected char in the input stream
     */
    
    private void eat(char expected) throws ScanErrorException
    {
        if (expected == currentChar)
        {
            getNextChar();
        }
        else
        {
            throw new ScanErrorException("Illegal character - expected " 
                + currentChar + " and found " + expected + ".");
        }
    }
    /**
     * Method: hasNext
     * Usage: determines if there is another char in the input stream
     * @return true if there is another char otherwise return false
     */
    public boolean hasNext()
    {
        return !eof;
    }
    /**
     * Method: isDigit
     * Usage: determines if a char is a digit
     * @param character the character to test
     * @return true if the character is a digit otherwise return false
     */
    public static boolean isDigit(char character)
    {
        return character >= '0' && character <= '9';
    }
    /**
     * Method: isLetter
     * Usage: determine if a char is a letter
     * @param character the character to test
     * @return true if character is a letter otherwise return false
     */
    public static boolean isLetter(char character)
    {
        String string = String.valueOf(character);
        return string.toLowerCase().compareTo("a") >= 0 
            && string.toLowerCase().compareTo("z") <= 0;
    }
    /**
     * Method: isWhiteSpace
     * Usage: determine if a char is a white space
     * @param character the character to test
     * @return true if character is a white space otherwise return false
     */
    public static boolean isWhiteSpace(char character)
    {
        char[] whiteSpaces = {' ', '\t', '\r', '\n'};
        for (char character1 : whiteSpaces)
        {
            if (character == character1)
                return true;
        }
        return false;
    }
    /**
     * Method: scanNumber - Combines digit chars into a number
     * @return the number parsed by the scanner
     */
    private String scanNumber() throws ScanErrorException
    {
        String number = "";
        while (!eof && isDigit(currentChar))
        {
            number += currentChar;
            eat(currentChar);
        }
        return number;
    }
    /**
     * Method: scanIdentifier
     * Usage: Combines digit and letter chars into an identifier
     * @return the identifier parsed by the scanner
     */
    private String scanIdentifier() throws ScanErrorException
    {
        String identifier = String.valueOf(currentChar);
        eat(currentChar);
        while (!eof && (isDigit(currentChar) || isLetter(currentChar)))
        {
            identifier += currentChar;
            eat(currentChar);
        }
        return identifier;
    }
    /**
     * Method: scanOperand
     * Usage: Parses operands from the input stream
     * @return the parsed operand
     */
    private String scanOperand() throws ScanErrorException
    {
        String operand = String.valueOf(currentChar);
        char[] operands = {'=', '+', '-', '*', '%', '(', ')', ';', ','};
        for (char character : operands)
        {
            if (currentChar == character)
            {
                eat(currentChar);
                return operand;
            }
        }
        if (currentChar == ':')
        {
            eat(currentChar);
            if (currentChar == '=')
            {
                eat(currentChar);
                return ":=";
            }
            return ":";
        }
        if (currentChar == '>' )
        {
            char temp = currentChar;
            eat(currentChar);
            if (currentChar == '=')
            {
                eat(currentChar);
                return String.valueOf(temp) + "=";
            }
            return String.valueOf(temp);
        }
        if (currentChar == '<')
        {
            char temp = currentChar;
            eat(currentChar);
            if (currentChar == '>')
            {
                eat(currentChar);
                return String.valueOf(temp) + ">";
            }
            if (currentChar == '=')
            {
                eat(currentChar);
                return String.valueOf(temp) + "=";
            }
            return String.valueOf(temp);
        }
        if (currentChar == '/')
        {
            eat(currentChar);
            if (currentChar == '/')
            {
                while (currentChar != '\r' || currentChar != '\n')
                {
                    eat(currentChar);
                }
                return String.valueOf(currentChar);
            }
            return "/";
        } 
        else
        {
            //return String.valueOf(currentChar);
            throw new ScanErrorException(
                "Illegal character - not a number, identifier, or operand"); 
        }
    }
    /**
     * Method: nextToken
     * Usage: Obtains the next token in the input stream
     * @return the next token in the input stream
     */
    public String nextToken() throws ScanErrorException
    {
        while (isWhiteSpace(currentChar))
        {
            eat(currentChar);
        }
        if (!hasNext())
        {
            return "END";
        }
        if (isDigit(currentChar))
        {
            return scanNumber();
        }
        else if (isLetter(currentChar))
        {
            return scanIdentifier();
        }
        return scanOperand();
    }  
}
