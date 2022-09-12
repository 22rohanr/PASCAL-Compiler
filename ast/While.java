
package ast;
import environment.*;
import emitter.*;
/**
 * Defines the while loop statement
 *
 * @author Rohan Rashingkar
 * @version 11/16/21
 */
public class While extends Statement
{
    private Condition condition;
    private Statement statement;

    /**
     * Constructor for objects of class While
     * @param newCondition the condition of the if statement
     * @param newStatement the statement that will repeatedly run
     * if the condition is met
     */
    public While(Condition newCondition, Statement newStatement)
    {
        condition = newCondition;
        statement = newStatement;
    }

    /**
     * Runs the full while statement
     *
     * @param env the environment of evaluation
     */
    public void exec(Environment env)
    {
        while (condition.eval(env) == 1)
        {
            statement.exec(env);
        }
    }
    /**
     * Compiles the program into MIPS code
     * @param e the emitter for this compiling method
     */
    public void compile(Emitter e)
    {
	String whileLabel = "while" + e.nextLabelID();
	String endifLabel = "endif" + e.nextLabelID();
	e.emit(whileLabel + ":");
	condition.compile(e, endifLabel);
	statement.compile(e);
	e.emit("j " + whileLabel + "\t #continues the loop");
	e.emit(endifLabel + ":");
    }
}
