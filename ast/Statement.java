package ast;
import environment.*;
import emitter.*;
/**
 * Describes a statement that can be executed
 *
 * @author Rohan Rashingkar
 * @version 11/16/21
 */
public abstract class Statement
{
    /**
     * Runs the statement
     * @param env the environment of execution
     */
    public abstract void exec(Environment env);
    /**
     * Compiles the program into MIPS code
     * @param e the emitter for this compiling method
     */
    public void compile(Emitter e)
    {   
        throw new RuntimeException("Implement me!!!!!");
    } 
}
