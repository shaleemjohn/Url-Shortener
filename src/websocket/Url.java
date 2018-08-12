package websocket;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.DbConnect;



/**
 * Servlet implementation class Url
 */
@WebServlet("/")
public class Url extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Url() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("--------------------------*******************------------------------");
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		StringBuffer requestUrl = request.getRequestURL();
		String URL = requestUrl.toString();
		
		String Actual;
		   
		DbConnect DBobj = new DbConnect();
		Actual = DBobj.getActualURL(URL);
		
		if(Actual.equals("")) {
			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			out.println("<HTML><BODY>");
			out.println("<H1><font face='Aller' color='blue'> The URL is Invalid </font><H1>");
			out.println("</BODY></HTML>");
		} else
			try {
				if (DBobj.linkExpired(URL)) {
					PrintWriter out = response.getWriter();
					response.setContentType("text/html");
					out.println("<HTML><BODY>");
					out.println("<H1><font face='Aller' color='blue'> The Short URL is Expired </font><H1>");
					out.println("</BODY></HTML>");
				}
				else {
					response.sendRedirect(Actual);
				}
			} catch (ParseException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		/*
		if (URL.equals(new String("http://localhost:8080/UrlShortener/abc"))) {
			System.out.println(URL);
			response.sendRedirect("http://www.google.com");
		}
		else {
			System.out.println("URL is not Valid");
		}
		*/
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
