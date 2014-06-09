package jsp.controller;

import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Controller
 */

public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String RESULT_JSP = "/result.jsp";
	private static String SHOWALL_JSP = "/index.jsp";

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String forward = "";

		NetworkScanner.refresh(Calendar.getInstance());
		
		@SuppressWarnings("unchecked")
		Map parameters = request.getParameterMap();
		if (parameters.containsKey("search")) {
			request.setAttribute("result", NetworkScanner.getResult(((String[])(parameters.get("name")))[0]));
			forward = RESULT_JSP;
		} else {
			forward = SHOWALL_JSP;
		}
		
		RequestDispatcher view = request.getRequestDispatcher(forward);
		view.forward(request, response);
	}
}