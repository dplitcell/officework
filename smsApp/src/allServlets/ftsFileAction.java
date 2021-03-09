package allServlets;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import allClasses.Utility;
import allClasses.ftsFile;

@WebServlet("/ftsFileAction")
public class ftsFileAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ftsFileAction() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sessionId = (String) request.getSession().getAttribute("unique");
		if(sessionId==null)	{
			response.sendRedirect("Login");
		} 
		else {
			String ftsFileNo = request.getParameter("fileno");
			String ftsAction = request.getParameter("action");
			String ftsType = request.getParameter("retun");
			String ftsReturn = "ftsFileList?TypeId="+ftsType; 
			Utility.ftsAction(sessionId, ftsAction, ftsFileNo);
			System.out.println("updated.. and now returning to " + ftsReturn);
			List<ftsFile> fileList=null;
			String sqlQuery="SELECT file_no, DECODE(cat_type, 'G', UPPER(subject), 'CONFIDENTIAL FILE') AS subject, custfield, link_button FROM v_fts_file_list WHERE session_id = '"+sessionId+"' AND file_type = '" + ftsType + "' ORDER BY trn_id DESC";
			fileList = Utility.GenerateFileRecord(sqlQuery);	
			request.getSession().setAttribute("fileList", fileList);
			response.sendRedirect(ftsReturn);
		}	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sessionId = (String) request.getSession().getAttribute("unique");
		if(sessionId==null)	{
			response.sendRedirect("Login");
		} 
		else {
			doGet(request,response);
		}
	}

}
