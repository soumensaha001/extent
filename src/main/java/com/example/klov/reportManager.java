package com.example.klov;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

@Component
public class reportManager {
	ExtentTest test;
	ExtentReports report;
	@RabbitListener(queues={"${queue.name}"})
	public void consumeMessage(@Payload String fileBody) throws ParseException, IOException {
		System.out.println(fileBody);
		   JSONParser parser = new JSONParser();
		   Object obj = parser.parse(fileBody);
		   org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;
		   String testcasename= jsonObject.get("testname").toString();
		   org.json.simple.JSONArray steps = (org.json.simple.JSONArray) jsonObject.get("steps");
		report = new ExtentReports(System.getProperty("user.dir")+"/src/main/resources/template/ExtentReportResults.html");
		test = report.startTest(testcasename);
		for (int i=0;i<steps.size();i++)
		{
			
			org.json.simple.JSONObject stepObject=(org.json.simple.JSONObject)steps.get(i);
			String screenshotPath=System.getProperty("user.dir")+"/src/main/resources/template/"+stepObject.get("screenshot").toString();
			if(stepObject.get("status").toString().equalsIgnoreCase("Pass")) {
				test.log(LogStatus.PASS,stepObject.get("description").toString());
				test.log(LogStatus.PASS,test.addScreenCapture(screenshotPath));
			}
			else {
				
				test.log(LogStatus.FAIL, stepObject.get("description").toString());
				test.log(LogStatus.FAIL,test.addScreenCapture(screenshotPath));
			}
			
		}
		report.endTest(test);
		report.flush();
		File file=new File(System.getProperty("user.dir")+"/src/main/resources/template/ExtentReportResults.html");
		  FileInputStream fl = new FileInputStream(file);
		  
	        // Now creating byte array of same length as file
	        byte[] arr = new byte[(int)file.length()];
	 
	        // Reading file content to byte array
	        // using standard read() method
	        fl.read(arr);
	 
	        // lastly closing an instance of file input stream
	        // to avoid memory leakage
	        fl.close();
	 

	}
	
}
