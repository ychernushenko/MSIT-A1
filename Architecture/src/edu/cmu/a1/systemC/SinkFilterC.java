package edu.cmu.a1.systemC;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import edu.cmu.a1.util.Configuration;
import edu.cmu.a1.util.FilterFramework;
import edu.cmu.a1.util.Record;

public class SinkFilterC extends FilterFramework
{
	private String fileName;
	public SinkFilterC(int inputPortNum, int outputPortNum, String fName) {
		super(inputPortNum, outputPortNum);
		this.fileName = fName;
	}
	
	public void run()
	{
		// Next we write a message to the terminal to let the world know we are alive...
		System.out.print( "\n" + this.getName() + "::SinkFilter C starts working ");
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(this.fileName)) ;
			//Time:               Temperature (C):    Altitude (m):
			//-----------------------------------------------------
			String header = "Time:     Velocity:    Altitude:    Pressure:    Temperature:    Attitude:\n"
					+ "------------------------------------------------------------------------------\n";
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
							Configuration.Field.VELOCITY,
							Configuration.Field.ALTITUDE,
							Configuration.Field.PRESSURE,
							Configuration.Field.TEMPERATURE,
							Configuration.Field.ATTITUDE};

					String str= record.printFormat(formatList);
					bufferedWriter.write(str+"\n");

				} // try
				catch (EndOfStreamException e){
					System.out.print( "\n" + this.getName() + "::SinkFilter C Exiting; all bytes are written in output file" );
					ClosePorts();
					bufferedWriter.close();
					break;
				} // catch
			}
		} catch (IOException iox) {
			System.out.print( "\n" + this.getName() + ":: SinkFilter C :: Problem writing output data file::" + iox );
		}
	} // run

} // SingFilter