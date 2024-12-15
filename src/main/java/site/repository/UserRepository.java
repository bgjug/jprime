package site.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import site.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	List<User> findByEmail(String email);
	
	User findUserByEmail(String email);

	@Modifying(clearAutomatically = true)
	@Query(value = "update User set DTYPE = 'Speaker' where id = :userId", nativeQuery = true)
	void convertToSpeaker(long userId);

}
