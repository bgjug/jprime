package site.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import site.model.User;

@Repository(value = UserRepository.NAME)
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

	String NAME = "userRepository";

	List<User> findByEmail(String email);
	
	public User findUserByEmail(String email);

}
