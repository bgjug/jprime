package site.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import site.model.Submission;
import site.model.SubmissionStatus;

import java.util.List;

/**
 * @author Ivan St. Ivanov
 */
@Repository(value = SubmissionRepository.NAME)
@RepositoryRestResource(path = "submissions")
public interface SubmissionRepository extends PagingAndSortingRepository<Submission, Long> {

    String NAME = "submissionRepository";

    List<Submission> findAll();

    List<Submission> findByStatus(SubmissionStatus status);
}
