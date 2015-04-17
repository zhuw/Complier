package A4;

import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;

import A2.Token;

public class SemanticAnalyzer {
  
  private static final Hashtable<String, Vector<SymbolTableItem>> symbolTable = new Hashtable<String, Vector<SymbolTableItem>>();
  private static final Stack stack = new Stack();
 
  // create here a data structure for the cube of types
  public static String[] cube_unary(String tpye,String opetator)
  {
	  String[] array=new String[7];
	  if(opetator.equals("!"))
	  {
		  array[0]="error";array[1]="error";array[2]="error";array[3]="error";array[4]="boolean";array[5]="error";array[6]="error";
	  }
	  
	  else if(opetator.equals("-"))
	  {
		  array[0]="int";array[1]="float";array[2]="error";array[3]="error";array[4]="error";array[5]="error";array[6]="error";
	  }
	  return array;
  }
  
  public static String[][] cube_binary(String opetator)
  {
	  String[][] array=new String[7][7];
	  
	  if(opetator.equals("+"))
	  {	  		 
		  array[0][0]="int";    array[0][1]="float";array[0][2]="error";
		  array[0][3]="string"; array[0][4]="error";array[0][5]="error";array[0][6]="error";
		  
		  array[1][0]="float";  array[1][1]="float";array[1][2]="error";
		  array[1][3]="string"; array[1][4]="error";array[1][5]="error";array[1][6]="error";
		  
		  array[2][0]="error";  array[2][1]="error";array[2][2]="error";
		  array[2][3]="string"; array[2][4]="error";array[2][5]="error";array[2][6]="error";
		  
		  array[3][0]="string"; array[3][1]="string";array[3][2]="string";
		  array[3][3]="string"; array[3][4]="string";array[3][5]="error";array[3][6]="error";
		  
		  array[4][0]="error"; array[4][1]="error";array[4][2]="error";
		  array[4][3]="string";array[4][4]="error";array[4][5]="error";array[4][6]="error";
		  
		  array[5][0]="error";array[5][1]="error";array[5][2]="error";
		  array[5][3]="error";array[5][4]="error";array[5][5]="error";array[5][6]="error";
		  
		  array[6][0]="error";array[6][1]="error";array[6][2]="error";
		  array[6][3]="error";array[6][4]="error";array[6][5]="error";array[6][6]="error";			  
	  }	
	  
	  if(opetator.equals("-")||opetator.equals("*")||opetator.equals("/"))
	  {	  		 
		  array[0][0]="int";   array[0][1]="float";array[0][2]="error";
		  array[0][3]="error"; array[0][4]="error";array[0][5]="error";array[0][6]="error";
		  
		  array[1][0]="float"; array[1][1]="float";array[1][2]="error";
		  array[1][3]="error"; array[1][4]="error";array[1][5]="error";array[1][6]="error";
		  
		  array[2][0]="error";array[2][1]="error";array[2][2]="error";
		  array[2][3]="error";array[2][4]="error";array[2][5]="error";array[2][6]="error";
		  
		  array[3][0]="error"; array[3][1]="error";array[3][2]="error";
		  array[3][3]="error";array[3][4]="error";array[3][5]="error";array[3][6]="error";
		  
		  array[4][0]="error";array[4][1]="error";array[4][2]="error";
		  array[4][3]="error";array[4][4]="error";array[4][5]="error";array[4][6]="error";
		  
		  array[5][0]="error";array[5][1]="error";array[5][2]="error";
		  array[5][3]="error";array[5][4]="error";array[5][5]="error";array[5][6]="error";
		  
		  array[6][0]="error";array[6][1]="error";array[6][2]="error";
		  array[6][3]="error";array[6][4]="error";array[6][5]="error";array[6][6]="error";			  
	  }	
	  
	  if(opetator.equals(">")||opetator.equals("<"))
	  {	  		 
		  array[0][0]="boolean";array[0][1]="boolean";array[0][2]="error";
		  array[0][3]="error";  array[0][4]="error";  array[0][5]="error";array[0][6]="error";
		  
		  array[1][0]="boolean";array[1][1]="boolean";array[1][2]="error";
		  array[1][3]="error";  array[1][4]="error";  array[1][5]="error";array[1][6]="error";
		  
		  array[2][0]="error";array[2][1]="error";array[2][2]="error";
		  array[2][3]="error";array[2][4]="error";array[2][5]="error";array[2][6]="error";
		  
		  array[3][0]="error"; array[3][1]="error";array[3][2]="error";
		  array[3][3]="error";array[3][4]="error";array[3][5]="error";array[3][6]="error";
		  
		  array[4][0]="error";array[4][1]="error";array[4][2]="error";
		  array[4][3]="error";array[4][4]="error";array[4][5]="error";array[4][6]="error";
		  
		  array[5][0]="error";array[5][1]="error";array[5][2]="error";
		  array[5][3]="error";array[5][4]="error";array[5][5]="error";array[5][6]="error";
		  
		  array[6][0]="error";array[6][1]="error";array[6][2]="error";
		  array[6][3]="error";array[6][4]="error";array[6][5]="error";array[6][6]="error";			  
	  }	
	  
	  if(opetator.equals("!=")||opetator.equals("=="))
	  {	  		 
		  array[0][0]="boolean";array[0][1]="boolean";array[0][2]="error";
		  array[0][3]="error";  array[0][4]="error";  array[0][5]="error";array[0][6]="error";
		  
		  array[1][0]="boolean";array[1][1]="boolean";array[1][2]="error";
		  array[1][3]="error";  array[1][4]="error";  array[1][5]="error";array[1][6]="error";
		  
		  array[2][0]="error";array[2][1]="error";array[2][2]="boolean";
		  array[2][3]="error";array[2][4]="error";array[2][5]="error";array[2][6]="error";
		  
		  array[3][0]="error"; array[3][1]="error";array[3][2]="error";
		  array[3][3]="boolean";array[3][4]="error";array[3][5]="error";array[3][6]="error";
		  
		  array[4][0]="error";array[4][1]="error"; array[4][2]="error";
		  array[4][3]="error";array[4][4]="boolean";array[4][5]="error";array[4][6]="error";
		  
		  array[5][0]="error";array[5][1]="error";array[5][2]="error";
		  array[5][3]="error";array[5][4]="error";array[5][5]="error";array[5][6]="error";
		  
		  array[6][0]="error";array[6][1]="error";array[6][2]="error";
		  array[6][3]="error";array[6][4]="error";array[6][5]="error";array[6][6]="error";			  
	  }	
	  
	  if(opetator.equals("&")||opetator.equals("|"))
	  {	  		 
		  array[0][0]="error";array[0][1]="error";array[0][2]="error";
		  array[0][3]="error";array[0][4]="error";  array[0][5]="error";array[0][6]="error";
		  
		  array[1][0]="boolean";array[1][1]="error";array[1][2]="error";
		  array[1][3]="error"; array[1][4]="error";  array[1][5]="error";array[1][6]="error";
		  
		  array[2][0]="error";array[2][1]="error";array[2][2]="error";
		  array[2][3]="error";array[2][4]="error";array[2][5]="error";array[2][6]="error";
		  
		  array[3][0]="error"; array[3][1]="error";array[3][2]="error";
		  array[3][3]="error"; array[3][4]="error";array[3][5]="error";array[3][6]="error";
		  
		  array[4][0]="error";array[4][1]="error"; array[4][2]="error";
		  array[4][3]="error";array[4][4]="boolean";array[4][5]="error";array[4][6]="error";
		  
		  array[5][0]="error";array[5][1]="error";array[5][2]="error";
		  array[5][3]="error";array[5][4]="error";array[5][5]="error";array[5][6]="error";
		  
		  array[6][0]="error";array[6][1]="error";array[6][2]="error";
		  array[6][3]="error";array[6][4]="error";array[6][5]="error";array[6][6]="error";			  
	  }	
	  
	  if(opetator.equals("="))
	  {	  		 
		  array[0][0]="OK";   array[0][1]="error";array[0][2]="error";
		  array[0][3]="error";array[0][4]="error";array[0][5]="error";array[0][6]="error";
		  
		  array[1][0]="OK";   array[1][1]="OK";   array[1][2]="error";
		  array[1][3]="error";array[1][4]="error";array[1][5]="error";array[1][6]="error";
		  
		  array[2][0]="error";array[2][1]="error";array[2][2]="OK";
		  array[2][3]="error";array[2][4]="error";array[2][5]="error";array[2][6]="error";
		  
		  array[3][0]="error";array[3][1]="error";array[3][2]="error";
		  array[3][3]="OK";  array[3][4]="error"; array[3][5]="error"; array[3][6]="error";
		  
		  array[4][0]="error";array[4][1]="error";array[4][2]="error";
		  array[4][3]="error";array[4][4]="OK";   array[4][5]="error";array[4][6]="error";
		  
		  array[5][0]="error";array[5][1]="error";array[5][2]="error";
		  array[5][3]="error";array[5][4]="error";array[5][5]="OK";  array[5][6]="error";
		  
		  array[6][0]="error";array[6][1]="error";array[6][2]="error";
		  array[6][3]="error";array[6][4]="error";array[6][5]="error";array[6][6]="error";		  
	  }	
	  return array;
  }

  
  public static Hashtable<String, Vector<SymbolTableItem>> getSymbolTable() 
  {
    return symbolTable;
  }
  
  public static void clear()
  {
	  symbolTable.clear();
  }
  
  public static void clearStack()
  {
	  stack.clear();
  }
  
  //checking variable
  public static void checkVariable(Gui gui,String type, String id,Vector<A2.Token> tokens,int currentToken) 
  {
    // A. search the id in the symbol table
	 if( symbolTable.get(id)==null)
	 {		    
		 // B. if !exist then insert: type, scope=global, value={0, false, "", '')
	     Vector v = new Vector();
	     v.add(new SymbolTableItem(type,"global", ""));
	     symbolTable.put(id, v);
	 }
	 
	 else
	 {	
		// C. else error: variable id is already defined
		int n=tokens.get(currentToken).getLine();
		error(gui, 1, n,id);
	 } 
  }

  public static void pushStack(String type) 
  {
  
    // push type in the stack
	  stack.push(type);
  }
  
  public static String popStack() 
  {
    String result="";
    // pop a value from the stack
    return (String) stack.pop();
  }
  
 
  public static String calculateCube(String type, String operator) 
  {
    String result="";
    // unary operator ( - and !)
    if(operator.equals("-"))
    {
    	String[] array=cube_unary(type,"-");
    	
    	if(type.equals("int"))
    	{
    		return result=array[0];
    	}
    	
    	else if(type.equals("float"))
    	{
    		return result=array[1];
    	}

        return result=array[2];
    }
    
    if(operator.equals("!"))
    {
    	String[] array=cube_unary(type,"!");
    	
    	if(type.equals("boolean"))
    	{
    		return result=array[4];
    	}

        return result=array[0];
    }   
    return result;
  }

  public static String calculateCube(String type1, String type2, String operator) 
  {
    String result="";

    //binary operator
    if(operator.equals("="))
    {
    	String[][] array=cube_binary("=");
    	
    	if(type1.equals("int")&&type2.equals("int"))
    	{
           return result=array[0][0];
    	}
    	
    	if(type1.equals("float")&&type2.equals("int"))
    	{
           return result=array[1][0];
    	}
    	
    	if(type1.equals("float")&&type2.equals("float"))
    	{
           return result=array[1][1];
    	}
    	
    	if(type1.equals("char")&&type2.equals("char"))
    	{
           return result=array[2][2];
    	}
    	
    	if(type1.equals("string")&&type2.equals("string"))
    	{
           return result=array[3][3];
    	}
    	
    	if(type1.equals("boolean")&&type2.equals("boolean"))
    	{
           return result=array[4][4];
    	}
    	
    	if(type1.equals("void")&&type2.equals("void"))
    	{
           return result=array[5][5];
    	}
    	
    	else
    	{
    		return result="error";
    	}
    	
    }
    
    if(operator.equals("+"))
    {
    	String[][] array=cube_binary("+");
    	
    	if(type1.equals("int")&&type2.equals("int"))
    	{
           return result=array[0][0];
    	}
    	
    	if(type1.equals("int")&&type2.equals("float"))
    	{
           return result=array[0][1];
    	}
    	
    	if(type1.equals("int")&&type2.equals("string"))
    	{
           return result=array[0][3];
    	}
    	
    	if(type1.equals("float")&&type2.equals("int"))
    	{
           return result=array[1][0];
    	}
    	
    	if(type1.equals("float")&&type2.equals("float"))
    	{
           return result=array[1][1];
    	}
    	
    	if(type1.equals("float")&&type2.equals("string"))
    	{
           return result=array[1][3];
    	}
    	
    	if(type1.equals("char")&&type2.equals("string"))
    	{
           return result=array[2][3];
    	}
    	
    	if(type1.equals("string")&&type2.equals("int"))
    	{
           return result=array[3][0];
    	}
    	
       	if(type1.equals("string")&&type2.equals("float"))
    	{
           return result=array[3][1];
    	}
       	
       	if(type1.equals("string")&&type2.equals("char"))
    	{
           return result=array[3][2];
    	}
    	
    	
    	if(type1.equals("string")&&type2.equals("string"))
    	{
           return result=array[3][3];
    	}
    	
    	if(type1.equals("string")&&type2.equals("boolean"))
    	{
           return result=array[3][4];
    	}
    	
    	if(type1.equals("boolean")&&type2.equals("string"))
    	{
           return result=array[4][3];
    	}
    	
    	else{
    		return result="error";
    	}
    	
    }
    
    if(operator.equals("-")||operator.equals("*")||operator.equals("/"))
    {
    	String[][] array=cube_binary("*");
    	if(type1.equals("int")&&type2.equals("int"))
    	{
           return result=array[0][0];
    	}  
    	
    	if(type1.equals("int")&&type2.equals("float"))
    	{
           return result=array[0][1];
    	}  
    	
    	if(type1.equals("float")&&type2.equals("int"))
    	{
           return result=array[1][0];
    	}  
    	
    	if(type1.equals("float")&&type2.equals("float"))
    	{
           return result=array[1][1];
    	}  
    	
    	return result="error";
    }
    
    if(operator.equals(">")||operator.equals("<"))
    {
    	String[][] array=cube_binary(">");
    	if(type1.equals("int")&&type2.equals("int"))
    	{
           return result=array[0][0];
    	}  
    	
    	if(type1.equals("int")&&type2.equals("float"))
    	{
           return result=array[0][1];
    	}  
    	
    	if(type1.equals("float")&&type2.equals("int"))
    	{
           return result=array[1][0];
    	}  
    	
    	if(type1.equals("float")&&type2.equals("float"))
    	{
           return result=array[1][1];
    	}  
    	else{
    		return result="error";
    	}
    	
    }
    
    if(operator.equals("!=")||operator.equals("=="))
    {
    	String[][] array=cube_binary("==");
    	if(type1.equals("int")&&type2.equals("int"))
    	{
           return result=array[0][0];
    	}  
    	
    	if(type1.equals("int")&&type2.equals("float"))
    	{
           return result=array[0][1];
    	}  
    	
    	if(type1.equals("float")&&type2.equals("int"))
    	{
           return result=array[1][0];
    	}  
    	
    	if(type1.equals("float")&&type2.equals("float"))
    	{
           return result=array[1][1];
    	} 
    	
    	if(type1.equals("char")&&type2.equals("char"))
    	{
           return result=array[2][2];
    	} 
    	
    	if(type1.equals("string")&&type2.equals("string"))
    	{
           return result=array[3][3];
    	} 
    	
    	if(type1.equals("boolean")&&type2.equals("boolean"))
    	{
           return result=array[4][4];
    	} 
    	
    	return result="error";
    }
    
    if(operator.equals("&")||operator.equals("|"))
    {
    	String[][] array=cube_binary("&");
    	
    	if(type1.equals("boolean")&&type2.equals("boolean"))
    	{
           return result=array[4][4];
    	} 
    	
    	return result="error";
    }
    return result;
  }
  
  public static void error(Gui gui, int err, int n,String info) 
  {
    switch (err) 
    {
      case 0: 
        gui.writeConsole("Line" + (n-1) + ": variable"+"<"+info+">"+" not found"); 
        break;
      case 1: 
        gui.writeConsole("Line" + (n-1)+ ": variable"+"<"+info+">"+" is already defined"); 
        break;
      case 2: 
        gui.writeConsole("Line" + (n+1)+ ": incompatible types: type mismatch"); 
        break;
      case 3: 
        gui.writeConsole("Line" + (n+1)+ ": incompatible types: expected boolean"); 
        break;
    }
  } 
}
