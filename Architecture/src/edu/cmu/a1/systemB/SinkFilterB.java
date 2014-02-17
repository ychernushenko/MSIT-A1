package edu.cmu.a1.systemB;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import edu.cmu.a1.modules.FilterFramework;
import edu.cmu.a1.util.Configuration;
import edu.cmu.a1.util.Record;

public class SinkFilterB extends FilterFramework
{
	private String fileName;
	public SinkFilterB(int inputPortNum, int outputPortNum, String fileName) {
		super(inputPortNum, outputPortNum);
		this.fileName = fileName;
	}

	public void run()
	{
		// Next we write a message to the terminal to let the world know we are alive...
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName)) ;
			String header = "Time:             Pressure:\n"
					+ "-----------------------------------------------------\n";
			bufferedWriter.write(header);
			while (true)
			{
				try
				{
					byte[] recordBuffer = new byte[Configuration.RecordLength];
					for (int i=0; i<Configuration.RecordLength; i++ ){
						recordBuffer[i] = ReadFilterInputPort(0);	// This is where we read the byte from the stream...
					}
					Record record = new Record(recordBuffer);
					Configuration.Field formatList[] = {
							Configuration.Field.TIMESTAMP,
							Configuration.Field.PRESSURE};

					String str= record.printFormat(formatList);
					bufferedWriter.write(str+"\n");

				} // try
				catch (EndOfStreamException e){
					ClosePorts();
					bufferedWriter.close();
					break;
				} // catch
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			
		}
	} // run

} // SingFilter