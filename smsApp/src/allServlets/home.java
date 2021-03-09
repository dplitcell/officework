package allServlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import allClasses.Utility;

@WebServlet("/home")
public class home extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public home() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		new Utility();
		String sessionId = String.valueOf(request.getSession().getAttribute("unique"));
		if(sessionId==null)	{
			response.sendRedirect("Login");
		} else {
			Utility.doLogout(sessionId);
			request.getSession().invalidate();
		}
		response.sendRedirect("Login");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String sessionId = (String) request.getSession().getAttribute("unique");
		if(sessionId==null)	{
			response.sendRedirect("Login");
		} else {
			String moduleId = (String) request.getSession().getAttribute("module");
			String menuId=request.getParameter("menuSel").trim().toUpperCase();
			String query="SELECT menu_url FROM v_sms_menu WHERE session_id = '"+sessionId+"' AND module_id = '" + moduleId + "' AND '" + menuId + "' IN (TO_CHAR(menu_id), menu_shrt)";
			new Utility();
			String menuUrl = Utility.getStringVal(query);
			response.sendRedirect(menuUrl);
		}
	}
}
