package hackAssembler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class InMemoryCode {
	private List<String> currentCode;
	private List<String> newCode;

	public InMemoryCode() {
		initNewCode();
	}

	private void initCurrentCode() {
		currentCode = new LinkedList<>();
	}

	private void initNewCode() {
		newCode = new LinkedList<>();
	}

	public void new2Current() {
		currentCode = newCode;
		initNewCode();
	}

	public void addLine2New(String ln) {
		newCode.add(ln);
	}

	/**
	 * Returns null if there are no more lines
	 * 
	 * @return
	 */
	public String getLineFromCurrent() {
		return currentCode.remove(0);
	}

	/**
	 * Checks if empty, if empty then new2current;
	 * 
	 * @return
	 */
	public boolean currentIsEmpty() {
		boolean isEmpty = currentCode.isEmpty();
		if (isEmpty) {
			new2Current();
		}
		return isEmpty;
	}

	public void readNewFromFile(BufferedReader br) throws IOException {
		while (br.ready()) {
			String ln = br.readLine();
			addLine2New(ln);
		}
	}

	public void writeCurrent2File(BufferedWriter bw) throws IOException {
		for (String ln : currentCode) {
			bw.write(ln);
			bw.newLine();
		}
		bw.close();
	}

	@Override
	public String toString() {
		String current = "Current:";
		String newc = "New:";
		current += currentCode == null ? "Null" : currentCode.toString();
		newc += newCode == null ? "Null" : newCode.toString();
		return current + ", " + newc;
	}
}
