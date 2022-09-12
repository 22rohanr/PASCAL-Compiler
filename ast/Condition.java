package ast;
import environment.*;
import emitter.*;
/**
 * Describes a condition for an if statement
 *
 * @author Rohan Rashingkar
 * @version 11/16/21
 */
public class Condition
{
    private String operator;
    private Expression exp1;
    private Expression exp2;

    /**
     * Constructor for objects of class Condition
     * @param newOperator the operator that will be used
     * @param newExp1 the expression on the left side of the operator
     * @param newExp2 the expression on the right side of the operator
     */
    public Condition(String newOperator, Expression newExp1, Expression newExp2)
    {
        operator = newOperator;
        exp1 = newExp1;
        exp2 = newExp2;
    }

    /**
     * An example of a method - replace this comment with your own
     *
     * @param env  the environment of evaluation
     * @return 1 if it is true otherwise return 0 if it is false
     */
    public int eval(Environment env)
    {
        if (operator.equals("="))
        {
            if (exp1.eval(env) == exp2.eval(env))
            {
                return 1;
            }
        }
        else if (operator.equals("<>"))
        {
            if (exp1.eval(env) != exp2.eval(env))
            {
                return 1;
            }
        }
        else if (operator.equals(">"))
        {
            if (exp1.eval(env) > exp2.eval(env))
            {
                return 1;
            }
        }
        else if (operator.equals("<"))
        {
            if (exp1.eval(env) < exp2.eval(env))
            {
                return 1;
            }
        }
        else if (operator.equals("<="))
        {
            if (exp1.eval(env) <= exp2.eval(env))
            {
                return 1;
            }
        }
        else if (operator.equals(">="))
        {
            if (exp1.eval(env) >= exp2.eval(env))
            {
                return 1;
            }
        }
        return 0;
    }
    /**
     * Compiles the program into MIPS code
     * @param e the emitter for this compiling method
     * @param label to jump to in the code
     */
    public void compile(Emitter e, String label)
    {
        exp1.compile(e);
        e.emitPush("$v0");
        exp2.compile(e);
        e.emitPop("$t0");
        if (operator.equals("="))
        {
            e.emit("bne $t0 $v0 " + label);
        }
        else if (operator.equals("<>"))
        {
            e.emit("beq $t0 $v0 " + label);
        }
        else if (operator.equals(">"))
        {
            e.emit("ble $t0 $v0 " + label);
        }
        else if (operator.equals("<"))
        {
            e.emit("bge $t0 $v0 " + label);
        }
        else if (operator.equals("<="))
        {
            e.emit("bgt $t0 $v0 " + label);
        }
        else if (operator.equals(">="))
        {
            e.emit("blt $t0 $v0 " + label);
        }
    }
}
