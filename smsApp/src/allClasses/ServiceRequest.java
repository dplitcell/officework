package allClasses;

public class ServiceRequest 
{
	public int srNo; 
	public String catType; 
	public String areaCode; 
	public int subArea; 
	public int groupId; 
	public int subGroupId; 
	public String location; 
	public String contPers; 
	public String contMob; 
	public String contEmail; 
	public int contNgs; 
	public String contDesig; 
	public String groupDesc; 
	public String subGroupDesc; 
	public String contWANo;
	
	public ServiceRequest() {
		this.srNo=0 ; 
		this.catType= null ; 
		this.areaCode= null ; 
		this.subArea=0 ; 
		this.groupId=0 ; 
		this.subGroupId=0 ; 
		this.location= null ; 
		this.contPers= null ; 
		this.contMob= null ; 
		this.contEmail= null ; 
		this.contNgs=0 ; 
		this.contDesig= null ; 
		this.groupDesc= null ; 
		this.subGroupDesc= null ; 
		this.contWANo= null ;
	}
	
	public ServiceRequest(ServiceRequest sr) {
		this.srNo= sr.srNo ; 
		this.catType= sr.catType ; 
		this.areaCode= sr.areaCode ; 
		this.subArea= sr.subArea; 
		this.groupId= sr.groupId ; 
		this.subGroupId= sr.subGroupId ; 
		this.location= sr.location ; 
		this.contPers= sr.contPers ; 
		this.contMob= sr.contMob ; 
		this.contEmail= sr.contEmail ; 
		this.contNgs= sr.contNgs ; 
		this.contDesig= sr.contDesig ; 
		this.groupDesc= sr.groupDesc ; 
		this.subGroupDesc= sr.subGroupDesc ; 
		this.contWANo= sr.contWANo ;
	}
	
	public void setServiceRequest(ServiceRequest sr) {
		this.srNo= sr.srNo ; 
		this.catType= sr.catType ; 
		this.areaCode= sr.areaCode ; 
		this.subArea= sr.subArea; 
		this.groupId= sr.groupId ; 
		this.subGroupId= sr.subGroupId ; 
		this.location= sr.location ; 
		this.contPers= sr.contPers ; 
		this.contMob= sr.contMob ; 
		this.contEmail= sr.contEmail ; 
		this.contNgs= sr.contNgs ; 
		this.contDesig= sr.contDesig ; 
		this.groupDesc= sr.groupDesc ; 
		this.subGroupDesc= sr.subGroupDesc ; 
		this.contWANo= sr.contWANo ;
	}
	
	public void setServiceRequest(int srNo,String catType,String areaCode,int subArea,int groupId,int subGroupId,String location,String contPers,String contMob,String contEmail,int contNgs,String contDesig,String groupDesc,String subGroupDesc,String contWANo) {
		this.srNo= srNo ; 
		this.catType= catType ; 
		this.areaCode= areaCode ; 
		this.subArea= subArea; 
		this.groupId= groupId ; 
		this.subGroupId= subGroupId ; 
		this.location= location ; 
		this.contPers= contPers ; 
		this.contMob= contMob ; 
		this.contEmail= contEmail ; 
		this.contNgs= contNgs ; 
		this.contDesig= contDesig ; 
		this.groupDesc= groupDesc ; 
		this.subGroupDesc= subGroupDesc ; 
		this.contWANo= contWANo ;
	}
	
	public int getsrNo() { 
		return this.srNo ;
	} 
	
	public String getcatType() { 
		return this.catType ;
	} 
	
	public String getareaCode() { 
		return this.areaCode ;
	} 
	
	public int getsubArea() { 
		return this.subArea ;
	} 
	
	public int getgroupId() { 
		return this.groupId ;
	} 
	
	public int getsubGroupId() { 
		return this.subGroupId ;
	} 
	
	public String getlocation() { 
		return this.location ;
	} 
	
	public String getcontPers() { 
		return this.contPers ;
	} 
	
	public String getcontMob() { 
		return this.contMob ;
	} 
	
	public String getcontEmail() { 
		return this.contEmail ;
	} 
	
	public int getcontNgs() { 
		return this.contNgs ;
	} 
	
	public String getcontDesig() { 
		return this.contDesig ;
	} 
	
	public String getgroupDesc() { 
		return this.groupDesc ;
	} 
	
	public String getsubGroupDesc() { 
		return this.subGroupDesc ;
	} 
	
	public String getcontWANo() { 
		return this.contWANo ;
	}
}
