package com.wfg.main.metrics;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wfg.metrics.dataconstants.JiraHeaderFieldsVariables;

public class JiraSetHeaders {
	private static final Logger LOGGER = LoggerFactory.getLogger(JiraSetHeaders.class);
	
	public List<String> setHeaders(List<String> issueList){		
		issueList.add(JiraHeaderFieldsVariables.project_name);
        issueList.add(JiraHeaderFieldsVariables.key);
        issueList.add(JiraHeaderFieldsVariables.summary);
        issueList.add(JiraHeaderFieldsVariables.api_name);
        issueList.add(JiraHeaderFieldsVariables.sprint);
        issueList.add(JiraHeaderFieldsVariables.fixVersion);
        issueList.add(JiraHeaderFieldsVariables.root_cause);
        issueList.add(JiraHeaderFieldsVariables.label);
        issueList.add(JiraHeaderFieldsVariables.logged_by);
        issueList.add(JiraHeaderFieldsVariables.reporter);
        issueList.add(JiraHeaderFieldsVariables.status);
        issueList.add(JiraHeaderFieldsVariables.priority);
        issueList.add(JiraHeaderFieldsVariables.severity);
        issueList.add(JiraHeaderFieldsVariables.env);
        issueList.add(JiraHeaderFieldsVariables.resolution);
        issueList.add(JiraHeaderFieldsVariables.assignee);
        issueList.add(JiraHeaderFieldsVariables.component);
        issueList.add(JiraHeaderFieldsVariables.created);
        issueList.add(JiraHeaderFieldsVariables.updated);
        issueList.add(JiraHeaderFieldsVariables.resolved);
        issueList.add(JiraHeaderFieldsVariables.comp_env);
        issueList.add(JiraHeaderFieldsVariables.sort_col);
        issueList.add(JiraHeaderFieldsVariables.last_synched);
        issueList.add(JiraHeaderFieldsVariables.report_st_date);
        issueList.add(JiraHeaderFieldsVariables.report_end_date);
        LOGGER.info("Headers Added.");
        return issueList;
	
	}
}
