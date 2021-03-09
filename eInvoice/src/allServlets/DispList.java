package allServlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import allClasses.Utility;

@WebServlet("/DispList")
public class DispList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DispList() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sessionId = (String) request.getSession().getAttribute("unique");
		if(sessionId==null)	{
			response.sendRedirect("Login");
		} 
		else {
			if (request.getSession().getAttribute("heading") != null)
				request.getSession().removeAttribute("heading");
			if (request.getSession().getAttribute("display") != null)
				request.getSession().removeAttribute("display");
			if (request.getSession().getAttribute("fields") != null)
				request.getSession().removeAttribute("fields");
			if (request.getSession().getAttribute("initials") != null)
				request.getSession().removeAttribute("initials");
			
			String appMenuId = request.getParameter("MenuId");
			System.out.println("Menu ID: " + appMenuId);
			String sqlQuery = "select * from m_app_menu where menu_id = " + appMenuId;
			JSONArray paramVal = new JSONArray();
			
			String menuArray = Utility.getJSONArray(sqlQuery, paramVal);
			request.getSession().setAttribute("menu", menuArray);
			String listArray = null;
			JSONObject menuJson = (new JSONArray(menuArray)).getJSONObject(0);
			request.getSession().setAttribute("heading", menuJson.getString("menu_desc"));
    		if (menuJson.has("list_query")) {
    			String loadQuery = menuJson.getString("list_query");
    			//String sqlType="N";
    			if (menuJson.has("list_param")) {
    				JSONArray paramArray = new JSONArray(menuJson.getString("list_param"));
    				String paramKey;
    				for(int iLoop= 0; iLoop < paramArray.length(); iLoop++) {
////    					paramVal.put(request.getParameter(paramArray.getString(iLoop)));
		    			paramKey = paramArray.getString(iLoop);
		    			switch(paramKey.substring(0,1)) {
			    			case "P":
			    				if (request.getParameter(paramKey.substring(2))!=null)
			    					paramVal.put(request.getParameter(paramKey.substring(2)));
			    				else
			    					paramVal.put("0");
			    				break;
			    			case "A":
			    				if (request.getSession().getAttribute(paramKey.substring(2)) != null)
			    					paramVal.put(request.getSession().getAttribute(paramKey.substring(2)));
			    				else
			    					paramVal.put("0");
			    				break;
			    			case "V":
			    				paramVal.put(paramKey.substring(2));
			    				break;
			    			case "Q":
			    				//sqlType = paramKey.substring(2);
			    				break;
		    			}
    	    		}
    			}
    			listArray = Utility.getDispJArray(loadQuery,paramVal);
				request.getSession().setAttribute("display", listArray);
    		} 
    		sqlQuery = "select * from p_app_menu_field where is_active = 1 and menu_id = ? order by field_no desc";
    		paramVal = new JSONArray();
    		paramVal.put(appMenuId);
    		String fieldArray = Utility.getJSONArray(sqlQuery,paramVal);
    		JSONArray fieldJArray = new JSONArray(fieldArray);
    		JSONArray fieldRevArray = new JSONArray();
    		JSONObject fieldJSON;
    		int iLoop;
    		int iLoopParam= 0;
    		for(iLoop= 0; iLoop < fieldJArray.length(); iLoop++) {
    			fieldJSON = fieldJArray.getJSONObject(iLoop);
    			if (fieldJSON.getString("obj_type").matches("S")) {
    				sqlQuery = fieldJSON.getString("obj_query");
    	    		paramVal = new JSONArray();
    	    		JSONArray objPArray;
    				if(fieldJSON.has("obj_param")) {
    					String paramKey;
    					objPArray = new JSONArray(fieldJSON.getString("obj_param"));
        				for(iLoopParam= 0; iLoopParam < objPArray.length(); iLoopParam++) {
    		    			paramKey = objPArray.getString(iLoopParam);
    		    			switch(paramKey.substring(0,1)) {
    		    				case "P":
				    				if (request.getParameter(paramKey.substring(2))!=null)
				    					paramVal.put(request.getParameter(paramKey.substring(2)));
				    				else
				    					paramVal.put("0");
				    				break;
				    			case "A":
				    				if (request.getSession().getAttribute(paramKey.substring(2)) != null)
				    					paramVal.put(request.getSession().getAttribute(paramKey.substring(2)));
				    				else
				    					paramVal.put("0");
				    				break;
    			    			/*case "P":
    			    				paramVal.put(request.getParameter(paramKey.substring(2)));
    			    				break;
    			    			case "A":
    			    				paramVal.put(request.getSession().getAttribute(paramKey.substring(2)));
    			    				break;*/
    			    			case "V":
    			    				paramVal.put(paramKey.substring(2));
    			    				break;
    			    			case "Q":
    			    				//sqlType = paramKey.substring(2);
    			    				break;
    		    			}
        				}
    				}
    				fieldJSON.put("field_options", new JSONArray(Utility.getDispJArray(sqlQuery,paramVal)));
    				System.out.println("Rev Field Json: "+fieldJSON.toString());
    			}
    			fieldRevArray.put(fieldJSON);
    		}
    		System.out.println("Field Session Val: "+fieldRevArray.toString());
    		request.getSession().setAttribute("fields", fieldRevArray);
			
			String loadArray = null;
    		if (menuJson.has("load_query")) {
    			String loadQuery = menuJson.getString("load_query");
				paramVal = new JSONArray();
    			if (menuJson.has("load_param")) {
    				JSONArray paramArray = new JSONArray(menuJson.getString("load_param"));
    				System.out.println("Load Param: "+paramArray.toString());
    				String paramKey;
    				for(iLoop= 0; iLoop < paramArray.length(); iLoop++) {
////    					paramVal.put(request.getParameter(paramArray.getString(iLoop)));
		    			paramKey = paramArray.getString(iLoop);
		    			switch(paramKey.substring(0,1)) {
			    			case "P":
			    				if (request.getParameter(paramKey.substring(2))!=null)
			    					paramVal.put(request.getParameter(paramKey.substring(2)));
			    				else
			    					paramVal.put("0");
			    				break;
			    			case "A":
			    				if (request.getSession().getAttribute(paramKey.substring(2)) != null)
			    					paramVal.put(request.getSession().getAttribute(paramKey.substring(2)));
			    				else
			    					paramVal.put("0");
			    				break;
		    				/*case "P":
			    				paramVal.put(request.getParameter(paramKey.substring(2)));
			    				break;
			    			case "A":
			    				paramVal.put(request.getSession().getAttribute(paramKey.substring(2)));
			    				break;*/
			    			case "V":
			    				paramVal.put(paramKey.substring(2));
			    				break;
			    			case "Q":
			    				//sqlType = paramKey.substring(2);
			    				break;
		    			}
    	    		}
    			}
				loadArray = Utility.getJSONArray(loadQuery,paramVal);
				request.getSession().setAttribute("initials", loadArray);
    		}
    		request.getRequestDispatcher("/WEB-INF/jsp/DispList.jsp").forward(request, response);
    	}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sessionId = (String) request.getSession().getAttribute("unique");
		if(sessionId==null)	{
			response.sendRedirect("Login");
		} 
		else {
			String strRetUrl = "Login";
    		String menuArray = (String) request.getSession().getAttribute("menu");
    		JSONObject menuJson = (new JSONArray(menuArray)).getJSONObject(0);
    		if (menuJson.has("submit_query")) {
	    		String callStatement = menuJson.getString("submit_query");
	    		JSONArray paramVal = new JSONArray();
	    		String sqlType="N";
	    		if (menuJson.has("submit_param")) {
		    		JSONArray callParams = new JSONArray(menuJson.getString("submit_param"));
		    		System.out.println(callParams.toString());
		    		String paramKey;
		    		for(int iLoop= 0; iLoop < callParams.length(); iLoop++) {
		    			paramKey = callParams.getString(iLoop);
		    			switch(paramKey.substring(0,1)) {
			    			case "P":
			    				if (request.getParameter(paramKey.substring(2).toLowerCase())!=null)
			    					paramVal.put(request.getParameter(paramKey.substring(2).toLowerCase()));
			    				else
			    					paramVal.put("0");
			    				break;
			    			case "A":
			    				if (request.getSession().getAttribute(paramKey.substring(2)) != null)
			    					paramVal.put(request.getSession().getAttribute(paramKey.substring(2)));
			    				else
			    					paramVal.put("0");
			    				break;
			    			case "V":
			    				paramVal.put(paramKey.substring(2));
			    				break;
			    			case "Q":
			    				sqlType = paramKey.substring(2);
			    				break;
			    			case "R":
			    				strRetUrl = paramKey.substring(2);
			    				break;
		    			}
		    		}
		    		System.out.println(paramVal.toString());
	    		}
	    		else 
	    			System.out.println(menuJson.toString());
	    		strRetUrl= Utility.updateRecord(callStatement,paramVal,sqlType, strRetUrl);
				response.sendRedirect(strRetUrl);
    		} else 
    			response.sendRedirect("Login");
		}
	}
}
