package hackAssembler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import bufferedPipe.BufferedPipe;

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
			BufferedWriter fw = Files.newBufferedWriter(Paths.get(destFile));
			HackAssembler asm = new HackAssembler();
			asm.assemble(br, fw);
			fr.close();
			fw.close();
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
		BufferedPipe labelSymbolPipe = new BufferedPipe();
		BufferedPipe symbolParserPipe = new BufferedPipe();

		parseLabels(originBuffReader, labelSymbolPipe.getBufferedWriter());
		translateSymbols(labelSymbolPipe.getBufferedReader(), symbolParserPipe.getBufferedWriter());

		System.out.println(symbolProcessor.symbolTableToString());

		parseFile(symbolParserPipe.getBufferedReader());
		compileProgram(destinyBuffWriter);
	}

	private void translateSymbols(BufferedReader br, BufferedWriter bw) throws IOException {
		while (br.ready()) {
			String ln = br.readLine();
			// System.out.println(ln);
			if (SymbolProcessor.isSymbol(ln)) {
				String translatedLn = symbolProcessor.processSymbol(ln);
				bw.write(translatedLn);
				bw.newLine();
			} else {
				bw.write(ln);
				bw.newLine();
			}
		}
		bw.close();
	}

	private void parseLabels(BufferedReader br, BufferedWriter bw) throws IOException {
		int currentLine = -1;// Counting of lines starts from 0 and there is no
								// valid line yet
		while (br.ready()) {
			String ln = br.readLine();
			ln = cleanLine(ln);
			if (ln != null) {
				if (symbolProcessor.isLabel(ln)) {
					symbolProcessor.processLabel(ln, currentLine);
				} else {
					bw.write(ln);
					bw.newLine();
					currentLine++;
				}
			}
		}
		bw.close();
	}

	private void parseFile(BufferedReader br) throws IOException {
		while (br.ready()) {
			String ln = br.readLine();
			// System.out.println(ln);
			parseLine(ln);
		}
	}

	private void compileProgram(BufferedWriter fw) throws IOException {
		for (Instruction inst : program) {
			String binLine = inst.compile();
			// add line to file
			System.out.println(binLine);
			fw.write(binLine);
			fw.newLine();
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
		ln = ln.trim();
		int commentIndex = ln.indexOf(commentStarter);
		if (commentIndex >= 0) { // Instruction has a comment
			// Remove comment
			ln = ln.substring(0, commentIndex);
		}
		// Ignore empty line
		if (ln.length() == 0) {
			return null;
		}
		return ln;
	}

}
