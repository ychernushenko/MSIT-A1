package edu.cmu.a1.common;

public class Configuration {
	//Configuration class contains all needed constants in other classes
	public enum Field{ TIMESTAMP, TEMPERATURE, ALTITUDE, ATTITUDE, VELOCITY, PRESSURE};
	public final static int MeasurementLength = 8;		// This is the length of all measurements (including time) in bytes
	public final static int IdLength = 4;				// This is the length of IDs in the byte stream
	public final static int RecordLength = 72;     		// This is the length of record in the byte steam
	
	public final static double iswild = 1.0;
	public final static double isnormal = 0.0;
}
