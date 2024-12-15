package site.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import site.model.VenueHall;

@Repository
@RepositoryRestResource(path = "halls", exported = false)
public interface VenueHallRepository extends JpaRepository<VenueHall, Long> {
}
