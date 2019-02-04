package pdfparse;
import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class excelType {
public String excelType = "0";
public String mappingId;
public String documentId;
public String keyColumn;
public String keyColumnDetail;
public String sumColumns;
public String documentClass;
public String documentFormat = "PDF";
public String password = "";

private static final String ALGORITHM = "AES";
private static final byte[] keyValue = 
    new byte[] { 'T', 'h', 'a', 's', 'J', 's', 'A', 't', 'e', '1', 'r', 'e', 't', 'K', 'e', 'y' };
private static Key generateKey() throws Exception {
    Key key = new SecretKeySpec(keyValue, ALGORITHM);
    return key;
}
 public String encrypt(String valueToEnc) throws Exception {
    Key key = generateKey();
    Cipher c = Cipher.getInstance(ALGORITHM);
    c.init(Cipher.ENCRYPT_MODE, key);
    byte[] encValue = c.doFinal(valueToEnc.getBytes());
    String encryptedValue = Base64.getEncoder().encodeToString(encValue);
    return encryptedValue;
}
 public String decrypt(String encryptedValue) throws Exception {
	    Key key = generateKey();
	    Cipher c = Cipher.getInstance(ALGORITHM);
	    c.init(Cipher.DECRYPT_MODE, key);
	    byte[] decordedValue = Base64.getDecoder().decode(encryptedValue);
	    byte[] decValue = c.doFinal(decordedValue);
	    String decryptedValue = new String(decValue);
	    return decryptedValue;
	}


public excelType getExcelTypeFromTemplate(String template) throws ParserConfigurationException, SAXException, IOException
{
	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	File f = new File(template);
	  
		Document doc = docBuilder.parse(f);
		 NodeList nl = doc.getElementsByTagName("FixedFields");
		    if (nl != null) {
		        int length = nl.getLength();
		        for (int i = 0; i < length; i++) {
		            if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
		            	
		            	
		            	Element el = (Element) nl.item(i);
		            	NodeList cn = el.getChildNodes();
		            	int length1 = cn.getLength();
		            	 String currentField ="";
		            	for (int ii = 0; ii < length1; ii++) {
				            if (cn.item(ii).getNodeType() == Node.ELEMENT_NODE) {
				            	Element el1 = (Element) cn.item(ii);
				            	
				               
				                if (el1.getNodeName().contains("fieldname")) {
				                	currentField = el1.getTextContent();
				                }
				                if (el1.getNodeName().contains("misc") && currentField.equals("excelType")) {
				                	excelType = el1.getTextContent();
				                }
				                else if(el1.getNodeName().contains("misc") && currentField.equals("keyColumn")) {
		                			keyColumn = el1.getTextContent();
				                }
				                else if(el1.getNodeName().contains("misc") && currentField.equals("keyColumnDetail")) {
				                	keyColumnDetail = el1.getTextContent();
				                }
				                else if(el1.getNodeName().contains("misc") && currentField.equals("documentId")) {
				                	documentId = el1.getTextContent();
				                }
				                else if(el1.getNodeName().contains("misc") && currentField.equals("mappingId")) {
				                	mappingId = el1.getTextContent();
				                }
				                else if(el1.getNodeName().contains("misc") && currentField.equals("sumColumns")) {
				                	sumColumns = el1.getTextContent();
				                }
				                else if(el1.getNodeName().contains("misc") && currentField.equals("poDocumentClass")) {
				                	documentClass =el1.getTextContent(); 	
				                }
				                else if(el1.getNodeName().contains("misc") && currentField.equals("password2")) {
				                	try {
										password  =decrypt(el1.getTextContent());
									} catch (DOMException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} 	
				                }
				                	
		                			
		                			
		                			
				                }
			                	
			                			
			                		
			                    }
				            }
				         }
		            	
		                
		                	

		                }
		                
		                		                
		                
		    
		            
		    
		    
			return this;
}
}
