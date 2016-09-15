package hackAssembler;

import java.util.Hashtable;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SymbolProcessor {
	private static char labelStartChar = '(';
	private static char labelEndChar = ')';

	private Map<String, Integer> symbolTable = new Hashtable<>();
	private int topVariablePosition = 0;

	public SymbolProcessor() {
		fillSymbolTable();
	}

	private void fillSymbolTable() {
		// Add R symbols
		for (; topVariablePosition < 16; topVariablePosition++) {
			symbolTable.put("R" + topVariablePosition, topVariablePosition);
		}
		symbolTable.put("SP", 0);
		symbolTable.put("LCL", 1);
		symbolTable.put("ARG", 2);
		symbolTable.put("THIS", 3);
		symbolTable.put("THAT", 4);

		symbolTable.put("SCREEN", 16384);
		symbolTable.put("KBD", 24576);
	}

	public String processSymbol(String symbol) {
		symbol = symbol.substring(1); //leave only symbol (no identifier - @)
		// if symbol not in table add symbol
		if(!symbolTable.containsKey(symbol)){
			symbolTable.put(symbol, topVariablePosition);
			topVariablePosition++;
		}

		// return position
		return AInstruction.identifier+symbolTable.get(symbol).toString();
	}

	/**
	 * Process a line string in search for labels, if lable is found then add it
	 * to symbol table
	 * 
	 * @param ln
	 * @param currentLine
	 */
	public void processLabel(String ln, int currentLine) {
		if (isLabel(ln)) {
			String label = ln.substring(1, ln.length()-1);
			symbolTable.put(label, currentLine+1);
		}
	}
	
	/**
	 * Process a line string in search for labels, if lable is found then add it
	 * to symbol table
	 * 
	 * @param ln
	 * @param currentLine
	 */
	public boolean isLabel(String ln) {
		if (ln.charAt(0) == labelStartChar && ln.charAt(ln.length()-1)==labelEndChar) {
			return true;
		}
		return false;
	}
	
	public String symbolTableToString(){
		 return symbolTable.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toList()).toString();
	}

	public static boolean isSymbol(String ln) {
		if(Instruction.isAInstruction(ln)){
			try{
				new AInstruction(ln);
				return false;
			}catch(NumberFormatException e){
				return true; // Is an A instruction but not with number
			}			
		}
		return false;
	}
}
