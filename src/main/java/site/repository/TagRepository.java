package site.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import site.model.Tag;

@Repository(value = TagRepository.NAME)
public interface TagRepository extends JpaRepository<Tag, Long> {

	String NAME = "tagRepository";
}
