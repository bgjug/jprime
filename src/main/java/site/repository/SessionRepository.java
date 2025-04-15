package site.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import site.model.Branch;
import site.model.Session;

@Repository
@RepositoryRestResource(path = "sessions", exported = false)
public interface SessionRepository extends JpaRepository<Session, Long> {


    @Query(
        nativeQuery = true, value =
        "select s.id, s.created_by, s.created_date, s.last_modified_by, s.last_modified_date, \n" +
        "s.end_time, s.hall, s.start_time, s.submission, s.title \n" +
        "from Session s \n" +
        "left join Submission sbm on (s.submission = sbm.id)\n" +
        "left join VenueHall vh on (s.hall = vh.id)\n" +
        "where (vh.name=:hall and s.title is not null) or (vh.name = :hall and sbm.branch=:branch) or s.hall is null\n" +
        "order by s.start_time asc"
    )
    List<Session> findSessionsForBranchAndHallOrHallIsNull(@Param("hall") String hall,
        @Param("branch") String branch);

    List<Session> findBySubmissionBranchOrSubmissionIsNullOrderByStartTimeAsc(Branch branch);

}
