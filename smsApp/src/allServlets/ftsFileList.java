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

@WebServlet("/ftsFileList")
public class ftsFileList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ftsFileList() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sessionId = (String) request.getSession().getAttribute("unique");
		if(sessionId==null)	{
			response.sendRedirect("Login");
		} 
		else {
			String ftsFileNo = "";
			String ftsFileName = ""; 
			String ftsType = request.getParameter("TypeId");
			List<ftsFile> fileList=null;
			String sqlQuery=null;
			if (ftsType.matches("T")){
				ftsFileNo = request.getParameter("fileNo");
				ftsFileName = request.getParameter("fileName");
				sqlQuery="select to_char(a.trn_id) as trn_no, concat(concat(a.trn_type, ' BY '), b.NAME) as trn_desc, c.ACTIVITY_DESC, TO_CHAR(a.TRN_DATE, 'DD-MM-YYYY') AS trn_dt, a.FILE_NO, a.trn_id from X_FTS_FILE_TRN a, m_sms_user b, P_FTS_ACTIVITY c where a.file_no = '" + ftsFileNo + "' AND a.EMP_CODE = b.EMP_CODE and a.ACTIVITY_CODE = c.ACTIVITY_CODE and a.REF_USER_CODE not in (select emp_code from m_sms_user) union select to_char(a.trn_id) as trn_no, concat(concat(concat(a.trn_type, ' BY '), b.NAME), concat(' TO ', d.name)) as trn_desc, c.ACTIVITY_DESC, TO_CHAR(a.TRN_DATE, 'DD-MM-YYYY') AS trn_dt, a.FILE_NO, a.trn_id from X_FTS_FILE_TRN a, m_sms_user b, P_FTS_ACTIVITY c, m_sms_user d where a.file_no = '" + ftsFileNo + "' AND a.EMP_CODE = b.EMP_CODE and a.ACTIVITY_CODE = c.ACTIVITY_CODE and a.REF_USER_CODE = d.emp_code ORDER BY TRN_ID DESC" ;
			} 
			else {
				sqlQuery="SELECT file_no, DECODE(cat_type, 'G', UPPER(subject), 'CONFIDENTIAL FILE') AS subject, custfield, link_button FROM v_fts_file_list WHERE session_id = '"+sessionId+"' AND file_type = '" + ftsType + "' ORDER BY trn_id DESC";
			}
			if (ftsType.matches("H"))
				sqlQuery="SELECT a.file_no, DECODE(a.cat_type, 'G', UPPER(a.subject), 'CONFIDENTIAL FILE') AS subject, DECODE(concat(a.ORG_UNIT,a.ORG_DEPT), concat(a.USER_UNIT,a.USER_DEPT), 'ARCHIVE', CONCAT(CONCAT(a.TRN_TYPE, ' ON '), TO_CHAR(a.TRN_DATE, 'DD-MMMM-YYYY'))) AS CustField, 'SEND' AS link_button, a.trn_id, d.session_id FROM V_FTS_FILE_STATUS a, x_sms_login d where (a.TRN_TYPE= 'RECEIVE' OR a.TRN_TYPE = 'CREATE' OR a.TRN_TYPE = 'PULLIN') and trim(d.USER_ID) = trim(TO_CHAR(a.user_code)) AND d.session_id = '"+sessionId+"' ORDER BY a.trn_id DESC";
			if (ftsType.matches("I"))
				sqlQuery="SELECT a.file_no, DECODE(a.cat_type, 'G', UPPER(a.subject), 'CONFIDENTIAL FILE') AS subject, CONCAT('SEND BY ', a.USER_NAME) AS CustField, 'RECEIVE' AS link_button, a.trn_id, d.session_id FROM V_FTS_FILE_STATUS a, x_sms_login d where a.TRN_TYPE= 'SEND' and trim(d.USER_ID) = trim(TO_CHAR(a.ref_user_code)) AND d.session_id =  = '"+sessionId+"'  ORDER BY a.trn_id DESC";
			if (ftsType.matches("S"))
				sqlQuery="select DISTINCT e.FILE_NO, DECODE(e.cat_type, 'G', UPPER(e.subject), 'CONFIDENTIAL FILE') AS subject, e.TRN_TYPE || ' BY ' || e.USER_NAME || DECODE(e.ref_user_code, 0, '', ' TO ' || e.ref_user_name)  AS CustField, 'ON ' || TO_CHAR(e.TRN_DATE, 'DD-MM-YYYY') AS link_button from V_FTS_FILE_STATUS e, x_fts_file_trn d, app_vw_emp c, app_vw_emp a, x_sms_login b where d.file_no = e.file_no and to_number(c.emp_id) = d.emp_code and b.session_id = '"+sessionId+"' and to_number(b.user_id) = a.emp_id and a.dept = c.dept";
			fileList = Utility.GenerateFileRecord(sqlQuery);
			request.getSession().setAttribute("fileList", fileList);
			request.getSession().setAttribute("TypeId", ftsType);
			String heading = "";
			if (ftsType.matches("I")){
				heading = "List of Incomming Files";
			}
			if (ftsType.matches("H")){
				heading = "List of Holding Files";
			}
			if (ftsType.matches("A")){
				heading = "List of Archive Files";
			}
			if (ftsType.matches("T")){
				heading = "Transaction History of " + ftsFileName + " ["+ ftsFileNo+"]";
			}
			if (ftsType.matches("ID")){
				heading = "List of Incomming Files of the Department";
			}
			if (ftsType.matches("HD")){
				heading = "List of Holding Files of the Department";
			}
			if (ftsType.matches("AD")){
				heading = "List of Archive Files of the Department";
			}
			if (ftsType.matches("S")){
				heading = "List of All Files of the Department";
			}
			request.getSession().setAttribute("heading", heading);
			request.getRequestDispatcher("/WEB-INF/jsp/ftsFileList.jsp").forward(request, response);
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
