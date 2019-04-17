package site.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import site.model.Branch;
import site.model.Session;

import java.util.List;

@Repository(value = SessionRepository.NAME)
@RepositoryRestResource(path = "sessions")
public interface SessionRepository extends PagingAndSortingRepository<Session, Long> {

	String NAME = "sessionRepository";

    List<Session> findAll();

    @Query(
        "select s from Session s where (s.submission.branch = :branch and s.hall = :hall) or s.hall is null" +
			" order by s.startTime asc")
    List<Session> findSessionsForBranchAndHallOrHallIsNull(@Param("hall") String hall,
        @Param("branch") Branch branch);

    List<Session> findBySubmissionBranchOrSubmissionIsNullOrderByStartTimeAsc(Branch branch);

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
