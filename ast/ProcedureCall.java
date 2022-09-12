package ast;
import environment.*;
import java.util.*;
import emitter.*;
/**
 * Descibes a call to a procedure as well as its parameters
 *
 * @author Rohan Rashingkar
 * @version 12/10/21
 */
public class ProcedureCall extends Expression
{
    private String name;
    private List<Expression> parameters;
    /**
     * Constructor for objects of class ProcedureCall
     * @param n the name of this procedure call
     * @param p the parameters in this procedure call
     */
    public ProcedureCall(String n, List<Expression> p)
    {
        name = n;
        parameters = p;
    }

    /**
     * Gives the value of the procedure call number
     * @param env the environment of evaluation
     * @return the result of the procedure call
     */
    public int eval(Environment env)
    {
        ProcedureDeclaration declaration = env.getProcedure(name);
        Environment childEnv = new Environment(env);
        for (int i = 0; i < parameters.size(); i++)
        {
            childEnv.declareVariable(declaration.getParameters().get(i), 
                parameters.get(i).eval(env));
        }
        childEnv.declareVariable(name, 0);
        declaration.getProcedure().exec(childEnv);
        return childEnv.getVariable(name);
    }
    
    @Override
    public void compile(Emitter e)
    {
        e.emitPush("$ra");
        for (Expression p : parameters)
        {
            p.compile(e);
            e.emitPush("$v0");
        }
        e.emit("jal proc" + name);
        for (Expression p : parameters)
        {
            p.compile(e);
            e.emitPop("$v0");
        }
        e.emitPop("$ra");
    }
}
