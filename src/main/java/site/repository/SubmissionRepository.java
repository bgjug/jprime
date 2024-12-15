package site.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import site.model.Branch;
import site.model.Submission;
import site.model.SubmissionStatus;

/**
 * @author Ivan St. Ivanov
 */
@Repository(value = SubmissionRepository.NAME)
@RepositoryRestResource(path = "submissions", exported = false)
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    String NAME = "submissionRepository";

    List<Submission> findByStatus(SubmissionStatus status);

    List<Submission> findByBranchAndStatus(Branch branch, SubmissionStatus status);

    Page<Submission> findAllByBranch(Branch branch, Pageable pageable);
}
