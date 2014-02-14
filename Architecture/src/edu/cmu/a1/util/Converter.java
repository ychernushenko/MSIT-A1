package edu.cmu.a1.util;


public class Converter {
	/***************************************************************************
	 * 	Converter is the class that convert byte <--> long/int
	 *  and convert F o C, and feet to meters, etc. It can be expanded when
	// 	business logic needs any converting functionalities.
	 ****************************************************************************/
	public int byteToId(byte[] databytes, int offset)
	{
		int id = 0;

		for (int i=0; i<Configuration.IdLength; i++ )
		{
			id = id | (databytes[i+offset] & 0xFF);		// We append the byte on to ID...
			if (i != Configuration.IdLength-1)				// If this is not the last byte, then slide the
			{									// previously appended byte to the left by one byte
				id = id << 8;					// to make room for the next byte we append to the ID
			} // if
		} // for
		return id;
	}

	public long byteToMeasurement(byte[] databytes, int offset)
	{
		long measurement = 0;

		for (int i=0; i<Configuration.MeasurementLength; i++ )
		{
			byte databyte = databytes[i+offset];
			measurement = measurement | ( databyte & 0xFF);	// We append the byte on to measurement...

			if (i != Configuration.MeasurementLength-1)					// If this is not the last byte, then slide the
			{												// previously appended byte to the left by one byte
				measurement = measurement << 8;				// to make room for the next byte we append to the
				// measurement
			} // if
		} // if
		return measurement;
	}

	public byte[] measurementToByte(long num) {  
		byte[] result = new byte[8];
		for(int i=0; i<8;i++){
			result[i] = (byte) (num >>> ((7-i)*8));
		}
		return result;  
	} 
	public byte[] idToByte(int num) {  
		byte[] result = new byte[4];
		for(int i=0; i<4;i++){
			result[i] = (byte) (num >>> ((3-i)*8));
		}
		return result;  
	} 


	public long convertFtoC(long measurement){
		double temperature =  Double.longBitsToDouble(measurement);
		temperature = (temperature - 32)*5/9;
		return Double.doubleToLongBits(temperature);
	}


	public long convertMileToMeters(long measurement){
		double altitude =  Double.longBitsToDouble(measurement);
		altitude = altitude*0.3048;
		return Double.doubleToLongBits(altitude);
	}


	
}
