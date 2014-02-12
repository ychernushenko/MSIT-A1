package edu.cmu.a1.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.cmu.a1.common.Configuration.Field;
public class Record {
	// an object that can used to store every field in input/output.
	// It has formatting functions. 
	private String timeStamp;
	private double temperature;
	private double altitude;
	private double attitude;
	private double velocity;
	private double pressure;
	public Record() {
		this.timeStamp = "";
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	
	public void setTimeStamp(long timelong){
		Calendar tCalendar = Calendar.getInstance();
		tCalendar.setTimeInMillis(timelong);
		SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy:dd:hh:MM:ss");
		this.timeStamp= timeStampFormat.format(tCalendar.getTime());
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
		String pressureFormatString =rawPressure.replace('.', ':');
		return pressureFormatString;
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
		byte[] recordBytes = new byte[72];
		Converter converter = new Converter();
		
		return null;
	}
}
