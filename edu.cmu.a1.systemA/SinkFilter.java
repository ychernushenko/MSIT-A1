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

		Record record = new Record();
		while (true)
		{
			try
			{
				/***************************************************************************
				// We know that the first data coming to this filter is going to be an ID and
				// that it is IdLength long. So we first decommutate the ID bytes.
				 ****************************************************************************/
				Converter converter = new Converter();
				byte[] idBytes = new byte[Configuration.IdLength];
				for (int i=0; i<Configuration.IdLength; i++ ){
					idBytes[i] = ReadFilterInputPort(0);	// This is where we read the byte from the stream...
				}
				id = converter.byteToId(idBytes);

				/****************************************************************************
				// Here we read measurements. All measurement data is read as a stream of bytes
				// and stored as a long value.
				 *****************************************************************************/
				measurement = 0;
				byte[] measurementBytes = new byte[Configuration.MeasurementLength];
				for (int i=0; i<Configuration.MeasurementLength; i++ ){
					measurementBytes[i] = ReadFilterInputPort(0);	// This is where we read the byte from the stream...
				}
				measurement = converter.byteToMeasurement(measurementBytes);
				/****************************************************************************
				// We do check id and choose what attributes to be written in the System console.
				 ****************************************************************************/

				Configuration.Field formatList[] = {Configuration.Field.TIMESTAMP,Configuration.Field.TEMPERATURE,Configuration.Field.ALTITUDE};
				//We defined 
				switch (id) {
				case 0:// timestamp, when we read it we start creating a new string to output 
					String str= record.printFormat(formatList);
					System.out.println(str);
					record = new Record();
					record.setTimeStamp(measurement);
					break;
				case 4://temperature
					record.setTemperature(Double.longBitsToDouble(measurement));
					break;
				case 2://altitude
					record.setAltitude(Double.longBitsToDouble(measurement));
					break;
				default:
					break;
				}

			} // try

			catch (EndOfStreamException e)
			{
				ClosePorts();
				break;

			} // catch
		}

	} // run

} // SingFilter