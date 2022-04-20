package site.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import site.model.User;

import java.util.List;

@Repository(value = UserRepository.NAME)
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

	String NAME = "userRepository";

	List<User> findByEmail(String email);
	
	User findUserByEmail(String email);

	@Modifying(clearAutomatically = true)
	@Query(value = "update User set DTYPE = 'Speaker' where id = :userId", nativeQuery = true)
	void convertToSpeaker(long userId);

}
