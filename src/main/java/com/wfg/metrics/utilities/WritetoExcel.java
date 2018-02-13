package com.wfg.metrics.utilities;
/*
 * Write Issue list to Excel
 */
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wfg.main.metrics.CommonFunctions;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WritetoExcel {
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WritetoExcel.class);
	

	public void writeXlFile(Map<Integer, List<String>> data) throws FileNotFoundException 
    { 
	
	XSSFWorkbook workbook = new XSSFWorkbook();
	XSSFSheet sheet = workbook.createSheet("IssueList");
	CommonFunctions cf = new CommonFunctions();
	final String FILE_NAME = cf.getConfigValue("exportPath");
 
 
    Set<Integer> keyset = data.keySet();
    
    List<String> keys = new ArrayList<String>();
    //System.out.println("Test: "+keys.toString());
    Collections.sort(keys);
    
    
    LOGGER.info("Writing Data to Excel...");
    int rownum = 0;
    for (Integer key : keyset)
    {
        Row row = sheet.createRow(rownum++);
        List<String> issueList = data.get(key);
        
        int cellnum = 0;
        for (Object obj : issueList)
        {
            Cell cell = row.createCell(cellnum++);
            if (obj instanceof String)
            {
                cell.setCellValue((String) obj);
                //System.out.println(key);
            }
        }
    }
 


    try {
        
    	//String path = getClass().getResource("/config.properties").getPath();
    	String path = System.getProperty("user.dir")+ "/"+FILE_NAME;
    	//FileOutputStream outputStream = new FileOutputStream(path);
        FileOutputStream outputStream = new FileOutputStream(path);
        workbook.write(outputStream);
        workbook.close();
        LOGGER.info("Export to Excel complete");
    } catch (FileNotFoundException e) {
        e.printStackTrace();
        LOGGER.info("Export to Excel failed");
    } catch (IOException e) {
        e.printStackTrace();
    }

   
    
}

}
