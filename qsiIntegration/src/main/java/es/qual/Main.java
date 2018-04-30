package es.qual;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class Main {

	public static void main(String[] args) {
		Main p = new Main();
		p.openURL();
		//System.out.println("Hello");

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
	
	public void openURL() {
		
    	String url = "https://www.google.es/search?q=hello";
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
            String remoteFilePath = "/public_html/Exercici 61/test.jar";
            File localfile = new File("C:/testJavadw/temp/test.jar");
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
        
        //boolean b = new File("/../temp/").exists();
		//System.out.println(b);
		//p.openURL();
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
	
	

}
