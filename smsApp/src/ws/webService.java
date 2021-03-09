package ws;


import allClasses.Utility;

import java.util.List;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONObject;

@Path("webSrv")
public class webService {
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String retGet(@Context ServletContext context) {
		String realPathres="";
		String realPathpdf =""; 
		String realPath = context.getRealPath("/");
		String retVal =  " Hi,\n This is an webservice for DPL Employees ESS App.\n Developed by IT Cell and Current Major version is " + this.getCurVersion("0") + "\n The Root path of Server is ";
		return retVal+realPath+realPathpdf+realPathres;
	}
	
	public String getCurVersion(String versionId) {
		String query  = "select version_id FROM p_sms_version WHERE app_type = 'ANDROID_APP' and is_active = 1";
		new Utility();
		int curMajorVersion = (int) Float.parseFloat(Utility.getStringVal(query));
		int appMajorVersion = (int) Float.parseFloat(versionId);
		return String.valueOf(curMajorVersion-appMajorVersion);
	}

	public String checkValidID(String sessionID) {
		String query  = "select to_char(count(*)) from x_sms_login where logout_dt_tm is NULL and session_id = '" + sessionID +"'";
		new Utility();
		String validSession = Utility.getStringVal(query);
		return validSession;
	}

	@Path("/{id}/ViewSR")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response viewSR(@PathParam("id") String versionId, String jsonCredential){
		System.out.println("Input JSON String: " + jsonCredential);
		String sessionId= "0";
		JSONObject retJsonObj = new JSONObject();
		//JSONArray arrayParam = new JSONArray();
		String retVal = this.getCurVersion(versionId);
		System.out.println("Return value of differce in version ID: " + retVal);
		if (!retVal.matches("0")) {
			retJsonObj.put("is_error", "Y");
			if (Integer.parseInt(retVal)> 0)
				retJsonObj.put("msg", "Version Mismatch! Update App");
			else
				retJsonObj.put("msg", "App is currenty not servicable");
		} else {
			JSONObject jsonObject= new JSONObject(jsonCredential);
			sessionId= jsonObject.getString("session_id");
			new Utility();
			String validSession = "0";
			if (sessionId.matches("1")) {
				String keyDecode = jsonObject.getString("key_val");
				String userId = keyDecode.substring(0, keyDecode.indexOf("#user#"));
				String passwd = keyDecode.substring(keyDecode.indexOf("#user#")+6, keyDecode.indexOf("#pass#"));
				String module = keyDecode.substring(keyDecode.indexOf("#pass#")+6);
				String validUser = (String) Utility.verifyLogin(module, userId, passwd);
				if (validUser.matches("Y"))
				{
					sessionId=String.valueOf(Utility.getUnique());
					String usernm = Utility.updLoginInfo(userId, sessionId, module);
					retJsonObj.put("name", usernm);
					retJsonObj.put("user_id",userId);
					retJsonObj.put("module",module);
					retJsonObj.put("session_id",sessionId);
					retJsonObj.put("key_val","0");
					validSession = "1";
				} else
					validSession = "0";
			} else 
				validSession = this.checkValidID(sessionId);
			if (validSession.matches("0")) {
				retJsonObj.put("is_error", "Y");
				retJsonObj.put("msg", "Invalid Credential! Login Again");
			} else {
				String smsUpdType = jsonObject.getString("type_id");
				String smsSRNo = jsonObject.getString("sr_no");
				retVal = Utility.showSRQuery(sessionId,smsUpdType,smsSRNo);
				String smsHeading = retVal.substring(0, retVal.indexOf("#"));
				String sqlQuery = "select a.serial_no || '#1#' || a.sr_detail || '#2#' || a.sr_rem || '#3#' || a.sr_action || '#4#' || a.no_url || '#5#' || a.action_url as srlist from (" + retVal.substring(retVal.indexOf("#")+1) + ") a";
				JSONArray jsonParam = Utility.getArrayParam(sqlQuery);
				retJsonObj.put("is_error", "N");
				retJsonObj.put("msg", "List populated");
				retJsonObj.put("param_list",jsonParam);
				retJsonObj.put("heading",smsHeading);
			}
		}
		System.out.println("Return JSON: " + retJsonObj.toString());
		return Response.status(200).entity(retJsonObj.toString()).build();
	}
	
	@Path("/{id}/SaveSR")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveSR(@PathParam("id") String versionId, String jsonCredential){
		System.out.println("Input JSON String: " + jsonCredential);
		String sessionId= "0";
		//String requestTime = "0";
		JSONObject retJsonObj = new JSONObject();
		//JSONArray arrayParam = new JSONArray();
		String retVal = this.getCurVersion(versionId);
		System.out.println("Return value of differce in version ID: " + retVal);
		if (!retVal.matches("0")) {
			retJsonObj.put("is_error", "Y");
			if (Integer.parseInt(retVal)> 0)
				retJsonObj.put("msg", "Version Mismatch! Update App");
			else
				retJsonObj.put("msg", "App is currenty not servicable");
			//retJsonObj.put("param_list",arrayParam);
		} else {
			JSONObject jsonObject= new JSONObject(jsonCredential);
			sessionId= jsonObject.getString("session_id");
			//requestTime = jsonObject.getString("request_time");
			String validSession = this.checkValidID(sessionId);
			if (validSession.matches("0")) {
				retJsonObj.put("is_error", "Y");
				retJsonObj.put("msg", "Invalid Credential! Login Again");
				//retJsonObj.put("param_list",arrayParam);
			} else {
				String smsUpdType = jsonObject.getString("type_id");
				String smsSRNo = jsonObject.getString("sr_no");
				String contNGS = jsonObject.getString("cont_ngs");
				String smsLocation = jsonObject.getString("cont_loc");
				String contMob = jsonObject.getString("cont_mob");
				String contEmail = jsonObject.getString("cont_email");
				String smsSubArea = jsonObject.getString("sub_area");
				String srDesc = jsonObject.getString("sr_desc");
				String smsStatus = jsonObject.getString("sr_status");
				new Utility();
				String strMsg = Utility.CreateOrUpdSR(smsUpdType, smsSRNo, sessionId, contNGS, smsLocation, contMob, contEmail, smsSubArea, srDesc, smsStatus);
				retVal = Utility.showSRQuery(sessionId,"D",strMsg);
				String smsHeading = retVal.substring(0, retVal.indexOf("#"));
				String sqlQuery = "select a.serial_no || '#1#' || a.sr_detail || '#2#' || a.sr_rem || '#3#' || a.sr_action || '#4#' || a.no_url || '#5#' || a.action_url as srlist from (" + retVal.substring(retVal.indexOf("#")+1) + ") a";
				JSONArray jsonParam = Utility.getArrayParam(sqlQuery);
				retJsonObj.put("is_error", "N");
				retJsonObj.put("msg", "List populated");
				retJsonObj.put("param_list",jsonParam);
				retJsonObj.put("heading",smsHeading);
			}
		}
		System.out.println("Return JSON: " + retJsonObj.toString());
		return Response.status(200).entity(retJsonObj.toString()).build();
	}

	@Path("/{id}/CreateSR")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createSR(@PathParam("id") String versionId, String jsonCredential){
		System.out.println("Input JSON String: " + jsonCredential);
		String sessionId= "0";
		JSONObject retJsonObj = new JSONObject();
		String retVal = this.getCurVersion(versionId);
		System.out.println("Return value of differce in version ID: " + retVal);
		if (!retVal.matches("0")) {
			retJsonObj.put("is_error", "Y");
			if (Integer.parseInt(retVal)> 0)
				retJsonObj.put("msg", "Version Mismatch! Update App");
			else
				retJsonObj.put("msg", "App is currenty not servicable");
		} else {
			JSONObject jsonObject= new JSONObject(jsonCredential);
			sessionId= jsonObject.getString("session_id");
			String validSession = this.checkValidID(sessionId);
			if (validSession.matches("0")) {
				retJsonObj.put("is_error", "Y");
				retJsonObj.put("msg", "Invalid Credential! Login Again");
				//retJsonObj.put("param_list",arrayParam);
			} else {
				String smsUpdType = jsonObject.getString("type_id");
				String smsSRNo = jsonObject.getString("sr_no");
				retJsonObj.put("sr_no", smsSRNo);
				retJsonObj.put("type_id", smsUpdType);
				retJsonObj.put("session_id",sessionId);
				String sqlQuery = null;
				new Utility();
				if (smsUpdType.matches("C")){
					String smsEmpNo = jsonObject.getString("emp_no");
					sqlQuery = "select TO_CHAR(count(*)) from app_vw_emp where emp_id = " + smsEmpNo;
					if (Utility.getStringVal(sqlQuery).matches("0")) {
						retJsonObj.put("is_error", "Y");
						retJsonObj.put("msg", "Invalid Employee No");
					} else {
						retJsonObj.put("is_error", "N");
						retJsonObj.put("msg", "Parameter populated");
						retJsonObj.put("heading", "Create New Service Request");
						sqlQuery = "select 'cont_pers-' || b.emp_name, 'cont_mob-' || b.mobile_no, 'cont_email-' || NVL(b.email_id, ' '), 'cont_ngs-' || b.emp_id, 'cont_desig-' || b.desig, 'group_desc-' || b.group_desc, 'sub_group_desc-' || b.sub_group_desc from app_vw_emp b where b.emp_id = " + smsEmpNo;
						List<String> listString= Utility.convtColToRowList(sqlQuery,7);
						for (String param:listString) {
							retJsonObj.put(param.substring(0,  param.indexOf("-")), param.substring(param.indexOf("-")+1));
						}
						sqlQuery = "SELECT cat_type || ':' || cat_desc AS cat FROM p_sms_cat where is_active = 1 ORDER BY cat";
						JSONArray jsonParamCat = Utility.getArrayParam(sqlQuery);
						retJsonObj.put("param_cat",jsonParamCat);
						sqlQuery = "select a.cat_type||':'||a.area_code||'#'||a.area_desc AS area from p_sms_cat_area a, p_sms_cat b where a.CAT_TYPE = b.CAT_TYPE and b.IS_ACTIVE = 1 and a.IS_ACTIVE = 1 order BY area";
						JSONArray jsonParamArea = Utility.getArrayParam(sqlQuery);
						retJsonObj.put("param_area",jsonParamArea);
						sqlQuery = "select a.cat_type||':'||a.area_code||'#'||a.sub_area||'$'||a.sub_area_desc AS sub_area from p_sms_cat_area_sub a, p_sms_cat b, p_sms_cat_area c where a.CAT_TYPE = b.CAT_TYPE and a.AREA_CODE = c.AREA_CODE and c.CAT_TYPE = b.CAT_TYPE and b.IS_ACTIVE = 1 and a.IS_ACTIVE = 1 and c.IS_ACTIVE = 1 order BY sub_area";
						JSONArray jsonParamSubArea = Utility.getArrayParam(sqlQuery);
						retJsonObj.put("param_sub",jsonParamSubArea);
						sqlQuery = "select status_code||':'||STATUS_DESC as STATUS from P_SMS_STATUS where is_active = 1 and STATUS_CODE not in (select distinct STATUS_CODE from P_SMS_PRV_STATUS where is_active = 1) order by status";
						JSONArray jsonParamStatus = Utility.getArrayParam(sqlQuery);
						retJsonObj.put("param_status",jsonParamStatus);
					}
				}
				else{
					retJsonObj.put("is_error", "N");
					retJsonObj.put("msg", "Parameter populated");
					retJsonObj.put("heading", "Update the Service Request No " + smsSRNo);
					sqlQuery = "select 'cont_pers-' || a.cont_pers, 'cont_mob-' || a.cont_mob, 'cont_email-' || NVL(a.cont_email, ' '), 'cont_ngs-' || a.cont_ngs, 'cont_desig-' || a.cont_desig, 'group_desc-' || a.group_desc, 'sub_group_desc-' || a.sub_group_desc, 'cont_loc-'||location from X_SMS_SR_MSTR a where a.sr_no = " + smsSRNo + " and a.is_active = 1";
					List<String> listString= Utility.convtColToRowList(sqlQuery,8);
					for (String param:listString) {
						retJsonObj.put(param.substring(0,  param.indexOf("-")), param.substring(param.indexOf("-")+1));
					}
					sqlQuery = "SELECT a.cat_type || ':' || a.cat_desc AS cat FROM p_sms_cat a, X_SMS_SR_MSTR d where d.sr_no = " + smsSRNo + " and d.is_active = 1 and a.is_active = 1 and a.cat_type = d.cat_type ORDER BY cat";
					JSONArray jsonParamCat = Utility.getArrayParam(sqlQuery);
					retJsonObj.put("param_cat",jsonParamCat);
					sqlQuery = "select a.cat_type||':'||a.area_code||'#'||a.area_desc AS area from p_sms_cat_area a, p_sms_cat b, X_SMS_SR_MSTR d where d.sr_no = " + smsSRNo + " and d.is_active = 1 and a.is_active = 1 and a.cat_type = d.cat_type and a.area_code = d.area_code and a.CAT_TYPE = b.CAT_TYPE and b.IS_ACTIVE = 1 and a.IS_ACTIVE = 1 order BY area";
					JSONArray jsonParamArea = Utility.getArrayParam(sqlQuery);
					retJsonObj.put("param_area",jsonParamArea);
					sqlQuery = "select a.cat_type||':'||a.area_code||'#'||a.sub_area||'$'||a.sub_area_desc AS sub_area from p_sms_cat_area_sub a, p_sms_cat b, p_sms_cat_area c, X_SMS_SR_MSTR d where d.sr_no = " + smsSRNo + " and d.is_active = 1 and a.is_active = 1 and a.cat_type = d.cat_type and a.area_code = d.area_code and a.sub_area = d.sub_area and a.CAT_TYPE = b.CAT_TYPE and a.AREA_CODE = c.AREA_CODE and c.CAT_TYPE = b.CAT_TYPE and b.IS_ACTIVE = 1 and a.IS_ACTIVE = 1 and c.IS_ACTIVE = 1 order BY sub_area";
					JSONArray jsonParamSubArea = Utility.getArrayParam(sqlQuery);
					retJsonObj.put("param_sub",jsonParamSubArea);
					sqlQuery = "select a.status_code||':'||b.STATUS_DESC as status from P_SMS_PRV_STATUS a, P_SMS_STATUS b where a.is_active = 1 and b.IS_ACTIVE = 1 and b.STAGE_FOR <> 'A' and a.STATUS_CODE = b.STATUS_CODE and a.PRV_STATUS in (select last_status_code from V_SMS_SR_LAST_DESC where sr_no = " + smsSRNo + ") order by status";
					JSONArray jsonParamStatus = Utility.getArrayParam(sqlQuery);
					retJsonObj.put("param_status",jsonParamStatus);
				}
			}
		}
		System.out.println("Return JSON: " + retJsonObj.toString());
		return Response.status(200).entity(retJsonObj.toString()).build();
	}
	
	@Path("/{id}/Lists")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response listItems(@PathParam("id") String versionId, String jsonCredential){
		System.out.println("Input JSON String: " + jsonCredential);
		//String retJson = null;
		JSONObject retJsonObj = new JSONObject();
		JSONArray arrayParam = new JSONArray();
		String retVal = this.getCurVersion(versionId);
		System.out.println("Return value of differce in version ID: " + retVal);
		if (!retVal.matches("0")) {
			retJsonObj.put("is_error", "Y");
			if (Integer.parseInt(retVal)> 0)
				retJsonObj.put("msg", "Version Mismatch! Update App");
			else
				retJsonObj.put("msg", "App is currenty not servicable");
			retJsonObj.put("param_list",arrayParam);
		} else {
			JSONObject jsonObject= new JSONObject(jsonCredential);
			String sessionId= jsonObject.getString("session_id");
			String validSession = this.checkValidID(sessionId);
			if (validSession.matches("0")) {
				retJsonObj.put("is_error", "Y");
				retJsonObj.put("msg", "Invalid Credential! Login Again");
				retJsonObj.put("param_list",arrayParam);
			} else {
				String sqlQuery = jsonObject.getString("sql_query");
				new Utility();
				JSONArray jsonParam = Utility.getArrayParam(sqlQuery);
				if (jsonParam.length()==0) {
					retJsonObj.put("is_error", "Y");
					retJsonObj.put("msg", "Parameter Genertion Error");
					retJsonObj.put("param_list",jsonParam);
				} else {
					retJsonObj.put("is_error", "N");
					retJsonObj.put("msg", "Parameter populated");
					retJsonObj.put("param_list",jsonParam);
				}
			}
		}
		System.out.println("Return JSON: " + retJsonObj.toString());
		return Response.status(200).entity(retJsonObj.toString()).build();
	}
	
	@Path("/{id}/Report")	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response downPDF(@PathParam("id") String versionId, String jsonCredential, @Context ServletContext context){
		System.out.println("Input JSON String: " + jsonCredential);
		String realPathSrc = context.getRealPath("/WEB-INF/allReports/DPL_A4_Payslip_rpt.jrxml");
		String realPathDest = context.getRealPath("/allPDFs/PayslipReport.pdf").replaceAll("smsApp", "ROOT");
//		String retJson = null;
		String retVal = this.getCurVersion(versionId);
		JSONObject retJsonObj = new JSONObject();
		System.out.println("Return value of differce in version ID: " + retVal);
		if (!retVal.matches("0")) {
			retJsonObj.put("is_error", "Y");
			retJsonObj.put("repo_url", "0");
			retJsonObj.put("msg", "Version Mismatch! Update App");
//			retJson = "{\"is_error\":\"Y\",\"repo_url\":\"0\",\"msg\":\"Version Mismatch! Update App\"}";
		} else {
			JSONObject jsonObject= new JSONObject(jsonCredential);
			String sessionId= jsonObject.getString("session_id");
			new Utility();
			String validSession = "0";
			if (sessionId.matches("1")) {
				String keyDecode = jsonObject.getString("key_val");
				String userId = keyDecode.substring(0, keyDecode.indexOf("#user#"));
				String passwd = keyDecode.substring(keyDecode.indexOf("#user#")+6, keyDecode.indexOf("#pass#"));
				String module = keyDecode.substring(keyDecode.indexOf("#pass#")+6);
				String validUser = (String) Utility.verifyLogin(module, userId, passwd);
				if (validUser.matches("Y"))
				{
					sessionId=String.valueOf(Utility.getUnique());
					String usernm = Utility.updLoginInfo(userId, sessionId, module);
					retJsonObj.put("name", usernm);
					retJsonObj.put("user_id",userId);
					retJsonObj.put("module",module);
					retJsonObj.put("session_id",sessionId);
					retJsonObj.put("key_val","0");
					retJsonObj.put("rpt_title", jsonObject.getString("rpt_title"));
					retJsonObj.put("rpt_desc", jsonObject.getString("rpt_desc"));
					validSession = "1";
				} else
					validSession = "0";
			} else 
				validSession = this.checkValidID(sessionId);
			if (validSession.matches("0")) {
				retJsonObj.put("is_error", "Y");
				retJsonObj.put("repo_url", "0");
				retJsonObj.put("msg", "Invalid Credential! Login Again");
//				retJson = "{\"is_error\":\"Y\",\"repo_url\":\"0\",\"msg\":\"Invalid Credential! Login Again\"}";
			} else {
				String repoUrl = Utility.printReport(jsonObject, realPathSrc, realPathDest);
				System.out.println("Report Jasper Respose..: " + repoUrl);
				if (repoUrl.matches("0")) {
					retJsonObj.put("is_error", "Y");
					retJsonObj.put("repo_url", "0");
					retJsonObj.put("msg", "Error in Report Generation");
//					retJson = "{\"is_error\":\"Y\",\"repo_url\":\"0\",\"msg\":\"Error in Report Generation\"}";
				} else {
					retJsonObj.put("is_error", "N");
					retJsonObj.put("repo_url", repoUrl);
					retJsonObj.put("msg", "Report Generated");
//					retJson = "{\"is_error\":\"N\",\"repo_url\":\""+ repoUrl + "\",\"msg\":\"Report Generated\"}";
				}
			}
		}
		System.out.println("Return JSON: " + retJsonObj.toString());
		return Response.status(200).entity(retJsonObj.toString()).build();
	}

	@Path("/{id}/About")	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response aboutApp(@PathParam("id") String versionId, String jsonCredential){
		System.out.println("Input JSON String: " + jsonCredential);
		String retJson = null;
		String retVal = this.getCurVersion(versionId);
		System.out.println("Return value of differce in version ID: " + retVal);
		if (!retVal.matches("0")) {
			retJson = "{\"is_error\":\"Y\",\"display\":\"Version Mismatch! Update App\nFirst Upadte the App\",\"msg\":\"Version Mismatch! Update App\"}";
		} else {
			JSONObject jsonObject= new JSONObject(jsonCredential);
			String sessionId= jsonObject.getString("session_id");
			String validSession = this.checkValidID(sessionId);
			if (validSession.matches("0")) {
				retJson = "{\"is_error\":\"Y\",\"display\":\"Invalid Credential!!\nFirst Logout then again Login the App\",\"msg\":\"Invalid Credential! Login Again\"}";
			} else {
				String query  = "select 'Current Version ID: ' || version_id || '\n' || app_desc || '\n' || update_for FROM p_sms_version WHERE app_type = 'ANDROID_APP' and is_active = 1";
				new Utility();
				String versionInfo = Utility.getStringVal(query);
				retJson = "{\"is_error\":\"N\",\"display\":\"" + versionInfo + "\",\"msg\":\"App Version Info\"}";
			}
			//End
		}
		System.out.println("Return JSON: " + retJson.toString());
		return Response.status(200).entity(retJson).build();
	}
	
	@Path("/{id}/Profile")	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response aboutMe(@PathParam("id") String versionId, String jsonCredential){
		System.out.println("Input JSON String: " + jsonCredential);
		JSONObject retJsonObj = new JSONObject();
		JSONArray arrayParam = new JSONArray();
		String retVal = this.getCurVersion(versionId);
		System.out.println("Return value of differce in version ID: " + retVal);
		if (!retVal.matches("0")) {
			retJsonObj.put("is_error", "Y");
			if (Integer.parseInt(retVal)> 0)
				retJsonObj.put("msg", "Version Mismatch! Update App");
			else
				retJsonObj.put("msg", "App is currenty not servicable");
			retJsonObj.put("param_list",arrayParam);
		} else {
			JSONObject jsonObject= new JSONObject(jsonCredential);
			String sessionId= jsonObject.getString("session_id");
			String validSession = this.checkValidID(sessionId);
			if (validSession.matches("0")) {
				retJsonObj.put("is_error", "Y");
				retJsonObj.put("msg", "Invalid Credential! Login Again");
				retJsonObj.put("param_list",arrayParam);
			} else {
				String userId= jsonObject.getString("user_id");
				String sqlQuery = "select head || ':' || val from (select ngs, to_char(ngs) as emp_no, nam, desig_description as desig, department_name as dept, to_char(bdate, 'dd/mm/yyyy') as bdt, to_char(jdate) as jdt, banker, bank_ac_no as ac_no from VW_DCPY_EMP_DETAILS where ngs = " + userId + ") UNPIVOT (val FOR (head) IN (emp_no AS 'Employee No', Nam AS 'Employee Name', desig AS 'Designation', dept AS 'Department', bdt AS 'Date of Birth', jdt AS 'Date of Joining', banker AS 'Current Banker', ac_no AS 'Account No'))";
				new Utility();
				JSONArray jsonParam = Utility.getArrayParam(sqlQuery);
				if (jsonParam.length()==0) {
					retJsonObj.put("is_error", "Y");
					retJsonObj.put("msg", "Database Error");
					retJsonObj.put("param_list",jsonParam);
				} else {
					retJsonObj.put("is_error", "N");
					retJsonObj.put("msg", "Data populated");
					retJsonObj.put("param_list",jsonParam);
				}
			}
		}
		System.out.println("Return JSON: " + retJsonObj.toString());
		return Response.status(200).entity(retJsonObj.toString()).build();
	}

	
	@Path("/{id}/Notify")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response doNotify(@PathParam("id") String versionId, String jsonCredential){
		System.out.println("Input JSON String: " + jsonCredential);
		JSONObject retJsonObj = new JSONObject();
		String retVal = this.getCurVersion(versionId);
		System.out.println("Return value of differce in version ID: " + retVal);
		if (!retVal.matches("0")) {
			retJsonObj.put("is_error", "Y");
			if (Integer.parseInt(retVal)> 0)
				retJsonObj.put("msg", "Version Mismatch! Update App");
			else
				retJsonObj.put("msg", "App is currenty not servicable");
			retJsonObj.put("name","First Update the APP");
			retJsonObj.put("user_id","0");
			retJsonObj.put("session_id","0");
		} else {
			JSONObject jsonObject= new JSONObject(jsonCredential);
			String keyDecode = jsonObject.getString("key_val");
			new Utility();
			String userId = keyDecode.substring(0, keyDecode.indexOf("#user#"));
			String passwd = keyDecode.substring(keyDecode.indexOf("#user#")+6, keyDecode.indexOf("#pass#"));
			String module = keyDecode.substring(keyDecode.indexOf("#pass#")+6);
			String requestTime = jsonObject.getString("request_time");
			System.out.println("At allAPI : module- " + module + ", userid- " +userId + ", pass- " + passwd);
			retJsonObj = Utility.getNotify(module, userId, passwd, requestTime, keyDecode);
		}
		System.out.println("Return JSON: " + retJsonObj.toString());
		return Response.status(200).entity(retJsonObj.toString()).build();
	}

	@Path("/{id}/Login")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkLogin(@PathParam("id") String versionId, String jsonCredential){
		System.out.println("Input JSON String: " + jsonCredential);
		//String retJson = null;
		JSONObject retJsonObj = new JSONObject();
		String retVal = this.getCurVersion(versionId);
		System.out.println("Return value of differce in version ID: " + retVal);
		if (!retVal.matches("0")) {
			retJsonObj.put("is_error", "Y");
			if (Integer.parseInt(retVal)> 0)
				retJsonObj.put("msg", "Version Mismatch! Update App");
			else
				retJsonObj.put("msg", "App is currenty not servicable");
			retJsonObj.put("name","First Update the APP");
			retJsonObj.put("user_id","0");
			retJsonObj.put("session_id","0");
			//retJson = "{\"session_id\":\"0\",\"name\":\"First Update the APP\",\"is_error\":\"Y\",\"msg\":\"Version Mismatch! Update App\"}";
		} else {
			JSONObject jsonObject= new JSONObject(jsonCredential);
			String userId= jsonObject.getString("user");
			String passwd= jsonObject.getString("pass");
			//Start- New change for remember me
			String module= jsonObject.getString("module");
				//overwritten
			//String module = "01"; //Indentor Module
			String keyFlag = jsonObject.getString("key_flag");
			String keyDecode = jsonObject.getString("key_val");
			String remFlag = jsonObject.getString("rem_flag");
			new Utility();
			if (keyFlag.matches("1")) {
				userId = keyDecode.substring(0, keyDecode.indexOf("#user#"));
				passwd = keyDecode.substring(keyDecode.indexOf("#user#")+6, keyDecode.indexOf("#pass#"));
				module = keyDecode.substring(keyDecode.indexOf("#pass#")+6);
			}
				//overwritten
			//new Utility();
			//End- New change for remember me
//			System.out.println("At allAPI : module- " + module + ", userid- " +userId + ", pass- " + passwd);
			String validUser = (String) Utility.verifyLogin(module, userId, passwd);
			System.out.println("At allAPI : module- " + module + ", userid- " +userId + ", pass- " + passwd + ", validUser- " + validUser);
			if (validUser.matches("Y"))
			{
				String sessionId=String.valueOf(Utility.getUnique());
				String usernm = Utility.updLoginInfo(userId, sessionId, module);
				retJsonObj.put("is_error", "N");
				retJsonObj.put("msg", "Hi! " + usernm);
				retJsonObj.put("name", usernm);
				retJsonObj.put("user_id",userId);
				retJsonObj.put("module",module);
				retJsonObj.put("session_id",sessionId);
				if (remFlag.matches("1"))
					retJsonObj.put("key_val", userId + "#user#" + passwd + "#pass#" + module);
				else
					retJsonObj.put("key_val", "0");
			}
			else
			{
				retJsonObj.put("is_error", "Y");
				retJsonObj.put("msg", "Invalid Credential");
				retJsonObj.put("name", "Enter valid User ID and Password");
				retJsonObj.put("user_id","0");
				retJsonObj.put("module",module);
				retJsonObj.put("session_id","0");
				retJsonObj.put("key_val", "0");
			}
		}
		System.out.println("Return JSON: " + retJsonObj.toString());
		return Response.status(200).entity(retJsonObj.toString()).build();
	}

	@Path("/{id}/Logout")	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response doLogout(@PathParam("id") String versionId, String jsonCredential){
		System.out.println("Input JSON String: " + jsonCredential);
		JSONObject retJsonObj = new JSONObject();
		JSONObject jsonObject= new JSONObject(jsonCredential);
		String sessionId= jsonObject.getString("session_id");
		String retVal = this.getCurVersion(versionId);
		System.out.println("Return value of differce in version ID: " + retVal);
		String validSession = this.checkValidID(sessionId);
		if (!retVal.matches("0")) {
			retJsonObj.put("is_error", "N");
			if (Integer.parseInt(retVal)> 0)
				retJsonObj.put("msg", "Version Mismatch! Update App");
			else
				retJsonObj.put("msg", "App is currenty not servicable");
		} else {
			if (validSession.matches("0")) {
				retJsonObj.put("is_error", "N");
				retJsonObj.put("msg", "Invalid Credential! Login Again");
			} else {
				new Utility();
				String retMsg = Utility.doLogout(sessionId);
				retJsonObj.put("is_error", "N");
				retJsonObj.put("msg", retMsg.substring(2));
			}
		}
		System.out.println("Return JSON: " + retJsonObj.toString());
		return Response.status(200).entity(retJsonObj.toString()).build();
	}
}
