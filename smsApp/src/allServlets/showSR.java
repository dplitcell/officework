package allServlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import allClasses.SRStatus;
import allClasses.Utility;

@WebServlet("/showSR")
public class showSR extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public showSR() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sessionId = (String) request.getSession().getAttribute("unique");
		if(sessionId==null)	{
			response.sendRedirect("Login");
		} 
		else {
			String smsType = request.getParameter("TypeId");
			String smsSR = request.getParameter("ReqId");
			//new Utility();
			String retVal = Utility.showSRQuery(sessionId,smsType,smsSR);
			String smsHeading = retVal.substring(0, retVal.indexOf("#"));
			String query = retVal.substring(retVal.indexOf("#")+1);
			request.getSession().setAttribute("heading", smsHeading);
			List<SRStatus> srList=Utility.getSRStatusList(query);
			request.getSession().setAttribute("srList",srList);
			request.getRequestDispatcher("/WEB-INF/jsp/showSR.jsp").forward(request, response);
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String empCode = request.getParameter("empNo");
		String query = "select emp_name||'@'||desig||'#'||group_desc||'$'||sub_group_desc||'^'||mobile_no||':'||email_id from app_vw_emp where emp_id = " + empCode + " union select ' @ # $ ^ : ' from dual where " + empCode + " not in (select emp_id from app_vw_emp)";
		System.out.println("inside: the empInfo: query--  " + query);
		//new Utility();
		response.setContentType("text/plain");
		response.getWriter().write(Utility.getStringVal(query));
	}
}
