package site.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import site.model.Branch;
import site.model.Submission;
import site.model.SubmissionByStatus;
import site.model.SubmissionStatus;

/**
 * @author Ivan St. Ivanov
 */
@Repository
@RepositoryRestResource(path = "submissions", exported = false)
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    List<Submission> findByStatus(SubmissionStatus status);

    List<Submission> findByBranchAndStatus(Branch branch, SubmissionStatus status);

    Page<Submission> findByBranchAndStatus(Branch branch, SubmissionStatus status, Pageable pageable);

    Page<Submission> findAllByBranch(Branch branch, Pageable pageable);

    @Query("select new site.model.SubmissionByStatus(s.status, count(s)) from Submission as s where s.branch = :branch group by s.status order by s.status")
    List<SubmissionByStatus> countSubmissionsByStatusForBranch(Branch branch);
}
