package hackathon.familized.model.dto;

import java.util.List;

public class JoinGroupDTO {
	
	private String facebookId;
	private String groupCode;
	private List<AccountDTO> accounts;
	
	public String getFacebookId() {
		return facebookId;
	}
	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}
	
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public List<AccountDTO> getAccounts() {
		return accounts;
	}
	public void setAccounts(List<AccountDTO> accounts) {
		this.accounts = accounts;
	}
	
	
}
