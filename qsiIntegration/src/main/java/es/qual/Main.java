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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Main {

	/**
	 * Initialize the program and execute preExecution method.
	 * 
	 * @param Initial Params are not used in this program.
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		Main p = new Main();
		p.preExecution();
	}
	
	/**
	 * Does the complete sequence of update process. Once the running jar file is the latest, execute the program usually.
	 * 
	 * @throws IOException
	 */
	public void preExecution() throws IOException {
		if(this.hasCorParentFolder()) {
			this.replaceJar2();
		}
		else {
			if(this.testLatest()) {
				this.openURL("isTheLatest1.8");
			}
			else {
				this.replaceJar1();
			}
		}
		
	}
	
	public void deleteTempFolder() {
		
	}
	
	/**
	 * Create the temp folder in running .jar file folder, downloads the new .jar file in temp folder and executes downloaded .jar file.
	 * 
	 * @throws IOException
	 */
	
	public void replaceJar1() throws IOException {
		File dir = new File("temp");
		dir.mkdir();
		this.downftp();
		
	}
	
	/**
	 * Deletes old .jar file, copy running .jar file in old .jar file path, executes it and stop the execution.
	 * 
	 * @throws IOException
	 */
	public void replaceJar2() throws IOException {
		//Absolute Path
		File fPath = new File (Main.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		System.out.println(fPath.getAbsolutePath());
		//Jarfile Name
		String jarFilename = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getName();
		System.out.println(jarFilename);
		//Folder Name
		String folderName = "temp";
		//Parent Path
		String parentPath = fPath.getAbsolutePath().substring(0, fPath.getAbsolutePath().length()-jarFilename.length()-5)+"qsiIntegration.jar";
		File oldFile = new File (parentPath);
		//Old File
		//File oldFile = new File(fPath.getAbsolutePath().substring(fPath.getAbsolutePath().length()-jarFilename.length())+"qsiIntegration.jar");
		System.out.println(parentPath);
		//Deleting Old Destiny File
		oldFile.delete();
		FileUtils.copyFile(fPath,oldFile);
		Runtime rt = Runtime.getRuntime();
		String queryExec = "java -jar "+parentPath;
		rt.exec(queryExec);
		System.exit(0);
	}
	
	/**
	 * Gets the parent folder name and compares it with "temp" 
	 * 
	 * @return The response of verification.
	 */
	public boolean hasCorParentFolder() {
		String fullPath = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String jarFilename = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getName();
		String parentFolder = fullPath.substring(fullPath.length()-jarFilename.length()-5, fullPath.length()-jarFilename.length()-1);
		if(parentFolder.equals("temp")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Open a new window in the browser with a Google search of the String income param.
	 * 
	 * @param Google search word.
	 */
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
	
	/**
	 * Downloads latest .jar file in web server via FTP, executes it and stop the current execution.
	 * 
	 * @throws IOException
	 */
	public void downftp() throws IOException {

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
        }
        finally {
        	Runtime rt = Runtime.getRuntime();		
    		String fileTempDir = "java -jar "+System.getProperty("user.dir")+"\\temp\\qsiIntegration_temp.jar";
    		//FileUtils.writeStringToFile(new File("Output.txt"), fileTempDir);
    		rt.exec(fileTempDir);
    		System.exit(0);
        }
        

	}
	
	/**
	 * Verify that the current .jar file is the latest version.
	 * 
	 * @return The response of verification.
	 */
	private Boolean testLatest() {
		String url = "http://alejandro-lm.esy.es/latest.xml";
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        try {
			Document doc = factory.newDocumentBuilder().parse(new URL(url).openStream());
			NodeList nodes = doc.getElementsByTagName("pkver");
			Node n = nodes.item(0);
			String ver = n.getTextContent();
			if(ver.equals("1.7")) {
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
