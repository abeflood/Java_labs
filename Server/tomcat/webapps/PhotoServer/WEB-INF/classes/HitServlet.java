import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.sql.*;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import static java.lang.Math.asin;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
//javac -classpath .;c:\tomcat\lib\servlet-api.jar;c:\tomcat\lib\commons-fileupload-1.4.jar AndroidServlet.java
//javac -classpath .;c:\tomcat\lib\servlet-api.jar HitServlet.java
//javac -classpath .;c:\tomcat\lib\servlet-api.jar UploadServlet.java

public class HitServlet extends HttpServlet {
  
	int imageCount = 0;
	public static ArrayList<String> Gallery = null;
  
// Method to handle initial GET method request.
  public void doGet(HttpServletRequest request,
      HttpServletResponse response)
      throws ServletException, IOException {
		imageCount = 0;
    
		String title = "Photo Gallery";
	
	// Set response content type
      response.setContentType("text/html");

      PrintWriter out = response.getWriter();
      out.println(
			"<html>\n" +			
			"<body bgcolor=\"#FFFF00\">\n" +
			"<ul>\n" +
			"<div align=\"left\" >\n" +
			"<form action=\"/PhotoServer/hits\" method=\"POST\">\n" +
			"<h1> " + title + " </h1>\n" + 
			"<p> To Search Everything - Leave all fields blank </p>" +
			"<br />\n" +
			"<br />\n" +	
			"<br />\n" +
			"<br />\n" +

			"<b> Keyword</b>" +
			"<br />\n" +
			"<input type=\"text\" name=\"caption\" placeholder=\"Keyword\">\n"   +
			"<br />\n" +
			"<br />\n" +

			"<b> Time </b>" +
			"<br />\n" +
			"<input type=\"text\" name=\"start_time\" placeholder=\"Start Time\" /> <input type=\"text\" name=\"end_time\" placeholder=\"End Time\" />\n"   +
			"<br />\n" +
			"Format of time, yyyyMMddHHmmss" + 
			"<br />\n" +
			"<br />\n" +
			"<br />\n" +
			"<br />\n" +

			"<b> Area (Circle) </b>" +
			"<br />\n" +
			"<input type=\"text\" name=\"center_lat\" placeholder=\"Center Latitude\" /> \n"  +
			"<br />\n" +
			"<input type=\"text\" name=\"center_lon\" placeholder=\"Center Longitude\" /> \n" +
			"<br />\n" +
			"<input type=\"text\" name=\"Radius\" placeholder=\"Radius\" />\n" +
			"<br />\n" +
			"<br />\n" +				
			
			"<input type=\"submit\" value=\"Search It Up\" />\n" +
			
			"<br />\n" +
			"<br />\n" +	
			"<input type=\"button\" value=\"Upload From Desktop\" onclick=\"location.href='http://localhost:9091/PhotoServer/uploads';\" />\n" +
			"</div>\n</form>\n" +
			"</form>\n</body>\n</html>");



  }
  
// Method to handle POST method request.
  public void doPost(HttpServletRequest request,
                     HttpServletResponse response)
      throws ServletException, IOException {

	/*	
	request.getParameter("caption")
	request.getParameter("start_time")
	request.getParameter("end_time")
	request.getParameter("center_lat")
	request.getParameter("center_lon")
	request.getParameter("Radius")
	*/

	int Count;
	File file = new File("D:/Capstone/Server/tomcat/webapps/PhotoServer/Images");
	String[] imList = file.list();
		
	if (imList != null && request.getParameter("start_time") != null) {
		
		Gallery = new ArrayList<String>();
		boolean  minDateStatus;
		boolean  maxDateStatus;
		boolean  captionStatus;
		boolean  LocationStatus;
		int SearchStatus = imList.length;
		String FileCap = "";
		String Combo;		
		long Date = 0;
		float Lat = 0;
		float Lon = 0;

		for (Count = 0; Count <= imList.length - 1; Count++) {

			String[] separated = imList[Count].split("_");

			Combo = separated[1] + separated[2];
			Date = Long.parseLong(Combo);
			if (separated.length == 7) {
				Lat = Float.parseFloat(separated[4]);
				Lon = Float.parseFloat(separated[5]);
			}else{
				Lat = Float.parseFloat(separated[3]);
				Lon = Float.parseFloat(separated[4]);
			}

			if(separated.length == 7){
				FileCap = separated[3];
			}else{
				FileCap = "";
			}
			//START SEARCh

			if (request.getParameter("start_time").equals("")) {
				minDateStatus = true;
			} else if (Long.parseLong(request.getParameter("start_time")) < Date) {
				minDateStatus = true;
			} else {
				minDateStatus = false;
			}

			if (request.getParameter("end_time").equals("")) {
				maxDateStatus = true;
			} else if (Long.parseLong(request.getParameter("end_time")) > Date) {
				maxDateStatus = true;
			} else {
				maxDateStatus = false;
			}

			if (request.getParameter("caption").equals("")) {
				captionStatus = true;
			} else if (FileCap.toLowerCase().contains(request.getParameter("caption").toLowerCase())) {
				captionStatus = true;
			} else {
				captionStatus = false;
			}

			if(request.getParameter("center_lat").equals("")||request.getParameter("center_lon").equals("")||request.getParameter("Radius").equals("")){
				LocationStatus = true;
			}else if(DistanceCoords(Lat,Lon,Float.parseFloat(request.getParameter("center_lat")),Float.parseFloat(request.getParameter("center_lon"))) < Double.parseDouble(request.getParameter("Radius"))){
				LocationStatus = true;
			}else{
				LocationStatus = false;
			}

			if ((minDateStatus == true) && (maxDateStatus == true) && (captionStatus == true) && LocationStatus == true) {
				Gallery.add(imList[Count]);
			}
		}
	}

	PrintWriter out = response.getWriter();
	response.setContentType("text/html");
	String title = "Searched Gallery";			
	String action = request.getParameter("action");

	
	if ("Left".equals(action) && imageCount > 0) {
		imageCount = imageCount - 1;
	} else if ("Right".equals(action) && imageCount < (Gallery.size() - 1)) {
		imageCount = imageCount + 1;
	}	
			
		try{

			String docType =
			"<!doctype html public \"-//w3c//dtd html 4.0 " +
			"transitional//en\">\n";
			out.println(
				"<html>\n" +			
				"<body bgcolor=\"#FFFF00\">\n" +
				"<ul>\n" +
				"<div align=\"left\" >\n" +
				"<form action=\"/PhotoServer/hits\" method=\"POST\">\n" +
				"<h1> " + title + " </h1>\n" + 
				"<h2 align=\"center\">" + (imageCount + 1)  + "/" + (Gallery.size()) + " Photos" + "</h2>\n" +
				"<br />\n" +		
				"<br />\n" +
				"<input type=\"submit\" name=\"action\" value=\"Left\" />\n" +   //DownCount
				"<input type=\"button\" value=\"Search Again\" onclick=\"location.href='http://localhost:9091/PhotoServer/hits';\" />\n" +
				"<input type=\"submit\" name=\"action\" value=\"Right\" />\n" +   //UpCount
				"<br />\n" +
				"<br />\n" +
				"<img id=\"myImg\" src=\"Images/" + Gallery.get(imageCount) + "\"" + " width=\"500\" height=\"500\">\n\n" +
				"<br />\n" +
				"<b> " + Gallery.get(imageCount) + " </b>\n" + 
				"</div>\n</form>\n" +
				"</form>\n</body>\n</html>");		

		}
		catch(Exception e){
			response.sendRedirect("http://localhost:9091/PhotoServer/hits");
		}
											
  }
  
  public double DistanceCoords(float Lat1, float Long1, float Lat2, float Long2) {
	float EarthRad = 6371;
	double dLat = (3.1415/180)*(Lat2 - Lat1);
	double dLong = (3.1419/180)*(Long2 - Long1);

	double a = sin(dLat/2) * sin(dLat/2) + cos((3.1415/180)*Lat1) * cos((3.1415/180)*Lat2) * sin(dLong/2) * sin(dLong/2);
	double c = 2*asin(sqrt(a));
	double d = EarthRad * c;

    return d;
  }

}

