package hackAssembler;

public abstract class Instruction {
	static int binInstructionSize = 16;
	String instruction;

	public Instruction(String instruction) {
		this.instruction = instruction;
	}

	public static Instruction parseInstruction(String ln) {
		Instruction inst;
		if (isAInstruction(ln)) {
			inst = new AInstruction(ln);
		} else {
			inst = new CInstruction(ln);
		}
		return inst;
	}

	public static boolean isAInstruction(String ln) {
		if (ln.charAt(0) == AInstruction.identifier) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isCInstruction(String ln){
		return !isAInstruction(ln);
	}

	public abstract String compile();

}
