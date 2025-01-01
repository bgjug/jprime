package site.facade;

import java.math.BigDecimal;
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
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import site.config.Globals;
import site.model.Branch;
import site.model.BranchEnum;
import site.model.SponsorPackage;
import site.model.TicketPrice;
import site.model.TicketType;
import site.repository.BranchRepository;
import site.repository.TicketPriceRepository;

@Service
@Transactional
public class BranchService {

    @Value("${branch.data.preload:true}")
    private boolean preloadData;

    private static final long FOUR_HOURS = 4 * 60 * 60 * 1000L;

    private final BranchRepository branchRepository;

    private final TicketPriceRepository ticketPriceRepository;

    public BranchService(BranchRepository branchRepository, TicketPriceRepository ticketPriceRepository) {
        this.branchRepository = branchRepository;
        this.ticketPriceRepository = ticketPriceRepository;
    }

    @PostConstruct
    public void init() {
        if (!preloadData) {
            return;
        }
        preloadBranches();
        preloadTicketPrices();
    }

    private void preloadTicketPrices() {
        if (!ticketPriceRepository.findAll().isEmpty()) {
            return;
        }

        Branch branch2024 = findBranchByYear(2024);
        Branch branch2025 = findBranchByYear(2025);

        List<TicketPrice> ticketPrices = List.of(
            new TicketPrice(BigDecimal.valueOf(280.0), TicketType.REGULAR, branch2024,
                branch2024.getCfpCloseDate(), null),
            new TicketPrice(BigDecimal.valueOf(180.0), TicketType.EARLY_BIRD, branch2024, null,
                branch2024.getCfpCloseDate()),
            new TicketPrice(BigDecimal.valueOf(100.0), TicketType.STUDENT, branch2024),
            new TicketPrice(BigDecimal.valueOf(340.0), TicketType.REGULAR, branch2025,
                branch2025.getCfpCloseDate(), null),
            new TicketPrice(BigDecimal.valueOf(230.0), TicketType.EARLY_BIRD, branch2025, null,
                branch2025.getCfpCloseDate()),
            new TicketPrice(BigDecimal.valueOf(130.0), TicketType.STUDENT, branch2025));

        updateTicketPrices(ticketPrices, branch2025);
    }

    private void preloadBranches() {
        if (!branchRepository.findAll().isEmpty()) {
            return;
        }

        Arrays.stream(BranchEnum.values()).forEach(branch -> {
            if (branch.getYear() < 2023) {
                createBranch(branch.getYear(), branch.getStartDate());
            } else {
                createBranch(branch.getYear(), branch.getStartDate(), Duration.ofDays(2),
                    branch.getCfpOpenDate(), branch.getCfpCloseDate(), Arrays.stream(SponsorPackage.values())
                        .filter(branch::isSoldOut)
                        .collect(Collectors.toSet()), branch.isSoldOut());
            }
        });

        setAsCurrent(branchRepository.findByYear(Globals.CURRENT_BRANCH.getYear()).getLabel());
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
        return branchRepository.findByYear(year);
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

    public void deleteBranch(int year) {
        Branch branch = branchRepository.findByYear(year);
        if (branch == null) {
            throw new IllegalArgumentException("Invalid branch year: " + year);
        }
        ticketPriceRepository.deleteByBranch(branch);
        branchRepository.delete(branch);
    }

    public Branch createBranch(Branch branch, List<TicketPrice> ticketPrices) {
        Branch existing = branchRepository.findByYear(branch.getYear());
        if (existing != null) {
            branch.setCurrentBranch(existing.isCurrentBranch());
        }
        branch = branchRepository.save(branch);
        updateTicketPrices(ticketPrices, branch);
        return branch;
    }
}
