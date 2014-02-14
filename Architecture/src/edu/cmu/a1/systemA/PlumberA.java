package edu.cmu.a1.systemA;

import edu.cmu.a1.modules.AltitudeConvertFilter;
import edu.cmu.a1.modules.SourceFilter;
import edu.cmu.a1.modules.TemperatureFilter;


/******************************************************************************************************************
 * File:Plumber.java
 * Course: 17655
 * Project: Assignment 1
 * Copyright: Copyright (c) 2003 Carnegie Mellon University
 * Versions:
 *	1.0 November 2008 - Sample Pipe and Filter code (ajl).
 *
 * Description:
 *
 * This class serves as an example to illstrate how to use the PlumberTemplate to create a main thread that
 * instantiates and connects a set of filters. This example consists of three filters: a source, a middle filter
 * that acts as a pass-through filter (it does nothing to the data), and a sink filter which illustrates all kinds
 * of useful things that you can do with the input stream of data.
 *
 * Parameters: 		None
 *
 * Internal Methods:	None
 *
 ******************************************************************************************************************/
public class PlumberA
{
	public static void main( String argv[])
	{
		/****************************************************************************
		 * Here we instantiate three filters.
		 ****************************************************************************/
		String inputFileName = "D:/MSIT-SE/Architecture/Group Assignment/DataSets/FlightData.dat";
		String outputFileName = "D:/MSIT-SE/Architecture/Group Assignment/DataSets/OutputA.dat";
		SourceFilter sourceFilter = new SourceFilter(1,1,inputFileName);
		AltitudeConvertFilter altitudeConvertFilter = new AltitudeConvertFilter(1,1);
		TemperatureFilter temperatureFilter = new TemperatureFilter(1,1);
		SinkFilterA sinkFilter = new SinkFilterA(1,1,outputFileName);
		/****************************************************************************
		 * Here we connect the filters starting with the sink filter (Filter 1) which
		 * we connect to Filter2 the middle filter. Then we connect Filter2 to the
		 * source filter (Filter3).
		 ****************************************************************************/

		sinkFilter.Connect(altitudeConvertFilter,0,0); // This esstially says, "connect sinkFilter input port to altitudeConvertFilter output port
		altitudeConvertFilter.Connect(temperatureFilter, 0, 0); // This esstially says, "connect altitudeConvertFilter intput port to temperatureFilter output port
		temperatureFilter.Connect(sourceFilter, 0, 0);// This esstially says, "connect temperatureFilter input port to sourceFilter output port
		/****************************************************************************
		 * Here we start the filters up. All-in-all,... its really kind of boring.
		 ****************************************************************************/
		
		sourceFilter.start();
		altitudeConvertFilter.start();
		temperatureFilter.start();
		sinkFilter.start();

	} // main

} // Plumber