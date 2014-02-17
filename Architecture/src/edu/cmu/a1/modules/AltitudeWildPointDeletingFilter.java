package edu.cmu.a1.modules;

import edu.cmu.a1.util.Configuration;
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

public class AltitudeWildPointDeletingFilter extends FilterFramework
{
	public AltitudeWildPointDeletingFilter(int inputPortNum, int outputPortNum) {
		super(inputPortNum, outputPortNum);
	}

	public void run()
	{
		while (true)
		{
			try
			{
				byte[] recordBuffer = new byte[Configuration.RecordLength];
				for (int i=0; i<Configuration.RecordLength; i++ ){
					recordBuffer[i] = ReadFilterInputPort(0);	// This is where we read the byte from the stream...
				}
				Record record = new Record(recordBuffer);
				
				if (record.getAltitude() >= 10000.0)
				{
					//Print record to pipe
					for(int i=0; i<Configuration.RecordLength;i++) 
						WriteFilterOutputPort(recordBuffer[i],0);
				}
			} // try

			/*******************************************************************************
			 *	The EndOfStreamExeception below is thrown when reaching end of the input
			 *	stream (duh).
			 ********************************************************************************/
			catch (EndOfStreamException e)
			{
				ClosePorts();
				break;
			} // catch

		} // while

	} // run

} // MiddleFilter
