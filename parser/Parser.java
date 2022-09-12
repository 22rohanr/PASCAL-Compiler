package parser;
import scanner.*;
import java.io.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import ast.*;
import environment.*;
import emitter.*;
/**
 * The Parser analyzes the tokens from the Scanner and ensures 
 * that it forms logical syntax according to certain grammars
 * @author Rohan Rashingkar
 * @version 10/29/21
 */
public class Parser
{
    private Scanner scanner;
    private String currentToken;
    private Map<String, Integer> variables;
    /**
     * Constructor for objects of class Parser
     * @param sc a scanner that will be used with this parser
     */
    public Parser(Scanner sc) throws ScanErrorException
    {
        scanner = sc;
        currentToken = sc.nextToken();
        variables = new HashMap<>();
    }
    /**
     * Method: main
     * Usage: tests the parser
     * @param args a rudimentary array of strings
     * @throw IOException an exception from reading or outputting the file
     * @throw ScanErrorException an exception from scanning
     */
    public static void main (String[] args) throws ScanErrorException, IOException
    {
        FileInputStream inStream = new FileInputStream(
            new File("C:\\Users\\scare\\Downloads\\dummytester.txt"));
        Scanner sc = new Scanner(inStream);
        Parser p = new Parser(sc);
        Environment e = new Environment();
        Program pr = p.parseProgram();
        pr.compile("dummytest.txt");
        System.out.println("Done");
    }
    /**
     * Method: eat
     * Usage: Moves on to the next token if it matches an expected input
     * @param expected the expected current token
     */
    private void eat(String expected) throws ScanErrorException
    {
        if (expected.equals(currentToken))
        {
            currentToken = scanner.nextToken();
        }
        else 
            throw new IllegalArgumentException("Illegal character - expected " 
                + currentToken + " and found " + expected + ".");
    }
    
    /**
     * Method: parseNumber
     * Usage: Parses the current integer token
     * precondition: current token is an integer
     * postcondition: number token has been eaten
     * @return the value of the parsed integer
     */
    private ast.Number parseNumber() throws ScanErrorException
    {
        int num = Integer.parseInt(currentToken);
        eat(currentToken);
        return new ast.Number(num);
    }
    
    /**
     * Method: parseStatement
     * Usage: Parses the current statement tokens
     * precondition: current token is the beginning of a statement
     * postcondition: statement tokens have been eaten and printed
     */
    public Statement parseStatement() throws ScanErrorException
    {
        if (currentToken.equals("WRITELN"))
        {
            eat("WRITELN");
            eat("(");
            Expression exp = parseExpression();
            eat(")");
            eat(";");
            return new Writeln(exp);
        }
        else if (currentToken.equals("."))
        {    
            return null;
        }
        else if (currentToken.equals("BEGIN"))
        {
            eat("BEGIN");
            List<Statement> statements = new ArrayList<Statement>();
            while (! currentToken.equals("END"))
            {
               statements.add(parseStatement());
            }
            eat("END");
            eat(";");
            return new Block(statements);
        }
        else if (currentToken.equals("IF"))
        {
            eat("IF");
            Expression exp1 = parseExpression();
            String temp = currentToken;
            eat(currentToken);
            Condition condition = new Condition(temp,exp1,parseExpression());
            eat("THEN");
            return new If(condition, parseStatement());
        }
        else if (currentToken.equals("WHILE"))
        {
            eat("WHILE");
            Expression exp1 = parseExpression();
            String temp = currentToken;
            eat(currentToken);
            Condition condition = new Condition(temp,exp1,parseExpression());
            eat("DO");
            return new While(condition, parseStatement());
        }
        else
        {
            String temp = currentToken;
            eat(currentToken);
            eat(":=");
            Expression tempExp = parseExpression();
            eat(";");
            return new Assignment(temp, tempExp);
        }
    }
    
    /**
     * Method: parseFactor
     * Usage: Parses the current factor, a mini integer expression 
     * with (,), or -
     * @return the value of the parsed factor
     */
    private Expression parseFactor() throws ScanErrorException
    {
        if (currentToken.equals("("))
        {
            eat("(");
            Expression temp = parseExpression();
            eat(")");
            return temp;
        }
        else if (currentToken.equals("-"))
        {
            eat("-");
            return new BinOp("*", new ast.Number(-1), parseFactor());
        }
        else
        {
            try
            {
                return parseNumber();
            }
            catch (NumberFormatException e)
            {
               String temp = currentToken;
               eat(currentToken); 
               if (currentToken.equals("("))
               {
                   eat("(");
                   List<Expression> parameters = new ArrayList<>();
                   while (!currentToken.equals(")"))
                   {
                       parameters.add(parseExpression());
                       if (currentToken.equals(","))
                       {
                           eat(",");
                       }
                   }
                   eat(")");
                   return new ProcedureCall(temp, parameters);
               }
               return new Variable(temp);
            }
        }
    }
    
    /**
     * Method: parseTerm
     * Usage: Parses the current term, a mini expression with 
     * integers and * or /
     * @return the value of the parsed term
     */
    private Expression parseTerm() throws ScanErrorException
    {
        Expression factor = parseFactor();
        while (currentToken.equals("*") || currentToken.equals("/"))
        {
            String temp = currentToken;
            eat(currentToken);
            if (temp.equals("*"))
            {
                factor = new BinOp("*", factor, parseFactor());
            }
            if (temp.equals("/"))
            {
                factor = new BinOp("/", factor, parseFactor());
            }
        }
        return factor;
    }
   
    /**
     * Method: parseExpression
     * Usage: Parses the current expression
     * @return the value of the parsed expression
     */
    private Expression parseExpression() throws ScanErrorException
    {
        Expression term = parseTerm();
        while (currentToken.equals("+") || currentToken.equals("-"))
        {
            String temp = currentToken;
            eat(currentToken);
            if (temp.equals("+"))
            {
                term = new BinOp("+", term, parseTerm());
            }
            if (temp.equals("-"))
            {
                term = new BinOp("-", term, parseTerm());
            }
        }
        return term;
    }
    
    /**
     * Method: parseProgram
     * Usage: Parses the current program with its procedure 
     * declarations, parameters, and statements
     * @return the program with its procedures and correct name
     */
    public Program parseProgram() throws ScanErrorException
    {
        List<String> variables = new ArrayList<>();
        while (currentToken.equals("VAR"))
        {
            eat("VAR");
            variables.add(currentToken);
            eat(currentToken);
            while (!currentToken.equals(";"))
            {
                eat(",");
                variables.add(currentToken);
                eat(currentToken);
            }
            eat(";");
        }
        
        List<ProcedureDeclaration> procedures = new ArrayList<>();
        while (currentToken.equals("PROCEDURE"))
        {
            eat("PROCEDURE");
            String temp = currentToken;
            eat(currentToken);
            eat("(");
            List<String> parameters = new ArrayList<>();
            while (!currentToken.equals(")"))
            {
                parameters.add(currentToken);
                eat(currentToken);
                if (currentToken.equals(","))
                {
                    eat(",");
                }
            }
            eat(")");
            eat(";");
            
            while (currentToken.equals("VAR"))
            {
                eat("VAR");
                variables.add(currentToken);
                eat(currentToken);
                while (!currentToken.equals(";"))
                {
                    eat(",");
                    variables.add(currentToken);
                    eat(currentToken);
                }
                eat(";");
            }
            Statement statement = parseStatement();
            ProcedureDeclaration dec = new ProcedureDeclaration(temp, statement, parameters);
            procedures.add(dec);
        }
        Statement newStatement = parseStatement();
        return new Program(procedures, newStatement, variables);
    }
}