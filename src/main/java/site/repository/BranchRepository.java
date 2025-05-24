package site.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import site.model.Branch;

public interface BranchRepository extends JpaRepository<Branch, String> {

    Optional<Branch> findByYear(int year);

    Optional<Branch> findByCurrentBranch(boolean currentBranch);
}
