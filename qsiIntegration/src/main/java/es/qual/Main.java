package es.qual;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Main {

	public static void main(String[] args) throws IOException {
		Main p = new Main();
		p.preExecution();
		//p.openURL();

	}

	public void preExecution() throws IOException {
		//Testing Temp Parent Folder
		if(this.hasCorParentFolder()) {
			this.replaceJar2();
		}
		else {
			if(this.testLatest()) {
				this.openURL("isTheLatest");
			}
			else {
				this.replaceJar1();
			}
		}
		
	}
	
	public void replaceJar1() throws IOException {
		//System.out.println(b);
		//p.openURL();
		File dir = new File("temp");
		dir.mkdir();
		this.downftp();
		Runtime rt = Runtime.getRuntime();
		String worDir = System.getProperty("user.dir");
		rt.exec("java -jar "+worDir+"/temp/qsiIntegration_temp.jar");
		/*if(new File("/../temp/").exists()){
			try {
				Runtime rt = Runtime.getRuntime();
	            rt.exec("del C:\testJavadw\test.jar");
	            rt.exec("copy C:\testJavadw\temp\test.jar C:\testJavadw\test.jar");
	            rt.exec("java -jar C:/testJavadw/test.jar");
	            System.exit(0);
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
		}
		else{
			
		}*/
		
	}
	
	public void replaceJar2() throws IOException {
		String worDir = System.getProperty("user.dir");
		File worDirFile = new File (worDir);
		File fOrigin = new File(worDir+"qsiIntegration_temp.jar");
		File fDest = new File(worDirFile.getParent()+"qsiIntegration.jar");
		//Deleting Old Destiny File
		fDest.delete();
		//FileUtils.copyFile(fOrigin,fDest);
	}
	
	public boolean hasCorParentFolder() {
		String parentfoldname = System.getProperty("user.dir").substring(System.getProperty("user.dir").length()-4);
		if(parentfoldname.equals("temp")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void openURL(String test) {
		
    	String url = "https://www.google.es/search?q="+test;
		if(Desktop.isDesktopSupported()){
             Desktop desktop = Desktop.getDesktop();
             try {
                 desktop.browse(new URI(url));
             } catch (IOException | URISyntaxException e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
             }
         }else{
             Runtime runtime = Runtime.getRuntime();
             try {
                 runtime.exec("xdg-open " + url);
             } catch (IOException e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
             }
         }
    }
	
	public void downftp() {

		String serverAddress = "alejandro-lm.esy.es"; // ftp server address 
        int port = 21; // ftp uses default port Number 21
        String username = "u206666135";// username of ftp server
        String password = "qualuser"; // password of ftp server
        FTPClient ftpClient = new FTPClient();
        try {
        	  
            ftpClient.connect(serverAddress, port);
            ftpClient.login(username,password);
 
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            String remoteFilePath = "/public_html/contInte/qsiIntegration.jar";
            String worDir = System.getProperty("user.dir");
            File localfile = new File(worDir+"/temp/qsiIntegration_temp.jar");
            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(localfile));
            boolean success = ftpClient.retrieveFile(remoteFilePath, outputStream);
            outputStream.close();
  
            if (success) {
                System.out.println("Ftp file successfully download.");
            }
  
        } catch (IOException ex) {
            System.out.println("Error occurs in downloading files from ftp Server : " + ex.getMessage());
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                    Runtime rt = Runtime.getRuntime();
                    rt.exec("java -jar C:/testJavadw/temp/test.jar");
                    System.exit(1);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
        
        
        
  

	}
	
	private Boolean testLatest() {
		String url = "http://alejandro-lm.esy.es/latest.xml";
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        try {
			Document doc = factory.newDocumentBuilder().parse(new URL(url).openStream());
			NodeList nodes = doc.getElementsByTagName("pkver");
			Node n = nodes.item(0);
			String ver = n.getTextContent();
			if(ver.equals("1.1")) {
				return true;
			}
			else {
				return false;
			}
        } catch (SAXException | IOException | ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    	
    }
	
	

}
