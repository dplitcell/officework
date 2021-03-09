package allServlets;

import java.io.IOException;
import java.util.List;
import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import allClasses.ServiceRequest;
import allClasses.Utility;

@WebServlet("/smsCreate")
public class smsCreate extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public smsCreate() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sessionId = (String) request.getSession().getAttribute("unique");
		if(sessionId==null)	{
			response.sendRedirect("Login");
		} 
		else {
			String smsUpdType = request.getParameter("TypeId");
			String smsSRNo = request.getParameter("ReqId");
			ServiceRequest sr = new ServiceRequest();
			String heading=" ";
			String query = null;
			if (smsUpdType.matches("C"))
			{
				heading= "C:0#Create New Service Request" ;
				query = "select ' ' as sr_no, a.cat_type, a.area_code, a.sub_area, b.group_id, b.sub_group_id, '' as location, b.emp_name as cont_pers, b.mobile_no as cont_mob, b.email_id as cont_email, b.emp_id as cont_ngs, b.desig as cont_desig, b.group_desc, b.sub_group_desc, ' 'as cont_wa_no from (select * from p_sms_cat_area_sub p where p.rowid = (select min(q.rowid) from p_sms_cat_area_sub q where q.is_active = 1)) a, app_vw_emp b, x_sms_login c, m_sms_user d where c.user_id = d.user_id and d.emp_code = b.emp_id and c.session_id = '" + sessionId + "'";
				smsSRNo= "0";
			}
			else
			{
				heading="U:" +smsSRNo+"#Update the Status for Service Request No " + smsSRNo;
				query = "select sr_no, cat_type, area_code, sub_area, group_id, sub_group_id, location, cont_pers, cont_mob, cont_email, cont_ngs, cont_desig, group_desc, sub_group_desc, cont_wa_no from X_SMS_SR_MSTR where sr_no = " + smsSRNo + " and is_active = 1";
			}
			//new Utility();
			sr.setServiceRequest(Utility.createSR(sessionId, smsSRNo));
			request.getSession().setAttribute("sr", sr);
			request.getSession().setAttribute("heading", heading);
			
			query = "SELECT CONCAT(CONCAT(cat_type,':'),cat_desc) AS cat, cat_type FROM p_sms_cat where is_active = 1 ORDER BY cat_type"; 
			List<String> smsCatList= Utility.getStringList(query);
			request.getSession().setAttribute("CatList", smsCatList);
			String catJson = new Gson().toJson(smsCatList);
			request.setAttribute("catJson", catJson);
	
			query = "select CONCAT(CONCAT(CONCAT(a.cat_type,':'),CONCAT(a.area_code, '#')), a.area_desc) AS areas, a.cat_type, a.area_code from p_sms_cat_area a, p_sms_cat b where a.CAT_TYPE = b.CAT_TYPE and b.IS_ACTIVE = 1 and a.IS_ACTIVE = 1 order BY a.cat_type, a.area_code"; 
			List<String> smsAreaList= Utility.getStringList(query);
			request.setAttribute("AreaList", smsAreaList);
			String areaJson = new Gson().toJson(smsAreaList);
			request.setAttribute("areaJson", areaJson);
			
			query = "select CONCAT(CONCAT(CONCAT(a.cat_type,':'),CONCAT(a.area_code, '#')), CONCAT(CONCAT(a.sub_area, '$'), a.sub_area_desc)) AS areas, a.cat_type, a.area_code, a.sub_area from p_sms_cat_area_sub a, p_sms_cat b, p_sms_cat_area c where a.CAT_TYPE = b.CAT_TYPE and a.AREA_CODE = c.AREA_CODE and c.CAT_TYPE = b.CAT_TYPE and b.IS_ACTIVE = 1 and a.IS_ACTIVE = 1 and c.IS_ACTIVE = 1 order BY a.cat_type, a.area_code, a.sub_area"; 
			List<String> smsSubAreaList= Utility.getStringList(query);
			request.setAttribute("SubAreaList", smsSubAreaList);
			String subJson = new Gson().toJson(smsSubAreaList);
			request.setAttribute("subJson", subJson);
	
			if (smsUpdType.matches("C"))
			{
				query = "select concat(concat(status_code,':'), STATUS_DESC), STATUS_CODE  from P_SMS_STATUS where is_active = 1 and STATUS_CODE not in (select distinct STATUS_CODE from P_SMS_PRV_STATUS where is_active = 1) order by status_code";
			}
			else
			{
				query = "select concat(concat(a.status_code,':'), b.STATUS_DESC), a.status_code from P_SMS_PRV_STATUS a, P_SMS_STATUS b where a.is_active = 1 and b.IS_ACTIVE = 1 and b.STAGE_FOR <> 'A' and a.STATUS_CODE = b.STATUS_CODE and a.PRV_STATUS in (select last_status_code from V_SMS_SR_LAST_DESC where sr_no = " + smsSRNo + ")";
			}
			List<String> smsStatusList= Utility.getStringList(query);
			request.setAttribute("statusList", smsStatusList);
			String statusJson = new Gson().toJson(smsStatusList);
			request.setAttribute("statusJson", statusJson);
			request.getRequestDispatcher("/WEB-INF/jsp/smsCreate.jsp").forward(request, response);
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sessionId = (String) request.getSession().getAttribute("unique");
		if(sessionId==null)	{
			response.sendRedirect("Login");
		} 
		else {
			String heading = String.valueOf(request.getSession().getAttribute("heading"));
			String smsUpdType = heading.substring(0, heading.indexOf(":"));
			String smsSRNo = heading.substring(heading.indexOf(":")+1,heading.indexOf("#"));
			if (smsUpdType == "C"){
				smsSRNo = "0";
			}
			String contNGS = request.getParameter("contNGS");
			String smsLocation = request.getParameter("smsLocation");
			String contMob = request.getParameter("contMob");
			String contEmail = request.getParameter("contEmail");
			String smsSubArea = request.getParameter("smsSubArea");
			String srDesc = request.getParameter("srDesc");
			String smsStatus = request.getParameter("smsStatus");
			new Utility();
			String strMsg = Utility.CreateOrUpdSR(smsUpdType, smsSRNo, sessionId, contNGS, smsLocation, contMob, contEmail, smsSubArea, srDesc, smsStatus);
			response.sendRedirect("showSR?TypeId=D&ReqId="+strMsg);
		}
	}

}
