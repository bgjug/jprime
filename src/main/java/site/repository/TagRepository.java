package site.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import site.model.Tag;

@Repository(value = TagRepository.NAME)
public interface TagRepository extends PagingAndSortingRepository<Tag, Long> {

	String NAME = "tagRepository";
	
	public List<Tag> findAll();

}
