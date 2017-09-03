package hackathon.familized.model.dto;

import java.util.List;

public class CreateGroupDTO {
	
	private String groupName;
	private String facebookId;
	private List<AccountDTO> accounts;
	
	
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getFacebookId() {
		return facebookId;
	}
	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}
	public List<AccountDTO> getAccounts() {
		return accounts;
	}
	public void setAccounts(List<AccountDTO> accounts) {
		this.accounts = accounts;
	}
	
	
}
