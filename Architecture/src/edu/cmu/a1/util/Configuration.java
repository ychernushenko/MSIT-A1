package edu.cmu.a1.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
	//Configuration class contains all needed constants in other classes
	public enum Field{ TIMESTAMP, TEMPERATURE, ALTITUDE, ATTITUDE, VELOCITY, PRESSURE};
	public final static int MeasurementLength = 8;		// This is the length of all measurements (including time) in bytes
	public final static int IdLength = 4;				// This is the length of IDs in the byte stream
	public final static int RecordLength = 72;     		// This is the length of record in the byte steam
	
	public final static double iswild = 1.0;
	public final static double isnormal = 0.0;
	
	public static String ReadProperty(String propName)
	{
		Properties prop = new Properties();
		InputStream input = null;
		try {
			 
			input = new FileInputStream("config.properties");
	 
			// load a properties file
			prop.load(input);
			return prop.getProperty(propName);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
