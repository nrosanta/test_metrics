package com.wfg.main.metrics;
/*
 * Load Issue List to a List for writing to Excel
 */
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Date;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;
import com.wfg.metrics.dataconstants.JiraCustomFieldsVariables;
import net.rcarz.jiraclient.Field;
import net.rcarz.jiraclient.Issue;

public class JiraLoadData {
	String value, name;
	static Issue issue;
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(JiraSetHeaders.class);
	
	public List<String> setData(Issue i,List<String> issueList){
		issue= i;
    	issueList = new ArrayList<String>();
    	
    	/*
    	 * Add JIRA item to the list
    	 */
    	try{
	    	issueList.add(i.getProject().toString()); //Project
	    	issueList.add(i.getKey()); //Key
	    	issueList.add(i.getSummary()); //Summary
	    	issueList.add(getCustomValue(JiraCustomFieldsVariables.api_name)); //API
	    	issueList.add(getFieldStr(JiraCustomFieldsVariables.sprint)); //Sprint
	    	issueList.add(i.getFixVersions().toString()); //fixVersion
	    	issueList.add(getCustomValue(JiraCustomFieldsVariables.root_cause));
	    	issueList.add(i.getLabels().toString());
	    	issueList.add("TBD");//Calculated Value
	    	issueList.add(i.getReporter().toString());//reporter
	    	issueList.add(i.getStatus().toString());//Status
	    	issueList.add(getCustomValue(JiraCustomFieldsVariables.priority));//Priority
	    	issueList.add(getCustomValue(JiraCustomFieldsVariables.severity));//Severity
	    	issueList.add(getCustomValue(JiraCustomFieldsVariables.env));//Environment
	    	issueList.add(getField(JiraCustomFieldsVariables.resolution));
	    	issueList.add(getAssignee("assignee"));//Assignee
	    	issueList.add(i.getComponents().toString()); //Component
	    	issueList.add(getField("created")); /*Created*/
	    	issueList.add(getField("updated"));//Updated
	    	issueList.add(getField("resolved"));//Resolved
	    	issueList.add(compEnv());//comp env
	    	issueList.add(Integer.toString(sortCol()));//sort col for env
	    	issueList.add(new SimpleDateFormat("yyyy/MM/dd h:mm a").format(new Date())+" EST");//last synched
	    	issueList.add(getDates("stDate")); //reporting start date
	    	issueList.add(getDates("endDate")); //reporting end date
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}

    	
    	
    	return issueList;
	}
	
	/*
	 * Render custom data value
	 * @param - custom_field name in JIRA
	 * @return - display name as in JIRA UI
	 */
	public String getCustomValue(String cf_name){
		Object cfvalue;
        Gson gsonObj = new Gson();
        String cfstring;
        JiraLoadData getJira;
        cfvalue = issue.getField(cf_name);
    	cfstring = Field.getString(cfvalue.toString());
    	getJira = gsonObj.fromJson(cfstring, JiraLoadData.class);

    	
    	if(cf_name=="customfield_17306" && getJira==null)
    		return "Medium"; /*Default to Medium Priority if Priority field is missing*/
    	else if (cf_name=="customfield_16701" && getJira==null)
    			return "DEV"; /*Default to DEV if env field or value is missing*/
    		else if(getJira==null||cf_name==null)
    				return "Not Populated";/*Default to Not Populated if field value is missing or null*/		
    			else
    				return cf_name = getJira.value;  
        
	}
	
	/*
	 * @param custom_field name as defined in JIRA
	 * @return equivalent display name in JIRA UI
	 */
	public String getField(String cf_name){
		Object cfvalue;
        String cfstring;
        cfvalue = issue.getField(cf_name);
        
        
        if(cf_name=="customfield_10117")
        	System.out.println("Debug: "+Field.getString(cfvalue.toString()));;
        
        
        if(cfvalue==null)
        	return cfstring = "Not Populated";
        else
        	cfstring = Field.getString(cfvalue.toString());
  
        if(cf_name=="created" ||cf_name=="updated"||cf_name=="resolved")
        	return cfstring=cfstring.substring(0, 10);
        else
        	return cfstring;
	}  
	
	public String getFieldStr(String cf_name){
		Object cfvalue;
        String cfstring;
        cfvalue = issue.getField(cf_name);
        cfstring = Field.getString(cfvalue.toString());
        
		Pattern r = Pattern.compile("name[\\s\\S]*?,");
		Matcher m = r.matcher(cfstring);
		if (m.find( )) {	
			cfstring = m.group();
			return cfstring = cfstring.replaceAll("name=|,","");
		}
		return cfstring="Not Populated";
	}
	
	/*
	 * @param Assignee field name defined in JIRA
	 * @return Assignee value
	 */
	public String getAssignee(String cf_name){
		Object cfvalue;
        Gson gsonObj = new Gson();
        String cfstring;
        JiraLoadData getJira;
        cfvalue = issue.getField(cf_name);
    	cfstring = Field.getString(cfvalue.toString());
    	getJira = gsonObj.fromJson(cfstring, JiraLoadData.class);
       	if(getJira==null)
       		return "Unassigned";
       	else
       		return cf_name = getJira.name;  
        
	}
	
  
	public enum Env{
		DEV, TST, SIT, SAN, CER, PRO
	}
	
	
	/*
	 * Sort column by the environment enum names
	 */
	public int sortCol(){
		
		String option = getCustomValue(JiraCustomFieldsVariables.env).substring(0, 3);
		Env env = Env.valueOf(option);

		switch (env){
			case DEV:
				return 1;
			case SIT:
				return 2;
				
			case TST: 
				return 3;
	
			case SAN:
				return 4;
			case CER:
				return 5;
			case PRO:
				return 6;
			default:
				return 7;
				
		}
	}
	

	
	/*
	 * Derive shortened environment name based on the env field
	 */
	public String compEnv(){
		String env = getCustomValue(JiraCustomFieldsVariables.env).substring(0, 3);
		
		if(env.equalsIgnoreCase("DEV"))
				return "DEV";
		else if(env.equalsIgnoreCase("TST"))
				return "TST";
			else if(env.equalsIgnoreCase("SIT"))
				return "SIT";
		 	else if(env.equalsIgnoreCase("SAN"))
		 		return "SANDBOX";
		 	else if(env.equalsIgnoreCase("CER"))
		 		return "CERTIFICATION";
		 	else if(env.equalsIgnoreCase("PRO"))
	 			return "PRODUCTION";
		
		return env;
	}
	

	
	
	public String getDates(String report_date){
		CommonFunctions cf = new CommonFunctions();
		
		String stDate, endDate;
		Calendar cal = Calendar.getInstance();
		
		
		
		try{
			cal.add(Calendar.MONTH, -3);
			Date result = cal.getTime();
			stDate=new SimpleDateFormat("yyyy-MM-dd").format(result);
			endDate= new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		
			if(cf.getConfigValue("Last3MonthsOnly").equals("Yes")){
				
				if(report_date=="stDate")
					return stDate;
				else
					return endDate;
				
			}
			else{
				if(report_date=="stDate")
					return cf.getConfigValue("stDate");
				else
					return cf.getConfigValue("endDate");
				

			}
		}
		
		catch(Exception e){
			LOGGER.error(e.getMessage());
		}
		
		return "";
	}
	
	

}
