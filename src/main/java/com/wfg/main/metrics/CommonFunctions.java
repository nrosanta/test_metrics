package com.wfg.main.metrics;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.wfg.metrics.utilities.XProperties;
import com.wfg.metrics.utilities.encryptPassUtil;
import net.rcarz.jiraclient.BasicCredentials;
import net.rcarz.jiraclient.Issue;
import net.rcarz.jiraclient.JiraClient;
import net.rcarz.jiraclient.JiraException;

public class CommonFunctions {
	 BasicCredentials creds;
	 JiraClient jira;
	 public Properties properties = null;
	 encryptPassUtil decryptPass;
	 private static final Logger LOGGER = LoggerFactory.getLogger(GetJiraList.class);
	
	/*
	 * Login to JIRA based on information in the config file
	 */
	public void AuthenticateUser() throws FileNotFoundException{
		String pass, userId,jiraURL;
		encryptPassUtil decryptPass = new encryptPassUtil();
		properties = configProperties();
		pass = decryptPass.decryptPass();
		userId = properties.getProperty("UID");
		jiraURL = properties.getProperty("jiraURL");
		
        creds = new BasicCredentials(userId, pass);
        jira = new JiraClient(jiraURL, creds);
	}
	
	/*
	 * Return config property value
	 */
	public String getConfigValue(String str) throws FileNotFoundException{
		properties = configProperties();
		return str = properties.getProperty(str);
	}
	
	
	/*
	 * Search JIRA based on criteria specified and return the result
	 */
	public Issue.SearchResult getIssueList() throws FileNotFoundException{
		Issue.SearchResult sr=null;
		try{
			
			
			String JQLStr = getConfigValue("JQL");
			
			if(getConfigValue("Last3MonthsOnly").equals("Yes")){
				JQLStr = JQLStr + " " + getDates();
				//System.out.println("Debug Query: "+JQLStr);
			}
			
			LOGGER.info("Running JQL Query... please wait");
			LOGGER.info("JQL Query: "+JQLStr);
					
			sr = jira.searchIssues(JQLStr,Integer.parseInt(getConfigValue("maxResults")));
			//sr = jira.searchIssues(getConfigValue("JQL"),"*all",1000,0);
			
		}
		
       catch (JiraException ex) {
            System.err.println(ex.getMessage());

            if (ex.getCause() != null)
                System.err.println(ex.getCause().getMessage());
        }
		
		return sr;

	}
	
	/*
	 * Search JIRA based on criteria specified and return the result
	 */
	public Issue.SearchResult getIssueList(int nxt) throws FileNotFoundException{
		Issue.SearchResult sr=null;
		try{
			LOGGER.info("JQL Query: "+getConfigValue("JQL"));
			LOGGER.info("Running JQL Query... please wait");
			//sr = jira.searchIssues(getConfigValue("JQL"),"*all",Integer.parseInt(getConfigValue("maxResults")),10);
			sr = jira.searchIssues(getConfigValue("JQL"),"*all",Integer.parseInt(getConfigValue("maxResults")),nxt);
			
		}
		
       catch (JiraException ex) {
            System.err.println(ex.getMessage());

            if (ex.getCause() != null)
                System.err.println(ex.getCause().getMessage());
        }
		
		return sr;

	}
	
	/*
	 * Setup config property
	 * @return property instance
	 * @throws FileNotFoundException
	 */
	public Properties configProperties() throws FileNotFoundException
	{
		Properties prop = new XProperties();
		InputStream input = null;
		String filePath="config";
		String fileName = "/config.properties";
		//String resource_path = ClassLoader.getSystemClassLoader().getResource(".config/config.properties").toString();
		
		try {
			//String resource_path = CommonFunctions.class.getClassLoader().getResource("DefectList.xlsx").getPath();
			//System.out.println("Debug: "+resource_path);
			String path = System.getProperty("user.dir")+"/"+filePath+fileName;
			input = new FileInputStream(path);
			
			//input = getClass().getResourceAsStream("/config.properties");
			//System.out.println("Debug: "+filePath+fileName);
			//System.out.println("Debug: "+path);
			
			// load a properties file
			prop.load(input);

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
		return prop;
	}
	
	/**
	 * returns Start and End Dates in JQL String going back 3 months  based on current date
	 **/
	public String getDates(){

		String stDate, endDate;
		Calendar cal = Calendar.getInstance();
		String JQLString = "";
		
		try{
			
			cal.add(Calendar.MONTH, -3);
			Date result = cal.getTime();
			
	
			
			stDate=new SimpleDateFormat("yyyy-MM-dd").format(result);
			endDate= new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			
			JQLString = "AND created >= "+ stDate + " AND created <= " + endDate;
			//System.out.println("Date: "+JQLString);
			
		}
		
		catch(Exception e){
			LOGGER.error(e.getMessage());
		}
		return JQLString;
	}
	
	public String getTimeElapsed(long time){
		String timeString=null;
        try{
			timeString = String.format("%02d:%02d:%02d", 
			TimeUnit.MILLISECONDS.toHours(time),
			TimeUnit.MILLISECONDS.toMinutes(time) -  
			TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time)), // The change is in this line
			TimeUnit.MILLISECONDS.toSeconds(time) - 
			TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));
        }
        
        catch(Exception e){
        	e.printStackTrace();
        }
		return timeString;
	}

}
