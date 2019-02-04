package com.fourthousandfour.pdfparse;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class globals {
	
		public static String urlPrefix = ".";
	    //public static String projectpath = "C:/Users/Vinnie881.NPWULOCAL/workspace/gitPDFTest/WebContent/";
	    public static String projectpath = "C:/Users/Vinnie881.NPWULOCAL/workspace/gitPDFTest/WebContent/";
	    public static String server = "P";
	//public static String projectpath = "/home/tomcat/tomcat/webapps/mailjackplus/WebContent";
		
		public static void changePassword2(String path,String passwordEncrypted) 
				  throws IOException 
				{
				  byte[] encoded = Files.readAllBytes(Paths.get(path));
				  String s =  new String(encoded);
				 s = s.replaceAll("<fieldname>password2</fieldname><x>0</x><y>0</y><width>0</width><height>0</height><misc>.*</misc><Visible>false</Visible><Setting>false</Setting></FixedFields>","<fieldname>password2</fieldname><x>0</x><y>0</y><width>0</width><height>0</height><misc>" + passwordEncrypted + "</misc><Visible>false</Visible><Setting>false</Setting></FixedFields>" );
				  
				  try(  PrintWriter out = new PrintWriter( path )  ){
					    out.println( s );
					}
				}
		
		
	    public static int getEnvironment()
	    {
	    	
	    	
	    	
	    	if (server.equalsIgnoreCase("D")) {
	    	return 1;
	    	} else if (server.equalsIgnoreCase("S")) {
	    	return 2;
	    	} else if (server.equalsIgnoreCase("P")) {
	    	return 3;
	    	} else {
	    		return 3;
	    	}

	    }
	   
	}
