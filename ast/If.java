package ast;
import environment.*;
import emitter.*;

/**
 * Describes a classic if statement
 *
 * @author Rohan Rashingkar
 * @version 11/16/21
 */
public class If extends Statement
{
    private Condition condition;
    private Statement statement;

    /**
     * Constructor for objects of class If
     * @param newCondition the condition of the if statement
     * @param newStatement the statement that will run if the condition is met
     */
    public If(Condition newCondition, Statement newStatement)
    {
        condition = newCondition;
        statement = newStatement;
    }

    /**
     * Runs the full if statement
     *
     * @param env the environment of evaluation
     */
    public void exec(Environment env)
    {
        if (condition.eval(env) == 1)
        {
            statement.exec(env);
        }
    }
    
    @Override
    public void compile(Emitter e)
    {
        String label = "endif" + e.nextLabelID();
        condition.compile(e, label);
        statement.compile(e);
        e.emit(label + ":");
    }
}
