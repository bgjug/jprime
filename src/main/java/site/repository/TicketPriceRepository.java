package site.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import site.model.Branch;
import site.model.TicketPrice;
import site.model.TicketType;

@Repository
public interface TicketPriceRepository extends JpaRepository<TicketPrice, Long> {

    List<TicketPrice> findByBranch(Branch branch);

    Optional<TicketPrice> findByBranchAndTicketType(Branch branch, TicketType ticketType);

    void deleteByBranch(Branch branch);
}
