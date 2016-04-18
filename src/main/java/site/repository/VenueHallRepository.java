package site.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import site.model.VenueHall;

import java.util.List;

@Repository(value = VenueHallRepository.NAME)
@RepositoryRestResource(path = "halls")
public interface VenueHallRepository extends PagingAndSortingRepository<VenueHall, Long> {

	String NAME = "hallRepository";

	List<VenueHall> findAll();
}
