package site.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import site.model.Session;

@Repository(value = SessionRepository.NAME)
@RepositoryRestResource(path = "sessions")
public interface SessionRepository extends PagingAndSortingRepository<Session, Long> {

	String NAME = "sessionRepository";

}
