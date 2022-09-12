package environment;
import ast.*;
import java.util.*;

/**
 * A space that stores variables and their associated values
 *
 * @author Rohan Rashingkar
 * @version 11/16/21
 */
public class Environment
{
    private Map<String, Integer> variables;
    private Map<String, ProcedureDeclaration> procedures;
    private Environment parent;
    /**
     * Constructor for objects of class Environment
     */
    public Environment()
    {
        variables = new HashMap<String, Integer>();
        procedures = new HashMap<String, ProcedureDeclaration>();
        parent = null;
    }
    
    /**
     * Constructor for objects of class Environment
     * @param env the parent environment of this environment
     */
    public Environment(Environment env)
    {
        variables = new HashMap<String, Integer>();
        procedures = new HashMap<String, ProcedureDeclaration>();
        parent = env;
    }

    /**
     * Recursive helper method that checks if
     * the environment and its parent have a certain variable
     * @param variable the variable to be tested
     */
    private boolean containsVariable(String variable)
    {
        if (variables.containsKey(variable))
        {
            return true;
        }
        return parent != null && parent.containsVariable(variable);
    }
    /**
     * Associates the given variable name with the given value
     * @param variable the variable that will receive the new value
     * @param value the new value of the variable
     */
    public void setVariable(String variable, int value)
    {
        if (variables.containsKey(variable))
        {
            variables.put(variable, value);
        }
        else if (containsVariable(variable))
        {
            parent.setVariable(variable, value);
        }
        else
        {
            declareVariable(variable, value);
        }
    }
    
    /**
     * Declares the variable and places it in the 
     * map of variables
     * @param variable the variable to be declared
     * @param value the value of the variable
     */
    public void declareVariable(String variable, int value)
    {
        variables.put(variable, value);
    }
    
    /**
     * Returns the value associated with the given variable 
     * name
     * @param variable the variable whose value will be retrieved
     * @return the value of the given variable
     */
    
    public int getVariable(String variable)
    {
        if (variables.containsKey(variable))
        {
            return variables.get(variable);
        }
        else
        {
            return parent.getVariable(variable);
        }
    }
    
    /**
     * Allocates a name and declaration to a procedure
     * @param name the name of the procedure
     * @param declaration the declaration of the procedure with all
     * of its statements and its name
     */
    public void setProcedure(String name, ProcedureDeclaration declaration)
    {
        if (parent == null)
        {
            procedures.put(name, declaration);
        }
        else
        {
            parent.setProcedure(name, declaration);
        }
    }
    
    /**
     * Gives public access to the procedure with the given name
     * @param name the name of the procedure
     * @return the procedure with the specific name
     */
    public ProcedureDeclaration getProcedure(String name)
    {
        if (parent == null)
        {
            return procedures.get(name);
        }
        else
        {
            return parent.procedures.get(name);
        }
    }
}
