package allClasses;
/*
import java.io.File;

//import allClasses.Menu;
//import allClasses.ServiceRequest;

import java.io.IOException;
*/
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/*
import java.util.Map;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.VFS;
import org.codehaus.jackson.map.ObjectMapper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
//import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
*/

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;


public class Utility 
{
/*	
	private static ObjectMapper objMap;
	
	static {
		objMap = new ObjectMapper();
	}
	
	public static String convtJavaToJson (Object classObj) {
		String strJson = null;
		try {
			strJson = objMap.writeValueAsString(classObj);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strJson;
	}
	
	public static <T> T convtJsonToJava (String strJson, Class<T> classObj) {
		T retClass = null;
		try {
			retClass = objMap.readValue(strJson, classObj);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retClass;
	}
*/	
	
	public Connection make_connection()
	{ 
		Connection con=null;
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			//con = DriverManager.getConnection("jdbc:oracle:thin:@172.16.0.51:1521:pamsprod", "pamsprod", "pamsprod");
			//con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl1", "dplprod", "dplprod");
			con = DriverManager.getConnection("jdbc:oracle:thin:@172.16.0.51:1521:pamsuat", "gst", "gst123");
//			con = DriverManager.getConnection("jdbc:oracle:thin:@172.16.0.51:1521:pamsuat", "pamsuat", "pamsuat");
			
			return con;
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return con;
	}
	
/*
	public Connection makeConnection()
	{ 
		Connection con=null;
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			//con = DriverManager.getConnection("jdbc:oracle:thin:@172.16.0.51:1521:pamsuat", "dplprod", "dplprod");
			//con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl1", "dplprod", "dplprod");
			con = DriverManager.getConnection("jdbc:oracle:thin:@172.16.0.51:1521:pamsuat", "pamsuat", "pamsuat");
			
			return con;
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return con;
	}
*/

	public static String getDispJArray(String sqlQuery, JSONArray paramVal)
	{
		System.out.println("inside JA: " + sqlQuery);
		JSONArray arrayParam = new JSONArray();
		Connection conn=new Utility().make_connection();
    	try {
    		ResultSet rs;
    		if (paramVal.length()>0) {
	    		PreparedStatement statement =conn.prepareStatement(sqlQuery);
	    		for(int iLoop= 0; iLoop < paramVal.length(); iLoop++) {
	    			statement.setString(iLoop + 1, paramVal.getString(iLoop));
	    		}
	    		rs = statement.executeQuery();
    		} else {
    			rs=conn.createStatement().executeQuery(sqlQuery);
    		}
    		ResultSetMetaData rsmd = rs.getMetaData();
    		int iColNos = rsmd.getColumnCount();
    		int iLoop = 0;
    		JSONArray fieldArray;
			
    		while(rs.next()) {
    			fieldArray = new JSONArray();
    			for (iLoop = 0; iLoop < iColNos; iLoop++) {
    				fieldArray.put(rs.getString(iLoop+1));
    			}
    			arrayParam.put(fieldArray);
    		}
    		
    		fieldArray = new JSONArray();
			for (iLoop = 0; iLoop < iColNos; iLoop++) {
				fieldArray.put(rsmd.getColumnName(iLoop+1));
			}
			arrayParam.put(fieldArray);
    		
    		conn.close();
    	}
    	catch(SQLException | JSONException ex) {
    		ex.printStackTrace();
    	}
    	System.out.println("Json Array: " + arrayParam.toString());
		return arrayParam.toString();
	}
/*	
	public static String getJSONArray(String sqlQuery)
	{
		System.out.println("inside only: " + sqlQuery);
		JSONArray arrayParam = new JSONArray();
		Connection conn=new Utility().make_connection();
    	try {
    		ResultSet rs=conn.createStatement().executeQuery(sqlQuery);
    		ResultSetMetaData rsmd = rs.getMetaData();
    		int iColNos = rsmd.getColumnCount();
    		int iLoop = 0;
    		JSONObject fieldJson;
    		
    		while(rs.next()) {
    			fieldJson = new JSONObject ();
				for (iLoop = 0; iLoop < iColNos; iLoop++) {
    				fieldJson.put(rsmd.getColumnName(iLoop+1).toLowerCase(), rs.getString(iLoop+1));
    			}
				if (fieldJson.has("field_param")) {
					fieldJson.put("field_param", (new JSONObject(fieldJson.getString("field_param"))));
				}
    			arrayParam.put(fieldJson);
    		}
    		conn.close();
    	}
    	catch(SQLException | JSONException ex) {
    		ex.printStackTrace();
    	}
    	System.out.println("Json Array: " + arrayParam.toString());
		return arrayParam.toString();
	}
*/	
	public static List<String> getStringList(String query)
	{
		System.out.println("inside: " + query);
		ArrayList<String> listString=new ArrayList<String>();
		try
		{
			Connection conn= new Utility().make_connection();
			ResultSet rs=conn.createStatement().executeQuery(query);
			while(rs.next())
			{
				//System.out.println("Inside DO post Resultset");
				listString.add(rs.getString(1));
				//System.out.println(rs.getString(1).substring(0, 2));
			}
			conn.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return listString;
	}

	public static List<String> convtColToRowList(String query, int iColNos) {
		//System.out.println("inside");
		ArrayList<String> listString=new ArrayList<String>();
		try {
			Connection conn= new Utility().make_connection();
			ResultSet rs=conn.createStatement().executeQuery(query);
			while(rs.next())
			{
				for (int iLoop = 0; iLoop < iColNos; iLoop++) {
					listString.add(rs.getString(iLoop+1));
				}
			}
			conn.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return listString;
	}
/*	
	public static List<Menu> GenerateMenu(String session, String module) {
    	List<Menu> menuList = new ArrayList<Menu>();
    	String query="SELECT * FROM v_app_menu WHERE session_id = '"+session+"' AND module_id = '" + module + "'";
    	Connection conn=new Utility().make_connection();
    	try
    	{
    		ResultSet rs=conn.createStatement().executeQuery(query);
    		while(rs.next()) {
    			Menu menu=new Menu();
    			menu.setMenu(rs.getInt(2),rs.getString(3),rs.getString(4),rs.getString(5));
    			menuList.add(menu);
    		}
//    		Menu menu=new Menu();
//			menu.setMenu(99,"Logout","LO","logout");
//			menuList.add(menu);
//			Considered in the view
    		conn.close();
    	}
    	catch(SQLException ex) {
    		ex.printStackTrace();
    	}
    	return menuList;
    }
*/
	
	public static String showSRQuery(String sesssionId, String smsType, String smsSR)
	{
  		String smsHeading = " ";
		String query = "select concat(b.module_for,a.user_id) from X_SMS_LOGIN a, M_SMS_MODULE b where a.MODULE_ID = b.MODULE_ID and a.SESSION_ID = '" + sesssionId + "'";
		//new Utility();
		String retVal = Utility.getStringVal(query);
		String userId = retVal.substring(1);
		String moduleFor = "('" + retVal.substring(0, 1) + "')";
		String srUsrTbl = "v_sms_sr_providers";
		if (retVal.substring(0, 1).matches("S")){
			moduleFor = "('O','S')";
		}
		if (retVal.substring(0, 1).matches("I")){
			srUsrTbl = "v_sms_sr_indentors";
		}
		if (smsType.matches("A")){
			smsHeading = "List of Actionable Service Request";
			query = "select 999999999999 AS sort_no, 'Request No' AS serial_no, 'Request Description' as sr_detail, 'Request Initiator' as sr_rem, 'Current Status' as sr_action, ' ' as no_url, ' ' as action_url from DUAL UNION SELECT to_number(to_char(last_sr_date, 'yymmddhh24miss')) AS sort_no, 'SR No: ' || to_char(sr_no) || '<br>SR Date: ' || to_char(opn_sr_date, 'DD-MM-YYYY') AS serial_no, cat_type || '- ' || AREA_DESC || ': ' || SUB_AREA_DESC || '<br>' || opn_sr_desc AS sr_detail, cont_pers || ', ' || cont_desig || '<br>' || location || ', ' || sub_group_desc AS sr_rem, to_char(last_sr_date, 'dd-mm-yyyy') || ' ' || last_status_desc ||  '<br>' || last_sr_desc as sr_action, 'showSR?TypeId=D&ReqId=' || to_char(sr_no) as no_url, 'smsCreate?TypeId=U&ReqId=' || to_char(sr_no) as action_url from v_sms_all_sr_status where next_stage_for IN " + moduleFor + " and sr_no IN (SELECT sr_no from " + srUsrTbl + " where user_id = '" + userId + "') ORDER BY sort_no DESC";
		} 
		if (smsType.matches("D")){
			query = "SELECT 'Transactions for Service Request No: ' || to_char(sr_no) || ' Dated: ' || to_char(OPN_SR_DATE, 'DD-MON-YYYY') || '<br>' || 'Request Module: ' || cat_desc || '- ' || Area_desc || '- ' || sub_area_desc || '<br>' || 'Contact Person: ' || cont_pers || ', ' || cont_desig || ' Mobile No: ' || cont_mob || '<br>' || 'Service Location: ' || location || ', under ' || sub_group_desc || ' ' || group_desc AS heading FROM V_SMS_ALL_SR_STATUS where SR_NO = " + smsSR;
			smsHeading = Utility.getStringVal(query);
			query = "select 999999999999 AS sort_no, 'Date' AS serial_no, 'Status' as sr_detail, 'Description' as sr_rem, 'User' as sr_action, ' ' as no_url, ' ' as action_url from DUAL UNION SELECT to_number(to_char(sysdate, 'yymmddhh24miss')) - to_number(to_char(a.sr_date, 'yymmddhh24miss')) AS sort_no, to_char(a.sr_date, 'DD-MM-YYYY HH24:MI:SS') AS serial_no, b.status_desc AS sr_detail, a.sr_desc AS sr_rem, d.name as sr_action, ' ' as no_url, ' ' as action_url from x_sms_sr_trns a, p_sms_status b, x_sms_login c, m_sms_user d where a.status_code = b.status_code and a.session_id = c.session_id and a.is_active = 1 and c.user_id = d.user_id and a.sr_no = " + smsSR + " ORDER BY sort_no DESC";
		}
		if (!retVal.substring(0, 1).matches("I")){
			srUsrTbl = "v_sms_sr_all_providers";
		}
		if (smsType.matches("V")){
			smsHeading = "List of Pending Service Request";
			query = "select 999999999999 AS sort_no, 'Request No' AS serial_no, 'Request Description' as sr_detail, 'Request Initiator' as sr_rem, 'Request Status' as sr_action, ' ' as no_url, ' ' as action_url from DUAL UNION SELECT to_number(to_char(last_sr_date, 'yymmddhh24miss')) AS sort_no, 'SR No: ' || to_char(sr_no) || '<br>SR Date: ' || to_char(opn_sr_date, 'DD-MM-YYYY') AS serial_no, cat_type || '- ' || AREA_DESC || ': ' || SUB_AREA_DESC || '<br>' || opn_sr_desc AS sr_detail, cont_pers || ', ' || cont_desig || '<br>' || location || ', ' || sub_group_desc AS sr_rem, 'Last updated on ' || to_char(last_sr_date, 'dd-mm-yyyy') || '<br>' || user_name as sr_action, 'showSR?TypeId=D&ReqId=' || to_char(sr_no) as no_url, ' ' as action_url from v_sms_all_sr_status where next_stage_for <> '" + retVal.substring(0, 1) + "' and sr_no IN (SELECT sr_no from " + srUsrTbl + " where user_id = '" + userId + "') ORDER BY sort_no DESC";
		}
		return smsHeading + "#" + query;
	}

/*	
	public static List<SRStatus> getSRStatusList(String query) {
    	List<SRStatus> srList = new ArrayList<SRStatus>();
		System.out.println("Inside : query- " + query);
    	Connection conn=new Utility().make_connection();
    	try {
    		ResultSet rs=conn.createStatement().executeQuery(query);
    		while(rs.next())
    		{
    			SRStatus srStat=new SRStatus();
    			srStat.setSRStatus(rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7));
    			//System.out.println("Inside : 1- " + rs.getString(2) + ", 2- " + rs.getString(7));
    			srList.add(srStat);
    		}
    		conn.close();
    	}
    	catch(SQLException ex) {
    		ex.printStackTrace();
    	}
    	return srList;
    }
*/
	
	
	public static String verifyLogin(String module, String userid, String passwd) 
	{
    	Connection conn=new Utility().make_connection();
    	String retFlag = "N";
    	try {
    		System.out.println("At util : module- " + module + ", userid- " +userid + ", pass- " + passwd);
    		CallableStatement cstmt = conn.prepareCall("{? = call fn_app_verify_user(?,?)}");
    		//conn.prepareCall("{? = CALL balance(?)}");
    		cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
    		//cstmt.setString(2,module);
    		cstmt.setString(2,userid);
    		cstmt.setString(3,passwd);
    		
    		cstmt.executeUpdate();
    		retFlag = cstmt.getString(1);
    		conn.close();
    	} 
    	catch(SQLException ex) {
    		ex.printStackTrace();
    	}
 //   	System.out.println("Ret Rem:" + retFlag);
    	return retFlag;
	}

	public static String updLoginInfo(String userId, String session, String module)
	{
		String strReturn= " ";
		String query = "insert into x_app_login (session_id, user_id, login_dt_tm, module_id) values ('"+session+"','"+userId+"',systimestamp, '" + module +"')";
		Connection conn=new Utility().make_connection();
    	try
    	{
    		conn.createStatement().executeUpdate(query);
    		query="select name from m_app_user where user_id = '" + userId + "'";
    		ResultSet rs=conn.createStatement().executeQuery(query);
			while(rs.next()) {
				strReturn=rs.getString(1);
			}
			conn.close();
    	}
    	catch(SQLException ex) {
    		ex.printStackTrace();
    	}
		return strReturn;
	}

/*	
	public static String CreateOrUpdSR(String smsUpdType, String smsSRNo, String sessionId, String contNGS, String smsLocation,	String contMob, String contEmail, String smsSubArea, String srDesc, String smsStatus)
	{
		String strRetMsg= null;
    	Connection conn=new Utility().make_connection();
    	try
    	{
//    		System.out.println("check Val: typ- " + smsUpdType+", srno- " + smsSRNo + ", status- " + smsStatus);
    		String smsCat = smsSubArea.substring(0, smsSubArea.indexOf(":"));
    		String smsArea = smsSubArea.substring(smsSubArea.indexOf(":")+1,smsSubArea.indexOf("#"));
    		smsSubArea = smsSubArea.substring(smsSubArea.indexOf("#")+1);
    		CallableStatement cstmt = conn.prepareCall("{? = call fn_sms_create_upd_sr(?,?,?,?,?,?,?,?,?,?,?,?)}");
    		cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
    		cstmt.setString(2,smsUpdType);
    		cstmt.setString(3,smsSRNo);
    		cstmt.setString(4,sessionId);
    		cstmt.setString(5,contNGS);
    		cstmt.setString(6,smsLocation);
    		cstmt.setString(7,contMob);
    		cstmt.setString(8,contEmail);
    		cstmt.setString(9,smsCat);
    		cstmt.setString(10,smsArea);
    		cstmt.setString(11,smsSubArea);
    		cstmt.setString(12,srDesc);
    		cstmt.setString(13,smsStatus);
    		cstmt.executeUpdate();
    		strRetMsg = cstmt.getString(1);
    	}
    	catch(SQLException ex) {
    		ex.printStackTrace();
    	} 
	//	final String srNo = strRetMsg;
    	new Thread(new Runnable() {
		    public void run() {
		    	new Utility();
	//		    Utility.sendEmail(srNo);
		    }
		}).start();
		return strRetMsg;
	}*/
	
	public static String getStringVal(String query)
	{
		System.out.println("inside get String: "  + query);
		String strReturn= " ";
		try
		{
			Connection conn= new Utility().make_connection();
			ResultSet rs=conn.createStatement().executeQuery(query);
			while(rs.next())
			{
				//System.out.println("Inside DO post Resultset");
				strReturn=rs.getString(1);
				//System.out.println(rs.getString(1).substring(0, 2));
			}
			conn.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return strReturn;
	}

	public static synchronized long getUnique()
	{
		long current = System.currentTimeMillis();
	    return current++;
	}
	
/*
	public static ServiceRequest createSR(String sessionId, String SRNo)
	{
		ServiceRequest sr = new ServiceRequest();
		String query = null;
		if (SRNo.matches("0"))
		{
			query = "select 0 as sr_no, a.cat_type, a.area_code, a.sub_area, b.group_id, b.sub_group_id, ' ' as location, b.emp_name as cont_pers, b.mobile_no as cont_mob, b.email_id as cont_email, b.emp_id as cont_ngs, b.desig as cont_desig, b.group_desc, b.sub_group_desc, ' 'as cont_wa_no from (select * from p_sms_cat_area_sub p where p.rowid = (select min(q.rowid) from p_sms_cat_area_sub q where q.is_active = 1)) a, app_vw_emp b, x_sms_login c, m_sms_user d where c.user_id = d.user_id and d.emp_code = b.emp_id and c.session_id = '" + sessionId + "'";			  
		}
		else
		{
			query = "select sr_no, cat_type, area_code, sub_area, group_id, sub_group_id, location, cont_pers, cont_mob, cont_email, cont_ngs, cont_desig, group_desc, sub_group_desc, cont_wa_no from X_SMS_SR_MSTR where sr_no = '" + SRNo + "' and is_active = 1";
		}
		
		try
		{
			System.out.println("inside create sr query- "+query);	
			Connection conn= new Utility().make_connection();
			ResultSet rs=conn.createStatement().executeQuery(query);
			while(rs.next())
			{
				sr.setServiceRequest(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getInt(11), rs.getString(12), rs.getString(13), rs.getString(14), rs.getString(15));
			}
			conn.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return sr;
	}

	public static void sendEmail(String srNo) {
		try
		{
			Connection conn= new Utility().make_connection();
			String query = "insert into X_SMS_NOTIFY (USER_ID, USER_NAME, NOTIFY_ID, INITIATE_TIME, TITLE_TEXT, DESC_TEXT, TARGET_CLASS, WS_URL, RET_JSON) select USER_ID, USER_NAME, NOTIFY_ID, INITIATE_TIME, TITLE_TEXT, DESC_TEXT, TARGET_CLASS, WS_URL, RET_JSON from v_sms_sr_notify where notify_id = " + srNo;
			conn.createStatement().executeUpdate(query);
			query = "select SR_NO, TO_ADDRESS, EMAIL_SUBJECT, TO_NAME, USER_TYPE from V_SMS_EMAIL_MSG where SR_NO = " + srNo;
			ResultSet rs=conn.createStatement().executeQuery(query);
			CallableStatement cstmt = null;
			String emailText = null;
			while(rs.next())
			{
				cstmt = conn.prepareCall("{? = call fn_sms_email_sr(?,?,?)}");
				//(a.sr_no, b.cont_pers, a.user_type)
				cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
	    		cstmt.setString(2,rs.getString(1));
	    		cstmt.setString(3,rs.getString(4));
	    		cstmt.setString(4,rs.getString(5));
	    		
	    		cstmt.executeUpdate();
	    		emailText = cstmt.getString(1);
	    		
	    		cstmt = conn.prepareCall("{? = call fn_sms_get_mail()}");
	    		cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
	    		
	    		cstmt.executeUpdate();
		
		        // sets SMTP server properties
		        Properties properties = new Properties();
		        properties.put("mail.smtp.host", "smtpout.asia.secureserver.net");
		        properties.put("mail.smtp.port", "80");  //or 3535 or 25
		        properties.put("mail.smtp.auth", "true");
		        properties.put("mail.smtp.starttls.enable", "true");
		        
		        //sets User Name and Password
		        final String userName = cstmt.getString(1);
		        final String password = "Dpl@1961";
		 
		        // creates a new session with an authenticator
		        Authenticator auth = new Authenticator() {
		            public PasswordAuthentication getPasswordAuthentication() {
		                return new PasswordAuthentication(userName, password);
		            }
		        };
		 
		        Session session = Session.getInstance(properties, auth);
		 
		        // creates a new e-mail message
		        Message msg = new MimeMessage(session);
		 
		        msg.setFrom(new InternetAddress(userName));
		        InternetAddress[] toAddresses = { new InternetAddress(rs.getString(2)) };
		        msg.setRecipients(Message.RecipientType.TO, toAddresses);
		        msg.setSubject(rs.getString(3));
		        msg.setSentDate(new Date());
		        msg.setContent(emailText, "text/html");
		 
		        // sends the e-mail
		        Transport.send(msg);

		        //Checking
		        System.out.println("Send To: " + rs.getString(2));
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     }


	//Not in use currently for making the call async
	public static void sendEmail(final String userName, String toAddress, String subject, String message) throws AddressException, MessagingException {
        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtpout.asia.secureserver.net");
        properties.put("mail.smtp.port", "80");  //or 3535 or 25
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        
        //sets User Name and Password
        //final String userName = "do-not-reply@dpl.net.in";
        final String password = "Dpl@1961";
 
        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
 
        Session session = Session.getInstance(properties, auth);
 
        // creates a new e-mail message
        Message msg = new MimeMessage(session);
 
        msg.setFrom(new InternetAddress(userName));
        InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        msg.setContent(message, "text/html");
 
        // sends the e-mail
        Transport.send(msg);
     }

	public static String printReport(JSONObject paramJSON, String reportSourceInit, String reportDestInit) {
		//Report Source Variables
		String repoName = paramJSON.getString("report_name");
		String reportSource = null;
		String reportJasper = null;
		List<String> reportName = null;
		
		//Report Variable Initialization 
		Map<String, Object> params = new HashMap<String, Object>();
    	Connection conn=new Utility().make_connection();
        JasperDesign jasperDesign = null;

		//Report Destination File
		long timeStamp = new java.util.Date().getTime();
		String paramVal = paramJSON.getString("output_pdf");
		String reportDestReal = reportDestInit.replaceAll("PayslipReport.pdf", timeStamp + "_" + paramVal); 
		String reportDest = "/allPDFs/" + timeStamp + "_" + paramVal;
		File theDestDir = new File(reportDestInit.replaceAll("PayslipReport.pdf", " ").substring(0, reportDestInit.replaceAll("PayslipReport.pdf", " ").trim().length()-1));
		// if the directory does not exist, create it
		if (!theDestDir.exists()) {
		    System.out.println("creating directory: " + theDestDir.getName());
		    boolean result = false;
		    try{
		        result = theDestDir.mkdir();
		    } 
		    catch(SecurityException se){
		        se.printStackTrace();
		        System.out.println("creating directory Failed: " + se.getMessage());
		    }        
		    if(result) {    
		        System.out.println("DIR created");  
		    }
		}
		//Delete History Files
        File[] allFiles = theDestDir.listFiles();
        for (File repFile : allFiles){
            long fileTime = Long.parseLong(repFile.getName().substring(0, 13));
            if (fileTime < timeStamp-30000) {
            	System.out.println("File to Delete: " + repFile.getName());
            	repFile.delete();
            }
        }
		
		System.out.println("Report Dest Real- "+reportDestReal);
		switch(repoName) {
		case "PAYSLIP":
			reportName = new ArrayList<String>(3); 
			reportName.add("DPL_A4_Payslip_rpt.jrxml");
			reportName.add("emp_deduction.jrxml");
			reportName.add("emp_earning.jrxml");
			params.put("Pcomarker", "1");
			paramVal = paramJSON.getString("user_id");
			params.put("Pngs", paramVal);
			paramVal = paramJSON.getString("param_val");
			params.put("Pmonth", paramVal.substring(0, paramVal.indexOf("-")));
			params.put("Pyear", paramVal.substring(paramVal.indexOf("-")+1));
			params.put("Pdept", "0");
			break;
		case "PAYCERT":
			reportName = new ArrayList<String>(1); 
			reportName.add("PAY_CERTIFICATE_REPORT_PDF.jrxml");
			params.put("Pcomarker", "1");
			paramVal = paramJSON.getString("user_id");
			params.put("Pngs", paramVal);
			paramVal = paramJSON.getString("param_val");
			params.put("Pmonth", paramVal.substring(0, paramVal.indexOf("-")));
			params.put("Pyear", paramVal.substring(paramVal.indexOf("-")+1));
			break;
		case "TAXHIST":
			reportName = new ArrayList<String>(1); 
			reportName.add("itax_detail_hist.jrxml");
			params.put("Pcomarker", "1");
			paramVal = paramJSON.getString("user_id");
			params.put("Pngs", paramVal);
			paramVal = paramJSON.getString("param_val");
			params.put("Pmonth", paramVal.substring(0, paramVal.indexOf("-")));
			params.put("Pyear", paramVal.substring(paramVal.indexOf("-")+1));
		  	break;
		case "PFLED":
			reportName = new ArrayList<String>(1); 
			reportName.add("emp_pf_ledger_PDF.jrxml");
			params.put("Pcomarker", "1");
			paramVal = paramJSON.getString("user_id");
			params.put("Pngs", paramVal);
			paramVal = paramJSON.getString("param_val");
			params.put("Pfromyear", paramVal.substring(0, paramVal.indexOf("-")));
			params.put("Ptoyear", "20"+paramVal.substring(paramVal.indexOf("-")+1));
			params.put("PempType", "0");
		  	break;
		case "SALCERT":
			reportName = new ArrayList<String>(1); 
			reportName.add("SALARY_CERTIFICATE_REPORT_PDF.jrxml");
			params.put("Pcomarker", "1");
			paramVal = paramJSON.getString("user_id");
			params.put("Pngs", paramVal);
			paramVal = paramJSON.getString("param_val");
			params.put("Pfromyear", paramVal.substring(0, paramVal.indexOf("-")));
			params.put("Ptoyear", paramVal.substring(paramVal.indexOf("-")+1));
		  	break;
		case "TAXSAL":
			reportName = new ArrayList<String>(1); 
			reportName.add("Taxable_Salary_Display_PDF.jrxml");
			paramVal = paramJSON.getString("user_id");
			params.put("Pngs", paramVal);
			paramVal = paramJSON.getString("param_val");
			params.put("Ptaxyear", paramVal.substring(0, paramVal.indexOf("-")));
		  	break;
		case "TAXDISP":
			reportName = new ArrayList<String>(1); 
			reportName.add("itax_detail.jrxml");
			params.put("Pcomarker", "1");
			paramVal = paramJSON.getString("user_id");
			params.put("Pngs", paramVal);
			paramVal = paramJSON.getString("param_val");
			params.put("Pyear", paramVal.substring(0, paramVal.indexOf("-")));
		  	break;
		case "ROPA":
			reportName = new ArrayList<String>(1); 
			reportName.add("rpt_ropa2019_pdf.jrxml");
			reportName.add("subrpt_ropa2019_flag.jrxml");
			paramVal = paramJSON.getString("user_id");
			params.put("Pngs", paramVal);
		  	break;
		case "ITDECS":
			reportName = new ArrayList<String>(1); 
			reportName.add("ITDecSupplForm_PDF.jrxml");
			params.put("Pcomarker", "1");
			paramVal = paramJSON.getString("user_id");
			params.put("Pngs", paramVal);
		  	break;
		case "ITDEC":
			reportName = new ArrayList<String>(1); 
			reportName.add("ITDecBlankForm_PDF.jrxml");
			params.put("Pcomarker", "1");
			paramVal = paramJSON.getString("user_id");
			params.put("Pngs", paramVal);
		  	break;
	  	default:
	  		break;
		}
		switch(repoName) {
		case "FORM16":
			paramVal = paramJSON.getString("param_val");
			reportSource = "/DPL_PAYROLL/Traces/EmployeeWise/" + paramVal.substring(0, paramVal.indexOf("-")) + "/Form16_" + paramVal.substring(0, paramVal.indexOf("-")) + "_" + paramJSON.getString("user_id") + ".pdf";
			try {
				FileSystemManager manager = VFS.getManager();
				FileObject local = manager.resolveFile(reportDestReal);
			    //System.getProperty("user.dir") + "/" + localDir + "vfsFile.txt");
				FileObject remote = manager.resolveFile("sftp://root:Redhat@192.168.0.11" + reportSource);
				local.copyFrom(remote, Selectors.SELECT_SELF);
			    local.close();
			    remote.close();
			} catch (FileSystemException e1) {
				e1.printStackTrace();
				System.out.println("SFTP Error: " + e1.getMessage());
			}
			break;
		default:
			try {
				for (int i = 0; i < reportName.size(); i++) {
					reportSource =  reportSourceInit.replaceAll("DPL_A4_Payslip_rpt.jrxml", reportName.get(i));
					reportJasper = reportSource.replaceAll("jrxml", "jasper");
					File reportJasperFile = new File(reportJasper);
		            if (!reportJasperFile.exists()) {
						jasperDesign = JRXmlLoader.load(reportSource);
						JasperCompileManager.compileReportToFile(jasperDesign, reportJasper);
						System.out.println("Report Jasper Real in Loop No "+ i + " Name " + reportJasper);
		            }
				}
				reportJasper = reportSourceInit.replaceAll("DPL_A4_Payslip_rpt.jrxml", reportName.get(0)).replaceAll("jrxml", "jasper");
				System.out.println("Report Jasper Real of Main " + reportJasper);
	        	JasperPrint jasperPrint = JasperFillManager.fillReport(reportJasper, params, conn);
	        	System.out.println("Report Jasper to export to PDF");
	            JRPdfExporter exporter = new JRPdfExporter();
	            System.out.println("Report Jasper JRPdfExporter instance created");
	            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);                
	            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, reportDestReal);
	            System.out.println("Report Jasper JRPdfExporter parameters set");
	            exporter.exportReport();
	            System.out.println("Report Jasper exported to PDF");
	       	}
	        catch(JRException e)
	        {
	            e.printStackTrace();
	            System.out.println("Report Jasper Error.. ");
	            System.out.println("Report Jasper Error " + e.getMessage());
	            return "0";
	        }
	        catch(Exception ex)
	        {
	            ex.printStackTrace();
	            System.out.println("Report Jasper Error.. ");
	            System.out.println("Report Jasper Error " + ex.getMessage());
	            return "0";
	        }
		}
    	return reportDest;
	}
	
	public static JSONArray getArrayParam(String sqlQuery) {
		JSONArray arrayParam = new JSONArray();
		Connection conn=new Utility().make_connection();
    	try {
    		ResultSet rs=conn.createStatement().executeQuery(sqlQuery);
    		while(rs.next()) {
    			arrayParam.put(rs.getString(1));
    		}
    	}
    	catch(SQLException ex) {
    		ex.printStackTrace();
    	}
		return arrayParam;
	}
*/
	public static String doLogout(String sessionId) {
		String retMsg = "Y-Server Error";
		Connection conn=new Utility().make_connection();
		int n = 0;
		String query = "update x_sms_login set logout_dt_tm = sysdate where session_id = '"+sessionId+"'";
    	try {
    		n=conn.createStatement().executeUpdate(query);
    		conn.close();
    	} 
    	catch(SQLException ex) {
    		ex.printStackTrace();
    	}
    	if (n>0) {
    		retMsg = "N-Logout Successfully"; 
    	}
		return retMsg;
	}
/*
	public static JSONObject getNotify(String module, String userid, String passwd, String requestTime, String keyVal) {
		JSONObject retJsonObj = new JSONObject();
    	Connection conn=new Utility().make_connection();
    	String retFlag = "N";
    	try
    	{
    		CallableStatement cstmt = conn.prepareCall("{? = call fn_sms_notify(?,?,?,?)}");
    		cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
    		cstmt.setString(2,module);
    		cstmt.setString(3,userid);
    		cstmt.setString(4,passwd);
    		cstmt.setString(5,requestTime);
    		cstmt.executeUpdate();
    		retFlag = cstmt.getString(1);
    		if (retFlag.matches("Y")) {
    			retJsonObj.put("is_error", "N");
    			retJsonObj.put("msg", "Data populated successfully");
    			JSONArray arrayParam = new JSONArray();
    			JSONObject arrayObject;
    			String sqlQuery ="select ret_json from X_SMS_NOTIFY where (USER_ID, notify_id, initiate_time) IN (select user_id, notify_id, MAX(initiate_time) from X_SMS_NOTIFY where user_id = '" + userid  + "' and request_id = '" + requestTime  + "' group by user_id, notify_id)";
    			//System.out.println(sqlQuery);
    			ResultSet rs=conn.createStatement().executeQuery(sqlQuery);
        		while(rs.next()) {
        			//System.out.println(rs.getNString(1));
        			arrayObject = new JSONObject(rs.getString(1));
        			arrayObject.put("key_val", keyVal);
        			arrayObject.put("module", module);
        			arrayParam.put(arrayObject);
        		}
        		retJsonObj.put("param_array", arrayParam);
    		} else {
    			retJsonObj.put("is_error", "Y");
				retJsonObj.put("msg", "Invalid Credential/ No Notice");
				retJsonObj.put("name", "Enter valid User ID and Password");
				retJsonObj.put("user_id","0");
				retJsonObj.put("module",module);
				retJsonObj.put("session_id","0");
				retJsonObj.put("key_val", keyVal);
    		}
    	} 
    	catch(SQLException ex)
    	{
    		ex.printStackTrace();
			retJsonObj.put("is_error", "Y");
			retJsonObj.put("msg", "Database Error");
			retJsonObj.put("name", "Retry after sometime");
			retJsonObj.put("user_id","0");
			retJsonObj.put("module",module);
			retJsonObj.put("session_id","0");
			retJsonObj.put("key_val", keyVal);
    	}
		return retJsonObj;
	}
*/

	public static String getJSONArray(String sqlQuery, JSONArray paramVal) {
		System.out.println("inside: " + sqlQuery + " Parama:: " + paramVal.toString());
		JSONArray arrayParam = new JSONArray();
		Connection conn=new Utility().make_connection();
    	try {
    		ResultSet rs;
    		if (paramVal.length()>0) {
	    		PreparedStatement statement =conn.prepareStatement(sqlQuery);
	    		for(int iLoop= 0; iLoop < paramVal.length(); iLoop++) {
	    			statement.setString(iLoop + 1, paramVal.getString(iLoop));
	    		}
	    		rs = statement.executeQuery();
    		} else
    			rs=conn.createStatement().executeQuery(sqlQuery);
    		ResultSetMetaData rsmd = rs.getMetaData();
		/*
		 * try { PreparedStatement statement =conn.prepareStatement(sqlQuery); for(int
		 * iLoop= 0; iLoop < paramVal.length(); iLoop++) { statement.setString(iLoop +
		 * 1, paramVal.getString(iLoop)); } ResultSet rs = statement.executeQuery();
		 * ResultSetMetaData rsmd = rs.getMetaData();
		 */
    		int iColNos = rsmd.getColumnCount();
    		int iLoop = 0;
    		JSONObject fieldJson;
    		while(rs.next()) {
    			fieldJson = new JSONObject ();
				for (iLoop = 0; iLoop < iColNos; iLoop++) {
    				fieldJson.put(rsmd.getColumnName(iLoop+1).toLowerCase(), rs.getString(iLoop+1));
    			}
/*				if (fieldJson.has("field_param")) {
					fieldJson.put("field_param", (new JSONObject(fieldJson.getString("field_param"))));
				}*/
    			arrayParam.put(fieldJson);
    		}
    		conn.close();
    	}
    	catch(SQLException | JSONException ex) {
    		ex.printStackTrace();
    	}
    	System.out.println("Json Array: " + arrayParam.toString());
		return arrayParam.toString();
	}

	public static String updateRecord(String callStatement, JSONArray paramVal, String sqlType, String urlReturn) {
		String strRetUrl = "Login";
		System.out.println("Call Statement: " + callStatement);
		Connection conn=new Utility().make_connection();
    	try
    	{
    		ResultSet rs;
    		PreparedStatement statement;
    		int iUpdNos;
    		CallableStatement cstmt;
    		switch(sqlType) {
    			case "F":	//Function with parameter
		    		cstmt = conn.prepareCall(callStatement);
		    		cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
		    		for(int iLoop= 0; iLoop < paramVal.length(); iLoop++) {
		    			cstmt.setString(iLoop + 2, paramVal.getString(iLoop));
		    		}
		    		cstmt.executeUpdate();
		    		strRetUrl = cstmt.getString(1);
		    		break;
    			case "S":	//Query with parameter
    	    		statement =conn.prepareStatement(callStatement);
    	    		for(int iLoop= 0; iLoop < paramVal.length(); iLoop++) {
    	    			statement.setString(iLoop + 1, paramVal.getString(iLoop));
    	    		}
    	    		rs = statement.executeQuery();
		    		if(rs.next())
		    			strRetUrl = rs.getString(1);
    	    		break;
    			case "N":	//Plain Query without parameter
    				rs=conn.createStatement().executeQuery(callStatement);
		    		if(rs.next())
		    			strRetUrl = rs.getString(1);
    				break;
    			case "E":	//Insert or Update Query with parameter
    				statement=conn.prepareStatement(callStatement);  
    				for(int iLoop= 0; iLoop < paramVal.length(); iLoop++) {
    	    			statement.setString(iLoop + 1, paramVal.getString(iLoop));
    	    		}  
    				iUpdNos=statement.executeUpdate();
    				if (iUpdNos>0)
    					strRetUrl = urlReturn;
    				break;
    			case "P":	//Plain Insert or Update Query without parameter
    				iUpdNos=conn.createStatement().executeUpdate(callStatement);
    				if (iUpdNos>0)
    					strRetUrl = urlReturn;
    				break;
    		}
	    	conn.close();
	    	
/*	    		Enumeration paramNames = request.getParameterNames();
    		JSONObject allParams = new JSONObject();
    	    while(paramNames.hasMoreElements()) {
    	      String paramName = (String)paramNames.nextElement();
    	      String paramValue = request.getParameter(paramName);
    	      allParams.put(paramName, paramValue);
    	    }
    	    System.out.println("Params:: " + allParams.toString());*/

    	}
    	catch(SQLException ex) {
    		ex.printStackTrace();
    	} 
		return strRetUrl;
	}
}