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
			"<body bgcolor=\"#d9d9d9\">\n" +
			"<ul>\n" +
			"<div align=\"center\" >\n" +
			"<h1> " + "File Upload" + " </h1>\n" + 
			"<form action=\"/PhotoServer/uploads\" method=\"POST\" enctype=\"multipart/form-data\" >\n" +
					
            "Select file to upload: <input type=\"file\" name=\"file\" />" +
            "<br/><br/>" +
			
			"<b> Caption </b>" +
			"<br />\n" +
			"<input type=\"text\" name=\"caption\" placeholder=\"Caption\">\n"   +
			"<br />\n" +
			"<br />\n" +

			"<b> Time </b>" +
			"<br />\n" +
			"<input type=\"text\" name=\"timey\" placeholder=\"Time(yyyyMMdd)\" />\n <input type=\"text\" name=\"timeh\" placeholder=\"Time(HHmmss)\" />\n"  +
			"<br />\n" +
			"Format of time, yyyyMMdd HHmmss" + 
			"<br />\n" +
			"<br />\n" +

			"<b> Location </b>" +
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
							
			String cap = request.getParameter("caption");		
			String TimeY = request.getParameter("timey");	
			String TimeH = request.getParameter("timeh");	
			String lat = request.getParameter("lat");		
			String lng = request.getParameter("lon");		
			String filename = "JPEG_" + TimeY + "_" + TimeH + "_" + cap + "_" + lat + "_" + lng + "_" + "00975720";
			String filenamePath = "D:\\Capstone\\Server\\tomcat\\webapps\\PhotoServer\\Images\\" + "JPEG_" + TimeY + "_" + TimeH + "_" + cap + "_" + lat + "_" + lng + "_" + "00975720" + ".jpg";

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

		String OldFilePath =  "D:\\Capstone\\Server\\tomcat\\webapps\\PhotoServer\\Images\\" + fileName;
		
		File oldFile = new File(OldFilePath);
		File f = oldFile;
		File dest = new File(filenamePath);
		
		f.renameTo(dest);
		
		
		
		   

		
		
		response.setContentType("text/html");
		
		
		PrintWriter out = response.getWriter();
		out.println(
			"<html>\n" +			
			"<body bgcolor=\"#d9d9d9\">\n" +
			"<ul>\n" +
			"<div align=\"center\" >\n" +
			"<h1> " + "File Upload" + " </h1>\n" + 
			"<form action=\"/PhotoServer/uploads\" method=\"POST\">\n" +
					
			"Your Image with the Caption: " + cap + " has been added to our database under the filename: " + filename + "   " + fileName +
			"<br />\n" +
			OldFilePath + "   " + filenamePath +
			
			
			"<br />\n" +
			"<br />\n" +
			
            "<input type=\"button\" value=\"Return to Gallery\" onclick=\"location.href='http://localhost:9091/PhotoServer/hits';\" />\n" +
			"</div>\n"+
			"</form>\n</body>\n</html>");
		



    }
}
