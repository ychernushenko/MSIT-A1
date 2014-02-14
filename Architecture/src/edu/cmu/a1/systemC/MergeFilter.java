package edu.cmu.a1.systemC;

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

public class MergeFilter extends FilterFramework
{
	public MergeFilter(int i, int j) {
		super(i, j);
	}
	
	public Record ReadRecord(int portNumber)
	{
		try
		{
			byte[] recordBuffer = new byte[Configuration.RecordLength];
			for (int i=0; i<Configuration.RecordLength; i++ ){
				recordBuffer[i] = ReadFilterInputPort(portNumber);	// This is where we read the byte from the stream...
			}
			Record record = new Record(recordBuffer);
			return record;
		}
		catch(EndOfStreamException e)
		{
			ClosePorts();
		}
		return null;
	}
	
	public boolean WriteOutput(Record record, int port)
	{
		try
		{
			byte[] writeBuffer = record.toBytes();
			for(int i = 0; i< Configuration.RecordLength; i++){
				WriteFilterOutputPort(writeBuffer[i],port);
			}
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	public void run()
    {
			/*************************************************************
			*	Here we read a byte and write a byte
			*************************************************************/
				
				Record firstRecord = ReadRecord(0);
				Record secondRecord = ReadRecord(1);
				while(true)
				{
					try
					{
						if((this.getInputReadPort().get(0).available() == 0 ) && (this.getInputReadPort().get(1).available() == 0 ))
							break;
						if(this.getInputReadPort().get(0).available() == 0 ){
							WriteOutput(secondRecord, 0);
							secondRecord = ReadRecord(1);
						}
						else if (this.getInputReadPort().get(1).available() == 0 ){
							WriteOutput(firstRecord, 0);
							firstRecord = ReadRecord(0);
						}
						else
						{
							if(firstRecord.getTimeStampLong() < secondRecord.getTimeStampLong())
							{
								WriteOutput(firstRecord, 0);
								firstRecord = ReadRecord(0);
							}
							else if(firstRecord.getTimeStampLong() > secondRecord.getTimeStampLong())
							{
								WriteOutput(secondRecord, 0);
								secondRecord = ReadRecord(1);
							}
							else if(firstRecord.getTimeStampLong() == secondRecord.getTimeStampLong())
							{
								WriteOutput(firstRecord, 0);
								WriteOutput(secondRecord, 0);
								firstRecord = ReadRecord(0);
								secondRecord = ReadRecord(1);
							}
						}
					}
					catch(Exception e)
					{
						
					}
				}
   } // run

} // MiddleFilter