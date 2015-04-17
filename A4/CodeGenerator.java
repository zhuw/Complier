package A4;

import java.util.Vector;

/**
 *
 * @author javiergs
 */
public class CodeGenerator {
  
  public static final Vector<String> variables = new Vector<>();  
  public static final Vector<String> labels = new Vector<>();  
  public static final Vector<String> instructions = new Vector<>();

  static void addInstruction(String instruction, String p1, String p2) {
    instructions.add(instruction + " " + p1 + ", " + p2);
  }

  static void addLabel(String name, int value) {
	    labels.add("#"+name + ", global, " + value);
  }
    
  static void addVariable(String type, String name) {
    variables.add(name + ", " + type + ", global, 0" );
  }

  static void writeCode(Gui gui) {
    for (String variable : variables) {
      gui.writeCode(variable);    
    }
    for (String label : labels) {
      gui.writeCode(label);    
    }
    gui.writeCode("@");
    for (String instruction : instructions) {
      gui.writeCode(instruction);    
    }

  }
  
  static void clear(Gui gui) {
    variables.clear();
    instructions.clear();
  }  
}
