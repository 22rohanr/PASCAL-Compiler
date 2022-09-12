package emitter;

import java.io.*;
/**
 * Writes an output file with MIPS Code corresponding to 
 * the program's PASCAL code
 * @author Rohan Rashingkar
 * @version 1/8/21
 */
public class Emitter
{
    private PrintWriter out;
    private int nextLabel;
    /**
     * Creates an emitter for writing to a new file with given name
     * @param outputFileName the name of the output file
     */
    public Emitter(String outputFileName)
    {
        try
        {
            out = new PrintWriter(new FileWriter(outputFileName), true);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        nextLabel = 0;
    }

    /**
     * prints one line of code to file (with non-labels indented)
     * @param code the code to emit
     */
    public void emit(String code)
    {
        if (!code.endsWith(":"))
            code = "\t" + code;
        out.println(code);
    }

    /**
     * closes the file.  should be called after all calls to emit.
     */
    public void close()
    {
        out.close();
    }
    /**
     * Pushes a register onto the variable stack
     * @param reg the register to be used in this operation
     */
    public void emitPush(String reg)
    {
        emit("subu $sp $sp 4\t #allocating memory");
        emit("sw " + reg + " ($sp)\t #pushes register on the stack");
    }
    /**
     * Pops a register from the variable stack
     * @param reg the register to be used in this operation
     */
    public void emitPop(String reg)
    {
        emit("lw " + reg + " ($sp)\t #pops the content of the stack into the register");
	emit("addu $sp $sp 4\t #getting rid of the memory");
    }
    /**
     * Gets the id for the next label in MIPS
     * @return the ID for the next label
     */
    public int nextLabelID() 
    {
        nextLabel++;
        return nextLabel;
    }
}