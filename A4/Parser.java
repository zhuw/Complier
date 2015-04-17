package A4;

import java.util.Vector;

import javax.swing.Spring;
import javax.swing.tree.DefaultMutableTreeNode;

import A2.Token;

/**
 *
 * @author Wei Zhu
 */
public class Parser {

  private static DefaultMutableTreeNode root;
  private static Vector<A2.Token> tokens;
  private static int currentToken;
  private static Gui gui;
  static int flag;
  static int count=1;
  static int line=0;
  
  public static void error(int err)
  {
	  int n;
	  if(currentToken>=tokens.size())
	  {
		   n = tokens.get(tokens.size()-1).getLine();
	  }
	  
	  else
	  {
	   n = tokens.get(currentToken).getLine();
	  }
	  switch (err) 
	  { 
	  case 1: 
	  gui.writeConsole("Line" + n + ": expected {"); 
	  break;
	  
	  case 2: 
	  gui.writeConsole("Line" + (n+1) + ": expected }"); 
	  break;
	  
	  case 3:
      gui.writeConsole("Line" + n + ": expected ;"); 
	  break; 
	  
	  case 4: 
	  gui.writeConsole("Line" +n+": expected identifier or keyword"); 
	  break;
	  
	  case 5:		  
	  gui.writeConsole("Line" +n+": expected ="); 
	  break; 
	  
	  case 6: 		  
	  gui.writeConsole("Line" +n+": expected identifier"); 
	  break;
	  
	  case 7: 		  
	  gui.writeConsole("Line" +n+": expected )"); 
	  break; 
	  
	  case 8:		  
	  gui.writeConsole("Line" +n+": expected ("); 
	  break;	  
	  
	  case 9: 
	  gui.writeConsole("Line" +n+": expected value, identifier, ("); 
	  break;
	  }   
  } 
  
  //Begin to run this application
  public static DefaultMutableTreeNode run(Vector<A2.Token> t, Gui gui) {
	Parser.gui=gui;
    tokens = t;
    currentToken = 0;
    SemanticAnalyzer.clear();
    SemanticAnalyzer.clearStack();
    CodeGenerator.clear(gui);
    flag=0;
    root = new DefaultMutableTreeNode("program");
    //
    rule_PROGRAM(root);//The application start from rule_PROGRAM
    //
    gui.writeSymbolTable(SemanticAnalyzer.getSymbolTable());
    CodeGenerator.writeCode(gui);
    //
    return root;
  }
  
  //1.rule_PROGRAM
  private static boolean rule_PROGRAM(DefaultMutableTreeNode parent)
  {
	boolean error;
	DefaultMutableTreeNode node;

	
	if(currentToken < tokens.size()&&tokens.get(currentToken).getWord().equals("{"))
	{
		 node = new DefaultMutableTreeNode("{");
		 parent.add(node);
		 currentToken++;
	}
    //
	flag++;
    node = new DefaultMutableTreeNode("body");
    parent.add(node);
    error = rule_BODY(node);
    //
    
	if(currentToken < tokens.size() &&tokens.get(currentToken).getWord().equals("}"))
	{		 
		 node = new DefaultMutableTreeNode("}");
		 parent.add(node);
		 currentToken++; 
	}
	if(currentToken >=tokens.size())
      CodeGenerator.addInstruction("opr", "0", "0");
	
	
	return error;
	  
  }
  
  //2.rule_BODY
  private static boolean rule_BODY(DefaultMutableTreeNode parent) 
  {
	DefaultMutableTreeNode node;
	boolean error=false;
			
	while (currentToken < tokens.size() &&!tokens.get(currentToken).getWord().equals("}"))
	{		

		
		//2.1 ASSIGNMENT PART
		if (currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("IDENTIFIER"))
		{
			
			node = new DefaultMutableTreeNode("ASSIGNMENT");
			parent.add(node);
			error = rule_ASSIGNMENT(node);
				
			if (currentToken < tokens.size()&&tokens.get(currentToken).getWord().equals(";"))
			{
			    node = new DefaultMutableTreeNode(";");
			    parent.add(node);
			    currentToken++;	
			    
			     //execute more functions...
                while(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("int")||
            			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("float")||
            			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("boolean")||
            			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("char")||
            			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("string")||
            			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("void")||
            			currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("IDENTIFIER")||
            			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("while")||
            			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("if")||
            			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("print")||
            			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("return"))
                	{
                		
                    //2.1.1 execute VARIABLE function
                    if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("int")||
                       currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("float")||
                       currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("boolean")||
                       currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("char")||
                       currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("string")||
                       currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("void"))
                		{
                			node = new DefaultMutableTreeNode("VARIABLE");
                			parent.add(node);
                			error = rule_VARIABLE(node);
                			
                			if (currentToken < tokens.size()&&tokens.get(currentToken).getWord().equals(";"))
                			{
                			     node = new DefaultMutableTreeNode(";");
                			     parent.add(node);
                			     currentToken++;
                			}// end 2.1.1 execute VARIABLE function
                			else
                			  error(3);
                		}
                	
                    //2.1.2 or execute ASSIGNMENT function
                   else if(currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("IDENTIFIER"))
                    {
                                         
                   		node = new DefaultMutableTreeNode("ASSIGNMENT");
                   		parent.add(node);
                   		error = rule_ASSIGNMENT(node);
                   			
                   		if (currentToken < tokens.size()&&tokens.get(currentToken).getWord().equals(";"))
                   		{
                   		  node = new DefaultMutableTreeNode(";");
                   		  parent.add(node);
                   		  currentToken++;
                   		}
            			else
              			  error(3);
                       
                    } //end 2.1.2 execute ASSIGNMENT function
                  
                  
                  //2.1.3 or execute WHILE function
                 else if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("while"))
                    {
                	    line=CodeGenerator.instructions.size()+1;
                        node = new DefaultMutableTreeNode("WHILE");
              			parent.add(node);
              			error = rule_WHILE(node);      			          			
                     	
                   } //end 2.1.3 execute WHILE function
                  
                  //2.1.4 or execute IF function
                 else if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("if"))
                    {
                     	
                        node = new DefaultMutableTreeNode("IF");
              			parent.add(node);
              			error = rule_IF(node);      			          			
                     	
                   } //2.1.4  end execute WHILE function    
                  
                  //2.1.5 or execute PRINT function
                 else if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("print"))
                    {
                     	
                     	node = new DefaultMutableTreeNode("PRINT");
              			parent.add(node);
              			error = rule_PRINT(node); 
              			
              			if (currentToken < tokens.size()&&tokens.get(currentToken).getWord().equals(";"))
              			{
              			    node = new DefaultMutableTreeNode(";");
              			    parent.add(node);
              			    currentToken++;			    
              			}
            			else
              			  error(3);
                   } //end 2.1.5 execute PRINT function 
                  
                 //2.1.6 or execute RETURN function
                 else if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("return"))
                    {

                     	node = new DefaultMutableTreeNode("RETURN");
              			parent.add(node);
              			error = rule_RETURN(node); 
              			
              			if (currentToken < tokens.size()&&tokens.get(currentToken).getWord().equals(";"))
              			{
              			    node = new DefaultMutableTreeNode(";");
              			    parent.add(node);
              			    currentToken++;			    
              			}
            			else
              			  error(3);
                   } //end 2.1.6 execute RETURN function        			          			
               }
		  }
			else
			{
			 error(3);
			}

	   }// end of 2.1 ASSIGNMENT PART
			
	   //2.2 VARIABLE PART
	   else if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("int")||
				currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("float")||
				currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("boolean")||
				currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("char")||
				currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("string")||
				currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("void"))
		{
			node = new DefaultMutableTreeNode("VARIABLE");
			parent.add(node);
			error = rule_VARIABLE(node);
				
			if (currentToken < tokens.size()&&tokens.get(currentToken).getWord().equals(";"))
			{
			     node = new DefaultMutableTreeNode(";");
			     parent.add(node);
			     currentToken++;
			     
			     //execute more functions...
                while(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("int")||
            			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("float")||
            			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("boolean")||
            			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("char")||
            			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("string")||
            			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("void")||
            			currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("IDENTIFIER")||
            			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("while")||
            			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("if")||
            			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("print")||
            			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("return"))
                	{
                		
                    //2.2.1 execute VARIABLE function
                    if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("int")||
                       currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("float")||
                       currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("boolean")||
                       currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("char")||
                       currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("string")||
                       currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("void"))
                		{
                			node = new DefaultMutableTreeNode("VARIABLE");
                			parent.add(node);
                			error = rule_VARIABLE(node);
                			
                			if (currentToken < tokens.size()&&tokens.get(currentToken).getWord().equals(";"))
                			{
                			     node = new DefaultMutableTreeNode(";");
                			     parent.add(node);
                			     currentToken++;
                			}// end 2.2.1 execute VARIABLE function
                			else
                  			  error(3);
                		}
                	
                    //2.2.2 or execute ASSIGNMENT function
                   else if(currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("IDENTIFIER"))
                    {
                                         
                   		node = new DefaultMutableTreeNode("ASSIGNMENT");
                   		parent.add(node);
                   		error = rule_ASSIGNMENT(node);
                   			
                   		if (currentToken < tokens.size()&&tokens.get(currentToken).getWord().equals(";"))
                   		{
                   		  node = new DefaultMutableTreeNode(";");
                   		  parent.add(node);
                   		  currentToken++;
                   		}
            			else
              			  error(3);
                       
                    } //end 2.2.2 execute ASSIGNMENT function
                  
                  
                  //2.2.3 or execute WHILE function
                 else if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("while"))
                    {
                	    line=CodeGenerator.instructions.size()+1;
                        node = new DefaultMutableTreeNode("WHILE");
              			parent.add(node);
              			error = rule_WHILE(node);      			          			
                     	
                   } //end 2.2.3 execute WHILE function
                  
                  //2.2.4 or execute IF function
                 else if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("if"))
                    {
                     	
                        node = new DefaultMutableTreeNode("IF");
              			parent.add(node);
              			error = rule_IF(node);      			          			
                     	
                   } //2.2.4  end execute WHILE function    
                  
                  //2.2.5 or execute PRINT function
                 else if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("print"))
                    {
                     	
                     	node = new DefaultMutableTreeNode("PRINT");
              			parent.add(node);
              			error = rule_PRINT(node); 
              			
              			if (currentToken < tokens.size()&&tokens.get(currentToken).getWord().equals(";"))
              			{
              			    node = new DefaultMutableTreeNode(";");
              			    parent.add(node);
              			    currentToken++;			    
              			}
            			else
              			  error(3);
                   } //end 2.2.5 execute PRINT function 
                  
                 //2.2.6 or execute RETURN function
                 else if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("return"))
                    {

                     	node = new DefaultMutableTreeNode("RETURN");
              			parent.add(node);
              			error = rule_RETURN(node); 
              			
              			if (currentToken < tokens.size()&&tokens.get(currentToken).getWord().equals(";"))
              			{
              			    node = new DefaultMutableTreeNode(";");
              			    parent.add(node);
              			    currentToken++;			    
              			}
            			else
              			  error(3);
                   } //end 2.2.6 execute RETURN function        			          			
               }
		   }
			else
  			  error(3);
		} //end of 2.2 VARIABLE PART
			
		//2.3 WHILE PART
		else if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("while"))
		{
			node = new DefaultMutableTreeNode("WHILE");
			parent.add(node);
			error = rule_WHILE(node);
			
		  //execute more functions...
          while(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("int")||
        		currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("float")||
        		currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("boolean")||
        		currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("char")||
        		currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("string")||
        		currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("void")||
        		currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("IDENTIFIER")||
        		currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("while")||
        		currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("if")||
        		currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("print")||
        		currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("return"))
            	{
            		
                //2.3.1 execute VARIABLE function
                if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("int")||
                   currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("float")||
                   currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("boolean")||
                   currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("char")||
                   currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("string")||
                   currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("void"))
            		{
            			node = new DefaultMutableTreeNode("VARIABLE");
            			parent.add(node);
            			error = rule_VARIABLE(node);
            			
            			if (currentToken < tokens.size()&&tokens.get(currentToken).getWord().equals(";"))
            			{
            			     node = new DefaultMutableTreeNode(";");
            			     parent.add(node);
            			     currentToken++;
            			}// end 2.3.1 execute VARIABLE function
            			else
              			  error(3);
            		}
            	
                //2.3.2 or execute ASSIGNMENT function
               else if(currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("IDENTIFIER"))
                {
                                     
               		node = new DefaultMutableTreeNode("ASSIGNMENT");
               		parent.add(node);
               		error = rule_ASSIGNMENT(node);
               			
               		if (currentToken < tokens.size()&&tokens.get(currentToken).getWord().equals(";"))
               		{
               		  node = new DefaultMutableTreeNode(";");
               		  parent.add(node);
               		  currentToken++;
               		}
        			else
          			  error(3);
                   
                } //end 2.3.2 execute ASSIGNMENT function
              
              
              //2.3.3 or execute WHILE function
             else if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("while"))
                {

                    node = new DefaultMutableTreeNode("WHILE");
          			parent.add(node);
          			error = rule_WHILE(node);      			          			
                 	
               } //end 2.3.4 execute WHILE function
              
              //2.3.4 or execute IF function
             else if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("if"))
                {
                 	
                    node = new DefaultMutableTreeNode("IF");
          			parent.add(node);
          			error = rule_IF(node);      			          			
                 	
               } //2.3.4  end execute WHILE function    
              
              //2.3.5 or execute PRINT function
             else if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("print"))
                {
                 	
                 	node = new DefaultMutableTreeNode("PRINT");
          			parent.add(node);
          			error = rule_PRINT(node); 
          			
          			if (currentToken < tokens.size()&&tokens.get(currentToken).getWord().equals(";"))
          			{
          			    node = new DefaultMutableTreeNode(";");
          			    parent.add(node);
          			    currentToken++;			    
          			}
        			else
          			  error(3);
               } //end 2.3.5 execute PRINT function 
              
             //2.3.6 or execute RETURN function
             else if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("return"))
                {

                 	node = new DefaultMutableTreeNode("RETURN");
          			parent.add(node);
          			error = rule_RETURN(node); 
          			
          			if (currentToken < tokens.size()&&tokens.get(currentToken).getWord().equals(";"))
          			{
          			    node = new DefaultMutableTreeNode(";");
          			    parent.add(node);
          			    currentToken++;			    
          			}
        			else
          			  error(3);
               } //end 2.3.6 execute RETURN function        			          			
           }
			
		}// end of 2.3 WHILE PART
			
		//2.4 IF PART
		else if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("if"))
		{
			node = new DefaultMutableTreeNode("IF");
			parent.add(node);
			error = rule_IF(node);
			
		      //execute more functions...
              while(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("int")||
        			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("float")||
        			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("boolean")||
        			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("char")||
        			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("string")||
        			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("void")||
        			currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("IDENTIFIER")||
        			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("while")||
        			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("if")||
        			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("print")||
        			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("return"))
            	{
            		
                //2.4.1 execute VARIABLE function
                if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("int")||
                   currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("float")||
                   currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("boolean")||
                   currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("char")||
                   currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("string")||
                   currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("void"))
            		{
            			node = new DefaultMutableTreeNode("VARIABLE");
            			parent.add(node);
            			error = rule_VARIABLE(node);
            			
            			if (currentToken < tokens.size()&&tokens.get(currentToken).getWord().equals(";"))
            			{
            			     node = new DefaultMutableTreeNode(";");
            			     parent.add(node);
            			     currentToken++;
            			}
            			else
              			  error(3);// end 2.4.1 execute VARIABLE function
            		}
            	
                //2.4.2 or execute ASSIGNMENT function
               else if(currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("IDENTIFIER"))
                {
                                     
               		node = new DefaultMutableTreeNode("ASSIGNMENT");
               		parent.add(node);
               		error = rule_ASSIGNMENT(node);
               			
               		if (currentToken < tokens.size()&&tokens.get(currentToken).getWord().equals(";"))
               		{
               		  node = new DefaultMutableTreeNode(";");
               		  parent.add(node);
               		  currentToken++;
               		}
        			else
          			  error(3);
                   
                } //end 2.4.2 execute ASSIGNMENT function
              
              
              //2.4.3 or execute WHILE function
             else if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("while"))
                {
            	    line=CodeGenerator.instructions.size()+1;
                    node = new DefaultMutableTreeNode("WHILE");
          			parent.add(node);
          			error = rule_WHILE(node);      			          			
                 	
               } //end 2.4.3 execute WHILE function
              
              //2.4.4 or execute IF function
             else if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("if"))
                {
                 	
                    node = new DefaultMutableTreeNode("IF");
          			parent.add(node);
          			error = rule_IF(node);      			          			
                 	
               } //2.4.4  end execute WHILE function    
              
              //2.4.5 or execute PRINT function
             else if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("print"))
                {
                 	
                 	node = new DefaultMutableTreeNode("PRINT");
          			parent.add(node);
          			error = rule_PRINT(node); 
          			
          			if (currentToken < tokens.size()&&tokens.get(currentToken).getWord().equals(";"))
          			{
          			    node = new DefaultMutableTreeNode(";");
          			    parent.add(node);
          			    currentToken++;			    
          			}
        			else
          			  error(3);
               } //end 2.4.5 execute PRINT function 
              
             //2.4.6 or execute RETURN function
             else if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("return"))
                {

                 	node = new DefaultMutableTreeNode("RETURN");
          			parent.add(node);
          			error = rule_RETURN(node); 
          			
          			if (currentToken < tokens.size()&&tokens.get(currentToken).getWord().equals(";"))
          			{
          			    node = new DefaultMutableTreeNode(";");
          			    parent.add(node);
          			    currentToken++;			    
          			}
        			else
          			  error(3);
               } //end 2.4.6 execute RETURN function        			          			
           }
			
		}//end of 2.4 IF PART
			
		//2.5 RETURN PART
		else if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("return"))
		{
			node = new DefaultMutableTreeNode("RETURN");
			parent.add(node);
			error = rule_RETURN(node);
				
			if (currentToken < tokens.size()&&tokens.get(currentToken).getWord().equals(";"))
			{
			    node = new DefaultMutableTreeNode(";");
			    parent.add(node);
			    currentToken++;	
			    
			      //execute more functions...
                  while(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("int")||
            			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("float")||
            			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("boolean")||
            			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("char")||
            			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("string")||
            			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("void")||
            			currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("IDENTIFIER")||
            			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("while")||
            			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("if")||
            			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("print")||
            			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("return"))
                	{
                		
                    //2.5.1 execute VARIABLE function
                    if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("int")||
                       currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("float")||
                       currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("boolean")||
                       currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("char")||
                       currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("string")||
                       currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("void"))
                		{
                			node = new DefaultMutableTreeNode("VARIABLE");
                			parent.add(node);
                			error = rule_VARIABLE(node);
                			
                			if (currentToken < tokens.size()&&tokens.get(currentToken).getWord().equals(";"))
                			{
                			     node = new DefaultMutableTreeNode(";");
                			     parent.add(node);
                			     currentToken++;
                			}
                			else
                  			  error(3);
                		}// end 2.5.1 execute VARIABLE function
                	
                    //2.5.2 or execute ASSIGNMENT function
                    else if(currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("IDENTIFIER"))
                    {
                                         
                   		node = new DefaultMutableTreeNode("ASSIGNMENT");
                   		parent.add(node);
                   		error = rule_ASSIGNMENT(node);
                   			
                   		if (currentToken < tokens.size()&&tokens.get(currentToken).getWord().equals(";"))
                   		{
                   		  node = new DefaultMutableTreeNode(";");
                   		  parent.add(node);
                   		  currentToken++;
                   		}
            			else
              			  error(3);
                       
                    } //end 2.5.2 execute ASSIGNMENT function
                  
                  
                 //2.5.3 or execute WHILE function
                 else if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("while"))
                   {
                	    line=CodeGenerator.instructions.size()+1;
                        node = new DefaultMutableTreeNode("WHILE");
              			parent.add(node);
              			error = rule_WHILE(node);      			          			
                     	
                   } //end 2.5.3 execute WHILE function
                  
                  //2.5.4 or execute IF function
                 else if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("if"))
                   {
                     	
                        node = new DefaultMutableTreeNode("IF");
              			parent.add(node);
              			error = rule_IF(node);      			          			
                     	
                   } //2.5.4  end execute WHILE function    
                  
                  //2.5.5 or execute PRINT function
                 else if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("print"))
                    {
                     	
                     	node = new DefaultMutableTreeNode("PRINT");
              			parent.add(node);
              			error = rule_PRINT(node); 
              			
              			if (currentToken < tokens.size()&&tokens.get(currentToken).getWord().equals(";"))
              			{
              			    node = new DefaultMutableTreeNode(";");
              			    parent.add(node);
              			    currentToken++;			    
              			}
            			else
              			  error(3);
                   } //end 2.5.5 execute PRINT function 
                  
                 //2.5.6 or execute RETURN function
                 else if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("return"))
                    {

                     	node = new DefaultMutableTreeNode("RETURN");
              			parent.add(node);
              			error = rule_RETURN(node); 
              			
              			if (currentToken < tokens.size()&&tokens.get(currentToken).getWord().equals(";"))
              			{
              			    node = new DefaultMutableTreeNode(";");
              			    parent.add(node);
              			    currentToken++;			    
              			}
            			else
              			  error(3);
                   } //end 2.5.6 execute RETURN function        			          			
               }
			}
			else
  			  error(3);
		}//end of 2.5 RETURN PART
			
		//2.6 PRINT PART
		else if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("print"))
		{
			node = new DefaultMutableTreeNode("PRINT");
			parent.add(node);
			error = rule_PRINT(node);
			
			if (currentToken < tokens.size()&&tokens.get(currentToken).getWord().equals(";"))
			{
			    node = new DefaultMutableTreeNode(";");
			    parent.add(node);
			    currentToken++;	
			    
			   //execute more functions...
               while(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("int")||
          			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("float")||
          			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("boolean")||
          			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("char")||
          			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("string")||
          			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("void")||
          			currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("IDENTIFIER")||
          			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("while")||
          			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("if")||
          			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("print")||
          			currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("return"))
              	{
              		
                  //2.6.1 execute VARIABLE function
                  if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("int")||
                     currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("float")||
                     currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("boolean")||
                     currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("char")||
                     currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("string")||
                     currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("void"))
              		{
              			node = new DefaultMutableTreeNode("VARIABLE");
              			parent.add(node);
              			error = rule_VARIABLE(node);
              			
              			if (currentToken < tokens.size()&&tokens.get(currentToken).getWord().equals(";"))
              			{
              			     node = new DefaultMutableTreeNode(";");
              			     parent.add(node);
              			     currentToken++;
              			}
            			else
              			  error(3);
              		}// end 2.6.1 execute VARIABLE function
              	
                  //2.6.2 or execute ASSIGNMENT function
                  else if(currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("IDENTIFIER"))
                  {
                                       
                 		node = new DefaultMutableTreeNode("ASSIGNMENT");
                 		parent.add(node);
                 		error = rule_ASSIGNMENT(node);
                 			
                 		if (currentToken < tokens.size()&&tokens.get(currentToken).getWord().equals(";"))
                 		{
                 		  node = new DefaultMutableTreeNode(";");
                 		  parent.add(node);
                 		  currentToken++;
                 		}
            			else
              			  error(3);
                     
                  } //end 2.6.2 execute ASSIGNMENT function
                
                
               //2.6.3 or execute WHILE function
               else if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("while"))
                 {
            	    line=CodeGenerator.instructions.size()+1;
                    node = new DefaultMutableTreeNode("WHILE");
             		parent.add(node);
            		error = rule_WHILE(node);      			          			
                   	
                 } //end 2.6.3 execute WHILE function
                
                //2.6.4 or execute IF function
               else if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("if"))
                 {
                   	
                      node = new DefaultMutableTreeNode("IF");
            			parent.add(node);
            			error = rule_IF(node);      			          			
                   	
                 } //2.6.4  end execute WHILE function    
                
                //2.6.5 or execute PRINT function
               else if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("print"))
                  {
                   	
                   	node = new DefaultMutableTreeNode("PRINT");
            			parent.add(node);
            			error = rule_PRINT(node); 
            			
            			if (currentToken < tokens.size()&&tokens.get(currentToken).getWord().equals(";"))
            			{
            			    node = new DefaultMutableTreeNode(";");
            			    parent.add(node);
            			    currentToken++;			    
            			}
            			else
              			  error(3);
                 } //end 2.6.5 execute PRINT function 
                
               //2.6.6 or execute RETURN function
               else if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("return"))
                  {

                   	node = new DefaultMutableTreeNode("RETURN");
            			parent.add(node);
            			error = rule_RETURN(node); 
            			
            			if (currentToken < tokens.size()&&tokens.get(currentToken).getWord().equals(";"))
            			{
            			    node = new DefaultMutableTreeNode(";");
            			    parent.add(node);
            			    currentToken++;			    
            			}
            			else
              			  error(3);
                 } //end 2.6.6 execute RETURN function        			          			
             }
		  }
		   else
  			 error(3);
	   }// end of 2.6 PRINT PART
	  return error;
	  
	}
	
   return error;
  }
  
  //3.rule_ASSIGNMENT
  private static boolean rule_ASSIGNMENT(DefaultMutableTreeNode parent) 
  {
		DefaultMutableTreeNode node;
		boolean error;
		
		if (currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("IDENTIFIER"))
		{
	        node = new DefaultMutableTreeNode("IDENTIFIER" + "(" + tokens.get(currentToken).getWord() + ")");
	        parent.add(node);
	        String m=tokens.get(currentToken).getWord();
	        currentToken++;	
	        
	        if(SemanticAnalyzer.getSymbolTable().get(m)==null)
	        {
	        	int n=tokens.get(currentToken).getLine();
	        	SemanticAnalyzer.error(gui, 0, n,m);	        	  	        	
	        }
	        
	        else if(SemanticAnalyzer.getSymbolTable().get(m)!=null)
	        {
		        String n= SemanticAnalyzer.getSymbolTable().get(m).get(0).getType();
		        SemanticAnalyzer.pushStack(n); 
		        currentToken++;
		        
				if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("="))
				{
			        node = new DefaultMutableTreeNode("=");
			        parent.add(node);
			        currentToken++;	  
				}
						
			    node = new DefaultMutableTreeNode("expersion");
			    parent.add(node);
			    error = rule_EXPRESSION(node);
			    
			    String x = SemanticAnalyzer.popStack(); 
			    String y = SemanticAnalyzer.popStack(); 
			    String result = SemanticAnalyzer.calculateCube(x, y, "=");
			    
			    CodeGenerator.addInstruction("sto", m, "0");
			    
			    if (!result.equals("OK"))
			    { 
			    	int line = tokens.get(currentToken).getLine();
			    	SemanticAnalyzer.error(gui, 2, line,x);
			    } 
			    
			    return error;
	        }
	        
			if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("="))
			{
		        node = new DefaultMutableTreeNode("=");
		        parent.add(node);
		        currentToken++;	  
			}

		}
			
	    node = new DefaultMutableTreeNode("expersion");
	    parent.add(node);
	    error = rule_EXPRESSION(node);
		
		return error;
  }
  
  //4.rule_VARIABLE
  private static boolean rule_VARIABLE(DefaultMutableTreeNode parent) 
  {
	    
		DefaultMutableTreeNode node;
		boolean error=false;
		String type="";
		String name="";
		
		
		if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("int"))
		{
	        node = new DefaultMutableTreeNode("int");
	        parent.add(node);
	        type="int";
	        currentToken++;
	        	        
		}
		
		else if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("float"))
		{
	        node = new DefaultMutableTreeNode("float");
	        parent.add(node);
	        type="float";
	        currentToken++;
	        	        
		}
		
		else if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("boolean"))
		{
	        node = new DefaultMutableTreeNode("boolean");
	        parent.add(node);
	        type="boolean";
	        currentToken++;
	        	        
		}
		
		else if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("char"))
		{
	        node = new DefaultMutableTreeNode("char");
	        parent.add(node);
	        type="char";
	        currentToken++;
	        	        
		}
				
		else if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("string"))
		{
	        node = new DefaultMutableTreeNode("string");
	        parent.add(node);
	        type="string";
	        currentToken++; 
	        	        
		}	
		
		else if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("void"))
		{
	        node = new DefaultMutableTreeNode("void");
	        parent.add(node);
	        type="string";
	        currentToken++;
		}
		
		if (currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("IDENTIFIER"))
		{
	        node = new DefaultMutableTreeNode("IDENTIFIER" + "(" + tokens.get(currentToken).getWord() + ")");
	        parent.add(node);
	        name=tokens.get(currentToken).getWord();
	        SemanticAnalyzer.checkVariable(gui,tokens.get(currentToken-1).getWord(), tokens.get(currentToken).getWord(),tokens,currentToken);
			CodeGenerator.addVariable(type,name);
	        currentToken++;	  
	        return true;
		}
			  
	  return false;
  }
  
  //5.rule_IF
  private static boolean rule_IF(DefaultMutableTreeNode parent) 
  {
	DefaultMutableTreeNode node;
	boolean error;
	String label1 = "e"+count;
	count++;
	String label2 = "e"+count++;
	if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("if"))
	{
        node = new DefaultMutableTreeNode("if");
        parent.add(node);
        currentToken++;
	}
	
	if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("("))
	{
        node = new DefaultMutableTreeNode("(");
        parent.add(node);
        currentToken++;
	}
	
	 node = new DefaultMutableTreeNode("experssion");
	 parent.add(node);
	 error = rule_EXPRESSION(node);	 
	 String x = SemanticAnalyzer.popStack(); 
	   
	   if (!x.equals("boolean"))
	   { 
		   int n=tokens.get(currentToken).getLine();
		   SemanticAnalyzer.error(gui, 3, n,x);
	   } 
	
	if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals(")"))
	{
	     node = new DefaultMutableTreeNode(")");
	     parent.add(node);	     
	     currentToken++;
	     CodeGenerator.addInstruction("jmc","#"+label1,"false");
		 node = new DefaultMutableTreeNode("program");
		 parent.add(node);
		 error = rule_PROGRAM(node);
	}
	 

	if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("else"))
	{
	     node = new DefaultMutableTreeNode("else");
	     parent.add(node);
	     CodeGenerator.addInstruction("jmp", "#"+label2, "0");
	     line = CodeGenerator.instructions.size()+1;
	     CodeGenerator.addLabel(label1, line);
	     currentToken++;
	     
		 node = new DefaultMutableTreeNode("program");
		 parent.add(node);
		 error= rule_PROGRAM(node);	
		 line = CodeGenerator.instructions.size()+1;
		 CodeGenerator.addLabel(label2, line);

	}
	
	  return error;
  } 
  
  //6.rule_WHILE
  private static boolean rule_WHILE(DefaultMutableTreeNode parent) 
  {
	DefaultMutableTreeNode node;
	boolean error;
	String label1="e"+count;
	count++;
	String label2="e"+count++;
	
	CodeGenerator.addLabel(label2, line);
	
	if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("while"))
	{
	   node = new DefaultMutableTreeNode("while");
	   parent.add(node);
	   currentToken++;
	}
	
	if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("("))
	{
	   node = new DefaultMutableTreeNode("(");
	   parent.add(node);
	   currentToken++;
	}	   
	   node = new DefaultMutableTreeNode("expression");
	   parent.add(node);	   
	   error=rule_EXPRESSION(node);
	   
	   
	   String x = SemanticAnalyzer.popStack(); 
	   if (!x.equals("boolean"))
	   { 
		   int n=tokens.get(currentToken).getLine();
		   SemanticAnalyzer.error(gui, 3, n,x);
	   } 
	   
	if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals(")"))
	{
	   node = new DefaultMutableTreeNode(")");
	   parent.add(node);
	   currentToken++;
	   CodeGenerator.addInstruction("jmc","#"+label1,"false");
	   node = new DefaultMutableTreeNode("program");
	   parent.add(node);	   
	   error=rule_PROGRAM(node); 
	   CodeGenerator.addInstruction("jmp","#"+label2,"0");
	   
	   line=CodeGenerator.instructions.size()+1;
	   CodeGenerator.addLabel(label1, line);
	   
	} 
		
	  return error;
  }
  
  //7.rule_RETURN
  private static boolean rule_RETURN(DefaultMutableTreeNode parent) 
  {
	DefaultMutableTreeNode node;
	boolean error=false;
	
	if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("return"))
	{
	   node = new DefaultMutableTreeNode("return");
	   parent.add(node);
	   CodeGenerator.addInstruction("opr", "1", "0");
	   currentToken++;
	   return true;
	}
	
	return false;
  }
  
  //8.rule_PRINT
  private static boolean rule_PRINT(DefaultMutableTreeNode parent) 
  {	
	DefaultMutableTreeNode node;
	boolean error;
	
	if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("print"))
	{
	   node = new DefaultMutableTreeNode("print");
	   parent.add(node);
	   currentToken++;
	}
	
	if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("("))
	{
		   node = new DefaultMutableTreeNode("(");
		   parent.add(node);
		   currentToken++;
	}
	
	   node = new DefaultMutableTreeNode("expression");
	   parent.add(node);
	   error=rule_EXPRESSION(node);
	   
	if(currentToken < tokens.size() && tokens.get(currentToken).getWord().equals(")"))
	{
	    node = new DefaultMutableTreeNode(")");
	    parent.add(node);
	    currentToken++;
	}
	if(currentToken < tokens.size() && !tokens.get(currentToken).getWord().equals("\""))
		CodeGenerator.addInstruction("opr", "21", "0");
	else
		CodeGenerator.addInstruction("opr", "20", "0");
	
   return error;
  }
  
  //9.rule_EXPRESSION
  private static boolean rule_EXPRESSION(DefaultMutableTreeNode parent) 
  {
	 boolean error;
	 boolean twiceHere;
	 DefaultMutableTreeNode node;
	 node = new DefaultMutableTreeNode("X");
     parent.add(node);
     error = rule_X(node);
     
     while (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("|")) 
     {
    	 if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("|")) 
         {
          node = new DefaultMutableTreeNode("|");
          parent.add(node);
          String loopOperator="|";
          currentToken++;
          
          node = new DefaultMutableTreeNode("X");
          parent.add(node);
          twiceHere=true;
          error = rule_X(node);
          
          if (twiceHere)
          {
        	 String x = SemanticAnalyzer.popStack(); 
        	 String y = SemanticAnalyzer.popStack(); 
        	 String result = SemanticAnalyzer.calculateCube(x, y, loopOperator);
        	 SemanticAnalyzer.pushStack(result); 
        	 CodeGenerator.addInstruction("opr", "8", "0");
        	 twiceHere = false; // reset the flag 
          } 
          
         } 
     }
	  
	  return error;
  }
  
  //10.rule_X
  private static boolean rule_X(DefaultMutableTreeNode parent) 
  {
	 boolean error;
	 DefaultMutableTreeNode node;
	 boolean twiceHere;
	 node = new DefaultMutableTreeNode("Y");
     parent.add(node);
     error = rule_Y(node);
	     
    while (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("&")) 
    {
	    if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("&")) 
	      {
            node = new DefaultMutableTreeNode("&");
            parent.add(node);
            String loopOperator="&";
            currentToken++;
	          
            node = new DefaultMutableTreeNode("Y");
            parent.add(node);
            twiceHere=true;
            error = rule_Y(node);
            
            if (twiceHere)
            {
          	 String x = SemanticAnalyzer.popStack(); 
          	 String y = SemanticAnalyzer.popStack(); 
          	 String result = SemanticAnalyzer.calculateCube(x, y, loopOperator);
          	 SemanticAnalyzer.pushStack(result); 
          	 CodeGenerator.addInstruction("opr", "9", "0");
          	 twiceHere = false; // reset the flag 
            } 
         } 
     }
    
    return error;
  }
  
  //11.rule_Y
  private static boolean rule_Y(DefaultMutableTreeNode parent) 
  {	
	boolean error;
	DefaultMutableTreeNode node;
	boolean operatorWasUSed=false;
	
    if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("!")) 
    {
      operatorWasUSed=true;  
  	  node = new DefaultMutableTreeNode("!");
      parent.add(node);       
      currentToken++;
    } 
    
    node = new DefaultMutableTreeNode("R");
    parent.add(node);
    error = rule_R(node);
    
    if (operatorWasUSed)
    {
    	 String x = SemanticAnalyzer.popStack(); 
    	 String result = SemanticAnalyzer.calculateCube(x, "!");
    	 SemanticAnalyzer.pushStack(result); 	
    	 CodeGenerator.addInstruction("opr", "10", "0");
    }   
   return error;
  }
  
  //12.rule_R
  private static boolean rule_R(DefaultMutableTreeNode parent) 
  {	
	DefaultMutableTreeNode node;
	boolean error;
	boolean twiceHere;
    node = new DefaultMutableTreeNode("E");
    parent.add(node);
    error = rule_E(node);
    
    
    while (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals(">") ||
    	   currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("<")||
    	   currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("==")||
    	   currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("!="))
    {
	    if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals(">")) 
	      {
          node = new DefaultMutableTreeNode(">");
          parent.add(node);
          String loopOperator=">";
          currentToken++;
	          
          node = new DefaultMutableTreeNode("E");
          parent.add(node);
          twiceHere=true;
          error = rule_E(node);
          
          if (twiceHere)
          {
        	 String x = SemanticAnalyzer.popStack(); 
        	 String y = SemanticAnalyzer.popStack(); 
        	 String result = SemanticAnalyzer.calculateCube(x, y, loopOperator);
        	 SemanticAnalyzer.pushStack(result); 
        	 CodeGenerator.addInstruction("opr", "11", "0");
        	 twiceHere = false; // reset the flag 
          }  
       } 
	    
	    else if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("<")) 
	      {
	          node = new DefaultMutableTreeNode("<");
	          parent.add(node);
	          String loopOperator="<";
	          currentToken++;
		          
	          node = new DefaultMutableTreeNode("E");
	          parent.add(node);
	          twiceHere=true;
	          error = rule_E(node);
	          
	          if (twiceHere)
	          {
	        	 String x = SemanticAnalyzer.popStack(); 
	        	 String y = SemanticAnalyzer.popStack(); 
	        	 String result = SemanticAnalyzer.calculateCube(x, y, loopOperator);
	        	 SemanticAnalyzer.pushStack(result); 
	        	 CodeGenerator.addInstruction("opr", "12", "0");
	        	 twiceHere = false; // reset the flag 
	          } 
	       } 
	    
	    else if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("==")) 
	      {
	          node = new DefaultMutableTreeNode("==");
	          parent.add(node);
	          String loopOperator="==";
	          currentToken++;
		          
	          node = new DefaultMutableTreeNode("E");
	          parent.add(node);
	          twiceHere=true;
	          error = rule_E(node);
	          
	          if (twiceHere)
	          {
	        	 String x = SemanticAnalyzer.popStack(); 
	        	 String y = SemanticAnalyzer.popStack(); 
	        	 String result = SemanticAnalyzer.calculateCube(x, y, loopOperator);
	        	 SemanticAnalyzer.pushStack(result); 
	        	 CodeGenerator.addInstruction("opr", "15", "0");
	        	 twiceHere = false; // reset the flag 
	          } 
	       } 
	    
	    else if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("!=")) 
	      {
	          node = new DefaultMutableTreeNode("!=");
	          parent.add(node);
	          String loopOperator="!=";
	          currentToken++;
		          
	          node = new DefaultMutableTreeNode("E");
	          parent.add(node);
	          twiceHere=true;
	          error = rule_E(node);
	          
	          if (twiceHere)
	          {
	        	 String x = SemanticAnalyzer.popStack(); 
	        	 String y = SemanticAnalyzer.popStack(); 
	        	 String result = SemanticAnalyzer.calculateCube(x, y, loopOperator);
	        	 SemanticAnalyzer.pushStack(result); 
	        	 CodeGenerator.addInstruction("opr", "16", "0");
	        	 twiceHere = false; // reset the flag 
	          } 
	       } 
    }
    	
	return error;
  }

  //13.rule_E
  private static boolean rule_E(DefaultMutableTreeNode parent) {
    boolean error;
    DefaultMutableTreeNode node;
    boolean twiceHere;
    node = new DefaultMutableTreeNode("A");
    parent.add(node);
    error = rule_A(node);
    
    while (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("+") || 
    	   currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("-")) 
    {
       if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("+")) 
       {
        node = new DefaultMutableTreeNode("+");
        parent.add(node);
        String loopOperator="+";
        currentToken++;
        
        node = new DefaultMutableTreeNode("A");
        parent.add(node);
        twiceHere=true;
        error = rule_A(node);
        
        if (twiceHere)
          {
        	 String x = SemanticAnalyzer.popStack(); 
        	 String y = SemanticAnalyzer.popStack(); 
        	 String result = SemanticAnalyzer.calculateCube(x, y, loopOperator);
        	 SemanticAnalyzer.pushStack(result); 
        	 CodeGenerator.addInstruction("opr", "2", "0");
        	 twiceHere = false; // reset the flag 
          } 
       } 
       
       else if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("-")) 
       {
        node = new DefaultMutableTreeNode("-");
        parent.add(node);
        String loopOperator="-";
        currentToken++;
        
        node = new DefaultMutableTreeNode("A");
        parent.add(node);
        twiceHere=true;
        error = rule_A(node);
        
        if (twiceHere)
        {
      	 String x = SemanticAnalyzer.popStack(); 
      	 String y = SemanticAnalyzer.popStack(); 
      	 String result = SemanticAnalyzer.calculateCube(x, y, loopOperator);
      	 SemanticAnalyzer.pushStack(result); 
      	 CodeGenerator.addInstruction("opr", "3", "0");
      	 twiceHere = false; // reset the flag 
        }       
      }      
    }
    return error;
  }

  //14.rule_A
  private static boolean rule_A(DefaultMutableTreeNode parent) {
    boolean error;
    boolean twiceHere;
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("B");
    parent.add(node);
    error = rule_B(node);
    
    while (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("*") || 
    	   currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("/")) 
    {
      if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("*")) 
      {
        node = new DefaultMutableTreeNode("*");
        parent.add(node);
        String loopOperator="*";
        currentToken++;
        
        node = new DefaultMutableTreeNode("B");
        parent.add(node);
        twiceHere=true;
        error = rule_B(node);
        
        if (twiceHere)
        {
      	 String x = SemanticAnalyzer.popStack(); 
      	 String y = SemanticAnalyzer.popStack(); 
      	 String result = SemanticAnalyzer.calculateCube(x, y, loopOperator);
      	 SemanticAnalyzer.pushStack(result); 
      	 CodeGenerator.addInstruction("opr", "4", "0");
      	 twiceHere = false; // reset the flag 
        }  

      } 
      
      else if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("/")) 
      {
        node = new DefaultMutableTreeNode("/");
        parent.add(node);
        String loopOperator="/";
        currentToken++;
        
        node = new DefaultMutableTreeNode("B");
        parent.add(node);
        twiceHere=true;
        error = rule_B(node);
        
        if (twiceHere)
        {
      	 String x = SemanticAnalyzer.popStack(); 
      	 String y = SemanticAnalyzer.popStack(); 
      	 String result = SemanticAnalyzer.calculateCube(x, y, loopOperator);
      	 SemanticAnalyzer.pushStack(result); 
      	CodeGenerator.addInstruction("opr", "5", "0");
      	 twiceHere = false; // reset the flag 
        }  
      }
      
    }
    return error;
  }

  //15.rule_B
  private static boolean rule_B(DefaultMutableTreeNode parent) {
    boolean error;
    DefaultMutableTreeNode node;
    boolean operatorWasUSed=false;

    if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("-")) 
    {
      operatorWasUSed=true;
      node = new DefaultMutableTreeNode("-");
      parent.add(node);
      CodeGenerator.addInstruction("lit", "0", "0");
      currentToken++;
    }
    
    node = new DefaultMutableTreeNode("C");
    parent.add(node);
    error = rule_C(node);
    
    if (operatorWasUSed)
    {
    	 String x = SemanticAnalyzer.popStack(); 
    	 String result = SemanticAnalyzer.calculateCube(x, "-");
    	 SemanticAnalyzer.pushStack(result); 
    	 CodeGenerator.addInstruction("opr", "3", "0");
    } 
    return error;
  }

  //16.rule_C
  private static boolean rule_C(DefaultMutableTreeNode parent) {
    boolean error = false;
    DefaultMutableTreeNode node;
    String value="";
    String currenttoken="";
    
// execute integer       
    if (currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("INTEGER")) 
    {
      node = new DefaultMutableTreeNode("integer" + "(" + tokens.get(currentToken).getWord() + ")");
      parent.add(node);
      SemanticAnalyzer.pushStack("int"); 
      value=tokens.get(currentToken).getWord();
      currenttoken=tokens.get(currentToken).getToken();
      currentToken++;
    }
 
// execute float   
    else if (currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("FLOAT")) 
    {
      node = new DefaultMutableTreeNode("float" + "(" + tokens.get(currentToken).getWord() + ")");
      parent.add(node);
      SemanticAnalyzer.pushStack("float"); 
      value=tokens.get(currentToken).getWord();
      currenttoken=tokens.get(currentToken).getToken();
      currentToken++;
    }
    
 // execute binary       
    else if (currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("BINARY")) 
    {
      node = new DefaultMutableTreeNode("binary" + "(" + tokens.get(currentToken).getWord() + ")");
      parent.add(node);
      SemanticAnalyzer.pushStack("int"); 
      value=tokens.get(currentToken).getWord();
      currenttoken=tokens.get(currentToken).getToken();
      currentToken++;
    }
    
// execute octal      
    else if (currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("OCTAL")) 
    {
      node = new DefaultMutableTreeNode("octal" + "(" + tokens.get(currentToken).getWord() + ")");
      parent.add(node);
      SemanticAnalyzer.pushStack("int"); 
      value=tokens.get(currentToken).getWord();
      currenttoken=tokens.get(currentToken).getToken();
      currentToken++;
    }
    
 // execute hex  
    else if (currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("HEXADECIMAL")) 
    {
      node = new DefaultMutableTreeNode("hex" + "(" + tokens.get(currentToken).getWord() + ")");
      parent.add(node);
      SemanticAnalyzer.pushStack("int"); 
      value=tokens.get(currentToken).getWord();
      currenttoken=tokens.get(currentToken).getToken();
      currentToken++;
    }
// execute character  
    else if (currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("CHARACTER")) 
    {
      node = new DefaultMutableTreeNode("char" + "(" + tokens.get(currentToken).getWord() + ")");
      parent.add(node);
      SemanticAnalyzer.pushStack("char"); 
      value=tokens.get(currentToken).getWord();
      currenttoken=tokens.get(currentToken).getToken();
      currentToken++;
    }
    
 // execute string
    else if (currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("STRING")) 
    {
      node = new DefaultMutableTreeNode("string" + "(" + tokens.get(currentToken).getWord() + ")");
      parent.add(node);
      SemanticAnalyzer.pushStack("string"); 
      value=tokens.get(currentToken).getWord();
      currenttoken=tokens.get(currentToken).getToken();
      currentToken++;
    }
    
// execute keyword true and false
    else if (currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("KEYWORD")) 
    {
    	if(tokens.get(currentToken).getWord().equals("true"))
    	{
    	   node = new DefaultMutableTreeNode("true" + "(" + tokens.get(currentToken).getWord() + ")");
    	   parent.add(node);
    	   SemanticAnalyzer.pushStack("boolean"); 
    	   value=tokens.get(currentToken).getWord();
    	   currenttoken=tokens.get(currentToken).getToken();
    	   currentToken++;
    	}
    	
    	else if(tokens.get(currentToken).getWord().equals("false"))
    	{
  	      node = new DefaultMutableTreeNode("false" + "(" + tokens.get(currentToken).getWord() + ")");
  	      parent.add(node);
  	      SemanticAnalyzer.pushStack("boolean"); 
  	      value=tokens.get(currentToken).getWord();
  	      currenttoken=tokens.get(currentToken).getToken();
  	      currentToken++;
  	    }
    }
// execute identifier
    else if (currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("IDENTIFIER")) 
    {
      node = new DefaultMutableTreeNode("identifier" + "(" + tokens.get(currentToken).getWord() + ")");
      parent.add(node);
      String x=tokens.get(currentToken).getWord();
      
      if(SemanticAnalyzer.getSymbolTable().get(x)!=null)
      {
          String y= SemanticAnalyzer.getSymbolTable().get(x).get(0).getType();
          SemanticAnalyzer.pushStack(y); 
          value=tokens.get(currentToken).getWord();
          currenttoken=tokens.get(currentToken).getToken();
          currentToken++;
      }
      else
      {
    	  int n=tokens.get(currentToken).getLine();
    	  SemanticAnalyzer.error(gui, 0, n,x);
    	  SemanticAnalyzer.pushStack(x); 
    	  currentToken++;
      }
    } 
    
// execute rule expression
    else if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("(")) 
    {
      node = new DefaultMutableTreeNode("(");
      parent.add(node);
      currentToken++;
      //
      node = new DefaultMutableTreeNode("expression");
      parent.add(node);
      error = rule_EXPRESSION(node);
      //
      node = new DefaultMutableTreeNode(")");
      parent.add(node);
      currentToken++;      
    } 
    
    else 
    {
      return true;
    }
    
    if(currenttoken.equals("IDENTIFIER"))
    	CodeGenerator.addInstruction("lod", value, "0");
    else if(currenttoken.equals("INTEGER")||currenttoken.equals("FLOAT")||currenttoken.equals("BINARY")||
    		currenttoken.equals("OCTAL")||currenttoken.equals("HEXADECIMAL")||currenttoken.equals("CHARACTER")||
    		currenttoken.equals("STRING")||currenttoken.equals("KEYWORD"))
       CodeGenerator.addInstruction("lit", value, "0");
    
    return false;
  }

}