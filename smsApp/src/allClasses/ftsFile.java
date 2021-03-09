package allClasses;


// Comment to test github on 09032021

public class ftsFile {
	private String fileNo;
	private String fileDesc;
	private String fileCust;
	private String fileUrl;
	public ftsFile() {
		this.fileNo =  null;
		this.fileDesc =  null;
		this.fileCust =  null;
	    this.fileUrl =  null;
	}
	public void setFile(String fileNo, String fileDesc, String fileCust, String fileUrl) {
		this.fileNo = fileNo;
		this.fileDesc = fileDesc;
		this.fileCust = fileCust;
		this.fileUrl = fileUrl;
	}
	public String getFileNo() {
		return this.fileNo;	    
	}
	public String getFileDesc() {
		return this.fileDesc;
	}
	public String getFileCust() {
		return this.fileCust;
	}
	public String getFileUrl() {
		return this.fileUrl;
	}
}
