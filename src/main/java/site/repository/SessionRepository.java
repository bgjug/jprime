package site.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import site.model.Session;

import java.util.List;

@Repository(value = SessionRepository.NAME)
@RepositoryRestResource(path = "sessions")
public interface SessionRepository extends PagingAndSortingRepository<Session, Long> {

	String NAME = "sessionRepository";

	List<Session> findAll();
	
	List<Session> findByHallNameOrHallIsNullOrderByStartTimeAsc(@Param("hall") String hall);
	
	List<Session> findByHallIsNullOrderByStartTimeAsc();

	@Override
	@RestResource(exported = false)
	void deleteById(Long id);

	@Override
	@RestResource(exported = false)
	void delete(Session entity);

	@Override
	@RestResource(exported = false)
	void deleteAll(Iterable<? extends Session> var1);

	@Override
	@RestResource(exported = false)
	void deleteAll();

    @Override
    @RestResource(exported = false)
    <S extends Session> S save(S var1);

    @Override
    @RestResource(exported = false)
    <S extends Session> Iterable<S> saveAll(Iterable<S> var1);
}
