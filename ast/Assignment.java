package ast;
import environment.*;
import emitter.*;
/**
 * Describes statements specifically for variable assignment
 *
 * @author Rohan Rashingkar
 * @version 11/16/21
 */
public class Assignment extends Statement
{
    private String variable;
    private Expression exp;
    /**
     * Constructor for objects of class Assignment
     * @param newVariable the variable to receive the assignment
     * @param newExp the new value of the variable
     */
    public Assignment(String newVariable, Expression newExp)
    {
        variable = newVariable;
        exp = newExp;
    }

    /**
     * Runs the assignment statement, setting the given variable's value
     * to exp
     *
     * @param env the environment of execution
     */
    public void exec(Environment env)
    {
        env.setVariable(variable, exp.eval(env));
    }
    
    @Override
    public void compile(Emitter e)
    {
        exp.compile(e);
        e.emit("sw $v0 var" + variable);
    }
}
