package hackAssembler;

import java.util.Map;


public class LanguagePartAsm {
	public static enum LanguagePart {
		COMMAND, DESTINATION, JUMP
	};

	String name;
	String value;
	Map<String,String> map;

	public LanguagePartAsm(String name, String value, Map<String,String> map) {
		this.name = name;
		this.value = value;
		this.map = map;
	}

	public static LanguagePartAsm LanguagePartFactory(LanguagePart lPart, String value) {
		LanguagePartAsm lp = null;
		switch (lPart) {
			case COMMAND:
				lp = new LanguagePartAsm(lPart.name(), value, CInstruction.commandMap);
				break;
	
			case DESTINATION: {
				lp = new LanguagePartAsm(lPart.name(), value, CInstruction.destinationMap);
				break;
			}
			case JUMP: {
				lp = new LanguagePartAsm(lPart.name(), value, CInstruction.jumpMap);
				break;
			}
		}
		return lp;
	}

	public String getBin() {
		String bin = map.get(value);
		if (bin==null){
			System.err.println("Incorrect Syntax");
		}				
		return bin;
	}

}
