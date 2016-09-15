package hackAssembler;

public class AInstruction extends Instruction {
	static char identifier = '@';
	static char aInstructionIdentifier = '0';

	int address;

	public AInstruction(String instruction) {
		super(instruction);
		String sValue = instruction.substring(1);
		address = Integer.parseInt(sValue);
	}

	@Override
	public String compile() {
		// TODO check if address value is below 2^15 if it is throw exception
		String instructionSizePadderFormat = "%" + (Instruction.binInstructionSize - 1) + "s";
		String zeroPaddedBinAddress = String.format(instructionSizePadderFormat, getBinAddress()).replace(' ', '0');
		String binInstruction = aInstructionIdentifier + zeroPaddedBinAddress;
		return binInstruction;
	}

	public String getBinAddress() {
		return Integer.toBinaryString(address);
	}

}
