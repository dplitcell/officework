package allServlets;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import allClasses.Utility;

@WebServlet("/ftsFileSend")
public class ftsFileSend extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ftsFileSend() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sessionId = (String) request.getSession().getAttribute("unique");
		if(sessionId==null)	{
			response.sendRedirect("Login");
		} 
		else {
			String ftsFileNo = request.getParameter("fileno");
			String ftsFileName = request.getParameter("fileName");
			String ftsType = request.getParameter("retun");
			String ftsReturn = "ftsFileList?TypeId="+ftsType; 
			String sqlQuery = "select emp_name || ' [' || emp_id || '] ' || desig || ', ' || group_desc || ', ' || sub_group_desc || ', ' || dept_name from app_vw_emp where grade<9 order by emp_name";
			List<String> ftsOfficers= Utility.getStringList(sqlQuery);
			JSONArray offJson = new JSONArray(ftsOfficers);
			request.getSession().setAttribute("ftsOfficers", offJson.toString());
			
			sqlQuery = "SELECT concat(substr(to_char(1000+activity_code),2), concat('-',activity_desc)) as activity, activity_desc FROM p_fts_activity ORDER BY activity_desc";
			List<String> ftsActivityList= Utility.getStringList(sqlQuery);
			request.getSession().setAttribute("ftsActivityList", ftsActivityList);
			request.getSession().setAttribute("ftsFileNo", ftsFileNo);
			request.getSession().setAttribute("ftsFileName", ftsFileName);
			request.getSession().setAttribute("ftsReturn", ftsReturn);
			request.getRequestDispatcher("/WEB-INF/jsp/ftsFileSend.jsp").forward(request, response);
		}	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sessionId = (String) request.getSession().getAttribute("unique");
		if(sessionId==null)	{
			response.sendRedirect("Login");
		} 
		else {
			String ftsReturn = String.valueOf(request.getSession().getAttribute("ftsReturn"));
			String ftsFileNo = String.valueOf(request.getSession().getAttribute("ftsFileNo"));
			String ftsRefEmp =request.getParameter("ftsEmp").trim();
			String ftsActivity =request.getParameter("ftsActivity").trim();
			Utility.ftsSendFile(sessionId, ftsFileNo, ftsRefEmp, ftsActivity);
			System.out.println("records updated");
			response.sendRedirect(ftsReturn);
		}	
	}

}
