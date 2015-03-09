package site.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import site.model.Speaker;
import site.model.Sponsor;
import site.model.User;

@Repository(value = SpeakerRepository.NAME)
public interface SpeakerRepository extends PagingAndSortingRepository<Speaker, Long> {

	String NAME = "speakerRepository";

}
