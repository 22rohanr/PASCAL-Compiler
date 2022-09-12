package ast;
import java.util.*;
import environment.*;
import emitter.*;
/**
 * Describes block statements, a series of statements within 
 * a "BEGIN" and "END"
 *
 * @author Rohan Rashingkar
 * @version 12/16/21
 */
public class Block extends Statement
{
    private List<Statement> statements;

    /**
     * Constructor for objects of class Block
     * @param newStatements the list of statements in the block
     */
    public Block(List<Statement> newStatements)
    {
        statements = newStatements;
    }

    /**
     * Runs every statement in the block statement
     * @param  env the environment of execution
     */
    public void exec(Environment env)
    {
        for (Statement s : statements)
        {
            s.exec(env);
        }
    }
    
    @Override
    public void compile(Emitter e)
    {
        for (Statement st : statements)
        {
            st.compile(e);
        }
    }
}
