package site.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import site.model.Branch;
import site.model.Speaker;

@Repository(value = SpeakerRepository.NAME)
public interface SpeakerRepository extends JpaRepository<Speaker, Long> {

	String NAME = "speakerRepository";

    @Query("SELECT s FROM Speaker s WHERE s.firstName = :firstName AND s.lastName = :lastName")
    Speaker findSpeakerByName(@Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query("SELECT s FROM Speaker s WHERE s.featured = true and s.branch = :branch")
    List<Speaker> findFeaturedSpeakers(@Param("branch")Branch branch);

    @Query("SELECT s FROM Speaker s WHERE (s.accepted = true or s.featured = true) and s.branch = :branch")
    List<Speaker> findAcceptedSpeakers(@Param("branch")Branch branch);

    Speaker findByEmail(String email);

    @Query("SELECT s FROM Speaker s WHERE s.branch = :branch")
    Page<Speaker> findAllByBranch(Pageable pageable, Branch branch);
}
