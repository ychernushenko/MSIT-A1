package edu.cmu.a1.modules;


import edu.cmu.a1.util.Configuration;
import edu.cmu.a1.util.FilterFramework;
import edu.cmu.a1.util.Record;

/******************************************************************************************************************
 * File:MiddleFilter.java
 * Course: 17655
 * Project: Assignment 1
 * Copyright: Copyright (c) 2003 Carnegie Mellon University
 * Versions:
 *	1.0 November 2008 - Sample Pipe and Filter code (ajl).
 *
 * Description:
 *
 * This class serves as an example for how to use the FilterRemplate to create a standard filter. This particular
 * example is a simple "pass-through" filter that reads data from the filter's input port and writes data out the
 * filter's output port.
 *
 * Parameters: 		None
 *
 * Internal Methods: None
 *
 ******************************************************************************************************************/

public class PressureWildPointFilter extends FilterFramework
{
	public PressureWildPointFilter(int inputPortNum, int outputPortNum) {
		super(inputPortNum, outputPortNum);
	}

	public void run()
	{
		Record prevValidRecord = null;
		while (true)
		{
			try
			{
				/***************************************************************************
				// We create a new buffer that reads all information for a record in, and use
				 * it to create a new Record instance.
				 ****************************************************************************/
				byte[] recordBuffer = new byte[Configuration.RecordLength];
				for (int i=0; i<Configuration.RecordLength; i++ ){
					recordBuffer[i] = ReadFilterInputPort(0);	// This is where we read the byte from the stream...
				}
				Record record = new Record(recordBuffer);

				/***************************************************************************
				// We need to compare this record with last valid record to check if it is valid.
				 ****************************************************************************/
				if(prevValidRecord == null) 
					prevValidRecord = record;
				double flux = Math.abs(record.getPressure()-prevValidRecord.getPressure());
				if(record.getPressure()<0.0 ||flux>10.0)
					for(int i=0; i<Configuration.RecordLength;i++) 
						WriteFilterOutputPort(recordBuffer[i],0);
				else
					prevValidRecord = record;
			} // try

			/*******************************************************************************
			 *	The EndOfStreamExeception below is thrown when you reach end of the input
			 *	stream (duh). At this point, the filter ports are closed and a message is
			 *	written letting the user know what is going on.
			 ********************************************************************************/
			catch (EndOfStreamException e)
			{
				ClosePorts();
				break;
			} // catch

		} // while

	} // run

} // MiddleFilter
