package allClasses;

public class Menu 
{
	private int menuId;
	private String menuDesc;
	private String menuShrt;
	private String menuURL;
	
	public Menu() 
	{
		this.menuId = 0;
	    this.menuDesc = null;
	    this.menuShrt = null;
	    this.menuURL = null;
	}
		
	public void setMenu(int menuId, String menuDesc, String menuShrt, String menuURL) 
	{
		this.menuId = menuId;
		this.menuDesc = menuDesc;
		this.menuShrt = menuShrt;
		this.menuURL = menuURL;
	}
	
	public int getMenuID() 
	{
		return this.menuId;	    
	}
	
	public String getMenuDesc() 
	{
		return this.menuDesc;
	}
	
	public String getMenuShrt() 
	{
		return this.menuShrt;
	}
	
	public String getMenuURL() 
	{
		return this.menuURL;
	}
}
