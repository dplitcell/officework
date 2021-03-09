package allServlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import allClasses.Utility;

@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Login() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sessionId = String.valueOf(request.getSession().getAttribute("unique"));
		if(sessionId!=null) {
			HttpSession session=request.getSession(false);
			session.removeAttribute("module");
			session.removeAttribute("unique");
			session.removeAttribute("username");
			session.removeAttribute("modulename");
			session.removeAttribute("menuList");
			session.removeAttribute("home");
			Utility.doLogout(sessionId);
			request.getSession().invalidate();
		}
		if(request.getSession().getAttribute("unique")==null){
			String query="select concat(concat(module_id,'-'),description) from m_app_module where is_active = 1";
			List<String> listModule= Utility.getStringList(query);
			request.getSession().setAttribute("listModule", listModule);
			request.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(request, response);
		} 
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		if(request.getSession().getAttribute("unique")==null) {
			String userId=request.getParameter("user_id");
			if (userId == null) {
				System.out.println("User ID is Null");
				//response.sendRedirect("Login");
				request.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(request, response);
			} else
				System.out.println("User ID is not Null: " +userId);
			String passwd=request.getParameter("pass");
			String module = request.getParameter("listModule");
			String validUser = (String) Utility.verifyLogin(module, userId, passwd);
			System.out.println("At Login.java : module- " + module + ", userid- " +userId + ", pass- " + passwd + ", validUser- " + validUser);
			if (validUser.matches("Y")) {
				HttpSession session=request.getSession(false);
				session.setAttribute("module", module);
				
				String sessionId=String.valueOf(Utility.getUnique());
				session.setAttribute("unique", sessionId);
				
				String usernm = Utility.updLoginInfo(userId, sessionId, module);
				session.setAttribute("username", usernm);
				
				String query="select description from m_app_module where module_id = '" + module + "'";
				String modulenm = Utility.getStringVal(query);
				session.setAttribute("modulename", modulenm);
				
				query= "SELECT UPPER(menu_shrt) as menuId FROM v_app_menu WHERE session_id = '"+sessionId+"' AND module_id = '" + module + "' union SELECT to_char(menu_id) as menuId FROM v_app_menu WHERE session_id = '"+sessionId+"' AND module_id = '" + module + "'";
				ArrayList<String> menuList= (ArrayList<String>) Utility.getStringList(query);
				session.setAttribute("menuList", menuList);

				query= "SELECT menu_url FROM m_app_menu WHERE module_id = '" + module + "' AND is_active = 1 AND parent_id = 0";
				String homeUrl = Utility.getStringVal(query);
				session.setAttribute("home", homeUrl);
				
				response.sendRedirect(homeUrl);
				
/*				List<Menu> menuList= Utility.GenerateMenu(sessionId, module);
				query= "SELECT menu_shrt FROM v_app_menu WHERE session_id = '"+sessionId+"' AND module_id = '" + module + "' union SELECT to_char(menu_id) FROM v_app_menu WHERE session_id = '"+sessionId+"' AND module_id = '" + module + "'";
				ArrayList<String> listSelMenu= (ArrayList<String>) Utility.getStringList(query);
				session.setAttribute("menuList", menuList);
				session.setAttribute("listSelMenu", listSelMenu);
				request.getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(request, response);*/
			} else {
				request.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(request, response);
			}
		}
		else {
			int iNoofParam = Integer.valueOf(request.getParameter("totParam"));
			int iLoop= 0;
			String sqlQuery = request.getParameter("query");
			JSONArray paramArray = new JSONArray();
			for(iLoop= 0; iLoop < iNoofParam; iLoop++) {
				paramArray.put(request.getParameter(Integer.toString(iLoop)));
			}
			
			/*String sqlQuery = "select onblur_fn from p_app_menu_field where menu_id= " + request.getParameter("menu") + " and upper(field_name) = upper('" + request.getParameter("fld") + "')";
			JSONObject jsonParam =new JSONObject(Utility.getStringVal(sqlQuery));
			sqlQuery = jsonParam.getString("query");
			JSONArray inputArray = jsonParam.getJSONArray("input_param");
			JSONArray paramArray = new JSONArray();
			String paramVal;
			for(int iLoop= 0; iLoop < inputArray.length(); iLoop++) {
				paramVal = inputArray.getString(iLoop).substring(2);
				System.out.println("Loop No: " + String.valueOf(iLoop) + " :: " + paramVal + " = " + request.getParameter("module_id") + " == " + request.getParameter("MODULE_ID"));
				paramVal = request.getParameter(inputArray.getString(iLoop).substring(2));
				if (paramVal == null)
					paramArray.put("0");
				else
					paramArray.put(paramVal);
			}*/
			System.out.println("Ajax Param: " + paramArray.toString());
			String output = (new JSONArray(Utility.getJSONArray(sqlQuery,paramArray))).getJSONObject(0).toString();
			System.out.println("Ajax Out: " + output);
			response.setContentType("text/plain");
			response.getWriter().write(output);
			//response.setContentType("application/json");
			//PrintWriter out = response.getWriter();
			//out.print(output);
			//out.flush();
			//response.getWriter()
			//.print((new JSONArray(Utility.getJSONArray(sqlQuery,paramArray))).getJSONObject(0));
			//request.getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(request, response);
			//Ajax Call
/*
			String empCode = request.getParameter("empNo");
			String query = "select emp_name||'@'||desig||'#'||group_desc||'$'||sub_group_desc||'^'||mobile_no||':'||email_id from app_vw_emp where emp_id = " + empCode + " union select ' @ # $ ^ : ' from dual where " + empCode + " not in (select emp_id from app_vw_emp)";
			System.out.println("inside: the empInfo: query--  " + query);
			response.setContentType("text/plain");
			response.getWriter().write(Utility.getStringVal(query));
*/
		}
	}

}
