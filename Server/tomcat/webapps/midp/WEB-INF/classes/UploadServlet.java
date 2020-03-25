import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.nio.file.*;
 
import java.io.File;
import java.io.IOException;
 
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
  
@MultipartConfig
public class UploadServlet extends HttpServlet {
 
	public void doGet(HttpServletRequest request,
			HttpServletResponse response)
				throws ServletException, IOException {
					
		response.setContentType("text/html");

		PrintWriter out = response.getWriter();
		out.println(
			"<html>\n" +			
			"<body bgcolor=\"#FFFF00\">\n" +
			"<ul>\n" +
			"<div align=\"left\" >\n" +
			"<h1> " + "Upload From Desktop" + " </h1>\n" + 
			"<form action=\"/PhotoServer/uploads\" method=\"POST\" enctype=\"multipart/form-data\" >\n" +
					
            "Select what to upload: <input type=\"file\" name=\"file\" />" +
            "<br/><br/>" +
			
			"<b> Caption </b>" +
			"<br />\n" +
			"<input type=\"text\" name=\"caption\" placeholder=\"Caption\">\n"   +
			"<br />\n" +
			"<br />\n" +
			"<br />\n" +
			"<br />\n" +	
			"<b> Dates</b>" +
			"<br />\n" +
			"<input type=\"text\" name=\"timey\" placeholder=\"Date(yyyyMMdd)\" />\n <input type=\"text\" name=\"timeh\" placeholder=\"Date(HHmmss)\" />\n"  +
			"<br />\n" +
			"Use Format, yyyyMMdd HHmmss" + 
			"<br />\n" +
			"<br />\n" +
			"<br />\n" +
			"<br />\n" +	
			"<b> Area</b>" +
			"<br />\n" +
			"<input type=\"text\" name=\"lat\" placeholder=\"Latitude\" /> \n"  +
			"<br />\n" +
			"<input type=\"text\" name=\"lon\" placeholder=\"Longitude\" /> \n" +
			"<br />\n" +
			"<br />\n" +				
			
            "<input type=\"submit\" value=\"Upload\" /> <input type=\"button\" value=\"Back\" onclick=\"location.href='http://localhost:9091/PhotoServer/hits';\" />\n" +
			"</div>\n"+
			"</form>\n</body>\n</html>");
					
					
	}
  
 
 
    /**
     * handles file upload via HTTP POST method
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
							
			String caption = request.getParameter("caption");		
			String Time1 = request.getParameter("timey");	
			String Time2 = request.getParameter("timeh");	
			String latitude = request.getParameter("lat");		
			String longitutde= request.getParameter("lon");		
			String filename = "JPEG_" + Time1 + "_" + Time2 + "_" + caption + "_" + latitude + "_" + longitutde+ "_" + "0075720"; 
			String filenamePath = "Images\\" + "JPEG_" + Time1 + "_" + Time2 + "_" + caption + "_" + latitude + "_" + longitutde+ "_" + "0075720" + ".jpg";

		// SAVE TO DATABASE:
		Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
		String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
		InputStream fileContent = filePart.getInputStream();
		
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		String filePath = s + "\\..\\webapps\\PhotoServer\\Images\\" + fileName;
		
		FileOutputStream outfile = new FileOutputStream(filePath);
		byte[] buffer = new byte[1024];
		int read;
		while ((read = fileContent.read(buffer)) != -1) {
			outfile.write(buffer, 0, read);
		}
		outfile.close();
		
		// Rename File:

		String OldFilePath =  "Images\\" + fileName;
		
		File oldFile = new File(OldFilePath);
		File f = oldFile;
		File dest = new File(filenamePath);
		
		f.renameTo(dest);
		
		
		
		import java.io.*;   
public class simple {   
    public static void main(String[] args) {   
        //Obtain the reference of the existing file   
        File oldFile = new File("D:\\test\\Results.dat");    
        System.out.println(oldFile.getName());   
        //Now invoke the renameTo() method on the reference, oldFile in this case   
        // since oldFile is immutable, no need to create a new File
        File f = oldFile;
        File newFile = new File("D:\\test\\Results_May.dat");
        if (f.renameTo(newFile))
        {
            // rename succeeded
            f = newFile;
        }
        System.out.println(f.getName());   
    }   
}
		
		
		
		
		// Display HTML PAGE
				response.setContentType("text/html");

		PrintWriter out = response.getWriter();
		out.println(
			"<html>\n" +			
			"<body bgcolor=\"#FFFF00\">\n" +
			"<ul>\n" +
			"<div align=\"left\" >\n" +
			"<h1> " + "Photo Saved" + " </h1>\n" + 
			"<form action=\"/PhotoServer/uploads\" method=\"POST\">\n" +
					
			"Your Photo: " + caption + " has been added Server Successfully"
			
			
			"<br />\n" +
			"<br />\n" +
			"<br />\n" +
			"<br />\n" +
			"<br />\n" +
			"<br />\n" +
			
            "<input type=\"button\" value=\"Return\" onclick=\"location.href='http://localhost:9091/PhotoServer/hits';\" />\n" +
			"</div>\n"+
			"</form>\n</body>\n</html>");
		



    }
}
