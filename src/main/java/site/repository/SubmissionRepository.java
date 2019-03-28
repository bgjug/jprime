package site.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import site.model.Branch;
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

    List<Submission> findByBranchAndStatus(Branch branch, SubmissionStatus status);

    Page<Submission> findAllByBranch(Branch branch, Pageable pageable);

    @Override
    @RestResource(exported = false)
    void deleteById(Long id);

    @Override
    @RestResource(exported = false)
    void delete(Submission entity);

    @Override
    @RestResource(exported = false)
    void deleteAll(Iterable<? extends Submission> var1);

    @Override
    @RestResource(exported = false)
    void deleteAll();

    @Override
    @RestResource(exported = false)
    <S extends Submission> S save(S var1);

    @Override
    @RestResource(exported = false)
    <S extends Submission> Iterable<S> saveAll(Iterable<S> var1);
}
