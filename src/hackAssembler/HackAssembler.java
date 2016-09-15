package hackAssembler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

//TODO: change use of buffered pipe to a string queue.

public class HackAssembler {
	static String commentStarter = "//";

	ArrayList<Instruction> program = new ArrayList<>();
	SymbolProcessor symbolProcessor = new SymbolProcessor();

	public static void main(String[] args) {
		// in Filename to assemble .asm

		if (args.length != 1) {
			endAssembler("Incorrect number of parameters");
		}
		String filePath = args[0];
		try {
			FileReader fr = new FileReader(filePath);
			BufferedReader br = new BufferedReader(fr);
			String destFile = filePath.replaceFirst(".asm", ".hack");
			BufferedWriter bw = Files.newBufferedWriter(Paths.get(destFile));
			HackAssembler asm = new HackAssembler();
			asm.assemble(br, bw);
			fr.close();
			bw.close();
		} catch (IOException e) {
			endAssembler("File " + filePath + " could not be opened");
		}
	}

	public static void endAssembler(String msj) {
		System.err.println(msj);
		System.exit(1);
	}

	/**
	 * in File to assemble out wroten .hack assembled file
	 * 
	 * @param fr
	 * @throws IOException
	 */
	public void assemble(BufferedReader originBuffReader, BufferedWriter destinyBuffWriter) throws IOException {
		InMemoryCode code = new InMemoryCode();
		code.readNewFromFile(originBuffReader);
		code.new2Current();

		System.out.println("READ:" + code);

		parseLabels(code);
		System.out.println("Cleaned-LabelFree:" + code);

		translateSymbols(code);
		System.out.println("Translated:" + code);
		// System.out.println("SymbolTable:"+symbolProcessor.symbolTableToString());

		parseFile(code);
		compileProgram(code);
		
		code.new2Current();
		code.writeCurrent2File(destinyBuffWriter);
		
	}

	private void translateSymbols(InMemoryCode code) throws IOException {
		while (!code.currentIsEmpty()) {
			String ln = code.getLineFromCurrent();
			// System.out.println(ln);
			String ln2Write = ln;
			if (SymbolProcessor.isSymbol(ln)) {
				String translatedLn = symbolProcessor.processSymbol(ln);
				ln2Write = translatedLn;
			}
			code.addLine2New(ln2Write);
		}
	}

	private void parseLabels(InMemoryCode code) throws IOException {
		int currentLine = -1;// Counting of lines starts from 0 and there is no
								// valid line yet
		while (!code.currentIsEmpty()) {
			String ln = code.getLineFromCurrent();
			ln = cleanLine(ln);
			if (ln != null) {
				if (symbolProcessor.isLabel(ln)) {
					symbolProcessor.processLabel(ln, currentLine);
				} else {
					code.addLine2New(ln);
					currentLine++;
				}
			}
		}
	}

	private void parseFile(InMemoryCode code) throws IOException {
		while (!code.currentIsEmpty()) {
			String ln = code.getLineFromCurrent();
			// System.out.println(ln);
			parseLine(ln);
		}
	}

	private void compileProgram(InMemoryCode code) throws IOException {
		for (Instruction inst : program) {
			String binLine = inst.compile();
			// add line to file
			System.out.println(binLine);
			code.addLine2New(binLine);
		}
	}

	public void parseLine(String ln) {
		ln = cleanLine(ln);
		// skip empty line
		if (ln == null) {
			return;
		}
		// if not comment:
		Instruction currentInstruction = Instruction.parseInstruction(ln);
		// may throw exception if invalid line
		program.add(currentInstruction);
	}

	private String cleanLine(String ln) {
		int commentIndex = ln.indexOf(commentStarter);
		if (commentIndex >= 0) { // Instruction has a comment
			// Remove comment
			ln = ln.substring(0, commentIndex);
		}
		ln = ln.trim();
		// Ignore empty line
		if (ln.length() == 0) {
			return null;
		}
		return ln;
	}

}
