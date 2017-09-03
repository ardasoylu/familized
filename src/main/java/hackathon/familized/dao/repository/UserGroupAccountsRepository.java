package hackathon.familized.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hackathon.familized.dao.entity.UserGroupAccounts;

public interface UserGroupAccountsRepository extends JpaRepository<UserGroupAccounts, Long>{
	
	List<UserGroupAccounts> findByGroupCode(String groupCode);
	
}
