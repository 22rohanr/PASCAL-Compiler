package ast;
import environment.*;
import emitter.*;
/**
 * Describes Writeln statements which print out its contents
 *
 * @author Rohan Rashingkar
 * @version 11/16/21
 */
public class Writeln extends Statement
{
    private Expression exp;

    /**
     * Constructor for objects of class Writeln
     * @param exp the expression to be operated on
     */
    public Writeln(Expression exp)
    {
        this.exp = exp;
    }

    /**
     * Runs the Writeln statement and prints the value of exp
     *
     * @param  env the environment of execution
     */
    public void exec(Environment env)
    {
        System.out.println(exp.eval(env));
    }
    
    @Override
    public void compile(Emitter e)
    {
        exp.compile(e);
        e.emit("move $a0 $v0\t #moves contents of v0 to a0");
        e.emit("li $v0 1");
        e.emit("syscall\t #prints the integer in a0");
        e.emit("li $v0, 11"); //I found this on stackoverflow - ascii print
        e.emit("li $a0, 10"); //10 is the ascii value for newline
        e.emit("syscall\t #prints the newline character");
    }
}
