package bufferedPipe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

public class BufferedPipe {
	private PipedWriter pipedWriter;
	private PipedReader pipedReader;
	
	private BufferedWriter bufferedPipeWriter; 
	private BufferedReader bufferedPipeReader; 

	public BufferedPipe() {
		pipedWriter = new PipedWriter();
		try {
			pipedReader = new PipedReader(pipedWriter);
		} catch (IOException e) {
			e.printStackTrace();
		}

		bufferedPipeWriter = new BufferedWriter(pipedWriter);
		bufferedPipeReader = new BufferedReader(pipedReader);
	}
	
	public BufferedReader getBufferedReader(){
		return bufferedPipeReader;
	}
	
	public BufferedWriter getBufferedWriter(){
		return bufferedPipeWriter;
	}

}
