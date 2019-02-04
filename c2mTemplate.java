

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



public class c2mTemplate {
public String layout;
public boolean replacePassword = false;
public String documentClass;
public String productionTime;
public String envelope = "";
public String color;
public String paperType;
public String printOption;
public String mailClass;
public String password = "";
public String username;
public String raName;
public String raAddress1;
public String raAddress2;
public String raOrganization;
public String raCity;
public String raState;
public String raZip;
public int pageParseOverride = 0;
public String validatedText;
public int x;
public int y;
public int w;
public int h;
//Stationary Identifier
public int sx;
public int sy;
public int sw;
public int sh;
public String pdfFileName;
public boolean readSingle = false;
public int pageNo = 1;
public boolean testMode = true;
public String appSignature = "";
public boolean omitNonStandard = false;
public boolean omitNonStandardWarning = false;
public boolean omitNonValidated = false;
public boolean advancedOptions = false;
public String excelMapping = "";
public boolean useList =false;
private Base64.Encoder  encoder =  Base64.getEncoder();
private Base64.Decoder  decoder = Base64.getDecoder();
private Float pointToInch = 0.0138889F;
public boolean useGoogleForAddresses =false;
//public boolean useGoogleForAddresses =true;
public boolean validLogin =false;
public static final Charset UTF_16LE = Charset.forName("UTF-16LE");
public  static String _Drestapi = "https://dev-rest.click2mail.com";
public static String  _restapi = "https://rest.click2mail.com";
public static String  _Srestapi = "https://stage-rest.click2mail.com";
public String newfailEnvelope = "";
public String documentError = "";
ArrayList<String> llayouts = new ArrayList<String>();
ArrayList<String> lenvelopes = new ArrayList<String>();
ArrayList<String> lproductionTime = new ArrayList<String>();
ArrayList<String> lpaperType = new ArrayList<String>();
ArrayList<String> lprintColor = new ArrayList<String>();
ArrayList<String> lprintOption = new ArrayList<String>();
ArrayList<String> lmailClass = new ArrayList<String>();

	
	 	
	 	
	 	public c2mTemplate() {
		
	}
	


	
	

	public String writeNewPasswordSettings(String templateToModify,String password) throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		File f = new File(templateToModify);
		  //String filePath = f.getAbsolutePath().substring(0,f.getAbsolutePath().lastIndexOf(File.separator));
		File nf = new File(templateToModify);
		Document doc = docBuilder.parse(f);


		Element rootElement = doc.getDocumentElement();
		
		Element fixedFields = doc.createElement("FixedFields");
		rootElement.appendChild(fixedFields);
		
		Element fieldname = doc.createElement("fieldname");
		fieldname.setTextContent("password2");
		Element x = doc.createElement("x");
		x.setTextContent("0");
		Element y = doc.createElement("y");
		y.setTextContent("0");
		Element width = doc.createElement("width");
		width.setTextContent("0");
		Element height = doc.createElement("height");
		height.setTextContent("0");
		
		Element misc = doc.createElement("misc");
		 String pw = "";
		try {
			pw = new pdfparse.excelType().encrypt(password);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		misc.setTextContent(pw);
		Element visible = doc.createElement("Visible");
		visible.setTextContent("false");
		Element Settings = doc.createElement("Setting");
		Settings.setTextContent("false");
		Element rowid = doc.createElement("rowid");
		rowid.setTextContent("0");
		
		
		fixedFields.appendChild(fieldname);
		fixedFields.appendChild(x);
		fixedFields.appendChild(y);
		fixedFields.appendChild(width);
		fixedFields.appendChild(height);
		fixedFields.appendChild(misc);
		fixedFields.appendChild(visible);
		fixedFields.appendChild(Settings);
		

					

		
		TransformerFactory tFactory = TransformerFactory.newInstance();
				    Transformer transformer = 
				    tFactory.newTransformer();

				    DOMSource source = new DOMSource(doc);
				    
				    StreamResult result = new StreamResult(nf);
				   
				    try {
						transformer.transform(source, result);
					} catch (TransformerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return nf.getAbsolutePath();
				  
	
}
	
	public String decryptPasswordOLD(String myData) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException
	{
		//make sure to install files in jrehome/lib/security from http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html
				//String myData = "kgxCSfBSw5BRxmjgc4qYhwN12dxG0dyf2eu51dI8j7U=";
			    byte[] salt = new byte[] {0x49, 0x76, 0x61, 0x6E, 0x20, 0x4D, 0x65, 0x64, 0x76, 0x65, 0x64, 0x65, 0x76};
			    String pw  = "kmjfds(#1231SDSA()#rt32geswfkjFJDSKFJDSFd";

			    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			    PBEKeySpec pbeKeySpec = new PBEKeySpec(pw.toCharArray(), salt, 1000, 384);
			    Key secretKey = factory.generateSecret(pbeKeySpec);
			    byte[] key = new byte[32];
			    byte[] iv = new byte[16];
			    System.arraycopy(secretKey.getEncoded(), 0, key, 0, 32);
			    System.arraycopy(secretKey.getEncoded(), 32, iv, 0, 16);


			    SecretKeySpec secretSpec = new SecretKeySpec(key, "AES");
			    AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
			    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			 //   Cipher cipher1 = Cipher.getInstance("AES/CBC/PKCS5Padding");
			    
			    try {
			        cipher.init(Cipher.DECRYPT_MODE,secretSpec,ivSpec);
			      //  cipher1.init(Cipher.ENCRYPT_MODE,secretSpec,ivSpec);
			    } catch (InvalidKeyException e) {
			        // TODO Auto-generated catch block
			        e.printStackTrace();
			    } catch (InvalidAlgorithmParameterException e) {
			        // TODO Auto-generated catch block
			        e.printStackTrace();
			    }

			    //byte[] decordedValue;
			    //decordedValue = new BASE64Decoder().decodeBuffer(myData);
			    //decordedValue = myData.getBytes("ISO-8859-1");
			      //byte[] decValue = cipher.doFinal(myData.getBytes());
			    //Base64.getMimeEncoder().encodeToString(cipher.doFinal(myData.getBytes()));
			        //String decryptedValue = new String(decValue);
			    byte[] decodedValue = null;
				
					decodedValue = decoder.decode(myData);
				
			    
			    	

			      //String clearText = "ljfva09876FK";
			      
			      
			      //String encodedValue  = new Base64().encodeAsString(clearText.getBytes("UTF-16"));
			      
			      
			      	      
			     // byte[] cipherBytes = cipher1.doFinal(clearText.getBytes("UTF-16LE"));
			      //String cipherText = new String(cipherBytes, "UTF8");
			    //  String encoded = Base64.encodeBase64String(cipherBytes);
			      //System.out.println(encoded);
			      
			      
			      	byte[] decValue =    cipher.doFinal(decodedValue);
			    
			        return new String(decValue,UTF_16LE );
	}
	
	


	public void parsec2mFromTemplateFile(String fileName) throws Exception
	{
		
		 	  DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
	        
	        	InputStream is = new FileInputStream(fileName);
	            DocumentBuilder builder = domFactory.newDocumentBuilder();
	            Document dDoc = builder.parse(is);

	         //   XPath xPath = XPathFactory.newInstance().newXPath();
	        //    XPathExpression expr =  xPath.compile("//FixedFields");
	            //NodeList nodes = (NodeList) expr.evaluate(dDoc, XPathConstants.NODESET);
	            NodeList nodes = (NodeList) dDoc.getElementsByTagName("FixedFields");
	        	c2mTemplate c2mT = this;
	        	pdfparse.excelType et = new pdfparse.excelType();
	            for (int i = 0; i < nodes.getLength() ; )
	            {
	            		
	            		            	
	            		Node node = nodes.item(i);
	            		NodeList nodeChildren = node.getChildNodes();
	            		Node fieldName = null;
	            				Node x = null;
	            				Node y = null;
	            				Node w = null;
	            				Node h = null;
	            				Node misc = null;
	            				
	            						
	            					
	            		for (int ii = 0; ii < nodeChildren.getLength() ; )
	    	            {	
	            			Node nodeChild = nodeChildren.item(ii);
	            			if (nodeChild.getNodeName().equals("fieldname" )){
	            				fieldName = nodeChild;
	            			}
	            			else if (nodeChild.getNodeName().equals("x" ))
	            			{
	            				x = nodeChild;
	            			}
	            			else if (nodeChild.getNodeName().equals("y" )){
	            				y = nodeChild;
	            			}
	            			else if (nodeChild.getNodeName().equals("width" ))
	            			{
	            				w = nodeChild;
	            			}
	            			else if (nodeChild.getNodeName().equals("height" ))
	            			{
	            				h = nodeChild;
	            			}
	            			else if (nodeChild.getNodeName().equals("misc" ))
	            			{
	            				misc = nodeChild;
	            			}
	            			
	            		
	            			ii +=1;
	    	            }
	            		
	            	if(x == null)
	            	{
	            		i +=1;
	            		continue;
	            		
	            	}
	            		
	            	/*	System.out.println(fieldName.getNodeName());
	            		
	            		System.out.println(fieldName.getTextContent());
	            		System.out.println(x.getNodeName());
	            		
	            		System.out.println(x.getTextContent());
	            		
	            		System.out.println(y.getNodeName());
	            		
	            		System.out.println(y.getTextContent());
	            		
	            		System.out.println(w.getNodeName());
	            		
	            		System.out.println(w.getTextContent());
	            		
	            		System.out.println(h.getNodeName());
	            	           		System.out.println(h.getTextContent());
	            		System.out.println(misc.getNodeName());
	            		System.out.println(misc.getTextContent());
	            		*/
	            	
	            		
	            		if(fieldName.getTextContent().equals("AddressBlock"))
	            		{
	            			c2mT.x = (int) Double.parseDouble(x.getTextContent());
	            			c2mT.y = (int) Double.parseDouble(y.getTextContent());
	            			c2mT.w = (int) Double.parseDouble(w.getTextContent());
	            			c2mT.h = (int) Double.parseDouble(h.getTextContent());
	            				            			
	            		}
	            		else if(fieldName.getTextContent().equals("FirstPageConstant"))
	            		{
	            			c2mT.sx = (int) Double.parseDouble(x.getTextContent());
	            			c2mT.sy = (int) Double.parseDouble(y.getTextContent());
	            			c2mT.sw = (int) Double.parseDouble(w.getTextContent());
	            			c2mT.sh = (int) Double.parseDouble(h.getTextContent());
	            		}
	            		else 
	            		{
	            			
	            			if(fieldName.getTextContent().equals("FirstPageConstantCompare")){
	            				c2mT.validatedText = misc.getTextContent();
	            			}
	            			else if(fieldName.getTextContent().equals("username")){
	            				c2mT.username = misc.getTextContent();
	            			}
	            			else if(fieldName.getTextContent().equals("password")){
	            			// c2mT.password  = this.decryptPassword(misc.getTextContent());
	            			}
	            			else if(fieldName.getTextContent().equals("filename")){
	            			 c2mT.pdfFileName   = misc.getTextContent();
	            			}
	            			else if(fieldName.getTextContent().equals("appSignature")){
	            			c2mT.appSignature  = misc.getTextContent();
	            			}
	            			else if(fieldName.getTextContent().equals("poDocumentClass")){
	            			
	            			 c2mT.documentClass  = misc.getTextContent();
	            			}
	            			else if(fieldName.getTextContent().equals("poLayout")){
	            				
	            				c2mT.layout  = misc.getTextContent();
	            			}
	            			else if(fieldName.getTextContent().equals("poProductionTime")){
	            				
	            			  c2mT.productionTime  = misc.getTextContent();
	            			}
	            			else if(fieldName.getTextContent().equals("poEnvelope")){
	            			 c2mT.envelope  = misc.getTextContent();
	            			}
	            			else if(fieldName.getTextContent().equals("poColor")){
	            			c2mT.color  = misc.getTextContent();
	            			}
	            			else if(fieldName.getTextContent().equals("poPaperType")){
	            				c2mT.paperType  = misc.getTextContent();
	            			}
	            			else if(fieldName.getTextContent().equals("poPrintOption")){
	            				c2mT.printOption  = misc.getTextContent();
	            			}
	            			else if(fieldName.getTextContent().equals("poMailClass")){
	            				c2mT.mailClass  = misc.getTextContent();
	            			}
	            			else if(fieldName.getTextContent().equals("raName")){
	            				 c2mT.raName  = misc.getTextContent();
	            			}
	            			else if(fieldName.getTextContent().equals("raOrganization")){
	            			 c2mT.raOrganization  = misc.getTextContent();
	            			}
	            			else if(fieldName.getTextContent().equals("raAddress1")){
	            			 c2mT.raAddress1  = misc.getTextContent();
	            			}
	            			else if(fieldName.getTextContent().equals("raAddress2")){
	            			c2mT.raAddress2  = misc.getTextContent();
	            			}
	            			else if(fieldName.getTextContent().equals("raCity")){
	            				c2mT.raCity  = misc.getTextContent();
	            			}
	            			else if(fieldName.getTextContent().equals("raState")){
	            				 c2mT.raState  = misc.getTextContent();
	            			}
	            			else if(fieldName.getTextContent().equals("raPostalCode")){
	            				c2mT.raZip  = misc.getTextContent();
	            			}
	            			else if(fieldName.getTextContent().equals("omitNonStandard")){
	            				c2mT.omitNonStandard  = Boolean.parseBoolean(misc.getTextContent());
	            			}
	            			else if(fieldName.getTextContent().equals("omitNonStandardWarning")){
	            				c2mT.omitNonStandardWarning  = Boolean.parseBoolean(misc.getTextContent());
	            			}
	            			else if(fieldName.getTextContent().equals("omitNonValidated")){
	            				c2mT.omitNonValidated  = Boolean.parseBoolean(misc.getTextContent());
	            			}
	            			else if(fieldName.getTextContent().equals("testMode")){
	            				c2mT.testMode  = Boolean.parseBoolean(misc.getTextContent());
	            			}
	            			else if(fieldName.getTextContent().equals("advancedOptions")){
	            				c2mT.advancedOptions  = Boolean.parseBoolean(misc.getTextContent());
	            			}
	            			else if(fieldName.getTextContent().equals("PageParseOverride")){
	            				 c2mT.pageParseOverride  = Integer.parseInt(misc.getTextContent());
	            			}
	            			else if(fieldName.getTextContent().equals("excelMapping")){
	            				 c2mT.excelMapping  = misc.getTextContent();
	            			}
	            			else if(fieldName.getTextContent().equals("password2")){
            				//System.out.println(misc.getTextContent());
	            				//System.out.println(et.decrypt(misc.getTextContent()))
	            				try
	            				{
	            				 c2mT.password  =  et.decrypt(misc.getTextContent());
	            				}catch(Exception ex)
	            				{
	            					replacePassword = true;
	            				}
		            			 
		            			}
	            			
	            		}
	            			
	            	  	i +=1;
	            };
	        }
	
	
}
