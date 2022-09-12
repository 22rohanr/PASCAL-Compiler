package ast;
import environment.*;
import emitter.*;
/**
 * Describes a variable that will be used in the abstract syntax trees
 *
 * @author Rohan Rashingkar
 * @version 11/16/21
 */
public class Variable extends Expression
{
    private String name;
    
    
    /**
     * Constructor for objects of class Variable
     * @param newName the name of the variable
     */
    public Variable(String newName)
    {
        name = newName;
    }

    /**
     * Gives the value that the variable represents
     *
     * @param env the environment of the evaluation
     * @return the value of the given variable
     */
    public int eval(Environment env)
    {
        return env.getVariable(name);
    }
    
    @Override
    public void compile(Emitter e)
    {
        e.emit("la $t0 var " + name + "\t #loads address of var into t0");
        e.emit("lw $v0 ($t0)\t #loads the contents of the variable into t0");
    }
}
