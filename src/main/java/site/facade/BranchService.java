package site.facade;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.annotation.Nonnull;
import jakarta.transaction.Transactional;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import site.model.Branch;
import site.model.SponsorPackage;
import site.model.TicketPrice;
import site.model.TicketType;
import site.repository.BranchRepository;
import site.repository.TicketPriceRepository;

@Service
@Transactional
public class BranchService {

    private static final long FOUR_HOURS = 4 * 60 * 60 * 1000L;

    private final BranchRepository branchRepository;

    private final TicketPriceRepository ticketPriceRepository;

    public BranchService(BranchRepository branchRepository, TicketPriceRepository ticketPriceRepository) {
        this.branchRepository = branchRepository;
        this.ticketPriceRepository = ticketPriceRepository;
    }

    @CacheEvict(value = "currentBranch", allEntries = true)
    public void setAsCurrent(String branchId) {
        Branch current = findCurrentBranch();
        Branch newCurrent = branchRepository.findById(branchId).orElse(null);
        if (newCurrent == null) {
            throw new IllegalArgumentException("Invalid branch id: " + branchId);
        }

        if (current != null) {
            current.setCurrentBranch(false);
            branchRepository.save(current);
        }

        newCurrent.setCurrentBranch(true);
        branchRepository.save(newCurrent);
    }

    @Cacheable(cacheNames = "currentBranch")
    public Branch getCurrentBranch() {
        return findCurrentBranch();
    }

    private Branch findCurrentBranch() {
        return branchRepository.findByCurrentBranch(true).orElse(null);
    }

    public Branch createBranch(int year, LocalDateTime startDate) {
        if (year != startDate.getYear()) {
            throw new IllegalArgumentException("Invalid value for year or startDate");
        }

        Branch entity =
            new Branch(year, startDate, Duration.ofDays(2), startDate.minusMonths(6).minusDays(20),
                startDate.minusMonths(3).minusDays(15), new HashSet<>(Arrays.asList(SponsorPackage.values())),
                false);

        branchRepository.save(entity);
        return entity;
    }

    public Branch createBranch(int year, LocalDateTime startDate, Duration duration,
        LocalDateTime cfpOpenDate, LocalDateTime cfpCloseDate, Collection<SponsorPackage> soldOutPackages,
        boolean soldOut) {
        if (year != startDate.getYear()) {
            throw new IllegalArgumentException("Invalid value for year or startDate");
        }

        if (duration.isNegative() || duration.toMillis() < FOUR_HOURS) {
            throw new IllegalArgumentException("Invalid value for endDate or startDate");
        }

        if (startDate.isBefore(cfpOpenDate)) {
            throw new IllegalArgumentException("Invalid value for cfpOpenDate or startDate");
        }

        if (startDate.isBefore(cfpCloseDate)) {
            throw new IllegalArgumentException("Invalid value for cfpCloseDate or startDate");
        }

        Branch entity = new Branch(year, startDate, duration, cfpOpenDate, cfpCloseDate,
            new HashSet<>(soldOutPackages), soldOut);

        branchRepository.save(entity);
        return entity;
    }

    public Branch findBranchByYear(int year) {
        return branchRepository.findByYear(year).orElseThrow( ()-> new IllegalArgumentException("Invalid branch year: " + year));
    }

    public Page<Branch> allBranches(Pageable pageable) {
        return branchRepository.findAll(pageable);
    }

    public List<Branch> allBranches() {
        return branchRepository.findAll()
            .stream()
            .sorted(Comparator.comparingInt(Branch::getYear))
            .toList();
    }

    public void updateTicketPrices(List<TicketPrice> ticketPrices, Branch branch) {
        ticketPrices = ticketPrices.stream().map(ticketPrice -> ticketPrice.branchIfNotSet(branch))
            .map(ticketPrice -> Pair.of(ticketPrice,
                ticketPriceRepository.findByBranchAndTicketType(ticketPrice.getBranch(),
                    ticketPrice.getTicketType())))
            .map(p -> {
                p.getValue().ifPresent(v -> {
                    v.setPrice(p.getKey().getPrice());
                    v.setValidFrom(p.getKey().getValidFrom());
                    v.setValidUntil(p.getKey().getValidUntil());
                });
                return p.getValue().orElse(p.getKey());
            })
            .toList();
        ticketPriceRepository.saveAll(ticketPrices);
    }

    @Cacheable(cacheNames = "ticketPrices", key = "#branch.label", unless = "#result == null")
    public Map<TicketType, TicketPrice> getTicketPrices(@Nonnull Branch branch) {
        return ticketPriceRepository.findByBranch(branch)
            .stream()
            .collect(Collectors.toMap(TicketPrice::getTicketType, Function.identity()));
    }

    public TicketPrice getTicketPrice(Branch branch) {
        return getTicketPrice(branch, LocalDateTime.now());
    }

    public TicketPrice getTicketPrice(Branch branch, LocalDateTime atDateTime) {
        Map<TicketType, TicketPrice> ticketPrices = getTicketPrices(branch);
        atDateTime = atDateTime != null ? atDateTime : LocalDateTime.now();
        return branch.getCfpCloseDate().isAfter(atDateTime) ?
               ticketPrices.getOrDefault(TicketType.EARLY_BIRD, ticketPrices.get(TicketType.REGULAR)) :
               ticketPrices.get(TicketType.REGULAR);
    }

    @CacheEvict(value = "currentBranch", allEntries = true)
    public void deleteBranch(int year) {
        Branch branch = branchRepository.findByYear(year).orElseThrow( ()-> new IllegalArgumentException("Invalid branch year: " + year));
        ticketPriceRepository.deleteByBranch(branch);
        branchRepository.delete(branch);
    }

    @CacheEvict(value = {"currentBranch", "ticketPrices"}, allEntries = true)
    public Branch createBranch(Branch branch, List<TicketPrice> ticketPrices) {
        branchRepository.findByYear(branch.getYear()).ifPresent(existingBranch -> {
            branch.setCurrentBranch(existingBranch.isCurrentBranch());
        });

        Branch savedBranch = branchRepository.save(branch);
        updateTicketPrices(ticketPrices, savedBranch);
        return savedBranch;
    }

    public Branch findById(String branchLabel) {
        return branchRepository.findById(branchLabel).orElse(null);
    }

    public Branch getNextBranch() {
        Branch current = findCurrentBranch();
        return branchRepository.findByYear(current.getYear() + 1).orElse(null);
    }
}
