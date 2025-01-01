package site.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import site.model.Branch;
import site.model.Speaker;

@Repository
public interface SpeakerRepository extends JpaRepository<Speaker, Long> {

    @Query("SELECT s FROM Speaker s WHERE s.firstName = :firstName AND s.lastName = :lastName")
    Speaker findSpeakerByName(@Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query("SELECT s FROM Speaker s, in(s.submissions) as sub WHERE sub.featured = true and sub.branch = :branch")
    List<Speaker> findFeaturedSpeakers(@Param("branch") Branch branch);

    @Query("SELECT distinct s FROM Speaker s, in(s.submissions) as sub WHERE (sub.status = 'ACCEPTED' or sub.featured = true) and sub.branch = :branch")
    List<Speaker> findAcceptedSpeakers(@Param("branch") Branch branch);

    Speaker findByEmail(String email);

    @Query("SELECT distinct s FROM Speaker s, in(s.submissions) as sub WHERE sub.branch = :branch")
    Page<Speaker> findAllByBranch(Pageable pageable, Branch branch);

    @Query("SELECT distinct s FROM Speaker s, in(s.submissions) as sub WHERE sub.branch = :branch and sub.status = 'ACCEPTED'")
    Optional<Speaker> findAcceptedSpeaker(Long id, Branch currentBranch);
}
