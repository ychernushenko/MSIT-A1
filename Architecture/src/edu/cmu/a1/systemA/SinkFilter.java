package edu.cmu.a1.systemA;
import edu.cmu.a1.common.Configuration;
import edu.cmu.a1.common.Converter;
import edu.cmu.a1.common.FilterFramework;
import edu.cmu.a1.common.Record;

public class SinkFilter extends FilterFramework
{
	public SinkFilter(int inputPortNum, int outputPortNum) {
		super(inputPortNum, outputPortNum);
	}

	public void run()
	{
		long measurement;				// This is the word used to store all measurements - conversions are illustrated.
		int id;							// This is the measurement id

		// Next we write a message to the terminal to let the world know we are alive...

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
						Configuration.Field.TEMPERATURE,
						Configuration.Field.ALTITUDE};

				String str= record.printFormat(formatList);
				System.out.println(str);

			} // try

			catch (EndOfStreamException e)
			{
				ClosePorts();
				break;

			} // catch
		}

	} // run

} // SingFilter