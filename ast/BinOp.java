package ast;
import environment.*;
import emitter.*;
/**
 * Describes binary operators in expressions that will be 
 * used in the abstract syntax trees
 *
 * @author Rohan Rashingkar
 * @version 11/16/21
 */
public class BinOp extends Expression
{
    private String operator;
    private Expression exp1;
    private Expression exp2;

    /**
     * Constructor for objects of class BinOp
     * @param newOperator the binary operator that will be used 
     * on the expressions
     * @param newExp1 the first expression to be operated on
     * @param newExp2 the second expression to be operated on
     */
    public BinOp(String newOperator, Expression newExp1, Expression newExp2)
    {
        operator = newOperator;
        exp1 = newExp1;
        exp2 = newExp2;
    }

    /**
     * Calculates the value of the (exp1 (operator) exp2)
     *
     * @param env the environment of the evaluation
     * @return the result of the binary operaton
     */
    public int eval(Environment env)
    {
        if (operator.equals("+"))
        {
            return exp1.eval(env) + exp2.eval(env);
        }
        if (operator.equals("-"))
        {
            return exp1.eval(env) - exp2.eval(env);
        }
        if (operator.equals("*"))
        {
            return exp1.eval(env) * exp2.eval(env);
        }
        return exp1.eval(env) / exp2.eval(env);
    }
    @Override
    public void compile(Emitter e)
    {
        exp1.compile(e);
        e.emitPush("$v0");
        exp2.compile(e);
        e.emitPop("$t0");
        if (operator.equals("+"))
        {
            e.emit("addu $v0 $t0 $v0\t #moves the sum of v0 and t0 to v0");
        }
        if (operator.equals("-"))
        {
            e.emit("subu $v0 $t0 $v0\t #moves the difference of v0" + 
                 "and t0 to v0");
        }
        if (operator.equals("*"))
        {
            e.emit("mult $v0 $t0");
            e.emit("mflo $v0\t #moves the product of v0 and t0 to v0");
        }
        else
        {
            e.emit("div $t0 $v0");
            e.emit("mflo $v0\t #moves the product of v0 and t0 to v0");
        }
    }
}
