package planit_poc.helpers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Settings {
	
public static boolean isInitialized = false;
	public static Properties properties = new Properties();
	
	public static void  loadProperties() {
		FileInputStream fInStream;
		try {
			fInStream = new FileInputStream("config.properties");

			properties.load(fInStream);
			isInitialized=true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block-Modify throw runtime exception
			e.printStackTrace();
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
		
	}
	public static String getProperty(String name) {
		if(!isInitialized) {
			loadProperties();
		}
		return properties.getProperty(name);
		
	}
	
}
