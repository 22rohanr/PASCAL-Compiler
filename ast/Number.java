package ast;
import environment.*;
import emitter.*;
/**
 * Describes a number that will used in the abstract syntax trees

 *
 * @author Rohan Rashingkar
 * @version 11/16/21
 */
public class Number extends Expression
{

    private int val;

    /**
     * Constructor for objects of class Number
     * @param value the
     * value of the number
     */
    public Number(int value)
    {
        val = value;
    }
    
    /**
     * Gives the value of the number
     * @param env the environment of evaluation
     * @return the value of the number
     */
    public int eval(Environment env)
    {
        return val;
    }
    
    @Override
    public void compile(Emitter e)
    {
        e.emit("li $v0 " + val  + "\t#loads value to the register");
    }
}
