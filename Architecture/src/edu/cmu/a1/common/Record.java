package edu.cmu.a1.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import edu.cmu.a1.common.Configuration.Field;
public class Record {
	// an object that can used to store every field in input/output.
	// It has formatting functions. 
	private String timeStamp;
	private long timestamplong;
	private double temperature;
	private double altitude;
	private double attitude;
	private double velocity;
	private double pressure;
	private double isWild;
	public Record() {
		this.timeStamp = "";
		isWild = Configuration.isnormal;
	}
	
	
	public Record(byte[] buffer){
		Converter converter= new Converter();
		int offset = 0;
		for(int i=0; i<6; i++){
			int id = converter.byteToId(buffer,offset);
			offset += Configuration.IdLength;
			long measurement = converter.byteToMeasurement(buffer, offset);
			offset += Configuration.MeasurementLength;
			if(id == 0) 
				setTimeStamp(measurement);
			else if(id == 1) 
				velocity = Double.longBitsToDouble(measurement);
			else if(id == 2) 
				altitude = Double.longBitsToDouble(measurement);
			else if(id == 3) 
				pressure = Double.longBitsToDouble(measurement);
			else if(id == 4) 
				temperature = Double.longBitsToDouble(measurement);
			else if(id == 5) 
				attitude = Double.longBitsToDouble(measurement);
		}
		if(offset+Configuration.IdLength+Configuration.MeasurementLength == buffer.length){
			long measurement = converter.byteToMeasurement(buffer, offset+Configuration.IdLength);
			isWild = Double.longBitsToDouble(measurement);
		}// insert a wild point field
		
		return;
	}
	
	
	
	
	public String getTimeStamp() {
		return timeStamp;
	}
	
	public void setTimeStamp(long timelong){
		Calendar tCalendar = Calendar.getInstance();
		tCalendar.setTimeInMillis(timelong);
		SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy:dd:hh:MM:ss");
		this.timeStamp= timeStampFormat.format(tCalendar.getTime());
		this.timestamplong = timelong;
	}
	
	public double getTemperature() {
		return temperature;
	}
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	public double getAltitude() {
		return altitude;
	}
	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}
	public double getAttitude() {
		return attitude;
	}
	public void setAttitude(double attitude) {
		this.attitude = attitude;
	}
	public double getVelocity() {
		return velocity;
	}
	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}
	public double getPressure() {
		return pressure;
	}
	public void setPressure(double pressure) {
		this.pressure = pressure;
	}

	private String formatAltitude(){
		java.text.DecimalFormat decimalFormat =new java.text.DecimalFormat("00000.00000");  
		return decimalFormat.format(altitude);
	}
	private String formatTemperature(){
		java.text.DecimalFormat decimalFormat =new java.text.DecimalFormat("000.00000");  
		return decimalFormat.format(temperature);
	}
	private String formatAttitude(){
		return String.valueOf(attitude);
	}
	private String formatPressure(){
		java.text.DecimalFormat decimalFormat =new java.text.DecimalFormat("#####");  
		String rawPressure = decimalFormat.format(pressure);
		//String pressureFormatString =rawPressure.replace('.', ':');
		return rawPressure;
	}
	private String formatVelocity(){
		return String.valueOf(velocity);
	}
	
	
	public String printFormat(Field[] list){
		if(timeStamp.equals(""))
			return "";
		StringBuilder result = new StringBuilder();
		for(int i=0; i< list.length;i++){
			switch (list[i]) {
			case ALTITUDE:
				result.append(formatAltitude()+'\t');
				break;
			case TIMESTAMP:
				result.append(timeStamp+'\t');
				break;
			case ATTITUDE:
				result.append(formatAttitude()+'\t');
				break;
			case VELOCITY:
				result.append(formatVelocity()+'\t');
				break;
			case TEMPERATURE:
				result.append(formatTemperature()+'\t');
				break;
			case PRESSURE:
				result.append(formatPressure()+'\t');
				break;
			default:
				break;
			}
		}
		return result.toString();

	}
	
	public byte[] toBytes(){
		ArrayList<byte[]> recordBytes = new ArrayList<byte[]>();
		Converter converter = new Converter();
		recordBytes.add(converter.idToByte(0));
		recordBytes.add(converter.measurementToByte(timestamplong));
		recordBytes.add(converter.idToByte(1));
		recordBytes.add(converter.measurementToByte(Double.doubleToLongBits(velocity)));
		recordBytes.add(converter.idToByte(2));
		recordBytes.add(converter.measurementToByte(Double.doubleToLongBits(altitude)));
		recordBytes.add(converter.idToByte(3));
		recordBytes.add(converter.measurementToByte(Double.doubleToLongBits(pressure)));
		recordBytes.add(converter.idToByte(4));
		recordBytes.add(converter.measurementToByte(Double.doubleToLongBits(temperature)));
		recordBytes.add(converter.idToByte(5));
		recordBytes.add(converter.measurementToByte(Double.doubleToLongBits(attitude)));
		
		return concatAll(recordBytes);
	}
	
	public byte[] concatAll(ArrayList<byte[]> recordBytes) {
		  int totalLength = 72;
		  byte[] result = new byte[totalLength];
		  int offset  = 0;
		  for (int i=0; i<recordBytes.size();i++) {
			 byte[] array = recordBytes.get(i);
			 System.arraycopy(array, 0, result, offset, array.length);
			 offset += array.length;
		  }
		  return result;
		}

	public byte[] toBytesWithWild(){
		ArrayList<byte[]> list = new ArrayList<byte[]>();
		list.add(this.toBytes());
		byte[] wildRecord = new byte[Configuration.IdLength+Configuration.MeasurementLength];
		Converter converter = new Converter();
		System.arraycopy(converter.idToByte(6), 0, wildRecord, 0, Configuration.IdLength);
		System.arraycopy(converter.measurementToByte(
				Double.doubleToLongBits(Configuration.iswild)), 
				0, wildRecord, Configuration.IdLength, 
				Configuration.MeasurementLength);
		list.add(wildRecord);
		return concatAll(list);
	}
}
