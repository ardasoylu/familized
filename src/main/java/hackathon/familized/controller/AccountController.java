package hackathon.familized.controller;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.net.ssl.TrustManager;
import javax.ws.rs.core.MediaType;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.transport.http.HTTPConduit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hackathon.familized.dao.entity.UserGroupAccounts;
import hackathon.familized.dao.entity.UserGroups;
import hackathon.familized.dao.repository.UserGroupAccountsRepository;
import hackathon.familized.dao.repository.UserGroupsRepository;
import hackathon.familized.model.dto.AccountDTO;
import hackathon.familized.util.TrustAllX509TrustManager;

@Controller
@RequestMapping("account")
public class AccountController {
	
	@Autowired
	private UserGroupsRepository userGroupsRepository;
	
	@Autowired
	private UserGroupAccountsRepository userGroupAccountsRepository;
	
	private WebClient client;
	
	private ObjectMapper mapper;
	
	@PostConstruct
	public void init() {
		// https://api.unicredit.eu/accounts/v1/banks/29000/accounts/77000001
		client = WebClient.create("https://api.unicredit.eu/accounts/v1/");
		HTTPConduit conduit = (HTTPConduit) WebClient.getConfig(client).getConduit();
        TLSClientParameters tcp = new TLSClientParameters();
        tcp.setTrustManagers( new TrustManager[]{ new TrustAllX509TrustManager() } );
        conduit.setTlsClientParameters( tcp );
        client.header("keyId", "cfe46594-b9a6-49f1-97d1-63de291fa9fe");
        //WebClient.getConfig(client).getRequestContext().put("keyId", "cfe46594-b9a6-49f1-97d1-63de291fa9fe");
        
        mapper = new ObjectMapper();
    }
	
	@GetMapping("/{facebookid}")
	public ResponseEntity<List<AccountDTO>> getAccountsByFacebookId(@PathVariable("facebookid") String id) {
		List<AccountDTO> list = new ArrayList<AccountDTO>();
		
		UserGroups userGroup = userGroupsRepository.findByFacebookId(id);
		if(userGroup!=null) {
			String groupCode = userGroup.getGroupCode();
			
			List<UserGroupAccounts> groupAccounts = userGroupAccountsRepository.findByGroupCode(groupCode);
			for(UserGroupAccounts groupAccount : groupAccounts) {
				WebClient oneTimeClient = WebClient.fromClient(client, true);
				String response = oneTimeClient.path("banks/"+groupAccount.getBankId()+"/accounts/"+groupAccount.getAccountId()).accept(MediaType.APPLICATION_JSON).get(String.class);
				try {
					JsonNode actualObj = mapper.readTree(response);
					String name = actualObj.get("name").asText();
					String iban = actualObj.get("arrangementIdentifier").get("iban").asText();
					double availableBalance = actualObj.get("balance").get("availableBalance").asDouble();
					
					AccountDTO account = new AccountDTO();
					account.setGroupCode(groupAccount.getGroupCode());
					account.setFacebookId(groupAccount.getFacebookId());
					account.setAccountid(groupAccount.getAccountId());
					account.setBankid(groupAccount.getBankId());
					account.setName(name);
					account.setIban(iban);
					account.setAvailableBalance(availableBalance);
					list.add(account);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return new ResponseEntity<List<AccountDTO>>(list, HttpStatus.OK);
	}
	
	
	
	
}
