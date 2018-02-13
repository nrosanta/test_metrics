package com.wfg.main.metrics;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.beanutils.converters.IntegerConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wfg.metrics.utilities.WritetoExcel;

import net.rcarz.jiraclient.Issue;

public class GetJiraList {
	String value; /* Returned value for Custom Field*/
	private static final Logger LOGGER = LoggerFactory.getLogger(GetJiraList.class);
	

    public static void main(String[] args) {

    	CommonFunctions cf=new CommonFunctions();

        try {

            /* Login and Search for issues */
  	        cf.AuthenticateUser();
	        
        	Map<Integer, List<String>> data = new HashMap<Integer, List<String>>();
	        List<String> issueList = new ArrayList<String>();
	        int itr=1;
	        int j=1;
	        int k=0;
	        int nxt=0;
	        int batch =Integer.parseInt(cf.getConfigValue("batch"));
	        int loop=1;
	        long time = System.currentTimeMillis();
	        
	        /**
	         * Set Headers for Excel Output File
	         */
	        JiraSetHeaders setHeaders = new JiraSetHeaders();      
	        issueList = setHeaders.setHeaders(issueList);
	        data.put(itr, issueList);/*Add headers to List*/
	        
	        Issue.SearchResult sr;// = login.getIssueList(batch);
	        
            /*
             * Load Data to issue List for writing to Excel
             */
	        JiraLoadData loadData = new JiraLoadData();
	        

	        //sr = cf.getIssueList();
	        //LOGGER.info("Total tickets found: "+sr.total);
	        
	        /*
	         * Retrieve tickets from JIRA with pagination calls based on batch 
	         * and maxResults defined in the config property file
	         */
	        while (k<=loop){
	        	LOGGER.info("Retrieving batch #"+k +" to "+ (k+batch));
	        	sr = cf.getIssueList(nxt);
	        	LOGGER.info("Data Extracted and preparing for writing to Excel");
	        	LOGGER.info("Total tickets found: "+sr.total);
	        	nxt = nxt+batch;
	        	
	        	for (Issue i:sr.issues){
	        		//System.out.println("Result: " + (j++) +" -> " + i.getKey()+" "+i.getAssignee());
	        		issueList = new ArrayList<String>();
	        		issueList = loadData.setData(i,issueList);
	        		data.put(itr+1, issueList);
	        		itr++;
	        	}
	        	k=k+batch;
	        	loop=sr.total;
	        }
            
            WritetoExcel write = new WritetoExcel();
            write.writeXlFile(data);
            time = System.currentTimeMillis()-time;
            String timeString = cf.getTimeElapsed(time);
            LOGGER.info("Total time taken: "+timeString+" (hour:min:sec)");
     
        } 
        
        catch (Exception ex) {
            System.err.println(ex.getMessage());

            if (ex.getCause() != null)
                System.err.println(ex.getCause().getMessage());
        }
    }
    
}