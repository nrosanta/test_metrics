package com.wfg.metrics.utilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.Properties;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wfg.main.metrics.CommonFunctions;
import com.wfg.main.metrics.JiraSetHeaders;


public class encryptPassUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(encryptPassUtil.class);
	private StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
	CommonFunctions cf = new CommonFunctions();
	
	/*
	 * Encryption Method
	 */
	public encryptPassUtil() {	
		
		try{	    
	    String algo = cf.getConfigValue("algorithm");
	    String format = cf.getConfigValue("format");
	    //System.out.println("Algo: "+algo);
	    encryptor.setAlgorithm(algo);
	    encryptor.setPassword(format);
		}
		
		catch (Exception e){
			e.printStackTrace();
		}
		
	}
	

	//@Test
	public void encryptPass() throws FileNotFoundException{
	    String password = cf.getConfigValue("password");
	     encryptor.encrypt(password);
	    LOGGER.info("Encrypted: "+encryptor.encrypt(password));
	    
	}
	
	@Test
	public void decryptPassVoid() throws FileNotFoundException{
	    String password = cf.getConfigValue("password");
	    System.out.println(encryptor.decrypt(password));
	    
	}	
	
	public String decryptPass() throws FileNotFoundException{
	    String password = cf.getConfigValue("password");
	    return encryptor.decrypt(password);
	    
	}

}



