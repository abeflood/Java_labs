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
import org.apache.commons.fileupload.DiskFileUpload;
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
			"<body bgcolor=\"#d9d9d9\">\n" +
			"<ul>\n" +
			"<div align=\"center\" >\n" +
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
							
						
		String galleryPath = "/Images";

//Process upload.
ServletContext context = getServletContext();
String absGalleryPath = context.getRealPath(galleryPath);
String absThumbnailsPath = context.getRealPath(galleryPath + "/Thumbnails");
String absTempPath = context.getRealPath(galleryPath + "/Temp");

DiskFileUpload fu = new DiskFileUpload();
//Set maximum size before a FileUploadException will be thrown.
fu.setSizeMax(100000000);
//Set maximum size that will be stored in memory.
fu.setSizeThreshold(4096);
//Set the location for saving data that is larger than getSizeThreshold().

fu.setRepositoryPath(absTempPath);




			
			
		
try{
//Get uploaded files.

PrintWriter lout = response.getWriter();
lout.println(
			"<html>\n" +			
			"<body bgcolor=\"#d9d9d9\">\n" +
			"<ul>\n" +
			"<div align=\"center\" >\n" +
			"<h1> " + "File Upload From Android" + " </h1>\n" + 
			"<form action=\"/PhotoServer/sends\" method=\"POST\">\n" +

			"Press below button to accept the Upload" +
			"<br />\n" +
			"<input type=\"submit\" value=\"Upload From Android\" />\n" +

			"</div>\n"+
			"</form>\n</body>\n</html>");

List listFileItems = fu.parseRequest(request);



//Put them in hash table for fast access.
Hashtable fileItems = new Hashtable();

for (int i = 0; i < listFileItems.size(); i++) {
    FileItem fileItem = (FileItem)(listFileItems.get(i));
    fileItems.put(fileItem.getFieldName(), fileItem);
}

//Get total number of uploaded files (all files are uploaded in a single package).
int fileCount = Integer.parseInt(((FileItem) fileItems.get("FileCount")).getString());

//Iterate through uploaded data and save the original file, thumbnail, and description.
for (int i = 1; i <= fileCount; i++) {
    //Get source file and save it to disk.
    FileItem sourceFileItem = (FileItem) fileItems.get("SourceFile_" + Integer.toString(i));
    String fileName = new File(sourceFileItem.getName()).getName();
    File sourceFile = new File(absGalleryPath + File.separator + fileName);
    sourceFileItem.write(sourceFile);
    



    //Get first thumbnail (the single thumbnail in this code sample) and save it to disk.
    FileItem thumbnail1FileItem = (FileItem) fileItems.get("Thumbnail1_" + Integer.toString(i));
    File thumbnail1File = new File(absThumbnailsPath + File.separator + fileName + ".jpg");
    thumbnail1FileItem.write(thumbnail1File);
}
}
catch(Exception e){}

		
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
