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
        nativeQuery = true, value =
        //@formatter:off
        "select s.id, s.created_by, s.created_date, s.last_modified_by, s.last_modified_date, \n" +
        "s.end_time, s.hall, s.start_time, s.submission, s.title \n" +
        "from Session s \n" +
        "left join Submission sbm on (s.submission = sbm.id)\n" +
        "left join VenueHall vh on (s.hall = vh.id)\n" +
        "where (vh.name=:hall and sbm.branch=:branch) or s.hall is null\n" +
        "order by s.start_time asc"
        //@formatter:on
    )
    List<Session> findSessionsForBranchAndHallOrHallIsNull(@Param("hall") String hall,
        @Param("branch") String branch);

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
