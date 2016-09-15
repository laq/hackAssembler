package hackAssembler;

import java.util.Hashtable;
import java.util.Map;

import hackAssembler.LanguagePartAsm.LanguagePart;

public class CInstruction extends Instruction {
	static String cInstructionIdentifier = "111";
	static char destinationChar = '=';
	static char jumpChar = ';';
	
	static Map<String,String> commandMap = new Hashtable<>();
	static Map<String,String> destinationMap = new Hashtable<>();
	static Map<String,String> jumpMap = new Hashtable<>();
	
	static {
		commandMap.put("0",  "0101010");
		commandMap.put("1",  "0111111");
		commandMap.put("-1", "0111010");
		commandMap.put("D",  "0001100");
		commandMap.put("A",  "0110000");
		commandMap.put("M",  "1110000");
		commandMap.put("!D", "0001101");
		commandMap.put("!A", "0110001");
		commandMap.put("!M", "1110001");
		commandMap.put("-D", "0001111");
		commandMap.put("-A", "0110011");
		commandMap.put("-M", "1110011");
		commandMap.put("D+1","0011111");
		commandMap.put("A+1","0110111");
		commandMap.put("M+1","1110111");
		commandMap.put("D-1","0001110");
		commandMap.put("A-1","0110010");
		commandMap.put("M-1","1110010");
		commandMap.put("D+A","0000010");
		commandMap.put("D+M","1000010");
		commandMap.put("D-A","0010011");
		commandMap.put("D-M","1010011");
		commandMap.put("A-D","0000111");
		commandMap.put("M-D","1000111");
		commandMap.put("D&A","0000000");
		commandMap.put("D&M","1000000");
		commandMap.put("D|A","0010101");
		commandMap.put("D|M","1010101");
		
		
		//Initialize dest map
		destinationMap.put("", "000");
		destinationMap.put("M", "001");
		destinationMap.put("D", "010");
		destinationMap.put("MD", "011");
		destinationMap.put("A", "100");
		destinationMap.put("AM", "101");
		destinationMap.put("AD", "110");
		destinationMap.put("AMD", "111");
		
		//Initialize jump map
		jumpMap.put("", "000");
		jumpMap.put("JGT", "001");
		jumpMap.put("JEQ", "010");
		jumpMap.put("JGE", "011");
		jumpMap.put("JLT", "100");
		jumpMap.put("JNE", "101");
		jumpMap.put("JLE", "110");
		jumpMap.put("JMP", "111");
		
	}
	

	LanguagePartAsm command;
	LanguagePartAsm destination;
	LanguagePartAsm jump;

	/**
	 * dest = command;jump
	 * 
	 * @param instruction
	 */
	public CInstruction(String instruction) {
		super(instruction);

		// Find destination
		int destinationIndex = instruction.indexOf(destinationChar);
		String sDestination = "";
		if (destinationIndex >= 0) {
			sDestination = instruction.substring(0, destinationIndex);
			instruction = instruction.substring(destinationIndex + 1);
		}
		destination = LanguagePartAsm.LanguagePartFactory(LanguagePart.DESTINATION, sDestination);
		
		// Find jump
		int jumpIndex = instruction.indexOf(jumpChar);
		String sJump = "";
		if (jumpIndex >= 0) {
			sJump = instruction.substring(jumpIndex + 1);
			instruction = instruction.substring(0, jumpIndex);
		}
		jump = LanguagePartAsm.LanguagePartFactory(LanguagePart.JUMP, sJump);

		//Command is what is not destination neither jump
		command = LanguagePartAsm.LanguagePartFactory(LanguagePart.COMMAND, instruction);

	}

	@Override
	public String compile() {
		
		String binInstruction = cInstructionIdentifier + command.getBin() + destination.getBin() + jump.getBin();
		return binInstruction;
	}

}
