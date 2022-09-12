package ast;
import environment.*;
import java.util.*;
import emitter.*;
/**
 * Describes the declaration for a procedure, including its name, parameters,
 * and statement
 *
 * @author Rohan Rashingkar
 * @version 12/10/21
 */
public class ProcedureDeclaration extends Statement
{
    private String name;
    private Statement statement;
    private List<String> parameters;
    /**
     * Constructor for objects of class ProcedureDeclaration
     * @param n the name of the procedure
     * @param s the statement in the procedure
     * @param p the procedure's parameters in a list
     */
    public ProcedureDeclaration(String n, Statement s, List<String> p)
    {
        name = n;
        statement = s;
        parameters = p;
    }

    /**
     * Runs every statement in the block statement
     * @param  env the environment of execution
     */
    public void exec(Environment env)
    {
        env.setProcedure(name, this);
    }
    
    /**
     * Gives public access to the statement of this procedure
     * declaration
     * @return this procedure declaration's statement
     */
    public Statement getProcedure()
    {
        return statement;
    }
    
    /**
     * Gives public access to parameters of this procedure
     * declaration
     * @return the list of parameters in this procedure
     * declaration
     */
    public List<String> getParameters()
    {
        return parameters;
    }
    
    /**
     * Gives public access to the name of this procedure
     * declaration
     * @return this procedure declaration's name
     */
    public String getName()
    {
        return name;
    }
    
    @Override
    public void compile(Emitter e)
    {
        e.emit("proc" + name + ":");
        for (String p : parameters)
        {
            e.emit("li, $v0, 0");
            e.emitPush("$v0");
        }
        statement.compile(e);
        e.emit("jr $ra");
    }
}
