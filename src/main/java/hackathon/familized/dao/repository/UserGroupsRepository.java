package hackathon.familized.dao.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import hackathon.familized.dao.entity.UserGroups;

public interface UserGroupsRepository extends JpaRepository<UserGroups, Long>{
	
	//@Cacheable("UserGroups")
	UserGroups findByFacebookId(String facebookid);
	
	//@Cacheable("UserGroups")
	UserGroups findByGroupCode(String groupCode);
	
}
