package Server;


import java.net.InetAddress;
import java.util.Arrays;

public class Configurations {

	public Configurations() {
		
	}
	public String getRootPath() {
		return rootPath;
	}
	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}
	public String[] getExtensions() {
		return extensions;
	}
	public void setExtensions(String[] extensions) {
		this.extensions = extensions;
	}
	public int getNumberOfFiles() {
		return numberOfFiles;
	}
	public void setNumberOfFiles(int numberOfFiles) {
		this.numberOfFiles = numberOfFiles;
	}
	public Long getMaximumSize() {
		return maximumSize;
	}
	public void setMaximumSize(Long maximumSize) {
		this.maximumSize = maximumSize;
	}
	public Long getMinId() {
		return minId;
	}
	public void setMinId(Long minId) {
		this.minId = minId;
	}
	public Long getMaxId() {
		return maxId;
	}
	public void setMaxId(Long maxId) {
		this.maxId = maxId;
	}
	public boolean isFolderAllowed() {
		return folderAllowed;
	}
	public void setFolderAllowed(boolean folderAllowed) {
		this.folderAllowed = folderAllowed;
	}
	String rootPath;
	String[] extensions;
	int numberOfFiles;
	Long maximumSize,minId,maxId;
	boolean folderAllowed;
	public Configurations(String rootPath, String[] extensions,
			int numberOfFiles, Long maximumSize, Long minId, Long maxId,
			boolean folderAllowed) {
		super();
		this.rootPath = rootPath;
		this.extensions = extensions;
		this.numberOfFiles = numberOfFiles;
		this.maximumSize = maximumSize;
		this.minId = minId;
		this.maxId = maxId;
		this.folderAllowed = folderAllowed;
	}
	@Override
	public String toString() {
		return "Configurations [rootPath=" + rootPath + ", extensions="
				+ Arrays.toString(extensions) + ", numberOfFiles="
				+ numberOfFiles + ", maximumSize=" + maximumSize + ", minId="
				+ minId + ", maxId=" + maxId + ", folderAllowed="
				+ folderAllowed + "]";
	}
	
	public boolean isValidExtension(String fileName)
	{
		
		for (String s : extensions) {
			if(fileName.endsWith(s)) return true;
		}
		
		return false;
	}
	
	public boolean isValidSize(Long fileSize)
	{
		if(fileSize<=maximumSize)return true;
		return false;
	}
	
	public boolean isValidStudentId(Long studentId)
	{
		if(studentId>=minId && studentId<=maxId)return true;
		return false;
	}
	
	public boolean isValidIpAddress(Long studentId,InetAddress ipAddress)
	{
		InetAddress requiredIpAddress= (InetAddress)ServerRunningFrame.ipIdMap.get(studentId);
		if(requiredIpAddress==null)
		{
			return false;
		}
		else if(requiredIpAddress.equals(ipAddress))
		{
			return true;
		}
		
		return false;
		
	}
	
	public void insertNewIdIpMap(Long studentId,InetAddress inetAddress)
	{
		if(ServerRunningFrame.ipIdMap.containsValue(inetAddress))
		{
			return;
		}
		else
		{
			ServerRunningFrame.ipIdMap.put(studentId, inetAddress);
		}
	}
	
}
