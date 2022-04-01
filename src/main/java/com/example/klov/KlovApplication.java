package com.example.klov;

import java.io.File;
import java.io.FileInputStream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;


@SpringBootApplication
@Controller
@CrossOrigin("*")
public class KlovApplication {
	ExtentTest test;
	ExtentReports report;
	public static void main(String[] args) {
		SpringApplication.run(KlovApplication.class, args);
	}
	@PostMapping("/reporting")
	public  @ResponseBody byte[] reportResponse(@RequestBody String json) throws Exception {
		// Parsing Service-Module json 
		   JSONParser parser = new JSONParser();
		   Object obj = parser.parse(json);
		   org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;
		   String testcasename= jsonObject.get("testname").toString();
		   org.json.simple.JSONArray steps = (org.json.simple.JSONArray) jsonObject.get("steps");
		report = new ExtentReports(System.getProperty("user.dir")+"/src/main/resources/template/ExtentReportResults.html");
		test = report.startTest(testcasename);
		for (int i=0;i<steps.size();i++)
		{
			org.json.simple.JSONObject stepObject=(org.json.simple.JSONObject)steps.get(i);
			if(stepObject.get("status").toString().equalsIgnoreCase("Pass")) {
				test.log(LogStatus.PASS,stepObject.get("description").toString());
			}
			else {
				test.log(LogStatus.FAIL, stepObject.get("description").toString());
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
	 
	        // Returning above byte array
	        return arr;
	}

}
