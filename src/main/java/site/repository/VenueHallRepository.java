package site.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import site.model.VenueHall;

import java.util.List;

@Repository(value = VenueHallRepository.NAME)
@RepositoryRestResource(path = "halls")
public interface VenueHallRepository extends PagingAndSortingRepository<VenueHall, Long> {

	String NAME = "hallRepository";

	List<VenueHall> findAll();

	@Override
	@RestResource(exported = false)
	void deleteById(Long id);

	@Override
	@RestResource(exported = false)
	void delete(VenueHall entity);

	@Override
	@RestResource(exported = false)
	void deleteAll(Iterable<? extends VenueHall> var1);

	@Override
	@RestResource(exported = false)
	void deleteAll();

	@Override
	@RestResource(exported = false)
	<S extends VenueHall> S save(S var1);

	@Override
	@RestResource(exported = false)
	<S extends VenueHall> Iterable<S> saveAll(Iterable<S> var1);
}
