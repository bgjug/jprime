package site.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import site.config.Globals;
import site.model.Branch;
import site.model.Speaker;
import site.model.Sponsor;
import site.model.User;

@Repository(value = SpeakerRepository.NAME)
public interface SpeakerRepository extends PagingAndSortingRepository<Speaker, Long> {

	String NAME = "speakerRepository";

    final String currentBranch = Globals.CURRENT_BRANCH.toString();

    @Query("SELECT s FROM Speaker s WHERE s.firstName = :firstName AND s.lastName = :lastName")
    public Speaker findSpeakerByName(@Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query("SELECT s FROM Speaker s WHERE s.featured = true and s.branch = :branch")
    public List<Speaker> findFeaturedSpeakers(@Param("branch")Branch branch);

    @Query("SELECT s FROM Speaker s WHERE (s.accepted = true or s.featured = true) and s.branch = :branch")
    public List<Speaker> findAcceptedSpeakers(@Param("branch")Branch branch);

    public Speaker findByEmail(String email);
}
