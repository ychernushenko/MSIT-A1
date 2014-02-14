package edu.cmu.a1.modules;


import edu.cmu.a1.common.Configuration;
import edu.cmu.a1.common.Converter;
import edu.cmu.a1.common.FilterFramework;

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

public class AltitudeConvertFilter extends FilterFramework
{
	public AltitudeConvertFilter(int inputPortNum, int outputPortNum) {
		super(inputPortNum, outputPortNum);
		// TODO Auto-generated constructor stub
	}

	public void run()
	{
		long measurement;				// This is the word used to store all measurements - conversions are illustrated.
		int id;							// This is the measurement id

		System.out.print( "\n" + this.getName() + "::Altitude Convert Filter Reading ");
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
				// We do check ID here and convert temperature data from feet to meters.
				 ****************************************************************************/
				
				if ( id == 2 )
				{
					measurement = converter.convertMileToMeters(measurement);
					measurementBytes = converter.measurementToByte(measurement);
				} // if

				/****************************************************************************
				// We write what we have read and converted(only when ID == 4 ) into writeOutput.
				 ****************************************************************************/
				for(int i=0; i<Configuration.IdLength;i++){
					WriteFilterOutputPort(idBytes[i],0);
				}
				for(int i=0; i<Configuration.MeasurementLength;i++){
					WriteFilterOutputPort(measurementBytes[i],0);
				}

			} // try

			/*******************************************************************************
			 *	The EndOfStreamExeception below is thrown when you reach end of the input
			 *	stream (duh). At this point, the filter ports are closed and a message is
			 *	written letting the user know what is going on.
			 ********************************************************************************/
			catch (EndOfStreamException e)
			{
				System.out.print( "\n" + this.getName() + "::AltitudeFilter Exiting; converting completed");
				ClosePorts();
				break;
			} // catch

		} // while

	} // run

} // MiddleFilter
