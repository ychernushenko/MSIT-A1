package edu.cmu.a1.systemB;


import edu.cmu.a1.modules.FilterFramework;
import edu.cmu.a1.util.Configuration;
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
		//Record prevRecord = null;
		Record prevValidRecord = null;
		ArrayList<Record> wildPointRecords = new ArrayList<Record>(); // buffer for records with wild points



		// Next we write a message to the terminal to let the world know we are alive...
		System.out.print( "\n" + this.getName() + "::SinkFilter B1 starts working ");
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(this.fileName)) ;
			String header = "Time:       Temperature:       Altitude:        Pressure:\n"
					+ "-----------------------------------------------------\n";
			bufferedWriter.write(header);
			
			Configuration.Field formatList[] = {
					Configuration.Field.TIMESTAMP,
					Configuration.Field.TEMPERATURE,
					Configuration.Field.ALTITUDE,
					Configuration.Field.PRESSURE};
			
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

					if(prevValidRecord == null)// check if there is no preValid Record, i.e start of value.
					{
						if (record.getPressure()<0.0){
							wildPointRecords.add(record);
						}
						else{
							prevValidRecord = record;// The first valid value.
							
							for(Record r : wildPointRecords){
								r.setPressure(prevValidRecord.getPressure());// Use first valid value to replace wild points.
								String str= r.printFormat(formatList);
								bufferedWriter.write(str.substring(0,str.length()-1) + "*\t \n");
							}
						}
					}
					else
					{
						if ((record.getPressure()<0.0) || (Math.abs(record.getPressure()-prevValidRecord.getPressure())>10.0))
						{
							wildPointRecords.add(record);
						}
						else // this record is valid
						{
							for(Record r : wildPointRecords)//Replace all wild points in the list.
							{
								r.setPressure(Math.abs(record.getPressure() + prevValidRecord.getPressure())/2.0);
								String str= r.printFormat(formatList);
								bufferedWriter.write(str.substring(0,str.length()-1) + "*\n");
								prevValidRecord = r;
							}
							wildPointRecords.clear();
							
							// write this record into file.
							String str= record.printFormat(formatList);
							bufferedWriter.write(str+"\n");
							prevValidRecord = record;
						}
					}
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
						String str= r.printFormat(formatList);
						bufferedWriter.write(str.substring(0,str.length()-1) + "*\n");
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

