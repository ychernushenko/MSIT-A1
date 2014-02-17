package edu.cmu.a1.modules;
import edu.cmu.a1.util.Configuration;
import edu.cmu.a1.util.Converter;



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

public class TemperatureFilter extends FilterFramework
{
	public TemperatureFilter(int inputPortNum, int outputPortNum) {
		super(inputPortNum, outputPortNum);
	}

	public void run()
	{
		long measurement;				// This is the word used to store all measurements - conversions are illustrated.
		int id;							// This is the measurement id
		System.out.print( "\n" + this.getName() + "::Temperature Convert Filter Reading ");
		while (true)
		{
			try
			{
				/***************************************************************************
				// We know that the first data coming to this filter is going to be an ID and
				// that it is IdLength long. So we first decommutate the ID bytes.
				 ****************************************************************************/
				Converter converter = new Converter();
				id = 0;
				byte[] idBytes = new byte[Configuration.IdLength];
				for (int i=0; i<Configuration.IdLength; i++ ){
					idBytes[i] = ReadFilterInputPort(0);	// This is where we read the byte from the stream...
				}
				id = converter.byteToId(idBytes,0);
				/****************************************************************************
				// Here we read measurements. All measurement data is read as a stream of bytes
				// and stored as a long value. 
				 *****************************************************************************/
				measurement = 0;
				byte[] measurementBytes = new byte[Configuration.MeasurementLength];
				for (int i=0; i<Configuration.MeasurementLength; i++ ){
					measurementBytes[i] = ReadFilterInputPort(0);	// This is where we read the byte from the stream...
				}

				measurement= converter.byteToMeasurement(measurementBytes,0);
				/****************************************************************************
				// We do check ID here and convert temperature data from Fahrenheit to Celsius.
				 ****************************************************************************/
				
				
				
				if ( id == 4 )
				{
					measurement = converter.convertFtoC(measurement);
					measurementBytes = converter.measurementToByte(measurement);
				} // if

				/****************************************************************************
				// We write all bytes stored in buffers into OutputPort
				 ****************************************************************************/
				for(int i=0; i<Configuration.IdLength;i++){
					WriteFilterOutputPort(idBytes[i],0);
				}
				for(int i=0; i<Configuration.MeasurementLength;i++){
					WriteFilterOutputPort(measurementBytes[i],0);
				}
			} // try

			/*******************************************************************************
			 *	The EndOfStreamExeception below is thrown when reaching end of the input
			 *	stream (duh).
			 ********************************************************************************/
			catch (EndOfStreamException e)
			{
				ClosePorts();
				System.out.print( "\n" + this.getName() + "::Temperature Exiting; converting completed");
				break;
			} // catch

		} // while

	} // run

} // MiddleFilter
