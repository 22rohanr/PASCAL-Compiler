package ast;
import environment.*;
import emitter.*;
/**
 * Serves as a blueprint for all expressions that go through the parser
 * will analyzes
 *
 * @author Rohan Rashingkar
 * @version 11/16/21
 */
public abstract class Expression
{
    /**
     * Calculates the value of the expression
     * @param env the environment of the evaluation
     * @return the value of the expression
     */
    public abstract int eval(Environment env);
    /**
     * Compiles the program into MIPS code
     * @param e the emitter for this compiling method
     */
    public void compile(Emitter e)
    {
        throw new RuntimeException("Implement me!!!!!");
    } 
}
