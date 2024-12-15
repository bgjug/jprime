package site.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import site.model.BackgroundJob;

@Repository
public interface BackgroundJobRepository extends JpaRepository<BackgroundJob, String> {

    @Query("select j from BackgroundJob as j where j.completed is null")
    List<BackgroundJob> findPendingJobs();

    @Query("select j from BackgroundJob as j where j.completed >= ?1")
    List<BackgroundJob> findCompletedJobs(LocalDateTime afterTimeStamp);
}
