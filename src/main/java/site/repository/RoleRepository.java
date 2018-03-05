package site.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import site.model.Role;


/**
 * @author Trifon Trifonov
 */
@Repository(value = RoleRepository.NAME)
public interface RoleRepository extends PagingAndSortingRepository<Role, Long> {

	String NAME = "roleRepository";

    public Role findByName(String name);
}