package edu.cmu.a1.systemC;

import edu.cmu.a1.modules.*;


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
public class Plumber
{
	public static void main( String argv[])
	{
		/****************************************************************************
		 * Here we instantiate three filters.
		 ****************************************************************************/

		SourceFilter sourceFirstFilter = new SourceFilter("C:/Users/kosty_000/Documents/CMU/2nd Semester/S14-Architectures for Software Systems/Projects/A1/MSIT-A1/Architecture/src/edu/cmu/a1/SubSetA.dat", 1, 1);
		SourceFilter sourceSecondFilter = new SourceFilter("C:/Users/kosty_000/Documents/CMU/2nd Semester/S14-Architectures for Software Systems/Projects/A1/MSIT-A1/Architecture/src/edu/cmu/a1/SubSetB.dat", 1, 1);
		MergeFilter mergeFilter = new MergeFilter(2,1);
		SinkFilter sinkFilter = new SinkFilter(1,1);
	
		/****************************************************************************
		 * Here we connect the filters starting with the sink filter (Filter 1) which
		 * we connect to Filter2 the middle filter. Then we connect Filter2 to the
		 * source filter (Filter3).
		 ****************************************************************************/
		sinkFilter.Connect(mergeFilter, 0, 0);
		mergeFilter.Connect(sourceFirstFilter, 0, 0);
		mergeFilter.Connect(sourceSecondFilter, 1, 0);
		
		/****************************************************************************
		 * Here we start the filters up. All-in-all,... its really kind of boring.
		 ****************************************************************************/
		
		sourceFirstFilter.start();
		sourceSecondFilter.start();
		mergeFilter.start();
		sinkFilter.start();
	} // main

} // Plumber