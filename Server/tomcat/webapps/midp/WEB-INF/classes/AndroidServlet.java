import javax.servlet.http.*;
import javax.servlet.*;
import javax.servlet.ServletException;

import java.io.*;
import java.sql.*;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.io.File;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


public class AndroidServlet extends HttpServlet {
	
	private final String DIR = "Images";
	private final int File_Size_Max = 1024*1024*40; //40MB
	private final int Request_Size_Max = 1024*1024*50; //50MB
	
	protected void doGet(HttpServletRequest request,
		HttpServletResponse response) throws ServletException, IOException {
			
			PrintWriter out = response.getWriter();
			out.println(
			"<html>\n" +			
			"<body bgcolor=\"#FFFF00\">\n" +
			"<ul>\n" +
			"<div align=\"left\" >\n" +
			"<h1> " + "File Upload From Android" + " </h1>\n" + 
			"<form action=\"/PhotoServer/sends\" method=\"POST\">\n" +

			"Press below button to accept the Upload" +
			"<br />\n" +
			"<input type=\"submit\" value=\"Upload From Android\" />\n" +

			"</div>\n"+
			"</form>\n</body>\n</html>");
			
			
		}
	
    //handles file upload via HTTP POST method
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
							
						
		//If incoming file is not multipart stops here
		if(!ServletFileUpload.isMultipartContent(request)){
			return;
		}
		
		// configures upload settings
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // sets temporary location to store files
        factory.setRepository(new File(System.getProperty(DIR)));
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		//Set Max size of upload file and request
		upload.setFileSizeMax(File_Size_Max);
		upload.setSizeMax(Request_Size_Max);
		
		String uploadPath = getServletContext().getRealPath("")
			+ File.separator + DIR;
		
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdir();
		}
		
		try {
			//Parses the request to get all data
			List<FileItem> multiparts = upload.parseRequest(request);
		   
			for(FileItem item : multiparts){
				if(!item.isFormField()){
					String filename = new File(item.getName()).getName();
					String filePath = DIR + File.separator + filename;
					File storeFile = new File(filePath);
					
					item.write(storeFile);
					//File uploaded successfully
					request.setAttribute("message", "File Uploaded Successfully");
				}
			}
		
		} catch (Exception ex) {
		   request.setAttribute("message", "File Upload Failed due to " + ex);
		}
		
			PrintWriter out = response.getWriter();
			out.println(
			"<html>\n" +			
			"<body bgcolor=\"#d9d9d9\">\n" +
			"<ul>\n" +
			"<div align=\"center\" >\n" +
			"<h1> " + "File Upload From Android" + " </h1>\n" + 
			"<form action=\"/PhotoServer/sends\" method=\"POST\">\n" +

			"Hopefully the image is uploaded" +
			"<br />\n" +
			"<input type=\"submit\" value=\"Upload From Android\" />\n" +

			"</div>\n"+
			"</form>\n</body>\n</html>");	
      
	}
}
