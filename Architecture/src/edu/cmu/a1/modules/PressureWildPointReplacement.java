package edu.cmu.a1.modules;


import edu.cmu.a1.util.Configuration;
import edu.cmu.a1.util.FilterFramework;
import edu.cmu.a1.util.Record;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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
 * This class finds wild point and replace them with a values of nearby points.
 *
 * Parameters: 		None
 *
 * Internal Methods: None
 *
 ******************************************************************************************************************/

public class PressureWildPointReplacement extends FilterFramework
{
	private String fileName;
	public PressureWildPointReplacement(int inputPortNum, int outputPortNum, String fName) {
		super(inputPortNum, outputPortNum);
		this.fileName = fName;
	}

	public void run()
	{
		Record prevRecord = null;
		Record prevValidRecord = null;
		ArrayList<Record> wildPointRecords = new ArrayList<Record>(); // buffer for records with wild points



		// Next we write a message to the terminal to let the world know we are alive...
		System.out.print( "\n" + this.getName() + "::SinkFilter C starts working ");
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(this.fileName)) ;
			//Time:               Temperature (C):    Altitude (m):
			//-----------------------------------------------------
			String header = "Time:       Temperature:       Altitude:        Pressure:\n"
					+ "-----------------------------------------------------";
			bufferedWriter.write(header);
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
					{
						if(prevRecord == null)
						{
							if (record.getPressure()<0.0)
								wildPointRecords.add(record);
						}
						else if ((record.getPressure()<0.0) || (Math.abs(record.getPressure()-prevRecord.getPressure())>10.0))
						{
							wildPointRecords.add(record);
						}
						else
						{
							for(Record r : wildPointRecords)
							{
								r.setPressure(record.getPressure());
								//Print r to pipe
								Configuration.Field formatList[] = {
										Configuration.Field.TIMESTAMP,
										Configuration.Field.TEMPERATURE,
										Configuration.Field.ALTITUDE,
										Configuration.Field.PRESSURE};

								String str= r.printFormat(formatList);
								System.out.println(str + "*");
							}
							wildPointRecords.clear();

							for(int i=0; i<Configuration.RecordLength;i++) 
								WriteFilterOutputPort(recordBuffer[i],0);

							//Print record to pipe
							Configuration.Field formatList[] = {
									Configuration.Field.TIMESTAMP,
									Configuration.Field.TEMPERATURE,
									Configuration.Field.ALTITUDE,
									Configuration.Field.PRESSURE};

							String str= record.printFormat(formatList);
							System.out.println(str);

							prevValidRecord = record;
						}
					}
					else
					{
						if ((record.getPressure()<0.0) || (Math.abs(record.getPressure()-prevRecord.getPressure())>10.0))
						{
							wildPointRecords.add(record);
						}
						else
						{
							for(Record r : wildPointRecords)
							{
								r.setPressure(Math.abs(record.getPressure() - prevValidRecord.getPressure())/2.0);
								//Print r to pipe
								Configuration.Field formatList[] = {
										Configuration.Field.TIMESTAMP,
										Configuration.Field.TEMPERATURE,
										Configuration.Field.ALTITUDE,
										Configuration.Field.PRESSURE};

								String str= r.printFormat(formatList);
								System.out.println(str + "*");

								prevValidRecord = r;
							}
							wildPointRecords.clear();

							for(int i=0; i<Configuration.RecordLength;i++) 
								WriteFilterOutputPort(recordBuffer[i],0);

							//Print record to pipe
							Configuration.Field formatList[] = {
									Configuration.Field.TIMESTAMP,
									Configuration.Field.TEMPERATURE,
									Configuration.Field.ALTITUDE,
									Configuration.Field.PRESSURE};

							String str= record.printFormat(formatList);
							System.out.println(str);

							prevValidRecord = record;
						}
					}

					prevRecord = record;
				} // try

				/*******************************************************************************
				 *	The EndOfStreamExeception below is thrown when you reach end of the input
				 *	stream (duh). At this point, the filter ports are closed and a message is
				 *	written letting the user know what is going on.
				 ********************************************************************************/
				catch (EndOfStreamException e)
				{
					for(Record r : wildPointRecords)
					{
						r.setPressure(prevValidRecord.getPressure());
						//Print r to pipe
						Configuration.Field formatList[] = {
								Configuration.Field.TIMESTAMP,
								Configuration.Field.TEMPERATURE,
								Configuration.Field.ALTITUDE,
								Configuration.Field.PRESSURE};

						String str= r.printFormat(formatList);
						System.out.println(str + "*");
					}
					wildPointRecords.clear();

					System.out.print( "\n" + this.getName() + "::SinkFilter B Exiting; all bytes are written in output file" );
					ClosePorts();
					bufferedWriter.close();
					break;
				} // catch
			}//while
		}//try
		catch (IOException iox) {
			System.out.print( "\n" + this.getName() + ":: SinkFilter B :: Problem writing output data file::" + iox );
		}
	}//run
} // SingFilter

