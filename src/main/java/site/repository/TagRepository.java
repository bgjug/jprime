package site.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import site.model.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
}
