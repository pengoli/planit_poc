package planit_poc.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

public class FileUtils {

	/**
	 * Method to Convert input excel data of each row as hashmap in the object array which is returned to data provider
	 * @param path --> Input Excel data path 
	 * @return --> two dimensional object array 
	 * @throws FilloException
	 */
	
	public static Object[][] readInputExcelData(String path) throws FilloException{
		Fillo fillo = new Fillo();
		Connection conn = fillo.getConnection(path);
		String statement = "Select * from BookOrderDetails";
		Recordset resultSet = conn.executeQuery(statement);
		Map<String,String> inputDataAsMap = null ;
		System.out.println(resultSet.getCount());
		Object[][] inputDataObject = new Object[resultSet.getCount()][1];
		ArrayList<String> fieldNames = new ArrayList<String>();
		fieldNames = resultSet.getFieldNames();
		int count =0;
		while(resultSet.next()) {
			inputDataAsMap=new HashMap<String,String>();
			for(String field : fieldNames) {
				inputDataAsMap.put(field, resultSet.getField(field));
			}
			inputDataObject[count][0]=inputDataAsMap;
			count++;
		}
		
		return inputDataObject;
		
	}

}
