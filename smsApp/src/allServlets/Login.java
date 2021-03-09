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

import allClasses.Utility;
import allClasses.Menu;

@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Login() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getSession().getAttribute("unique")==null){
			String query="select concat(concat(module_id,'-'),description) from m_sms_module where is_active = 1";
			new Utility();
			List<String> listModule= Utility.getStringList(query);
			request.getSession().setAttribute("listModule", listModule);
			request.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(request, response);
		} else {
			request.getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		if(request.getSession().getAttribute("unique")==null) {
			String userId=request.getParameter("user_id");
			String passwd=request.getParameter("pass");
			String module = request.getParameter("listModule");
			//new Utility();
			String validUser = (String) Utility.verifyLogin(module, userId, passwd);
			System.out.println("At Login.java : module- " + module + ", userid- " +userId + ", pass- " + passwd + ", validUser- " + validUser);
			if (validUser.matches("Y")) {
				String sessionId=String.valueOf(Utility.getUnique());
				String usernm = Utility.updLoginInfo(userId, sessionId, module);
				String query="select description from m_sms_module where module_id = '" + module + "'";
				String modulenm = Utility.getStringVal(query);
				List<Menu> menuList= Utility.GenerateMenu(sessionId, module);
				query= "SELECT menu_shrt FROM v_sms_menu WHERE session_id = '"+sessionId+"' AND module_id = '" + module + "' union SELECT to_char(menu_id) FROM v_sms_menu WHERE session_id = '"+sessionId+"' AND module_id = '" + module + "'";
				ArrayList<String> listSelMenu= (ArrayList<String>) Utility.getStringList(query);
				HttpSession session=request.getSession(false);
				session.setAttribute("unique", sessionId);
				session.setAttribute("menuList", menuList);
				session.setAttribute("module", module);
				session.setAttribute("modulename", modulenm);
				session.setAttribute("username", usernm);
				session.setAttribute("userid", userId);
				session.setAttribute("listSelMenu", listSelMenu);
				request.getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(request, response);
			}
			else {
				request.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(request, response);
			}
		}
		else {
			request.getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(request, response);
		}
	}

}
