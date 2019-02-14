import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.FileNameMap;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Files;


import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.DOMSource;
import com.google.gson.*;
import org.json.*;



import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

//import com.google.gson.Gson;



public class DirMonTest {
	
	private final static String stageToken ="cbd51490f9a01eaeef42ebea8eb294a6";	
	private final static String productionToken ="94286ec055e736341e8595ff8d958734";	
	private final static String developerToken ="768f6482c55a54048f279d43975321d9";	
	
	public static String  get_url(String Server) {
		if(Server.equalsIgnoreCase("L"))
		{
		return "http://localhost";
		}
		else if(Server.equalsIgnoreCase("D"))
		{
			return "http://199.192.201.164:8080/c2mMJP";	
		}
		else if(Server.equalsIgnoreCase("S"))
		{
			return "http://199.192.201.164:8080/c2mMJP";	
		}
		else if(Server.equalsIgnoreCase("P"))
		{
			return "http://199.192.201.164:8080/c2mMJP";	
		}
		return "http://199.192.201.164:8080/c2mMJP";
		
	}
	public static String  get_url_rest(String Server,boolean test) {
		if(Server.equalsIgnoreCase("S") && test)
		{
			
			Server = "D";
		}
		else if(Server.equalsIgnoreCase("P") && test)
		{
			Server = "S";
		}
		
		
		if(Server.equalsIgnoreCase("L"))
		{
			return "https://dev-rest.click2mail.com";	
		}
		else if(Server.equalsIgnoreCase("D"))
		{
			return "https://dev-rest.click2mail.com";	
		}
		else if(Server.equalsIgnoreCase("S"))
		{
			return "https://stage-rest.click2mail.com";	
		}
		else if(Server.equalsIgnoreCase("P"))
		{
			return "https://rest.click2mail.com";	
		}
		return "https://rest.click2mail.com";
		
	}
	
	public static void printDocument(Document doc, OutputStream out) throws IOException, TransformerException {
		//example printDocument(templateXML, System.out);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

		transformer.transform(new DOMSource(doc), 
			new StreamResult(new OutputStreamWriter(out, "UTF-8")));
	}

	public results getTemplateToken(String server,String username,String password) throws IOException {

	
		String url =  get_url_rest(server,false) + "/molpro/system/account/authorizeSession";
		
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setReadTimeout(0);
		connection.setConnectTimeout(0);
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		connection.setInstanceFollowRedirects( false );
		
        String authString = username + ":" + password;
		String authStringEnc =  Base64.getEncoder().encodeToString(authString.getBytes());
        System.out.println("Basic " + authStringEnc);
        
		connection.setRequestProperty("Authorization", "Basic " + authStringEnc);		
		if(server.equalsIgnoreCase("S"))
		{
		connection.setRequestProperty("X-Auth-Token", stageToken);		
		}
		else if(server.equalsIgnoreCase("P"))
		{
			connection.setRequestProperty("X-Auth-Token", productionToken);

		}
		else if(server.equalsIgnoreCase("D"))
		{
			connection.setRequestProperty("X-Auth-Token", developerToken);
		}
		
	    			
	    //String data = URLEncoder.encode("billingType", "UTF-8") + "=" + URLEncoder.encode("User Credit", "UTF-8");
	    			
		String data ="";
	    OutputStreamWriter wr =  new OutputStreamWriter( connection.getOutputStream());
		wr.write(data);
		wr.flush();
					
		//String result = null;
		try
		{
			
		
	     InputStream is =connection.getInputStream();
	     
					InputStreamReader isr = new InputStreamReader(is);

					int numCharsRead;
					char[] charArray = new char[1024];
					StringBuffer sb = new StringBuffer();
					while ((numCharsRead = isr.read(charArray)) > 0) {
						sb.append(charArray, 0, numCharsRead);
					}
		 //result = sb.toString();	
		//System.out.println(result); // Should be 200
		isr.close();
		is.close();
		
		results result = new results();
		 if(Integer.parseInt(getString(sb.toString(),"/account/status/text()")) == 0)
		 {
			 result.id = "1";
			result.message = getString(sb.toString(),"/account/sessionId/text()");
			System.out.println(result.message);
			 
		 }
 		else
 		{
 			result.id = "0";
 			result.message = "";
 		//not auth	
 		}
		//System.out.println(result); // Should be 200
		isr.close();
		is.close();
		return result; 
		} catch (IOException e) {
			BufferedReader br = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
			String line = "";
			
			StringBuilder sb = new StringBuilder();	
			
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			
			results result = new results();
			
			
			result.id = "0";
		//	result.message =  getString(sb.toString(),"/job/description/text()");
			return result; 
		//	submitJob(jobId);
			
		
		}catch (Exception e) {
		results result = new results();
				
				
				result.id = "0";
				result.message =  e.getMessage();
				//System.out.println(sb.toString()); // Should be 200
				return result;
			}

		
		
	}

	public c2mTemplate getTemplateById(String server, String templateId, String sessionId) throws IOException {

		results r = new results();
		//get URL
		String data = "?";
		data += "sessionId" + "="
				+ sessionId + "&"
				//+ Base64.getEncoder().encodeToString(sessionId.getBytes())
				+ "id" + "=" + templateId;
				//+ Base64.getEncoder().encodeToString(templateId.getBytes());
		System.out.println(data);
		//String url =  get_url_rest(server,false) + "/molpro/system/mailingtemplate/" + data;
		String url =  get_url_rest(server,false) + "/molpro/system/mailingtemplate/" + templateId + "?" + "sessionId=" + sessionId;

		//Open URL connection
		System.out.println(url);
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setReadTimeout(0);
		connection.setConnectTimeout(0);
		connection.setDoOutput(true);
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Accept", "application/xml");
		//connection.setInstanceFollowRedirects( false );
		
        //String authString = username + ":" + password;
		//String authStringEnc =  Base64.getEncoder().encodeToString(authString.getBytes());
        
		//connection.setRequestProperty("Authorization", "Basic " + authStringEnc);		
		if(server.equalsIgnoreCase("S")) {
			connection.setRequestProperty("X-Auth-Token", stageToken);		
		} else if(server.equalsIgnoreCase("P")) {
			connection.setRequestProperty("X-Auth-Token", productionToken);
		} else if(server.equalsIgnoreCase("D")) {
			connection.setRequestProperty("X-Auth-Token", developerToken);
		}


		Document templateXML = null;
		c2mTemplate c2m = new c2mTemplate();
		try {
			InputStream is = connection.getInputStream(); 
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = domFactory.newDocumentBuilder();
			templateXML = builder.parse(is);
			is.close();
			//printDocument(templateXML, System.out);
			c2m = parsec2mFromTemplateFile(templateXML);
		} catch (IOException e) {
			//this here reads what's returned if stageAccount connection goes bad, I think.
			System.out.println(e.getMessage());
			if(connection == null){
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c2m;
	}
	
	public static c2mTemplate parsec2mFromTemplateFile(Document dDoc) throws Exception {

		/* 
			sample XML doc

			<?xml version="1.0" encoding="UTF-8"?><templates>
			<status>0</status>
			<description>Success</description>
			<id>344</id>
				<template>
					<addressBlockh>75.0</addressBlockh>
					<addressBlockw>100.0</addressBlockw>
					<addressBlockx>100.0</addressBlockx>
					<addressBlocky>140.0</addressBlocky>
					<addressOnPage>1</addressOnPage>
					<documentClass>Letter 8.5 x 11</documentClass>
					<documentId>330879</documentId>
					<documentName>50_US_Addresses_17</documentName>
					<envelope>#10 Double Window</envelope>
					<id>344</id>
					<isDefault>false</isDefault>
					<layout>Address on Separate Page</layout>
					<listId>366410</listId>
					<listName>50_US_Addresses_32</listName>
					<mailClass>First Class</mailClass>
					<mappingId>1490</mappingId>
					<pageConstantText>October 4th, 201</pageConstantText>
					<pageConstanth>65.0</pageConstanth>
					<pageConstantw>100.0</pageConstantw>
					<pageConstantx>50.0</pageConstantx>
					<pageConstanty>20.0</pageConstanty>
					<pageRange>0</pageRange>
					<paperType>White 24#</paperType>
					<printColor>Full Color</printColor>
					<printOption>Printing One side</printOption>
					<productionTime>Next Day</productionTime>
					<replyAddress/>
					<returnAddress>121317Test2</returnAddress>
					<sourceId>2</sourceId>
					<templateName>16091RegressionTesting</templateName>
					<userid>1422</userid>
				</template>
			</templates>
		*/

		printDocument(dDoc, System.out);
		c2mTemplate c2mT = new c2mTemplate();
		c2mT.h = (int) Double.parseDouble(dDoc.getElementsByTagName("addressBlockh").item(0).getTextContent());
		c2mT.w = (int) Double.parseDouble(dDoc.getElementsByTagName("addressBlockw").item(0).getTextContent());
		c2mT.x = (int) Double.parseDouble(dDoc.getElementsByTagName("addressBlockx").item(0).getTextContent());
		c2mT.y = (int) Double.parseDouble(dDoc.getElementsByTagName("addressBlocky").item(0).getTextContent());
		c2mT.documentClass = dDoc.getElementsByTagName("documentClass").item(0).getTextContent();
		c2mT.envelope = dDoc.getElementsByTagName("envelope").item(0).getTextContent();
		c2mT.layout = dDoc.getElementsByTagName("layout").item(0).getTextContent();
		c2mT.excelMapping = dDoc.getElementsByTagName("listId").item(0).getTextContent();
		c2mT.mailClass = dDoc.getElementsByTagName("mailClass").item(0).getTextContent();
		c2mT.validatedText = dDoc.getElementsByTagName("pageConstantText").item(0).getTextContent();
		c2mT.sh = (int) Double.parseDouble(dDoc.getElementsByTagName("pageConstanth").item(0).getTextContent());
		c2mT.sw = (int) Double.parseDouble(dDoc.getElementsByTagName("pageConstantw").item(0).getTextContent());
		c2mT.sx = (int) Double.parseDouble(dDoc.getElementsByTagName("pageConstantx").item(0).getTextContent());
		c2mT.sy = (int) Double.parseDouble(dDoc.getElementsByTagName("pageConstanty").item(0).getTextContent());
		c2mT.paperType = dDoc.getElementsByTagName("paperType").item(0).getTextContent();
		c2mT.color = dDoc.getElementsByTagName("printColor").item(0).getTextContent();
		c2mT.printOption = dDoc.getElementsByTagName("printOption").item(0).getTextContent();
		c2mT.productionTime = dDoc.getElementsByTagName("productionTime").item(0).getTextContent();
		//System.out.println("This is type coercion" + c2mT.h);
		return c2mT;
	}

	protected String getString(String xml,String exprStr) {
		   DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
	        try {
	        	InputStream is = new ByteArrayInputStream(xml.getBytes());
	            DocumentBuilder builder = domFactory.newDocumentBuilder();
	            Document dDoc = builder.parse(is);

	            XPath xPath = XPathFactory.newInstance().newXPath();
	            XPathExpression expr =  xPath.compile(exprStr);
	            NodeList nodes = (NodeList) expr.evaluate(dDoc, XPathConstants.NODESET);
	            for (int i = 0; i < nodes.getLength(); )
	            {
	            //	System.out.println( nodes.item(i).getNodeValue());
	            	return( nodes.item(i).getNodeValue());
	            	
	            //System.out.println(node.getNodeValue());
	            }
	            
	        	} catch (Exception e) {
	        		return xml;
	        	}
	       return "0";		
	}
	
	public results createDocumentRestAltered(String Server,String filePDF) throws MalformedURLException, IOException {
		
		//https://dev-mailjack-plus.click2mail.com/apiProcess
				String url = this.get_url_rest(Server,false) + "/molpro/documents";
				String charset = "UTF-8";
				
				
				File binaryFilePDF = new File(filePDF);
				Format formatter = new SimpleDateFormat("YYYY-MM-dd_hh-mm-ss");
				Date date = new Date();
				//Date date = new Date(0);	
				String documentName ="simplex_" + formatter.format(date) + ".pdf";
				String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
				String CRLF = "\r\n"; // Line separator required by multipart/form-data.

				HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
				connection.setReadTimeout(0);
				connection.setConnectTimeout(0);
				connection.setDoOutput(true);
				connection.setUseCaches(false);
				connection.setRequestMethod("POST");
				
				connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
				connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
				Base64.Encoder  encoder =  Base64.getEncoder();
		        String authString = "tkeatingbusiness" + ":" + "Its@cademic19"; //fix this
				String authStringEnc = encoder.encodeToString(authString.getBytes());
				connection.setRequestProperty("Authorization", "Basic " + authStringEnc);		
				
				
				    OutputStream output = connection.getOutputStream();
				    PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);
				    
				    writer.append("--" + boundary).append(CRLF);
				    writer.append("Content-Disposition: form-data; name=\"documentName\"").append(CRLF);
				    writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
				    writer.append(CRLF).append(documentName).append(CRLF).flush();
				    
				    
				    writer.append("--" + boundary).append(CRLF);
				    writer.append("Content-Disposition: form-data; name=\"documentClass\"").append(CRLF);
				    writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
				    writer.append(CRLF).append("Letter 8.5 x 11").append(CRLF).flush();
				    
				    writer.append("--" + boundary).append(CRLF);
				    writer.append("Content-Disposition: form-data; name=\"documentFormat\"").append(CRLF);
				    writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
				    writer.append(CRLF).append("PDF").append(CRLF).flush();
				    
	
			//binary file
				    writer.append("--" + boundary).append(CRLF);
				    writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + binaryFilePDF.getName() + "\"").append(CRLF);
				    writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(binaryFilePDF.getName())).append(CRLF);
				    writer.append("Content-Transfer-Encoding: binary").append(CRLF);
				    writer.append(CRLF).flush();
				    Files.copy(binaryFilePDF.toPath(), output);
				    output.flush(); // Important before continuing with writer!
				    writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.

				    // End of multipart/form-data.
				    writer.append("--" + boundary + "--").append(CRLF).flush();	    
				  

				output.close();
				// Request is lazily fired whenever you need to obtain information about response.
				//int responseCode = connection.getResponseCode();
				try{
					
			     InputStream is =connection.getInputStream();
							InputStreamReader isr = new InputStreamReader(is);

							int numCharsRead;
							char[] charArray = new char[1024];
							StringBuffer sb = new StringBuffer();
							while ((numCharsRead = isr.read(charArray)) > 0) {
								sb.append(charArray, 0, numCharsRead);
							}
				
				//System.out.println(result); // Should be 200
				isr.close();
				is.close();
				results result = new results();
				result.id = getString(sb.toString(),"/document/id/text()");
				result.message =  getString(sb.toString(),"/document/description/text()");
				//System.out.println(result); // Should be 200
				isr.close();
				is.close();
				return result; 
				} catch (IOException e) {
					BufferedReader br = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
					String line = "";
					
					StringBuilder sb = new StringBuilder();	
					
					while ((line = br.readLine()) != null) {
						sb.append(line);
					}
					
				
					results result = new results();
					
					
					result.id = "0";
					result.message =  getString(sb.toString(),"/document/description/text()");
					return result; 
				//	submitJob(jobId);
					
				
			}catch (Exception e) {
				results result = new results();		
				result.id = "0";
				result.message =  e.getMessage();
				//System.out.println(sb.toString()); // Should be 200
				return result;
					}
		
		/*
		https://{username}:{password}@rest.click2mail.com/molpro/documents
			/documentName = "Sample Letter"
			/documentClass = "Letter 8.5 x 11"
			/documentFormat = "PDF"
			/file = "@{filename_of_pdf}"
			*/
			 
	}

	public c2mTemplate documentHelper(String server, String sessionId, String documentName, String documentClass) throws MalformedURLException, IOException {
		results r = new results();
		//get URL
		
		String data = "?";
		data += "sessionId" + "="
				+ sessionId + "&"
				+ documentName + "&"
				+ documentClass + "&";
				
				
		System.out.println(data);
		String url =  get_url_rest(server,false) + "/molpro/system/mailingtemplate/" + data;

		//Open URL connection
		System.out.println(url);
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setReadTimeout(0);
		connection.setConnectTimeout(0);
		connection.setDoOutput(true);
		connection.setRequestMethod("GET");
		connection.setReadTimeout(0);
		connection.setConnectTimeout(0);
		connection.setRequestProperty("Accept", "application/json");
		
		//connection.setInstanceFollowRedirects( false );
		
        //String authString = username + ":" + password;
		//String authStringEnc =  Base64.getEncoder().encodeToString(authString.getBytes());
        
	    //connection.setRequestProperty("Authorization", "Basic " + authStringEnc);		
		if(server.equalsIgnoreCase("S"))
		{
		connection.setRequestProperty("X-Auth-Token", stageToken);		
		}
		else if(server.equalsIgnoreCase("P"))
		{
			connection.setRequestProperty("X-Auth-Token", productionToken);

		}
		else if(server.equalsIgnoreCase("D"))
		{
			connection.setRequestProperty("X-Auth-Token", developerToken);
		}

		try {
			
		
	     InputStream is = connection.getInputStream();
	     
					InputStreamReader isr = new InputStreamReader(is);

					int numCharsRead;
					char[] charArray = new char[1024];
					StringBuffer sb = new StringBuffer();
					while ((numCharsRead = isr.read(charArray)) > 0) {
						sb.append(charArray, 0, numCharsRead);
					}
		String result = sb.toString();
		System.out.println(result); // Should be 200
		isr.close();
		is.close();
		} catch (IOException e) {
			//this here reads what's returned if stageAccount connection goes bad, I think.
			System.out.println(e.getMessage());
			if(connection == null){
				return null;
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
					String line = "";
					
					StringBuilder sb = new StringBuilder();	
					
					while ((line = br.readLine()) != null) {
						sb.append(line);
					}
					
				
					results result = new results();
					
					
					result.id = "0";
					result.message =  getString(sb.toString(),"/document/description/text()");
					System.out.println(result.message);
					//get the json version
					
					//return result; 
		}
		catch (Exception e) {
			e.printStackTrace();
		}


		c2mTemplate c2m = new c2mTemplate();
		return c2m;
	}

	/* public void getParsedInfo(String server, String sessionId, Document template, Results documentId) {

		String data = "?";
		data += "sessionId" + "="
				+ sessionId + "&"
				//+ Base64.getEncoder().encodeToString(sessionId.getBytes())
				+ "id" + "=" + templateId;
				//+ Base64.getEncoder().encodeToString(templateId.getBytes());
		System.out.println(data);
		//String url =  get_url_rest(server,false) + "/molpro/system/mailingtemplate/" + data;
		String url =  get_url_rest(server,false) + "/molpro/system/mailingtemplate/" + templateId + "?" + "sessionId=" + sessionId;

		//Open URL connection
		System.out.println(url);
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setReadTimeout(0);
		connection.setConnectTimeout(0);
		connection.setDoOutput(true);
		connection.setRequestMethod("GET");
		connection.setReadTimeout(0);
		connection.setConnectTimeout(0);
		connection.setRequestProperty("Accept", "application/xml");
		//connection.setInstanceFollowRedirects( false );
		
        //String authString = username + ":" + password;
		//String authStringEnc =  Base64.getEncoder().encodeToString(authString.getBytes());
        
		//connection.setRequestProperty("Authorization", "Basic " + authStringEnc);		
		if(server.equalsIgnoreCase("S")) {
			connection.setRequestProperty("X-Auth-Token", stageToken);		
		} else if(server.equalsIgnoreCase("P")) {
			connection.setRequestProperty("X-Auth-Token", productionToken);
		} else if(server.equalsIgnoreCase("D")) {
			connection.setRequestProperty("X-Auth-Token", developerToken);
		}

	} */

	public String sendFileNEW(String filePDF, String templateId, Account stageAccount,String server) throws IOException {

		//https://dev-mailjack-plus.click2mail.com/apiProcess
		
		/*https://dev-mailjack-plus.click2mail.com/directoryMonitorAddressParsing \
			-H 'authorization: Basic aml0ZW5kcmExMDA6Sml0ZW5kcmExMjM=' \
			-H 'content-type: multipart/form-data' \
			-F userName=jitendra100 \
			-F password=***** \
			-F templateId=733 \
			-F 'appSignature=MailJack Address Capture' \
			-F 'file=@Six_Address_With_Multiple_Address Line.pdf'
			*/
		//String url = this.get_url(Server) + "/apiProcess";
		String pre = "";
		if(server.equals("S"))
				{
			pre = "stage-";
			
				}
		else if(server.equals("D"))
		{
			pre="dev-";
		}
		String url = "https://"+pre+"mailjack-plus.click2mail.com/directoryMonitorAddressParsing"+"?CostEstimate=true";
		System.out.println(url);
		String charset = "UTF-8";
		
		
		File binaryFilePDF = new File(filePDF);
		//File binaryFileC2M = new File(filec2m);
		
		String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
		String CRLF = "\r\n"; // Line separator required by multipart/form-data.

		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setReadTimeout(0);
		connection.setConnectTimeout(0);
		connection.setDoOutput(true);
		connection.setUseCaches(false);
		connection.setRequestMethod("POST");

		/*
		if(server.equalsIgnoreCase("S")) {
			connection.setRequestProperty("X-Auth-Token", stageToken);		
		} else if(server.equalsIgnoreCase("P")) {
			connection.setRequestProperty("X-Auth-Token", productionToken);
		} else if(server.equalsIgnoreCase("D")) {
			connection.setRequestProperty("X-Auth-Token", developerToken);
		} 
		*/
		
		String authString = stageAccount.accountName + ":" + stageAccount.accountPw;
		String authStringEnc =  Base64.getEncoder().encodeToString(authString.getBytes());
        connection.setRequestProperty("Authorization", "Basic " + authStringEnc);		
		
		
		connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");

        //String authString = name + ":" + password;
		//String authStringEnc = encoder.encode(authString.getBytes());
		//connection.setRequestProperty("Authorization", "Basic " + authStringEnc);		
		
		
		    OutputStream output = connection.getOutputStream();
		    PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);
		    writer.append("--" + boundary).append(CRLF);
		    writer.append("Content-Disposition: form-data; name=\"userName\"").append(CRLF);
		    writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
		    writer.append(CRLF).append(stageAccount.accountName).append(CRLF).flush();

		    writer.append("--" + boundary).append(CRLF);
		    writer.append("Content-Disposition: form-data; name=\"password\"").append(CRLF);
		    writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
		    writer.append(CRLF).append(stageAccount.accountPw).append(CRLF).flush();

			//System.out.println("log in info sent");
		    
		    writer.append("--" + boundary).append(CRLF);
		    writer.append("Content-Disposition: form-data; name=\"templateId\"").append(CRLF);
		    writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
		    writer.append(CRLF).append(templateId).append(CRLF).flush();
		    
			//System.out.println("template flushed");

		    writer.append("--" + boundary).append(CRLF);
		    writer.append("Content-Disposition: form-data; name=\"appSignature\"").append(CRLF);
		    writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
		    writer.append(CRLF).append("MailJack Address Capture").append(CRLF).flush();
		    
		    //System.out.println("MJ+ AC sent");

		/*
			if(wait)
			{
			    writer.append("--" + boundary).append(CRLF);
			    writer.append("Content-Disposition: form-data; name=\"wait\"").append(CRLF);
			    writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
			    writer.append(CRLF).append("Waiting").append(CRLF).flush();
				    
			}
			*/
	  	//text file
		    /*writer.append("--" + boundary).append(CRLF);
		    writer.append("Content-Disposition: form-data; name=\"textFile\"; filename=\"" + binaryFileC2M.getName()+ "\"").append(CRLF);
		    writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF); // Text file itself must be saved in this charset!
		    writer.append(CRLF).flush();
		    Files.copy(binaryFileC2M.toPath(), output);
		    output.flush(); // Important before continuing with writer!
		    writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.
			*/
		System.out.println("Sending File now");
		//binary file
		    writer.append("--" + boundary).append(CRLF);
		    writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + binaryFilePDF.getName() + "\"").append(CRLF);
		    writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(binaryFilePDF.getName())).append(CRLF);
		    writer.append("Content-Transfer-Encoding: binary").append(CRLF);
		    writer.append(CRLF).flush();
		    Files.copy(binaryFilePDF.toPath(), output);
		    output.flush(); // Important before continuing with writer!
		    writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.

			//int responseCode = connection.getResponseCode();
			//System.out.println(responseCode);

		    // End of multipart/form-data.
		    writer.append("--" + boundary + "--").append(CRLF).flush();	    
		  

			output.close();
		// Request is lazily fired whenever you need to obtain information about response.
		//int responseCode = connection.getResponseCode();
		//System.out.println(responseCode);
		try{
	     	InputStream is = connection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);

			int numCharsRead;
			char[] charArray = new char[1024];
			StringBuffer sb = new StringBuffer();
			while ((numCharsRead = isr.read(charArray)) > 0) {
				sb.append(charArray, 0, numCharsRead);
			}
		String result = sb.toString();
		//System.out.println("This is the response from Send File New: " + result);
		//	System.out.println(result); // Should be 200
		isr.close();
		is.close();
		return result;
		} catch (IOException e) {
			e.printStackTrace();
			BufferedReader br = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
			String line = "";
		
			StringBuilder sb = new StringBuilder();	
		
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			//	System.out.println(sb.toString()); // Should be 200
			return sb.toString(); 
			//	submitJob(jobId);
		
		}catch (Exception e) {
		return "{}";
		}
	}

	public String costEstimate(String server, c2mTemplate c2m, int quantity, String postage) throws MalformedURLException, IOException { //, c2mTemplate template) { //Should I go ahead and make stageAccount c2mTemplate or ? 

		String cost;

		//object to be returned
		results result = new results();
		//What information are we sending?
		String data = "?";
		data += "documentClass" + "=" + c2m.documentClass + "&"
				+ "layout" + "=" + c2m.layout + "&"
				+ "productionTime" + "=" + c2m.productionTime + "&"
				+ "envelope" + "=" + c2m.envelope + "&"
				+ "color" + "=" + c2m.color + "&"
				+ "paperType" + "=" + c2m.paperType + "&"
				+ "printOption" + "=" + c2m.printOption + "&"
				+ "mailClass" + "=" + c2m.mailClass + "&"
				//+ "quantity" + "=" + quantity + "&"
				//+ "numberOfPages" + "=" + numberOfPages + "&"
				+ "paymentType" + "=" + "Invoice";
		
		//+ "nonStandardQuantity" + "=" + nonStandardQuantity + "&"
		//+ "internationalQuantity" + "=" + internationalQuantity + "&"

		//Show me
		String pattern = "(\\s)";
		String pattern2 = "(\\#)";
		data = data.replaceAll(pattern, "%20");
		data = data.replaceAll(pattern2, "%23");
		//System.out.println(data);
		
		//Where are we sending this information?
		String url = this.get_url_rest(server, false) + "/molpro/costEstimate" + data;
		//URLEncoder enc = new URLEncoder;
		//Show Me
		//System.out.println(url);

		//Okay, let's send it 
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setReadTimeout(0);
		connection.setConnectTimeout(0);
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Accept", "application/xml");
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
		Base64.Encoder  encoder =  Base64.getEncoder();
		String authString = "tkeatingbusiness" + ":" + "Its@cademic19"; //fix this
		String authStringEnc = encoder.encodeToString(authString.getBytes());
		connection.setRequestProperty("Authorization", "Basic " + authStringEnc);

		//What did we recieve? What format is it in and how to put it into the format I'd like it to be in?
		try {	
			InputStream is = connection.getInputStream();	
			InputStreamReader isr = new InputStreamReader(is);

			int numCharsRead;
			char[] charArray = new char[1024];
			StringBuffer sb = new StringBuffer();
			while ((numCharsRead = isr.read(charArray)) > 0) {
				sb.append(charArray, 0, numCharsRead);
			}
			
			result.message = sb.toString();
			//System.out.println(result.message);

			cost = getString(result.message,"//subtotal/text()");
			//System.out.println(result); // Should be 200
			isr.close();
			is.close();
			return cost;
		} catch (IOException e) {
			System.out.println(e);
			BufferedReader br = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
			String line = "";
			
			StringBuilder sb = new StringBuilder();	
			
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			System.out.println(sb.toString());
			result.id = "0";
			return result.message; 
			} catch (Exception e) {
				result.id = "0";
				result.message =  e.getMessage();
				System.out.println(e); // Should be 200
				System.out.println("not io exception" + result.message);
				return result.message;
			}
	}

	public HashMap cleanSendFileNewResponse(String response) {
		response = response.substring(1, response.length()- 1);
		response = response.replace("\\", "");
		response = response.replace(" ", "");
		//response = response.replace("\"", "");
		System.out.println(response);
		Gson test = new Gson();
		//JsonReader reader = new JsonReader(new StringReader(response));
		//reader.setLenient(true);
		//HashMap jsonHash = test.fromJson(reader, HashMap.class);
		//no JsonReader and no leniency 
		HashMap<String, Integer>[] jsonHash = test.fromJson(response, HashMap[].class);
		HashMap<String, Integer> postageHash = jsonHash[1];
		System.out.println(postageHash.get("standard"));
		return postageHash;
	}
	
	/*  public int iterateHashMap(HashMap<String, Integer> postageHash, String server, c2mTemplate c2m ) {
		System.out.println("For Loop:");
		int totalCost = 0;
        for (Map.Entry me : postageHash.entrySet()) {
          System.out.println("Key: "+ me.getKey() + " & Value: " + me.getValue());
          int quantity = me.getValue();
          String standardization = me.getKey();
          totalCost = totalCost + Integer.parseInt((this.costEstimate(server, c2m, quantity, standardization)));
        }
        System.out.println(totalCost);
        return totalCost;
	} */
	public static void main(String[] args){

		DirMonTest devTest = new DirMonTest();
		DirMonTest stageTest = new DirMonTest();

		try {
			Account devAccount = new Account("tkeatingbusiness", "Its@cademic19", "D");
			Account stageAccount = new Account("tkeatingbusiness", "Its@cademic19", "S");
			String devEnvironment = "D";
			String stageEnvironment = "S";
			String filename = "electronicDistribution.pdf";
			String devTemplateId = "867";
			String stageTemplateId = "344";
			//userid = 3826
			
			results devSessionId = devTest.getTemplateToken(devEnvironment, "tkeatingbusiness", "Its@cademic19");
			//results stageSessionId = stageTest.getTemplateToken(stageEnvironment, "tkeatingbusiness", "Its@cademic19");
			
			c2mTemplate devTestTemplate = devTest.getTemplateById(devEnvironment, devTemplateId, devSessionId.message);
			//c2mTemplate stageTestTemplate = stageTest.getTemplateById(stageEnvironment, stageTemplateId, stageSessionId.message);
			
			String response = devTest.sendFileNEW(filename, devTemplateId, devAccount, devEnvironment);
			System.out.println("In Dev" + "\n" + "This is the response from sendFileNew " + response);
			//System.out.println("In Stage" + "\n" + "This is the response from sendFileNew" + stageTest.sendFileNEW(filename, stageTemplateId, stageAccount, stageEnvironment));
			//devTest.iterateHashMap(cleanSendFileNewResponse(response), stageEnvironment, devTestTemplate);
			//results documentId = test.createDocumentRestAltered("S", "sample.pdf");
			
			//System.out.println(devTestTemplate.documentClass);
			//System.out.println(stageTestTemplate.documentClass);
			
			//System.out.println(devTestTemplate.costEstimate("D", devTestTemplate));
			//System.out.println(test.costEstimate("S", stageTestTemplate));
			/* int b = 5;
			int c = 8;
			int d = 9;
			int e = 13;
			String status = "1";
			String description = "Successful";
			String jobJSON = "{\"nonstandard\": " + b + ","
						+ "\"standard\": " + c + "," 
						+ "\"international\": " + d + ","
						+ "\"totalItems\": " + e + "}";
			String statusJSON = "{\"status\": " + status + ","
						+ "\"description\": " + description + "}";
			String responseJSON = "[" + statusJSON + "," + jobJSON + "]";
			Gson gson = new Gson();
			gson.toJson(responseJSON); */

		} catch(IOException e) {
			e.printStackTrace();
		//} catch (TransformerException e) {
		//	e.printStackTrace();
		}
	  }

}