package ast;
import environment.*;
import java.util.*;
import emitter.*;
/**
 * Describes the overarching program with its various procedure declarations
 * and variable names
 * @author Rohan Rashingkar
 * @version 12/10/21
 */
public class Program extends Statement
{
    private List<ProcedureDeclaration> procedures;
    private Statement statement;
    private List<String> globalVariables;
    /**
     * Constructor for objects of class Program
     * @param procedureList the list of all procedures in the program
     * @param stmnt the statement for this prog
     * @param globVars the list of global variables
     */
    public Program(List<ProcedureDeclaration> procedureList, Statement stmnt, List<String> globVars)
    {
        procedures = procedureList;
        statement = stmnt;
        globalVariables = globVars;
    }

    /**
     * Executes the program with its procedures and statement
     * @param env the environment of execution
     */
    public void exec(Environment env)
    {
        for (ProcedureDeclaration pd : procedures)
        {
            pd.exec(env);
        }
        statement.exec(env);
    }
    
    /**
     * Compiles the program into MIPS code
     * @param fileName the name of the file for the emitter
     */
    public void compile(String fileName)
    {
        Emitter e = new Emitter(fileName);
        e.emit(".data");
        for (String str : globalVariables)
        {
            e.emit("var " + str + ": .word 0\t #creates a variable with default value 0");
        }
        e.emit(".text");
        e.emit(".globl main");
        e.emit("main:");
        statement.compile(e);
        e.emit("li $v0 10");
        e.emit("syscall");
        for (ProcedureDeclaration pd : procedures)
        {
            pd.compile(e);
        }
        
    }
}
