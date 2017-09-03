package hackathon.familized.controller;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;
import hackathon.familized.dao.entity.UserGroupAccounts;
import hackathon.familized.dao.entity.UserGroups;
import hackathon.familized.dao.repository.UserGroupAccountsRepository;
import hackathon.familized.dao.repository.UserGroupsRepository;
import hackathon.familized.model.dto.AccountDTO;
import hackathon.familized.model.dto.CreateGroupDTO;
import hackathon.familized.model.dto.JoinGroupDTO;
import hackathon.familized.util.RandomString;

@Controller
@RequestMapping("user")
public class GroupController {
	
	@Autowired
	private UserGroupsRepository userGroupsRepository;
	
	@Autowired
	private UserGroupAccountsRepository userGroupAccountsRepository;
	
	@PostMapping("createGroup")
	public ResponseEntity<String> createUserGroup(@RequestBody CreateGroupDTO createGroupInfo, UriComponentsBuilder builder) {
		UserGroups userGroup = userGroupsRepository.findByFacebookId(createGroupInfo.getFacebookId());
		if(userGroup==null) {
			userGroup = new UserGroups();
			userGroup.setGroupName(createGroupInfo.getGroupName());
			userGroup.setFacebookId(createGroupInfo.getFacebookId());
			RandomString randomString = new RandomString(6);
			userGroup.setGroupCode(randomString.nextString());
			userGroupsRepository.saveAndFlush(userGroup);
			
			List<AccountDTO> accountList = createGroupInfo.getAccounts();
			for(AccountDTO account : accountList) {
				UserGroupAccounts groupAccount = new UserGroupAccounts();
				groupAccount.setAccountId(account.getAccountid());
				groupAccount.setGroupCode(userGroup.getGroupCode());
				groupAccount.setBankId(account.getBankid());
				groupAccount.setFacebookId(userGroup.getFacebookId());
				userGroupAccountsRepository.saveAndFlush(groupAccount);
			}
			return new ResponseEntity<String>(userGroup.getGroupCode(), HttpStatus.CREATED);	
		}else {
			return new ResponseEntity<String>("User Group Already Created.", HttpStatus.CONFLICT);
		}
	}
	
	@PostMapping("joinGroup")
	public ResponseEntity<String> joinUserGroup(@RequestBody JoinGroupDTO joinGroupInfo, UriComponentsBuilder builder) {
		UserGroups userGroup = userGroupsRepository.findByFacebookId(joinGroupInfo.getFacebookId());
		if(userGroup==null) {
			userGroup = userGroupsRepository.findByGroupCode(joinGroupInfo.getGroupCode());
			if(userGroup!=null) {
				String groupName = userGroup.getGroupName();
				userGroup = new UserGroups();
				userGroup.setFacebookId(joinGroupInfo.getFacebookId());
				userGroup.setGroupCode(joinGroupInfo.getGroupCode());
				userGroup.setGroupName(groupName);
				userGroupsRepository.saveAndFlush(userGroup);
				
				List<AccountDTO> accountList = joinGroupInfo.getAccounts();
				for(AccountDTO account : accountList) {
					UserGroupAccounts groupAccount = new UserGroupAccounts();
					groupAccount.setAccountId(account.getAccountid());
					groupAccount.setGroupCode(userGroup.getGroupCode());
					groupAccount.setBankId(account.getBankid());
					groupAccount.setFacebookId(userGroup.getFacebookId());
					userGroupAccountsRepository.saveAndFlush(groupAccount);
				}
				return new ResponseEntity<String>(userGroup.getGroupCode(), HttpStatus.CREATED);
			}else {
				return new ResponseEntity<String>("User Group Not Created.", HttpStatus.CONFLICT);	
			}
		}else {
			return new ResponseEntity<String>("User Group Already Joined.", HttpStatus.CONFLICT);
		}
	}
}
