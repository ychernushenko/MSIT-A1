package edu.cmu.a1.common;

public class Configuration {
	//Configuration class contains all needed constants in other classes
	public enum Field{ TIMESTAMP, TEMPERATURE, ALTITUDE, ATTITUDE, VELOCITY, PRESSURE};
	public final static int MeasurementLength = 8;		// This is the length of all measurements (including time) in bytes
	public final static int IdLength = 4;				// This is the length of IDs in the byte stream

}
