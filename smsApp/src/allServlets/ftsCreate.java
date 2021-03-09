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

@WebServlet("/ftsCreate")
public class ftsCreate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ftsCreate() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sessionId = (String) request.getSession().getAttribute("unique");
		if(sessionId==null)	{
			response.sendRedirect("Login");
		} 
		else {
			String sqlQuery="select concat(substr(to_char(10000+a.fn_code),2), concat('-',a.fn)) as fn from P_FTS_FN a order by trim(a.FN)";
			List<String> ftsFunctionList= Utility.getStringList(sqlQuery);
			sqlQuery = "select emp_name || ' [' || emp_id || '] ' || desig from app_vw_emp where grade<9 order by emp_name";
			request.getSession().setAttribute("ftsOfficers", (new JSONArray(Utility.getStringList(sqlQuery))).toString());
	    	request.getSession().setAttribute("ftsFunctionList", ftsFunctionList);
			request.getRequestDispatcher("/WEB-INF/jsp/ftsCreate.jsp").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sessionId = (String) request.getSession().getAttribute("unique");
		if(sessionId==null)	{
			response.sendRedirect("Login");
		}
		else {
			String ftsFileName=request.getParameter("ftsFileName");
			String ftsFileCat=request.getParameter("ftsFileCat");
			String ftsFunction=request.getParameter("ftsFunction");
			String ftsFileNo = request.getParameter("ftsFileNo");
			String ftsInitEmp =request.getParameter("ftsEmp").trim();
			String userId = (String) request.getSession().getAttribute("userid");
			Utility.ftsCreate(sessionId, ftsFileName, ftsFileCat, ftsFunction,ftsFileNo,ftsInitEmp,userId);
			System.out.println("File Create procedure completed");
			response.sendRedirect("ftsFileList?TypeId=H");
		}
	}
}
