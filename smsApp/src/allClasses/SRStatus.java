package allClasses;

public class SRStatus {
	private String serialNo;
	private String srDesc;
	private String srRem;
	private String srAction;
	private String srNoURL;
	private String srActionURL;
	
	public SRStatus() 
	{
		this.serialNo = null;
	    this.srDesc = null;
	    this.srRem = null;
		this.srAction = null;
	    this.srNoURL = null;
	    this.srActionURL = null;
	}
		
	public void setSRStatus(String serialNo, String srDesc, String srRem, String srAction, String srNoURL, String srActionURL) 
	{
		this.serialNo = serialNo;
	    this.srDesc = srDesc;
	    this.srRem = srRem;
		this.srAction = srAction;
	    this.srNoURL = srNoURL;
	    this.srActionURL = srActionURL;
	}
	
	public String getserialNo() 
	{
		return this.serialNo;
	}
	
	public String getsrDesc() 
	{
		return this.srDesc;
	}
	
	public String getsrRem() 
	{
		return this.srRem;
	}

	public String getsrAction() 
	{
		return this.srAction;
	}
	
	public String getsrNoURL() 
	{
		return this.srNoURL;
	}
	
	public String getsrActionURL() 
	{
		return this.srActionURL;
	}

}
